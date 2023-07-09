package healthdata;

import com.scalar.db.api.DistributedTransaction;
import com.scalar.db.api.DistributedTransactionManager;
import com.scalar.db.api.Get;
import com.scalar.db.api.Put;
import com.scalar.db.api.Result;
import com.scalar.db.exception.transaction.TransactionException;
import com.scalar.db.io.Key;
import com.scalar.db.service.TransactionFactory;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public class HealthData {
  private static final String NAMESPACE = "customer";
  private static final String CUSTOMER_TABLENAME = "customers";
  private static final String BODY_COMPOSITION_METER_TABLENAME = "body-composition-meter";
  private static final String WEARABLE_WATCH_TABLENAME = "wearable-watch";
  private static final String CUSTOMER_ID = "customer-id";
  private static final String NAME = "name";

  private final DistributedTransactionManager manager;

  public HealthData(String scalarDBProperties) throws IOException {
    TransactionFactory factory = TransactionFactory.create(scalarDBProperties);
    manager = factory.getTransactionManager();
  }

  public String registerCustomerName(String name) throws TransactionException {
      // Start a transaction
    DistributedTransaction tx = manager.start();
    try {
      String id = UUID.randomUUID().toString();

      // Update the name
      Put put =
          Put.newBuilder()
              .namespace(NAMESPACE)
              .table(CUSTOMER_TABLENAME)
              .partitionKey(Key.ofText(CUSTOMER_ID, id))
              .textValue(NAME, name)
              .build();
      tx.put(put);

      // Commit the transaction (records are automatically recovered in case of failure)
      tx.commit();
      return id;
    } catch (Exception e) {
      tx.abort();
      throw e;
    }

  }

  public String getCustomerName(String id) throws TransactionException {
    // Start a transaction
    DistributedTransaction tx = manager.start();
    try {
      // Retrieve the current balances for id
      Get get =
          Get.newBuilder()
              .namespace(NAMESPACE)
              .table(CUSTOMER_TABLENAME)
              .partitionKey(Key.ofText(CUSTOMER_ID, id))
              .build();
      Optional<Result> result = tx.get(get);

      String name = null;
      if (result.isPresent()) {
        name = result.get().getText(NAME);
      }

      // Commit the transaction
      tx.commit();

      return name;
    } catch (Exception e) {
      tx.abort();
      throw e;
    }
  }  

  public void close() {
    manager.close();
  }

}
