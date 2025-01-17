package healthdata;

import java.util.HashMap;
import java.util.Map;
import java.io.File;


public class HealthDataSample {
  /* 
  input
    id: string  
    timestamp: string
    ex) "2019-06-30 23:10:26.947" 参考: https://magazine.techacademy.jp/magazine/22253
  output
    Map<String, Object> : body data (weight (kg), muscle_mass (kg), body_fat_percentage (%)): double
  */
  public Map<String, Object> getBodyData(String id, String timestamp){

    // idとtimestampからbody dataを取得

    Map<String, Object> body_datas = new HashMap<>();
    body_datas.put("weight", 50.0);
    body_datas.put("muscle_mass", 30.0);
    body_datas.put("body_fat_percentage", 25.0);

    return body_datas;
  }

  /* 
  input
    id: string
  output
    target_weight (kg): double 
  */
  public double getTargetWeight(String id){
    // idから目標体重を取得
    double target_weight = 48.0;

    return target_weight;
  }

  /* 
  input
    id: string
    startTime: string
  output
    running_time: double 
  */
  public double getRunningTime(String id, String startTime){
    double running_time = 52;

    return running_time;
  }

  /* 
  input
    id: string
    timestamp: string
  output
    numberOfSteps (歩): int
  */

  public int getNumberOfSteps(String id, String timestamp){
    int number_of_steps = 14;
    return number_of_steps;
  }

}