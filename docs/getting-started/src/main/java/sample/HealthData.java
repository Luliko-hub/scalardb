package healthdata;

import com.scalar.db.api.DistributedTransaction;
import com.scalar.db.api.DistributedTransactionManager;
import com.scalar.db.api.Get;
import com.scalar.db.api.Put;
import com.scalar.db.api.Scan;
import com.scalar.db.api.Result;
import com.scalar.db.exception.transaction.TransactionException;
import com.scalar.db.io.Key;
import com.scalar.db.service.TransactionFactory;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.sql.Timestamp;

public class HealthData {
  private static final String CUSTOMER_NAMESPACE = "customer";
  private static final String MACHINE_NAMESPACE = "machine";
  private static final String CUSTOMER_TABLENAME = "customers";
  private static final String BODY_COMPOSITION_METER_TABLENAME = "body-composition-meter";
  private static final String WEARABLE_WATCH_TABLENAME = "wearable-watch";
  private static final String MACHINE_USAGE_TABLENAME = "machine-usage";
  private static final String CUSTOMER_ID = "customer-id";
  private static final String NAME = "name";
  private static final String ADDRESS = "address";
  private static final String BIRTH_DATE = "birth-date";
  private static final String TARGET_WEIGHT = "target-weight";
  private static final String BODY_COMPOSITION_TIMESTAMP = "timestamp";
  private static final String WEIGHT = "weight";
  private static final String MUSCLE_MASS = "muscle-mass";
  private static final String BODY_FAT_PERCENTAGE = "body-fat-percentage";
  private static final String WEARABLE_WATCH_TIMESTAMP = "timestamp";
  private static final String HEART_RATE = "heart-rate";
  private static final String NUMBER_OF_STEPS = "number-of-steps";
  private static final String MACHINE_START_TIME = "start-time";
  private static final String MACHINE_END_TIME = "end-time";
  private static final String MACHINE_ID = "machine-id";


  private final DistributedTransactionManager manager;

  public HealthData(String scalarDBProperties) throws IOException {
    TransactionFactory factory = TransactionFactory.create(scalarDBProperties);
    manager = factory.getTransactionManager();
  }


