package healthdata;

import java.io.File;

public class HealthDataMain {
  public static void main(String[] args) throws Exception{
    String action = null;
    String id = null;
    String name = null;
    String scalarDBProperties = null;

    for (int i = 0; i < args.length; ++i) {
      if ("-action".equals(args[i])) {
        action = args[++i];    
      } else if ("-id".equals(args[i])) {
        id = args[++i];
      } else if ("-name".equals(args[i])) {
        name = args[++i];
      } else if ("-help".equals(args[i])) {
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
    healthData.close();
  }

  private static void printUsageAndExit() {
      System.err.println(
        "ElectronicMoneyMain -action getName [-id id (needed for getName)]");
      System.exit(1);
  }

}
