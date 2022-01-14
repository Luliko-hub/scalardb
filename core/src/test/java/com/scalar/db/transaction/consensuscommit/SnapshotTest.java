package com.scalar.db.transaction.consensuscommit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableMap;
import com.scalar.db.api.Consistency;
import com.scalar.db.api.Delete;
import com.scalar.db.api.DistributedStorage;
import com.scalar.db.api.DistributedStorageAdmin;
import com.scalar.db.api.Get;
import com.scalar.db.api.Put;
import com.scalar.db.api.Result;
import com.scalar.db.api.Scan;
import com.scalar.db.api.Scanner;
import com.scalar.db.api.TableMetadata;
import com.scalar.db.exception.storage.ExecutionException;
import com.scalar.db.exception.transaction.CommitConflictException;
import com.scalar.db.exception.transaction.CrudException;
import com.scalar.db.io.DataType;
import com.scalar.db.io.Key;
import com.scalar.db.io.TextValue;
import com.scalar.db.io.Value;
import com.scalar.db.storage.common.ResultImpl;
import com.scalar.db.util.ScalarDbUtils;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class SnapshotTest {
  private static final String ANY_NAMESPACE_NAME_1 = "namespace1";
  private static final String ANY_TABLE_NAME_1 = "table1";
  private static final String ANY_NAMESPACE_NAME_2 = "namespace2";
  private static final String ANY_TABLE_NAME_2 = "table2";
  private static final String ANY_ID = "id";
  private static final String ANY_NAME_1 = "name1";
  private static final String ANY_NAME_2 = "name2";
  private static final String ANY_NAME_3 = "name3";
  private static final String ANY_NAME_4 = "name4";
  private static final String ANY_NAME_5 = "name5";
  private static final String ANY_NAME_6 = "name6";
  private static final String ANY_TEXT_1 = "text1";
  private static final String ANY_TEXT_2 = "text2";
  private static final String ANY_TEXT_3 = "text3";
  private static final String ANY_TEXT_4 = "text4";
  private static final String ANY_TEXT_5 = "text5";
  private static final String ANY_TEXT_6 = "text6";

  private static final TableMetadata TABLE_METADATA =
      TableMetadata.newBuilder()
          .addColumn(ANY_NAME_1, DataType.TEXT)
          .addColumn(ANY_NAME_2, DataType.TEXT)
          .addColumn(ANY_NAME_3, DataType.TEXT)
          .addColumn(ANY_NAME_4, DataType.TEXT)
          .addColumn(Attribute.ID, DataType.TEXT)
          .addColumn(Attribute.STATE, DataType.INT)
          .addColumn(Attribute.VERSION, DataType.INT)
          .addColumn(Attribute.PREPARED_AT, DataType.BIGINT)
          .addColumn(Attribute.COMMITTED_AT, DataType.BIGINT)
          .addColumn(Attribute.BEFORE_PREFIX + ANY_NAME_3, DataType.TEXT)
          .addColumn(Attribute.BEFORE_PREFIX + ANY_NAME_4, DataType.TEXT)
          .addColumn(Attribute.BEFORE_ID, DataType.TEXT)
          .addColumn(Attribute.BEFORE_STATE, DataType.INT)
          .addColumn(Attribute.BEFORE_VERSION, DataType.INT)
          .addColumn(Attribute.BEFORE_PREPARED_AT, DataType.BIGINT)
          .addColumn(Attribute.BEFORE_COMMITTED_AT, DataType.BIGINT)
          .addPartitionKey(ANY_NAME_1)
          .addClusteringKey(ANY_NAME_2)
          .build();

  private static final TableMetadata ANOTHER_TABLE_METADATA =
      TableMetadata.newBuilder()
          .addColumn(ANY_NAME_5, DataType.TEXT)
          .addColumn(ANY_NAME_6, DataType.TEXT)
          .addColumn(Attribute.ID, DataType.TEXT)
          .addColumn(Attribute.STATE, DataType.INT)
          .addColumn(Attribute.VERSION, DataType.INT)
          .addColumn(Attribute.PREPARED_AT, DataType.BIGINT)
          .addColumn(Attribute.COMMITTED_AT, DataType.BIGINT)
          .addColumn(Attribute.BEFORE_ID, DataType.TEXT)
          .addColumn(Attribute.BEFORE_STATE, DataType.INT)
          .addColumn(Attribute.BEFORE_VERSION, DataType.INT)
          .addColumn(Attribute.BEFORE_PREPARED_AT, DataType.BIGINT)
          .addColumn(Attribute.BEFORE_COMMITTED_AT, DataType.BIGINT)
          .addPartitionKey(ANY_NAME_5)
          .addClusteringKey(ANY_NAME_6)
          .build();

  private Snapshot snapshot;
  private Map<Snapshot.Key, Optional<TransactionResult>> readSet;
  private Map<Scan, List<Snapshot.Key>> scanSet;
  private Map<Snapshot.Key, Put> writeSet;
  private Map<Snapshot.Key, Delete> deleteSet;
  private Map<String, TableSnapshot> tableSnapshots;

  @Mock private ConsensusCommitConfig config;
  @Mock private PrepareMutationComposer prepareComposer;
  @Mock private CommitMutationComposer commitComposer;
  @Mock private RollbackMutationComposer rollbackComposer;

  @Mock private DistributedStorageAdmin admin;
  private TransactionalTableMetadataManager tableMetadataManager;

  @Mock private TableSnapshot tableSnapshot;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.openMocks(this).close();

    // Arrange
    when(admin.getTableMetadata(ANY_NAMESPACE_NAME_1, ANY_TABLE_NAME_1)).thenReturn(TABLE_METADATA);
    when(admin.getTableMetadata(ANY_NAMESPACE_NAME_2, ANY_TABLE_NAME_2))
        .thenReturn(ANOTHER_TABLE_METADATA);
    tableMetadataManager = new TransactionalTableMetadataManager(admin, -1);
  }

  private Snapshot prepareSnapshot(Isolation isolation) {
    return prepareSnapshot(isolation, SerializableStrategy.EXTRA_WRITE);
  }

  private Snapshot prepareSnapshot(Isolation isolation, SerializableStrategy strategy) {
    readSet = new HashMap<>();
    scanSet = new HashMap<>();
    writeSet = new HashMap<>();
    deleteSet = new HashMap<>();

    tableSnapshots = new HashMap<>();
    tableSnapshots.put(
        ScalarDbUtils.getFullTableName(ANY_NAMESPACE_NAME_1, ANY_TABLE_NAME_1), tableSnapshot);

    return spy(
        new Snapshot(
            ANY_ID,
            isolation,
            strategy,
            tableMetadataManager,
            new ParallelExecutor(config),
            readSet,
            scanSet,
            writeSet,
            deleteSet,
            tableSnapshots));
  }

  private Get prepareGet() {
    Key partitionKey = new Key(ANY_NAME_1, ANY_TEXT_1);
    Key clusteringKey = new Key(ANY_NAME_2, ANY_TEXT_2);
    return new Get(partitionKey, clusteringKey)
        .withConsistency(Consistency.LINEARIZABLE)
        .forNamespace(ANY_NAMESPACE_NAME_1)
        .forTable(ANY_TABLE_NAME_1);
  }

  private TransactionResult prepareResult(String txId) {
    return new TransactionResult(
        new ResultImpl(
            ImmutableMap.<String, Value<?>>builder()
                .put(ANY_NAME_1, new TextValue(ANY_NAME_1, ANY_TEXT_1))
                .put(ANY_NAME_2, new TextValue(ANY_NAME_2, ANY_TEXT_2))
                .put(ANY_NAME_3, new TextValue(ANY_NAME_2, ANY_TEXT_3))
                .put(ANY_NAME_4, new TextValue(ANY_NAME_2, ANY_TEXT_4))
                .put(Attribute.ID, new TextValue(txId))
                .build(),
            TABLE_METADATA));
  }

  private TransactionResult prepareEmptyResult() {
    return new TransactionResult(new ResultImpl(ImmutableMap.of(), TABLE_METADATA));
  }

  private Get prepareAnotherGet() {
    Key partitionKey = new Key(ANY_NAME_5, ANY_TEXT_5);
    Key clusteringKey = new Key(ANY_NAME_6, ANY_TEXT_6);
    return new Get(partitionKey, clusteringKey)
        .withConsistency(Consistency.LINEARIZABLE)
        .forNamespace(ANY_NAMESPACE_NAME_2)
        .forTable(ANY_TABLE_NAME_2);
  }

  private Scan prepareScan() {
    Key partitionKey = new Key(ANY_NAME_1, ANY_TEXT_1);
    Key clusteringKey = new Key(ANY_NAME_2, ANY_TEXT_2);
    return new Scan(partitionKey)
        .withStart(clusteringKey)
        .withConsistency(Consistency.LINEARIZABLE)
        .forNamespace(ANY_NAMESPACE_NAME_1)
        .forTable(ANY_TABLE_NAME_1);
  }

  private Put preparePut() {
    Key partitionKey = new Key(ANY_NAME_1, ANY_TEXT_1);
    Key clusteringKey = new Key(ANY_NAME_2, ANY_TEXT_2);
    return new Put(partitionKey, clusteringKey)
        .withConsistency(Consistency.LINEARIZABLE)
        .forNamespace(ANY_NAMESPACE_NAME_1)
        .forTable(ANY_TABLE_NAME_1)
        .withValue(ANY_NAME_3, ANY_TEXT_3)
        .withValue(ANY_NAME_4, ANY_TEXT_4);
  }

  private Put prepareAnotherPut() {
    Key partitionKey = new Key(ANY_NAME_5, ANY_TEXT_5);
    Key clusteringKey = new Key(ANY_NAME_6, ANY_TEXT_6);
    return new Put(partitionKey, clusteringKey)
        .withConsistency(Consistency.LINEARIZABLE)
        .forNamespace(ANY_NAMESPACE_NAME_2)
        .forTable(ANY_TABLE_NAME_2);
  }

  private Delete prepareDelete() {
    Key partitionKey = new Key(ANY_NAME_1, ANY_TEXT_1);
    Key clusteringKey = new Key(ANY_NAME_2, ANY_TEXT_2);
    return new Delete(partitionKey, clusteringKey)
        .withConsistency(Consistency.LINEARIZABLE)
        .forNamespace(ANY_NAMESPACE_NAME_1)
        .forTable(ANY_TABLE_NAME_1);
  }

  private Delete prepareAnotherDelete() {
    Key partitionKey = new Key(ANY_NAME_5, ANY_TEXT_5);
    Key clusteringKey = new Key(ANY_NAME_6, ANY_TEXT_6);
    return new Delete(partitionKey, clusteringKey)
        .withConsistency(Consistency.LINEARIZABLE)
        .forNamespace(ANY_NAMESPACE_NAME_2)
        .forTable(ANY_TABLE_NAME_2);
  }

  private void configureBehavior() {
    doNothing().when(prepareComposer).add(any(Put.class), any(TransactionResult.class));
    doNothing().when(prepareComposer).add(any(Delete.class), any(TransactionResult.class));
    doNothing().when(commitComposer).add(any(Put.class), any(TransactionResult.class));
    doNothing().when(commitComposer).add(any(Delete.class), any(TransactionResult.class));
    doNothing().when(rollbackComposer).add(any(Put.class), any(TransactionResult.class));
    doNothing().when(rollbackComposer).add(any(Delete.class), any(TransactionResult.class));
  }

  @Test
  public void put_PutAndDeleteGiven_ShouldCreateTableSnapshotsProperly() throws CrudException {
    // Arrange
    snapshot = prepareSnapshot(Isolation.SNAPSHOT);
    tableSnapshots.clear();

    Put put = preparePut();
    Delete anotherDelete = prepareAnotherDelete();

    // Act
    snapshot.put(new Snapshot.Key(put), put);
    snapshot.put(new Snapshot.Key(anotherDelete), anotherDelete);

    // Assert
    assertThat(
            tableSnapshots.containsKey(
                ScalarDbUtils.getFullTableName(ANY_NAMESPACE_NAME_1, ANY_TABLE_NAME_1)))
        .isTrue();
    assertThat(
            tableSnapshots.containsKey(
                ScalarDbUtils.getFullTableName(ANY_NAMESPACE_NAME_2, ANY_TABLE_NAME_2)))
        .isTrue();
  }

  @Test
  public void put_GetAndResultGiven_ShouldCreateTableSnapshotProperly() throws CrudException {
    // Arrange
    snapshot = prepareSnapshot(Isolation.SNAPSHOT);
    tableSnapshots.clear();

    Get get = prepareGet();
    Snapshot.Key key = new Snapshot.Key(get);
    TransactionResult result = prepareResult(ANY_ID);

    // Act
    snapshot.put(key, get, Optional.of(result));

    // Assert
    assertThat(
            tableSnapshots.containsKey(
                ScalarDbUtils.getFullTableName(ANY_NAMESPACE_NAME_1, ANY_TABLE_NAME_1)))
        .isTrue();
  }

  @Test
  public void getAndScan_ShouldCreateTableSnapshotsProperly() throws CrudException {
    // Arrange
    snapshot = prepareSnapshot(Isolation.SNAPSHOT);
    tableSnapshots.clear();

    Scan scan = prepareScan();
    Get anotherGet = prepareAnotherGet();

    // Act
    snapshot.scan(scan);
    snapshot.get(anotherGet);

    // Assert
    assertThat(
            tableSnapshots.containsKey(
                ScalarDbUtils.getFullTableName(ANY_NAMESPACE_NAME_1, ANY_TABLE_NAME_1)))
        .isTrue();
    assertThat(
            tableSnapshots.containsKey(
                ScalarDbUtils.getFullTableName(ANY_NAMESPACE_NAME_2, ANY_TABLE_NAME_2)))
        .isTrue();
  }

  @Test
  public void put_ResultGiven_ShouldHoldWhatsGivenInReadSetAndUpdateTableSnapshot()
      throws CrudException {
    // Arrange
    snapshot = prepareSnapshot(Isolation.SNAPSHOT);
    Get get = prepareGet();
    Snapshot.Key key = new Snapshot.Key(get);
    TransactionResult result = prepareResult(ANY_ID);

    // Act
    snapshot.put(key, get, Optional.of(result));

    // Assert
    assertThat(readSet.get(key)).isEqualTo(Optional.of(result));
    verify(tableSnapshot).put(get.getPartitionKey(), result.getClusteringKey(), result.getValues());
  }

  @Test
  public void put_PutGiven_ShouldHoldWhatsGivenInWriteSetAndUpdateTableSnapshot()
      throws CrudException {
    // Arrange
    snapshot = prepareSnapshot(Isolation.SNAPSHOT);
    Put put = preparePut();
    Snapshot.Key key = new Snapshot.Key(put);

    // Act
    snapshot.put(key, put);

    // Assert
    assertThat(writeSet.get(key)).isEqualTo(put);
    verify(tableSnapshot).put(put.getPartitionKey(), put.getClusteringKey(), put.getValues());
  }

  @Test
  public void put_DeleteGiven_ShouldHoldWhatsGivenInDeleteSetAndUpdateTableSnapshot()
      throws CrudException {
    // Arrange
    snapshot = prepareSnapshot(Isolation.SNAPSHOT);
    Delete delete = prepareDelete();
    Snapshot.Key key = new Snapshot.Key(delete);

    // Act
    snapshot.put(key, delete);

    // Assert
    assertThat(deleteSet.get(key)).isEqualTo(delete);
    verify(tableSnapshot).delete(delete.getPartitionKey(), delete.getClusteringKey());
  }

  @Test
  public void put_ScanGiven_ShouldHoldWhatsGivenInScanSet() {
    // Arrange
    snapshot = prepareSnapshot(Isolation.SNAPSHOT);
    Scan scan = prepareScan();
    Snapshot.Key key =
        new Snapshot.Key(
            scan.forNamespace().get(),
            scan.forTable().get(),
            scan.getPartitionKey(),
            scan.getStartClusteringKey().get());
    List<Snapshot.Key> expected = Collections.singletonList(key);

    // Act
    snapshot.put(scan, expected);

    // Assert
    assertThat(scanSet.get(scan)).isEqualTo(expected);
  }

  @Test
  public void get_KeyGivenContainedInWriteSet_ShouldReturnFromTableSnapshot() throws CrudException {
    // Arrange
    snapshot = prepareSnapshot(Isolation.SNAPSHOT);
    Put put = preparePut();
    snapshot.put(new Snapshot.Key(put), put);
    TransactionResult result = prepareResult(ANY_ID);

    Get get = prepareGet();
    when(tableSnapshot.get(get.getPartitionKey(), get.getClusteringKey()))
        .thenReturn(Optional.of(result));

    // Act
    Optional<Result> actual = snapshot.get(get);

    // Assert
    assertThat(actual).isEqualTo(Optional.of(result));
    verify(tableSnapshot).put(put.getPartitionKey(), put.getClusteringKey(), put.getValues());
    verify(tableSnapshot).get(get.getPartitionKey(), get.getClusteringKey());
  }

  @Test
  public void get_KeyGivenContainedInReadSet_ShouldReturnFromTableSnapshot() throws CrudException {
    // Arrange
    snapshot = prepareSnapshot(Isolation.SNAPSHOT);
    Get get = prepareGet();
    TransactionResult result = prepareResult(ANY_ID);
    snapshot.put(new Snapshot.Key(get), get, Optional.of(result));

    when(tableSnapshot.get(get.getPartitionKey(), get.getClusteringKey()))
        .thenReturn(Optional.of(result));

    // Act
    Optional<Result> actual = snapshot.get(get);

    // Assert
    assertThat(actual).isEqualTo(Optional.of(result));
    verify(tableSnapshot).put(get.getPartitionKey(), result.getClusteringKey(), result.getValues());
    verify(tableSnapshot).get(get.getPartitionKey(), get.getClusteringKey());
  }

  @Test
  public void get_KeyGivenNotContainedInSnapshot_ShouldReturnEmpty() throws CrudException {
    // Arrange
    snapshot = prepareSnapshot(Isolation.SNAPSHOT);
    Get get = prepareGet();

    // Act
    Optional<Result> result = snapshot.get(get);

    // Assert
    assertThat(result.isPresent()).isFalse();
    verify(tableSnapshot, never()).put(any(), any(), anyMap());
    verify(tableSnapshot).get(get.getPartitionKey(), get.getClusteringKey());
  }

  @Test
  public void scan_ScanNotContainedInSnapshotGiven_ShouldReturnEmptyList() throws CrudException {
    // Arrange
    snapshot = prepareSnapshot(Isolation.SNAPSHOT);
    Scan scan = prepareScan();

    // Act
    List<Result> keys = snapshot.scan(scan);

    // Assert
    assertThat(keys).isEmpty();
    verify(tableSnapshot, never()).put(any(), any(), anyMap());
    verify(tableSnapshot)
        .scan(
            scan.getPartitionKey(),
            scan.getStartClusteringKey(),
            scan.getStartInclusive(),
            scan.getEndClusteringKey(),
            scan.getEndInclusive(),
            scan.getOrderings(),
            scan.getLimit());
  }

  @Test
  public void put_PutGivenAfterDelete_PutSupercedesDelete() throws CrudException {
    // Arrange
    snapshot = prepareSnapshot(Isolation.SNAPSHOT);
    Put put = preparePut();
    Snapshot.Key putKey = new Snapshot.Key(put);
    snapshot.put(putKey, put);

    // Act
    Delete delete = prepareDelete();
    Snapshot.Key deleteKey = new Snapshot.Key(prepareDelete());
    snapshot.put(deleteKey, delete);

    // Assert
    assertThat(writeSet.size()).isEqualTo(0);
    assertThat(deleteSet.size()).isEqualTo(1);
    assertThat(deleteSet.get(deleteKey)).isEqualTo(delete);
    verify(tableSnapshot).put(put.getPartitionKey(), put.getClusteringKey(), put.getValues());
    verify(tableSnapshot).delete(delete.getPartitionKey(), delete.getClusteringKey());
  }

  @Test
  public void put_DeleteGivenAfterPut_ShouldThrowIllegalArgumentException() throws CrudException {
    // Arrange
    snapshot = prepareSnapshot(Isolation.SNAPSHOT);
    Delete delete = prepareDelete();
    Snapshot.Key deleteKey = new Snapshot.Key(prepareDelete());
    snapshot.put(deleteKey, delete);

    // Act Assert
    Put put = preparePut();
    Snapshot.Key putKey = new Snapshot.Key(preparePut());
    assertThatThrownBy(() -> snapshot.put(putKey, put))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  public void put_PutGivenAfterPut_ShouldMergePuts() throws CrudException {
    // Arrange
    snapshot = prepareSnapshot(Isolation.SNAPSHOT);
    Put put1 = preparePut();
    Map<String, Value<?>> expectedPut1Values = new HashMap<>(put1.getValues());
    Snapshot.Key putKey = new Snapshot.Key(put1);
    snapshot.put(putKey, put1);

    // Act
    Put put2 =
        new Put(new Key(ANY_NAME_1, ANY_TEXT_1), new Key(ANY_NAME_2, ANY_TEXT_2))
            .withConsistency(Consistency.LINEARIZABLE)
            .forNamespace(ANY_NAMESPACE_NAME_1)
            .forTable(ANY_TABLE_NAME_1)
            .withValue(ANY_NAME_3, ANY_TEXT_5);
    snapshot.put(putKey, put2);

    // Assert
    assertThat(writeSet.size()).isEqualTo(1);
    assertThat(writeSet.get(putKey))
        .isEqualTo(
            new Put(new Key(ANY_NAME_1, ANY_TEXT_1), new Key(ANY_NAME_2, ANY_TEXT_2))
                .withConsistency(Consistency.LINEARIZABLE)
                .forNamespace(ANY_NAMESPACE_NAME_1)
                .forTable(ANY_TABLE_NAME_1)
                .withValue(ANY_NAME_3, ANY_TEXT_5)
                .withValue(ANY_NAME_4, ANY_TEXT_4));
    verify(tableSnapshot).put(put1.getPartitionKey(), put1.getClusteringKey(), expectedPut1Values);
    verify(tableSnapshot).put(put2.getPartitionKey(), put2.getClusteringKey(), put2.getValues());
  }

  @Test
  public void to_PrepareMutationComposerGivenAndSnapshotIsolationSet_ShouldCallComposerProperly()
      throws CommitConflictException, CrudException {
    // Arrange
    snapshot = prepareSnapshot(Isolation.SNAPSHOT);
    Put put = preparePut();
    Delete delete = prepareAnotherDelete();
    Get get = prepareGet();
    TransactionResult result = prepareResult(ANY_ID);
    snapshot.put(new Snapshot.Key(get), get, Optional.of(result));
    Get anotherGet = prepareAnotherGet();
    snapshot.put(new Snapshot.Key(anotherGet), anotherGet, Optional.of(result));
    snapshot.put(new Snapshot.Key(put), put);
    snapshot.put(new Snapshot.Key(delete), delete);
    configureBehavior();

    // Act
    snapshot.to(prepareComposer);

    // Assert
    verify(prepareComposer).add(put, result);
    verify(prepareComposer).add(delete, result);
  }

  @Test
  public void
      to_PrepareMutationComposerGivenAndSerializableWithExtraWriteIsolationSet_ShouldCallComposerProperly()
          throws CommitConflictException, CrudException {
    // Arrange
    snapshot = prepareSnapshot(Isolation.SERIALIZABLE, SerializableStrategy.EXTRA_WRITE);
    Put put = preparePut();
    Get get = prepareGet();
    TransactionResult result = prepareResult(ANY_ID);
    snapshot.put(new Snapshot.Key(get), get, Optional.of(result));
    Get anotherGet = prepareAnotherGet();
    snapshot.put(new Snapshot.Key(anotherGet), anotherGet, Optional.of(result));
    snapshot.put(new Snapshot.Key(put), put);
    configureBehavior();

    // Act
    snapshot.to(prepareComposer);

    // Assert
    Put putFromGet = prepareAnotherPut();
    verify(prepareComposer).add(put, result);
    verify(prepareComposer).add(putFromGet, result);
    verify(snapshot).toSerializableWithExtraWrite(prepareComposer);
  }

  @Test
  public void to_CommitMutationComposerGiven_ShouldCallComposerProperly()
      throws CommitConflictException, CrudException {
    // Arrange
    snapshot = prepareSnapshot(Isolation.SNAPSHOT);
    Put put = preparePut();
    Delete delete = prepareAnotherDelete();
    Get get = prepareGet();
    TransactionResult result = prepareResult(ANY_ID);
    snapshot.put(new Snapshot.Key(get), get, Optional.of(result));
    Get anotherGet = prepareAnotherGet();
    snapshot.put(new Snapshot.Key(anotherGet), anotherGet, Optional.of(result));
    snapshot.put(new Snapshot.Key(put), put);
    snapshot.put(new Snapshot.Key(delete), delete);

    // Act
    snapshot.to(commitComposer);

    // Assert
    verify(commitComposer).add(put, result);
    verify(commitComposer).add(delete, result);
  }

  @Test
  public void
      to_CommitMutationComposerGivenAndSerializableWithExtraWriteIsolationSet_ShouldCallComposerProperly()
          throws CommitConflictException, CrudException {
    // Arrange
    snapshot = prepareSnapshot(Isolation.SERIALIZABLE, SerializableStrategy.EXTRA_WRITE);
    Put put = preparePut();
    Delete delete = prepareAnotherDelete();
    Get get = prepareGet();
    TransactionResult result = prepareResult(ANY_ID);
    snapshot.put(new Snapshot.Key(get), get, Optional.of(result));
    Get anotherGet = prepareAnotherGet();
    snapshot.put(new Snapshot.Key(anotherGet), anotherGet, Optional.of(result));
    snapshot.put(new Snapshot.Key(put), put);
    snapshot.put(new Snapshot.Key(delete), delete);
    configureBehavior();

    // Act
    snapshot.to(commitComposer);

    // Assert
    // no effect on CommitMutationComposer
    verify(commitComposer).add(put, result);
    verify(commitComposer).add(delete, result);
    verify(snapshot).toSerializableWithExtraWrite(commitComposer);
  }

  @Test
  public void to_RollbackMutationComposerGiven_ShouldCallComposerProperly()
      throws CommitConflictException, CrudException {
    // Arrange
    snapshot = prepareSnapshot(Isolation.SNAPSHOT);
    Put put = preparePut();
    Delete delete = prepareAnotherDelete();
    Get get = prepareGet();
    TransactionResult result = prepareResult(ANY_ID);
    snapshot.put(new Snapshot.Key(get), get, Optional.of(result));
    Get anotherGet = prepareAnotherGet();
    snapshot.put(new Snapshot.Key(anotherGet), anotherGet, Optional.of(result));
    snapshot.put(new Snapshot.Key(put), put);
    snapshot.put(new Snapshot.Key(delete), delete);
    configureBehavior();

    // Act
    snapshot.to(rollbackComposer);

    // Assert
    verify(rollbackComposer).add(put, result);
    verify(rollbackComposer).add(delete, result);
  }

  @Test
  public void
      to_RollbackMutationComposerGivenAndSerializableWithExtraWriteIsolationSet_ShouldCallComposerProperly()
          throws CommitConflictException, CrudException {
    // Arrange
    snapshot = prepareSnapshot(Isolation.SERIALIZABLE, SerializableStrategy.EXTRA_WRITE);
    Put put = preparePut();
    Delete delete = prepareAnotherDelete();
    Get get = prepareGet();
    TransactionResult result = prepareResult(ANY_ID);
    snapshot.put(new Snapshot.Key(get), get, Optional.of(result));
    Get anotherGet = prepareAnotherGet();
    snapshot.put(new Snapshot.Key(anotherGet), anotherGet, Optional.of(result));
    snapshot.put(new Snapshot.Key(put), put);
    snapshot.put(new Snapshot.Key(delete), delete);
    configureBehavior();

    // Act
    snapshot.to(rollbackComposer);

    // Assert
    // no effect on RollbackMutationComposer
    verify(rollbackComposer).add(put, result);
    verify(rollbackComposer).add(delete, result);
    verify(snapshot).toSerializableWithExtraWrite(rollbackComposer);
  }

  @Test
  public void toSerializableWithExtraWrite_UnmutatedReadSetExists_ShouldConvertReadSetIntoWriteSet()
      throws CrudException {
    // Arrange
    snapshot = prepareSnapshot(Isolation.SERIALIZABLE, SerializableStrategy.EXTRA_WRITE);
    Get get = prepareAnotherGet();
    Put put = preparePut();
    TransactionResult result = prepareResult(ANY_ID);
    TransactionResult txResult = new TransactionResult(result);
    snapshot.put(new Snapshot.Key(get), get, Optional.of(txResult));
    snapshot.put(new Snapshot.Key(put), put);

    // Act Assert
    assertThatCode(() -> snapshot.toSerializableWithExtraWrite(prepareComposer))
        .doesNotThrowAnyException();

    // Assert
    Put expected =
        new Put(get.getPartitionKey(), get.getClusteringKey().get())
            .withConsistency(Consistency.LINEARIZABLE)
            .forNamespace(get.forNamespace().get())
            .forTable(get.forTable().get());
    assertThat(writeSet).contains(entry(new Snapshot.Key(get), expected));
    assertThat(writeSet.size()).isEqualTo(2);
    verify(prepareComposer, never()).add(any(), any());
  }

  @Test
  public void
      toSerializableWithExtraWrite_UnmutatedReadSetForNonExistingExists_ShouldThrowCommitConflictException()
          throws CrudException {
    // Arrange
    snapshot = prepareSnapshot(Isolation.SERIALIZABLE, SerializableStrategy.EXTRA_WRITE);
    Get get = prepareAnotherGet();
    Put put = preparePut();
    snapshot.put(new Snapshot.Key(get), get, Optional.empty());
    snapshot.put(new Snapshot.Key(put), put);

    // Act Assert
    Throwable thrown = catchThrowable(() -> snapshot.toSerializableWithExtraWrite(prepareComposer));

    // Assert
    assertThat(thrown).doesNotThrowAnyException();
    Get expected =
        new Get(get.getPartitionKey(), get.getClusteringKey().get())
            .withConsistency(Consistency.LINEARIZABLE)
            .forNamespace(get.forNamespace().get())
            .forTable(get.forTable().get());
    verify(prepareComposer).add(expected, null);
  }

  @Test
  public void
      toSerializableWithExtraWrite_ScanSetAndWriteSetExist_ShouldThrowCommitConflictException()
          throws CrudException {
    // Arrange
    snapshot = prepareSnapshot(Isolation.SERIALIZABLE, SerializableStrategy.EXTRA_WRITE);
    Scan scan = prepareScan();
    Snapshot.Key key =
        new Snapshot.Key(
            scan.forNamespace().get(),
            scan.forTable().get(),
            scan.getPartitionKey(),
            scan.getStartClusteringKey().get());
    Put put = preparePut();
    snapshot.put(scan, Collections.singletonList(key));
    snapshot.put(new Snapshot.Key(put), put);

    // Act Assert
    Throwable thrown = catchThrowable(() -> snapshot.toSerializableWithExtraWrite(prepareComposer));

    // Assert
    assertThat(thrown).isInstanceOf(CommitConflictException.class);
  }

  @Test
  public void toSerializableWithExtraRead_ReadSetNotChanged_ShouldProcessWithoutExceptions()
      throws ExecutionException, CrudException {
    // Arrange
    snapshot = prepareSnapshot(Isolation.SERIALIZABLE, SerializableStrategy.EXTRA_READ);
    Get get = prepareAnotherGet();
    Put put = preparePut();
    TransactionResult result = prepareResult(ANY_ID);
    TransactionResult txResult = new TransactionResult(result);
    snapshot.put(new Snapshot.Key(get), get, Optional.of(txResult));
    snapshot.put(new Snapshot.Key(put), put);
    DistributedStorage storage = mock(DistributedStorage.class);
    when(storage.get(get)).thenReturn(Optional.of(txResult));

    // Act Assert
    assertThatCode(() -> snapshot.toSerializableWithExtraRead(storage)).doesNotThrowAnyException();

    // Assert
    verify(storage).get(get);
  }

  @Test
  public void toSerializableWithExtraRead_ReadSetUpdated_ShouldThrowCommitConflictException()
      throws ExecutionException, CrudException {
    // Arrange
    snapshot = prepareSnapshot(Isolation.SERIALIZABLE, SerializableStrategy.EXTRA_READ);
    Get get = prepareAnotherGet();
    Put put = preparePut();

    TransactionResult txResult = prepareResult(ANY_ID);
    snapshot.put(new Snapshot.Key(get), get, Optional.of(txResult));
    snapshot.put(new Snapshot.Key(put), put);

    DistributedStorage storage = mock(DistributedStorage.class);
    TransactionResult changedTxResult = prepareResult(ANY_ID + "x");
    when(storage.get(get)).thenReturn(Optional.of(changedTxResult));

    // Act Assert
    assertThatThrownBy(() -> snapshot.toSerializableWithExtraRead(storage))
        .isInstanceOf(CommitConflictException.class);

    // Assert
    verify(storage).get(get);
  }

  @Test
  public void toSerializableWithExtraRead_ReadSetExtended_ShouldThrowCommitConflictException()
      throws ExecutionException, CrudException {
    // Arrange
    snapshot = prepareSnapshot(Isolation.SERIALIZABLE, SerializableStrategy.EXTRA_READ);
    Get get = prepareAnotherGet();
    Put put = preparePut();
    snapshot.put(new Snapshot.Key(get), get, Optional.empty());
    snapshot.put(new Snapshot.Key(put), put);
    DistributedStorage storage = mock(DistributedStorage.class);
    TransactionResult txResult = prepareEmptyResult();
    when(storage.get(get)).thenReturn(Optional.of(txResult));

    // Act Assert
    assertThatThrownBy(() -> snapshot.toSerializableWithExtraRead(storage))
        .isInstanceOf(CommitConflictException.class);

    // Assert
    verify(storage).get(get);
  }

  @Test
  public void toSerializableWithExtraRead_ScanSetNotChanged_ShouldProcessWithoutExceptions()
      throws ExecutionException, CrudException {
    // Arrange
    snapshot = prepareSnapshot(Isolation.SERIALIZABLE, SerializableStrategy.EXTRA_READ);
    Scan scan = prepareScan();
    Get get = prepareGet();
    Put put = preparePut();
    TransactionResult txResult = prepareResult(ANY_ID);
    Snapshot.Key key = new Snapshot.Key(get);
    snapshot.put(key, get, Optional.of(txResult));
    snapshot.put(scan, Collections.singletonList(key));
    snapshot.put(new Snapshot.Key(put), put);
    DistributedStorage storage = mock(DistributedStorage.class);
    Scanner scanner = mock(Scanner.class);
    when(scanner.iterator()).thenReturn(Collections.singletonList((Result) txResult).iterator());
    when(storage.scan(scan)).thenReturn(scanner);

    // Act Assert
    assertThatCode(() -> snapshot.toSerializableWithExtraRead(storage)).doesNotThrowAnyException();

    // Assert
    verify(storage).scan(scan);
  }

  @Test
  public void toSerializableWithExtraRead_ScanSetUpdated_ShouldProcessWithoutExceptions()
      throws ExecutionException, CrudException {
    // Arrange
    snapshot = prepareSnapshot(Isolation.SERIALIZABLE, SerializableStrategy.EXTRA_READ);
    Scan scan = prepareScan();
    Get get = prepareGet();
    Put put = preparePut();
    TransactionResult txResult = prepareResult(ANY_ID);
    Snapshot.Key key = new Snapshot.Key(get);
    snapshot.put(key, get, Optional.of(txResult));
    snapshot.put(scan, Collections.singletonList(key));
    snapshot.put(new Snapshot.Key(put), put);
    DistributedStorage storage = mock(DistributedStorage.class);
    TransactionResult changedTxResult = prepareResult(ANY_ID + "x");
    Scanner scanner = mock(Scanner.class);
    when(scanner.iterator())
        .thenReturn(Collections.singletonList((Result) changedTxResult).iterator());
    when(storage.scan(scan)).thenReturn(scanner);

    // Act Assert
    assertThatThrownBy(() -> snapshot.toSerializableWithExtraRead(storage))
        .isInstanceOf(CommitConflictException.class);

    // Assert
    verify(storage).scan(scan);
  }

  @Test
  public void toSerializableWithExtraRead_ScanSetExtended_ShouldProcessWithoutExceptions()
      throws ExecutionException, CrudException {
    // Arrange
    snapshot = prepareSnapshot(Isolation.SERIALIZABLE, SerializableStrategy.EXTRA_READ);
    Scan scan = prepareScan();
    Put put = preparePut();
    snapshot.put(scan, Collections.emptyList());
    snapshot.put(new Snapshot.Key(put), put);
    DistributedStorage storage = mock(DistributedStorage.class);
    TransactionResult txResult = prepareResult(ANY_ID + "x");
    Scanner scanner = mock(Scanner.class);
    when(scanner.iterator()).thenReturn(Collections.singletonList((Result) txResult).iterator());
    when(storage.scan(scan)).thenReturn(scanner);

    // Act Assert
    assertThatThrownBy(() -> snapshot.toSerializableWithExtraRead(storage))
        .isInstanceOf(CommitConflictException.class);

    // Assert
    verify(storage).scan(scan);
  }

  @Test
  public void
      toSerializableWithExtraRead_MultipleScansInScanSetExist_ShouldThrowCommitConflictException()
          throws ExecutionException, CrudException {
    // Arrange
    snapshot = prepareSnapshot(Isolation.SERIALIZABLE, SerializableStrategy.EXTRA_READ);

    Scan scan1 =
        new Scan(new Key(ANY_NAME_1, ANY_TEXT_1))
            .withStart(new Key(ANY_NAME_2, ANY_TEXT_2))
            .withConsistency(Consistency.LINEARIZABLE)
            .forNamespace(ANY_NAMESPACE_NAME_1)
            .forTable(ANY_TABLE_NAME_1);
    Scan scan2 =
        new Scan(new Key(ANY_NAME_1, ANY_TEXT_2))
            .withStart(new Key(ANY_NAME_2, ANY_TEXT_1))
            .withConsistency(Consistency.LINEARIZABLE)
            .forNamespace(ANY_NAMESPACE_NAME_1)
            .forTable(ANY_TABLE_NAME_1);

    Snapshot.Key key1 =
        new Snapshot.Key(
            scan1.forNamespace().get(),
            scan1.forTable().get(),
            scan1.getPartitionKey(),
            scan1.getStartClusteringKey().get());
    Snapshot.Key key2 =
        new Snapshot.Key(
            scan2.forNamespace().get(),
            scan2.forTable().get(),
            scan2.getPartitionKey(),
            scan2.getStartClusteringKey().get());

    Result result1 = mock(TransactionResult.class);
    when(result1.getValues())
        .thenReturn(ImmutableMap.of(Attribute.ID, new TextValue(Attribute.ID, "id1")));
    Result result2 = mock(TransactionResult.class);
    when(result2.getValues())
        .thenReturn(ImmutableMap.of(Attribute.ID, new TextValue(Attribute.ID, "id2")));

    snapshot.put(scan1, Collections.singletonList(key1));
    snapshot.put(scan2, Collections.singletonList(key2));
    snapshot.put(key1, scan1, Optional.of(new TransactionResult(result1)));
    snapshot.put(key2, scan2, Optional.of(new TransactionResult(result2)));

    DistributedStorage storage = mock(DistributedStorage.class);

    Scanner scanner1 = mock(Scanner.class);
    when(scanner1.iterator()).thenReturn(Collections.singletonList(result1).iterator());
    when(storage.scan(scan1)).thenReturn(scanner1);

    Scanner scanner2 = mock(Scanner.class);
    when(scanner2.iterator()).thenReturn(Collections.singletonList(result2).iterator());
    when(storage.scan(scan2)).thenReturn(scanner2);

    // Act Assert
    assertThatCode(() -> snapshot.toSerializableWithExtraRead(storage)).doesNotThrowAnyException();
  }
}