  public void loadInitialData() throws TransactionException {
  DistributedTransaction tx = manager.start();
  try {
    // "2023-07-11 23:30:30" の間の空白がコマンドラインからの入力だとダメっぽい
    loadCustomerIfNotExists(tx, "1", "TaroYamada", "Tokyo", "2000-01-12", 53.2);    
    loadCustomerIfNotExists(tx, "2", "HanakoTanaka", "Tokyo", "1996-01-14", 52.2);    
    loadCustomerIfNotExists(tx, "3", "YukaSuzuki", "Tokyo", "2000-02-22", 52.2);    
    loadCustomerIfNotExists(tx, "4", "HumioSato", "Tokyo", "2003-05-14", 51.2);   
    loadBodyCompositionIfNotExists(tx, "1", "2023-07-11 23:30:30", 54.1, 28.0, 20.1);    
    loadBodyCompositionIfNotExists(tx, "1", "2023-07-10 23:30:30", 54.3, 28.0, 21.1);  
    loadBodyCompositionIfNotExists(tx, "1", "2023-07-09 12:30:30", 55.3, 28.0, 20.1); 

    loadBodyCompositionIfNotExists(tx, "2", "2023-07-09 12:30:30", 45.3, 28.0, 14.1);  
    loadBodyCompositionIfNotExists(tx, "2", "2023-07-08 21:30:30", 43.3, 28.0, 184.1);  
    loadBodyCompositionIfNotExists(tx, "2", "2023-07-07 23:30:30", 44.1, 28.0, 14.1);    
    loadBodyCompositionIfNotExists(tx, "2", "2023-07-06 23:30:30", 45.3, 28.0, 13.9);  
    
    loadBodyCompositionIfNotExists(tx, "3", "2023-07-12 21:30:30", 41.3, 28.2, 18.0);  
    loadBodyCompositionIfNotExists(tx, "3", "2023-07-11 12:30:30", 44.3, 28.2, 18.1);    
    loadBodyCompositionIfNotExists(tx, "3", "2023-07-10 23:30:30", 43.3, 28.1, 18.0);  
    loadBodyCompositionIfNotExists(tx, "3", "2023-07-09 23:30:30", 44.1, 28.2, 18.1);    
    loadBodyCompositionIfNotExists(tx, "3", "2023-07-08 21:30:30", 43.3, 28.1, 18.0);  

    loadBodyCompositionIfNotExists(tx, "4", "2023-07-03 12:30:30", 55.3, 26.0, 19.0);
    loadBodyCompositionIfNotExists(tx, "4", "2023-07-02 23:30:30", 54.3, 26.0, 19.2);  
    loadBodyCompositionIfNotExists(tx, "4", "2023-07-01 23:30:30", 5.1, 26.0, 19.1);    

    loadWearableWatchIfNotExists(tx, "1", "2023-07-11 23:30:30", 87.2, 90);
    loadWearableWatchIfNotExists(tx, "2", "2023-07-11 21:30:30", 87.2, 90);
    loadWearableWatchIfNotExists(tx, "1", "2023-07-10 23:30:30", 87.2, 100);
    loadWearableWatchIfNotExists(tx, "2", "2023-07-10 21:30:30", 87.2, 100);
    loadWearableWatchIfNotExists(tx, "1", "2023-07-09 23:30:30", 87.2, 90);
    loadWearableWatchIfNotExists(tx, "2", "2023-07-09 21:30:30", 87.2, 90);
    loadWearableWatchIfNotExists(tx, "1", "2023-07-09 23:30:30", 87.2, 100);
    loadWearableWatchIfNotExists(tx, "2", "2023-07-10 21:30:30", 87.2, 100);
    loadMachineUsageIfNotExists(tx, "1", "2023-07-11 23:30:30", "1", "2023-07-11 24:30:30");
    loadMachineUsageIfNotExists(tx, "2", "2023-07-11 21:30:30", "2", "2023-07-12 24:30:30");
    loadMachineUsageIfNotExists(tx, "1", "2023-07-10 23:30:30", "2", "2023-07-10 24:30:30");
    loadMachineUsageIfNotExists(tx, "2", "2023-07-10 21:30:30", "2", "2023-07-09 24:30:30");
    loadMachineUsageIfNotExists(tx, "1", "2023-07-09 23:30:30", "1", "2023-07-11 24:30:30");
    loadMachineUsageIfNotExists(tx, "2", "2023-07-09 21:30:30", "2", "2023-07-12 24:30:30");
    loadMachineUsageIfNotExists(tx, "1", "2023-07-08 23:30:30", "2", "2023-07-10 24:30:30");
    loadMachineUsageIfNotExists(tx, "2", "2023-07-08 21:30:30", "2", "2023-07-09 24:30:30");

    tx.commit();
  } catch (Exception e) {
      tx.abort();
      throw e;
    }
    
  }

  private void loadCustomerIfNotExists(
      DistributedTransaction transaction,
      String customerId,
      String name,
      String address,
      String birthDate,
      Double weight
      )
      throws TransactionException {
    Optional<Result> customer =
        transaction.get(
            Get.newBuilder()
                .namespace(CUSTOMER_NAMESPACE)
                .table(CUSTOMER_TABLENAME)
                .partitionKey(Key.ofText(CUSTOMER_ID, customerId))
                .build());
    if (!customer.isPresent()) {
      transaction.put(
          Put.newBuilder()
              .namespace(CUSTOMER_NAMESPACE)
              .table(CUSTOMER_TABLENAME)
              .partitionKey(Key.ofText(CUSTOMER_ID, customerId))
              .textValue(NAME, name)
              .textValue(ADDRESS, address)
              .textValue(BIRTH_DATE, birthDate)
              .doubleValue(TARGET_WEIGHT, weight)
              .build());
    }
  }  

