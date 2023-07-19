package healthdata;

import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HealthDataMain {
  public static void main(String[] args) throws Exception{
    String action = null;
    String id = null;
    String name = null;
    String scalarDBProperties = null;
    Map<String, Double> body_composition = new HashMap<>();
    Double weight = 0.0;
    Double muscle_mass = 0.0;
    Double body_fat_percentage = 0.0;
    String timestamp = null;
    String start_time = null;
    String end_time = null;
    Double target_weight = 0.0;
    int number_of_steps = 0;


    for (int i = 0; i < args.length; ++i) {
      if ("-action".equals(args[i])) {
        action = args[++i];    
      } else if ("-id".equals(args[i])) {
        id = args[++i];
      } else if ("-name".equals(args[i])) {
        name = args[++i];
      }
        else if ("-timestamp".equals(args[i])) {
        timestamp = args[++i];
      }
        else if ("-start_time".equals(args[i])) {
        start_time = args[++i];
      }
        else if ("-end_time".equals(args[i])) {
        end_time = args[++i];
      }
       else if ("-help".equals(args[i])) {
        printUsageAndExit();
        return;
      }
    }
    if (action == null) {
      printUsageAndExit();
      return;
    }

    HealthData healthData;
    if (scalarDBProperties != null) {
      healthData = new HealthData(scalarDBProperties);
    } else {
      scalarDBProperties = System.getProperty("user.dir") + File.separator + "scalardb.properties";
      healthData = new HealthData(scalarDBProperties);
    }

    
    if (action.equalsIgnoreCase("getName")) {
      if (id == null) {
        printUsageAndExit();
        return;
      }
      name = healthData.getCustomerName(id);
      System.out.println("The Name for" + id + " is " + name);
    }
    else if (action.equalsIgnoreCase("registerName")){
      if (name == null){
        printUsageAndExit();
        return;
      }
      id = healthData.registerCustomerName(name);
      System.out.println("Registered Name is" + name + ". id is " + id);
    }
    else if (action.equalsIgnoreCase("loadInitialData")){
      healthData.loadInitialData();
    }
    else if (action.equalsIgnoreCase("getBodyComposition")){
      if (id == null || timestamp == null){
        printUsageAndExit();
        return;
      }
      body_composition = healthData.getBodyComposition(id, timestamp);
      weight = body_composition.get("weight");
      muscle_mass = body_composition.get("muscle_mass");
      body_fat_percentage = body_composition.get("body_fat_percentage");
      System.out.println("Body data for id " + id + " is weight" + weight + " muscle_mass " + muscle_mass + " body_fat_percentage " + body_fat_percentage);
    }
    else if (action.equalsIgnoreCase("getBodyCompositions")){
      if (id == null){
        printUsageAndExit();
        return;
      }
      start_time = "2023-07-11 23:30:29";
      end_time = "2023-07-11 23:30:31";
      List<Map<String, Double>> body_compositions = healthData.getBodyCompositions(id, start_time, end_time);
      System.out.println("Body data for id " + id + " is " + body_compositions);

    }
    else if (action.equalsIgnoreCase("getTargetWeight")){
      if (id == null){
        printUsageAndExit();
        return;
      }
      target_weight = healthData.getTargetWeight(id);
      System.out.println("Target weight for id " + id + " is " + target_weight);
    }
    else if (action.equalsIgnoreCase("getNumberOfSteps")){
      if (id == null){
        printUsageAndExit();
        return;
      }
      start_time = "2023-07-11 23:30:29";
      end_time = "2023-07-11 23:30:31";
      List<Integer> number_of_steps_list = healthData.getNumberOfSteps(id, start_time, end_time);
      System.out.println("Number of steps for id " + id + " is " + number_of_steps_list);

    }
    else if (action.equalsIgnoreCase("getRunningTimes")){
      start_time = "2023-07-11 23:30:29";
      end_time = "2023-07-11 23:30:31";

      List<Long> running_time_list = healthData.getRunningTimes(id, start_time, end_time); 
      System.out.println("Running time for id " + id + " is " + running_time_list);
    }

    healthData.close();
  }

  private static void printUsageAndExit() {
      System.err.println(
        "ElectronicMoneyMain -action getName [-id id (needed for getName)]");
      System.exit(1);
  }

}
