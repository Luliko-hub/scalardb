package com.scalar.db.transaction.consensuscommit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableMap;
import com.scalar.db.api.DistributedStorage;
import com.scalar.db.api.Get;
import com.scalar.db.api.Result;
import com.scalar.db.api.Scan;
import com.scalar.db.api.Scanner;
import com.scalar.db.api.TableMetadata;
import com.scalar.db.api.TransactionState;
import com.scalar.db.exception.storage.ExecutionException;
import com.scalar.db.exception.transaction.CrudException;
import com.scalar.db.exception.transaction.UncommittedRecordException;
import com.scalar.db.io.DataType;
import com.scalar.db.io.Key;
import com.scalar.db.io.TextValue;
import com.scalar.db.io.Value;
import com.scalar.db.storage.common.ResultImpl;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CrudHandlerTest {
  private static final String ANY_KEYSPACE_NAME = "keyspace";
  private static final String ANY_TABLE_NAME = "table";
  private static final String ANY_ID_1 = "id1";
  private static final String ANY_ID_2 = "id2";
  private static final String ANY_NAME_1 = "name1";
  private static final String ANY_NAME_2 = "name2";
  private static final String ANY_TEXT_1 = "text1";
  private static final String ANY_TEXT_2 = "text2";
  private static final String ANY_TX_ID = "tx_id";
  private static final TableMetadata TABLE_METADATA =
      TableMetadata.newBuilder()
          .addColumn(ANY_NAME_1, DataType.TEXT)
          .addColumn(ANY_NAME_2, DataType.TEXT)
          .addPartitionKey(ANY_NAME_1)
          .addClusteringKey(ANY_NAME_2)
          .build();

  @Mock private Snapshot snapshot;
  @Mock private DistributedStorage storage;
  @Mock private ParallelExecutor parallelExecutor;
  @Mock private Scanner scanner;
  @Mock private Result result;

  @InjectMocks private CrudHandler handler;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.openMocks(this).close();
  }

  private Get prepareGet() {
    Key partitionKey = new Key(ANY_NAME_1, ANY_TEXT_1);
    Key clusteringKey = new Key(ANY_NAME_2, ANY_TEXT_2);
    return new Get(partitionKey, clusteringKey)
        .forNamespace(ANY_KEYSPACE_NAME)
        .forTable(ANY_TABLE_NAME);
  }

  private Scan prepareScan() {
    Key partitionKey = new Key(ANY_NAME_1, ANY_TEXT_1);
    return new Scan(partitionKey).forNamespace(ANY_KEYSPACE_NAME).forTable(ANY_TABLE_NAME);
  }

  private Result prepareResult(TransactionState state) {
    return new ResultImpl(
        ImmutableMap.<String, Value<?>>builder()
            .put(ANY_NAME_1, new TextValue(ANY_NAME_1, ANY_TEXT_1))
            .put(ANY_NAME_2, new TextValue(ANY_NAME_2, ANY_TEXT_2))
            .put(Attribute.ID, Attribute.toIdValue(ANY_ID_2))
            .put(Attribute.STATE, Attribute.toStateValue(state))
            .put(Attribute.VERSION, Attribute.toVersionValue(2))
            .put(Attribute.BEFORE_ID, Attribute.toBeforeIdValue(ANY_ID_1))
            .put(Attribute.BEFORE_STATE, Attribute.toBeforeStateValue(TransactionState.COMMITTED))
            .put(Attribute.BEFORE_VERSION, Attribute.toBeforeVersionValue(1))
            .build(),
        TABLE_METADATA);
  }

  private void assertResult(Optional<Result> actual, Optional<Result> expected) {
    assertThat(actual.isPresent() == expected.isPresent()).isTrue();
    actual.ifPresent(a -> assertResult(a, expected.get()));
  }

  private void assertResult(Result actual, Result expected) {
    Result a =
        actual instanceof FilteredResult ? ((FilteredResult) actual).getOriginalResult() : actual;
    Result e =
        expected instanceof FilteredResult
            ? ((FilteredResult) expected).getOriginalResult()
            : expected;
    assertThat(a.getValues()).isEqualTo(e.getValues());
  }

  private void assertResults(List<Result> actual, List<Result> expected) {
    assertThat(actual.size()).isEqualTo(expected.size());
    for (int i = 0; i < actual.size(); i++) {
      assertResult(actual.get(i), expected.get(i));
    }
  }

  @Test
  public void get_KeyExistsInSnapshot_ShouldReturnFromSnapshot() throws CrudException {
    // Arrange
    Get get = prepareGet();
    Optional<Result> expected = Optional.of(prepareResult(TransactionState.COMMITTED));
    when(snapshot.containsKeyInReadSet(new Snapshot.Key(get))).thenReturn(true);
    when(snapshot.get(get)).thenReturn(expected);

    // Act
    Optional<Result> actual = handler.get(get);

    // Assert
    assertResult(actual, expected);
  }

  @Test
  public void
      get_KeyNotExistsInSnapshotAndRecordInStorageCommitted_ShouldReturnFromStorageAndUpdateSnapshot()
          throws CrudException, ExecutionException {
    // Arrange
    Get get = prepareGet();
    result = prepareResult(TransactionState.COMMITTED);
    when(snapshot.containsKeyInReadSet(new Snapshot.Key(get))).thenReturn(false);
    when(storage.get(get)).thenReturn(Optional.of(result));
    doNothing().when(snapshot).put(any(), any(), any());
    when(snapshot.get(get)).thenReturn(Optional.of(result));

    // Act
    Optional<Result> actual = handler.get(get);

    // Assert
    TransactionResult expected = new TransactionResult(result);
    assertResult(actual, Optional.of(expected));
    verify(storage).get(get);
    verify(snapshot).put(new Snapshot.Key(get), get, Optional.of(expected));
  }

  @Test
  public void
      get_KeyNotExistsInSnapshotAndRecordInStorageNotCommitted_ShouldThrowUncommittedRecordException()
          throws ExecutionException, CrudException {
    // Arrange
    Get get = prepareGet();
    Optional<Result> expected = Optional.of(prepareResult(TransactionState.PREPARED));
    when(snapshot.get(get)).thenReturn(Optional.empty());
    when(storage.get(get)).thenReturn(expected);

    // Act Assert
    assertThatThrownBy(() -> handler.get(get)).isInstanceOf(UncommittedRecordException.class);
  }

  @Test
  public void get_KeyNeitherExistsInSnapshotNorInStorage_ShouldReturnEmpty()
      throws CrudException, ExecutionException {
    // Arrange
    Get get = prepareGet();
    when(snapshot.containsKeyInReadSet(new Snapshot.Key(get))).thenReturn(false);
    when(storage.get(get)).thenReturn(Optional.empty());
    when(snapshot.get(get)).thenReturn(Optional.empty());

    // Act
    Optional<Result> result = handler.get(get);

    // Assert
    assertThat(result).isNotPresent();
    verify(snapshot).put(new Snapshot.Key(get), get, Optional.empty());
  }

  @Test
  public void get_KeyNotExistsInCrudSetAndExceptionThrownInStorage_ShouldThrowCrudException()
      throws ExecutionException, CrudException {
    // Arrange
    Get get = prepareGet();
    when(snapshot.get(get)).thenReturn(Optional.empty());
    ExecutionException toThrow = mock(ExecutionException.class);
    when(storage.get(get)).thenThrow(toThrow);

    // Act Assert
    assertThatThrownBy(() -> handler.get(get)).isInstanceOf(CrudException.class).hasCause(toThrow);
  }

  @Test
  public void get_ScanCalledAfterGet_ShouldReturnFromSnapshot()
      throws ExecutionException, CrudException {
    // Arrange
    Get get = prepareGet();
    result = prepareResult(TransactionState.COMMITTED);
    when(snapshot.containsKeyInReadSet(new Snapshot.Key(get))).thenReturn(false).thenReturn(true);
    when(storage.get(get)).thenReturn(Optional.of(result));
    doNothing().when(snapshot).put(any(), any(), any());
    when(snapshot.get(get)).thenReturn(Optional.of(result));

    Scan scan = prepareScan();
    when(snapshot.containsKeyInScanSet(scan)).thenReturn(false);
    when(storage.scan(scan)).thenReturn(scanner);
    when(scanner.iterator()).thenReturn(Collections.singletonList(result).iterator());
    when(snapshot.scan(scan)).thenReturn(Collections.singletonList(result));

    // Act
    Optional<Result> getResult = handler.get(get);
    List<Result> results = handler.scan(scan);

    // Assert
    TransactionResult expected = new TransactionResult(result);
    assertThat(getResult).isPresent();
    assertResult(getResult.get(), results.get(0));
    verify(snapshot).put(new Snapshot.Key(get), get, Optional.of(expected));
    Snapshot.Key key =
        new Snapshot.Key(
            scan.forNamespace().get(),
            scan.forTable().get(),
            scan.getPartitionKey(),
            result.getClusteringKey().get());
    verify(snapshot, never()).put(key, scan, Optional.of(expected));
  }

  @Test
  public void scan_ResultGivenFromStorage_ShouldUpdateSnapshotAndReturn()
      throws ExecutionException, CrudException {
    // Arrange
    Scan scan = prepareScan();
    when(snapshot.containsKeyInScanSet(scan)).thenReturn(false);
    when(storage.scan(scan)).thenReturn(scanner);
    result = prepareResult(TransactionState.COMMITTED);
    when(scanner.iterator()).thenReturn(Collections.singletonList(result).iterator());
    Snapshot.Key key =
        new Snapshot.Key(
            scan.forNamespace().get(),
            scan.forTable().get(),
            scan.getPartitionKey(),
            result.getClusteringKey().get());
    when(snapshot.containsKeyInReadSet(key)).thenReturn(false);
    doNothing().when(snapshot).put(any(), any(), any());
    when(snapshot.scan(scan)).thenReturn(Collections.singletonList(result));

    // Act
    List<Result> results = handler.scan(scan);

    // Assert
    TransactionResult expected = new TransactionResult(result);
    assertThat(results.size()).isEqualTo(1);
    assertResult(results.get(0), expected);
    verify(snapshot).put(key, scan, Optional.of(expected));
    verify(snapshot).put(scan, Collections.singletonList(key));
  }

  @Test
  public void
      scan_PreparedResultGivenFromStorage_ShouldNeverUpdateSnapshotThrowUncommittedRecordException()
          throws ExecutionException, CrudException {
    // Arrange
    Scan scan = prepareScan();
    result = prepareResult(TransactionState.PREPARED);
    when(scanner.iterator()).thenReturn(Collections.singletonList(result).iterator());
    when(storage.scan(scan)).thenReturn(scanner);

    // Act
    assertThatThrownBy(() -> handler.scan(scan)).isInstanceOf(UncommittedRecordException.class);

    // Assert
    verify(snapshot, never()).put(any(), any(), any());
    verify(snapshot, never()).put(any(), anyList());
  }

  @Test
  public void scan_CalledTwice_SecondTimeShouldReturnTheSameFromSnapshot()
      throws ExecutionException, CrudException {
    // Arrange
    Scan scan = prepareScan();
    when(snapshot.containsKeyInScanSet(scan)).thenReturn(false).thenReturn(true);
    when(storage.scan(scan)).thenReturn(scanner);
    result = prepareResult(TransactionState.COMMITTED);
    when(scanner.iterator()).thenReturn(Collections.singletonList(result).iterator());
    Snapshot.Key key =
        new Snapshot.Key(
            scan.forNamespace().get(),
            scan.forTable().get(),
            scan.getPartitionKey(),
            result.getClusteringKey().get());
    when(snapshot.containsKeyInReadSet(key)).thenReturn(false);
    doNothing().when(snapshot).put(any(), any(), any());
    when(snapshot.scan(scan)).thenReturn(Collections.singletonList(result));

    // Act
    List<Result> results1 = handler.scan(scan);
    List<Result> results2 = handler.scan(scan);

    // Assert
    TransactionResult expected = new TransactionResult(result);
    assertThat(results1.size()).isEqualTo(1);
    assertResult(results1.get(0), expected);
    assertResults(results1, results2);
    verify(snapshot).put(key, scan, Optional.of(expected));
    verify(snapshot).put(scan, Collections.singletonList(key));
  }

  @Test
  public void scan_CalledTwiceUnderRealSnapshot_SecondTimeShouldReturnTheSameFromSnapshot()
      throws ExecutionException, CrudException {
    // Arrange
    Scan scan = prepareScan();
    result = prepareResult(TransactionState.COMMITTED);
    TransactionalTableMetadataManager tableMetadataManager =
        mock(TransactionalTableMetadataManager.class);
    TransactionalTableMetadata metadata = mock(TransactionalTableMetadata.class);
    snapshot =
        new Snapshot(
            ANY_TX_ID,
            Isolation.SNAPSHOT,
            SerializableStrategy.EXTRA_WRITE,
            tableMetadataManager,
            parallelExecutor);
    handler = new CrudHandler(storage, snapshot);

    when(tableMetadataManager.getTransactionalTableMetadata(any())).thenReturn(metadata);
    when(metadata.getTableMetadata()).thenReturn(TABLE_METADATA);
    when(scanner.iterator()).thenReturn(Collections.singletonList(result).iterator());
    when(storage.scan(scan)).thenReturn(scanner);

    // Act
    List<Result> results1 = handler.scan(scan);
    List<Result> results2 = handler.scan(scan);

    // Assert
    TransactionResult expected = new TransactionResult(result);
    assertThat(results1.size()).isEqualTo(1);
    assertResult(results1.get(0), expected);
    assertResults(results1, results2);
  }

  @Test
  public void scan_GetCalledAfterScan_ShouldReturnFromSnapshot()
      throws ExecutionException, CrudException {
    // Arrange
    Scan scan = prepareScan();
    when(snapshot.containsKeyInScanSet(scan)).thenReturn(false);
    when(storage.scan(scan)).thenReturn(scanner);
    result = prepareResult(TransactionState.COMMITTED);
    when(scanner.iterator()).thenReturn(Collections.singletonList(result).iterator());
    Snapshot.Key key =
        new Snapshot.Key(
            scan.forNamespace().get(),
            scan.forTable().get(),
            scan.getPartitionKey(),
            result.getClusteringKey().get());
    when(snapshot.containsKeyInReadSet(key)).thenReturn(false).thenReturn(true);
    doNothing().when(snapshot).put(any(), any(), any());
    when(snapshot.scan(scan)).thenReturn(Collections.singletonList(result));
    Get get = prepareGet();
    when(snapshot.get(get)).thenReturn(Optional.of(result));

    // Act
    List<Result> results = handler.scan(scan);
    Optional<Result> getResult = handler.get(get);

    // Assert
    assertThat(getResult).isPresent();
    assertResult(results.get(0), getResult.get());
    verify(storage, never()).get(any());
    TransactionResult expected = new TransactionResult(result);
    verify(snapshot).put(key, scan, Optional.of(expected));
    verify(snapshot, never()).put(new Snapshot.Key(get), get, Optional.of(expected));
  }

  @Test
  public void scan_GetCalledAfterScanUnderRealSnapshot_ShouldReturnFromSnapshot()
      throws ExecutionException, CrudException {
    // Arrange
    Scan scan = prepareScan();
    result = prepareResult(TransactionState.COMMITTED);
    TransactionalTableMetadataManager tableMetadataManager =
        mock(TransactionalTableMetadataManager.class);
    TransactionalTableMetadata metadata = mock(TransactionalTableMetadata.class);
    snapshot =
        new Snapshot(
            ANY_TX_ID,
            Isolation.SNAPSHOT,
            SerializableStrategy.EXTRA_WRITE,
            tableMetadataManager,
            parallelExecutor);
    handler = new CrudHandler(storage, snapshot);

    when(tableMetadataManager.getTransactionalTableMetadata(any())).thenReturn(metadata);
    when(metadata.getTableMetadata()).thenReturn(TABLE_METADATA);
    when(scanner.iterator()).thenReturn(Collections.singletonList(result).iterator());
    when(storage.scan(scan)).thenReturn(scanner);

    // Act
    List<Result> results = handler.scan(scan);
    Optional<Result> getResult = handler.get(prepareGet());

    // Assert
    assertThat(getResult).isPresent();
    assertResult(results.get(0), getResult.get());
  }
}