  private void loadBodyCompositionIfNotExists(
      DistributedTransaction transaction,
      String customerId,
      String timestamp,
      Double weight,
      Double muscle_mass,
      Double body_fat_percentage
      )
      throws TransactionException {
    Optional<Result> customer =
        transaction.get(
            Get.newBuilder()
                .namespace(CUSTOMER_NAMESPACE)
                .table(BODY_COMPOSITION_METER_TABLENAME)
                .partitionKey(Key.ofText(CUSTOMER_ID, customerId))
                .clusteringKey(Key.ofText(BODY_COMPOSITION_TIMESTAMP, timestamp))
                .build());
    if (!customer.isPresent()) {
      transaction.put(
          Put.newBuilder()
              .namespace(CUSTOMER_NAMESPACE)
              .table(BODY_COMPOSITION_METER_TABLENAME)
              .partitionKey(Key.ofText(CUSTOMER_ID, customerId))
              .clusteringKey(Key.ofText(BODY_COMPOSITION_TIMESTAMP, timestamp))
              .doubleValue(WEIGHT, weight)
              .doubleValue(MUSCLE_MASS, muscle_mass)
              .doubleValue(BODY_FAT_PERCENTAGE, body_fat_percentage)
              .build());
    }
  }  

  private void loadWearableWatchIfNotExists(
      DistributedTransaction transaction,
      String customerId,
      String timestamp,
      Double heartRate,
      int numberOfSteps
      )
      throws TransactionException {
    Optional<Result> customer =
        transaction.get(
            Get.newBuilder()
                .namespace(CUSTOMER_NAMESPACE)
                .table(WEARABLE_WATCH_TABLENAME)
                .partitionKey(Key.ofText(CUSTOMER_ID, customerId))
                .clusteringKey(Key.ofText(WEARABLE_WATCH_TIMESTAMP, timestamp))
                .build());
    if (!customer.isPresent()) {
      transaction.put(
          Put.newBuilder()
              .namespace(CUSTOMER_NAMESPACE)
              .table(WEARABLE_WATCH_TABLENAME)
              .partitionKey(Key.ofText(CUSTOMER_ID, customerId))
              .clusteringKey(Key.ofText(WEARABLE_WATCH_TIMESTAMP, timestamp))
              .doubleValue(HEART_RATE, heartRate)
              .intValue(NUMBER_OF_STEPS, numberOfSteps)
              .build());
    }
  }  

  private void loadMachineUsageIfNotExists(
      DistributedTransaction transaction,
      String customerId,
      String start_time,
      String machineId,
      String end_time
      )
      throws TransactionException {
    Optional<Result> customer =
        transaction.get(
            Get.newBuilder()
                .namespace(MACHINE_NAMESPACE)
                .table(MACHINE_USAGE_TABLENAME)
                .partitionKey(Key.ofText(CUSTOMER_ID, customerId))
                .clusteringKey(Key.ofText(MACHINE_START_TIME, start_time))
                .build());
    if (!customer.isPresent()) {
      transaction.put(
          Put.newBuilder()
              .namespace(MACHINE_NAMESPACE)
              .table(MACHINE_USAGE_TABLENAME)
              .partitionKey(Key.ofText(CUSTOMER_ID, customerId))
              .clusteringKey(Key.ofText(MACHINE_START_TIME, start_time))
              .textValue(MACHINE_ID, machineId)
              .textValue(MACHINE_END_TIME, end_time)
              .build());
    }
  }  

  
  public String registerCustomerName(String name) throws TransactionException {
      // Start a transaction
    DistributedTransaction tx = manager.start();
    try {
      // String id = UUID.randomUUID().toString();
      String id = "1";

      // Update the name
      Put put =
          Put.newBuilder()
              .namespace(CUSTOMER_NAMESPACE)
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
              .namespace(CUSTOMER_NAMESPACE)
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

  /* 
  input
    id: string
    timestamp: string
    ex) "2019-06-30 23:10:26.947" 参考: https://magazine.techacademy.jp/magazine/22253
  output
    Map<String, Double> : body data (weight (kg), muscle_mass (kg), body_fat_percentage (%)): Double
  */
  public Map<String, Double> getBodyComposition(String id, String timestamp) throws TransactionException {
    // Start a transaction
    DistributedTransaction tx = manager.start();

    // idとtimestampからbody dataを取得
    try {
      // Retrieve the current balances for id
      Get get =
          Get.newBuilder()
              .namespace(CUSTOMER_NAMESPACE)
              .table(BODY_COMPOSITION_METER_TABLENAME)
              .partitionKey(Key.ofText(CUSTOMER_ID, id))
              .clusteringKey(Key.ofText(BODY_COMPOSITION_TIMESTAMP, timestamp))
              .build();
      Optional<Result> result = tx.get(get);

      Double weight = 0.0;
      Double muscle_mass = 0.0;
      Double body_fat_percentage = 0.0;

      if (result.isPresent()) {
        weight = result.get().getDouble(WEIGHT);
        muscle_mass = result.get().getDouble(MUSCLE_MASS);
        body_fat_percentage = result.get().getDouble(BODY_FAT_PERCENTAGE);
      }

      Map<String, Double> body_composition = new HashMap<>();
      body_composition.put("weight", weight);
      body_composition.put("muscle_mass", muscle_mass);
      body_composition.put("body_fat_percentage", body_fat_percentage);


      // Commit the transaction
      tx.commit();

      return body_composition;
    } catch (Exception e) {
      tx.abort();
      throw e;
    }
  }

  /* 
  input
    id: string
    start_time: string
    end_time: string
    ex) "2019-06-30 23:10:26.947" 参考: https://magazine.techacademy.jp/magazine/22253
  output
    Map<String, Double> [] : body data (weight (kg), muscle_mass (kg), body_fat_percentage (%)): Double
  */
  public List<Map<String, Double>> getBodyCompositions(String id, String start_time, String end_time) throws TransactionException {
    // Start a transaction
    DistributedTransaction tx = manager.start();

    // idとtimestampからbody dataを取得
    try {
      // Retrieve the current balances for id
      Scan scan =
          Scan.newBuilder()
              .namespace(CUSTOMER_NAMESPACE)
              .table(BODY_COMPOSITION_METER_TABLENAME)
              .partitionKey(Key.ofText(CUSTOMER_ID, id))
              .build();

      List<Result> results = tx.scan(scan);
      List<Map<String, Double>> body_compositions = new ArrayList<>();

      Timestamp start_timestamp = Timestamp.valueOf(start_time);
      Timestamp end_timestamp = Timestamp.valueOf(end_time);

      body_compositions = results.stream().filter(result -> {
                        Timestamp timestamp = Timestamp.valueOf(result.getText(BODY_COMPOSITION_TIMESTAMP));
                        return timestamp.compareTo(start_timestamp) > 0 && timestamp.compareTo(end_timestamp) < 0;
      })
      .map(result -> {
        Map<String, Double> body_composition = new HashMap<>();
        Double weight = result.getDouble(WEIGHT);
        Double muscle_mass = result.getDouble(MUSCLE_MASS);
        Double body_fat_percentage = result.getDouble(BODY_FAT_PERCENTAGE);
        body_composition.put("weight", weight);
        body_composition.put("muscle_mass", muscle_mass);
        body_composition.put("body_fat_percentage", body_fat_percentage);
        return body_composition;
      })
      .collect(Collectors.toList());


      // Commit the transaction
      tx.commit();

      return body_compositions;
    } catch (Exception e) {
      tx.abort();
      throw e;
    }
  }

  /* 
  input
    id: string
  output
    target_weight (kg): Double 
  */
  public Double getTargetWeight(String id) throws TransactionException {
    // Start a transaction
    DistributedTransaction tx = manager.start();
    try {
      // Retrieve the current balances for id
      Get get =
          Get.newBuilder()
              .namespace(CUSTOMER_NAMESPACE)
              .table(CUSTOMER_TABLENAME)
              .partitionKey(Key.ofText(CUSTOMER_ID, id))
              .build();
      Optional<Result> result = tx.get(get);

      Double target_weight = null;
      if (result.isPresent()) {
        target_weight = result.get().getDouble(TARGET_WEIGHT);
      }

      // Commit the transaction
      tx.commit();
      return target_weight;

    } catch (Exception e) {
      tx.abort();
      throw e;
    }
    
  }

  /* 
  input
    id: string
    start_time: string
    end_time: string
    output
    running_times[ms]: long[]
  */
  public List<Long> getRunningTimes(String id, String start_time, String end_time) throws TransactionException {
    // Start a transaction
    DistributedTransaction tx = manager.start();
    try {
      // Retrieve the current balances for id
      Scan scan =
          Scan.newBuilder()
              .namespace(MACHINE_NAMESPACE)
              .table(MACHINE_USAGE_TABLENAME)
              .partitionKey(Key.ofText(CUSTOMER_ID, id))
              .build();
      List<Result> results = tx.scan(scan);
      List<Long> running_times = new ArrayList<>();

      Timestamp start_timestamp = Timestamp.valueOf(start_time);
      Timestamp end_timestamp = Timestamp.valueOf(end_time);

      running_times = results.stream().filter(result -> {
                      Timestamp timestamp = Timestamp.valueOf(result.getText(MACHINE_START_TIME));
                      return timestamp.compareTo(start_timestamp) > 0 && timestamp.compareTo(end_timestamp) < 0;
      }).map(result -> {
        Timestamp machineStart_time = Timestamp.valueOf(result.getText(MACHINE_START_TIME));
        Timestamp machineEnd_time = Timestamp.valueOf(result.getText(MACHINE_END_TIME));
        Long runningTime = machineEnd_time.getTime() - machineStart_time.getTime();
        return runningTime;
      })
      .collect(Collectors.toList());

      // Commit the transaction
      tx.commit();
      return running_times;

    } catch (Exception e) {
      tx.abort();
      throw e;
    }
  }

  /* 
  input
    id: string
    start_time: string
    end_time: string
  output
    numberOfSteps (歩): int[]
  */
  public List<Integer> getNumberOfSteps(String id, String start_time, String end_time) throws TransactionException{
    // Start a transaction
    DistributedTransaction tx = manager.start();
    try {
      Scan scan =
          Scan.newBuilder()
              .namespace(CUSTOMER_NAMESPACE)
              .table(WEARABLE_WATCH_TABLENAME)
              .partitionKey(Key.ofText(CUSTOMER_ID, id))
              .build();

      List<Result> results = tx.scan(scan);
      List<Integer> number_of_steps = new ArrayList<>();

      Timestamp start_timestamp = Timestamp.valueOf(start_time);
      Timestamp end_timestamp = Timestamp.valueOf(end_time);

      number_of_steps = results.stream().filter(result -> {
                        Timestamp timestamp = Timestamp.valueOf(result.getText(WEARABLE_WATCH_TIMESTAMP));
                        return timestamp.compareTo(start_timestamp) > 0 && timestamp.compareTo(end_timestamp) < 0;
      })
      .map(result -> result.getInt(NUMBER_OF_STEPS))
      .collect(Collectors.toList());
                                  
      // Commit the transaction
      tx.commit();

      return number_of_steps;

    } catch (Exception e) {
      tx.abort();
      throw e;
    }

  }


  public void close() {
    manager.close();
  }

}
