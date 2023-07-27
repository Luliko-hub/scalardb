package healthdata;
// import healthdata.HealthData;
import com.scalar.db.api.DistributedTransaction;
import com.scalar.db.exception.transaction.TransactionException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.data.category.*;
import java.util.Map;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;


public class GUI extends JFrame  
{
    JLabel label;
    JTextField text;
    private JTextField idTextField;
    private JTextField selectTextField;

    private JButton button1;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private DefaultCategoryDataset dataset;
    private JFreeChart chart;
    private ChartPanel chartPanel;
    public Double target_weight;
  
    public GUI(String title,String scalarDBProperties) 
    {
        setTitle(title);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        HealthData healthData;
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        JPanel p1 = new JPanel();
        // public List output()
        p1.setBackground(Color.WHITE);
        JLabel label1 = new JLabel("ID入力:");
        JLabel select_label = new JLabel("表示データ");
        JLabel explanation_label = new JLabel("weight, muscle_mass,body_fat_percentage, run_time,NumberOfSteps の中から表示するデータを選択して入力");
        explanation_label.setVerticalAlignment(JLabel.CENTER);
        idTextField = new JTextField(10);
        selectTextField = new JTextField(10);
        button1 = new JButton("登録");

        button1.addActionListener
        (
            new ActionListener() 
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    String id  = idTextField.getText();
                    String select  = selectTextField.getText();
                    System.out.println(select+"_______________________");


                    try{

                        HealthData healthData = new HealthData(scalarDBProperties);
                        String start_time = "2023-06-31 23:30:29";
                        String end_time = "2023-07-15 23:30:31";
                        String timestamp ="2023-07-11 23:30:31.947";
                        List<Long> running_time = healthData.getRunningTimes(id, start_time, end_time);
                        List<Integer> numberOfSteps = healthData.getNumberOfSteps(id, start_time, end_time);
            
                        Map<String, Double>body_data = healthData.getBodyComposition(id, timestamp);  
                        List<Map<String, Double>> inputList = healthData.getBodyCompositions(id, start_time, end_time);  
                        Integer listsize =inputList.size();
                        List <Double> weight_list = new ArrayList <>();
                        List<Long> running_time_list = new ArrayList <>();
                        List<Integer> steps_list = new ArrayList <>();

                
                        for (int i = 0; i < listsize; i++)
                        {
                            Map<String, Double> data=inputList.get(i);
                            weight_list.add(data.get(select));
                        }
                        List use;
                  
                        Collections.reverse(weight_list);
                        if (select.equals("runtime"))

                        {
                            use =weight_list;

                        }
                        else if (select.equals("steps"))

                        {
                             use =weight_list;
    
                        }
                        else
                        {
                            use =weight_list;


                        }
                        // List use =weight_list;
                        createWeightChart(id,use,select);
                        System.out.print( id);
                        cardLayout.show(cardPanel, "panel2"); 

                  

                      } 
                      catch (TransactionException er) 
                      {
                          er.printStackTrace(); // Handle the exception appropriately
                      }
                      catch (IOException ie) 
                      {
                          ie.printStackTrace(); // Handle the exception appropriately
                      }
                
        

                }
            }
        );
        
        p1.add(label1);
        p1.add(idTextField );
        p1.add(select_label);
        p1.add(selectTextField );
        p1.add(button1);
        p1.add(explanation_label);
        // パネルをカードパネルに追加
        JPanel panel2 = new JPanel();
        panel2.setLayout(new BorderLayout());
        cardPanel.add(p1, "panel1");
        cardPanel.add(panel2, "panel2");
        JButton backButton = new JButton("戻る");

        backButton.addActionListener
        (
            new ActionListener() 
            {
                  // @Override
                public void actionPerformed(ActionEvent e) 
                { 
                    String id  = idTextField.getText();
                    try 
                    {
                        String scalarDBProperties = System.getProperty("user.dir") + File.separator + "scalardb.properties";
                        HealthData healthData = new HealthData(scalarDBProperties);
                        String name=healthData.getCustomerName(id);
                        String start_time = "2023-06-31 23:30:29";
                        String end_time = "2023-07-15 23:30:31";
                        cardLayout.show(cardPanel, "panel1"); 
                    } 
                    catch (TransactionException er) 
                    {
                        er.printStackTrace(); // Handle the exception appropriately
                    }
                    catch (IOException ie) 
                    {
                        ie.printStackTrace(); // Handle the exception appropriately
                    }
                }
            }
        );
      
        String id  = idTextField.getText();
        String print_target_weight = String.valueOf(target_weight);
        JLabel label2 = new JLabel("目標体重"+print_target_weight);
        panel2.add(backButton, BorderLayout.NORTH);
        panel2.add(label2);
        cardPanel.add(p1, "panel1");add(cardPanel);
        cardPanel.add(panel2, "panel2");
        cardPanel.add(panel2, "panel2");
      
        
    }
    private void createWeightChart(String id,List use,String thema) 
    {
        // 体重のデータを取得
        Map<String, Double> weightData = getWeightData(id,use);
        // データセットを作成
        dataset = new DefaultCategoryDataset();
        for (Map.Entry<String, Double> entry : weightData.entrySet()) 
        {
            String date = entry.getKey();
            double weight = entry.getValue();
            dataset.addValue(weight, thema, date);
        }

        // 折れ線グラフを作成
        chart = ChartFactory.createLineChart
        (
            "過去一週間の"+thema+"の変化",
            "日付",
            thema,
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );
        
          // グラフパネルを作成
        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(300, 500));
    
          // 画面2にグラフパネルを追加
          
        JPanel panel2 = (JPanel) cardPanel.getComponent(1);
        // panel2.add(label2);
        panel2.add(chartPanel, BorderLayout.SOUTH);
        panel2.revalidate();
        panel2.repaint();
    }
        
        //   // 体重のデータをダミーで取得するメソッド（実際のデータ取得処理に置き換えてください）
    private Map<String, Double> getWeightData(String id, List data) 

    {
        Map<String, Double> weightData = new LinkedHashMap<>();
        List date_list=new ArrayList <>();

        // Add days of the week to the list
        date_list.add("7日前");
        date_list.add("6日前");
        date_list.add("5日前");
        date_list.add("4日前");
        date_list.add("3日前");
        date_list.add("一昨日");
        date_list.add("昨日");

        Integer listsize =data.size();
        for (int i = 0; i < listsize; i++)
        {
            System.out.println(date_list.get(i)+"__________________________________");
            String str = date_list.get(i).toString();
            System.out.println(date_list.get(i)+"__________________________________");

            Double num =(double) data.get(i);
            weightData.put(str,num);
            System.out.println(str+num);
        }
          System.out.println(weightData +"__________________________________");
          return weightData;
    }
    
    


    
    public static void main(String[] args) throws Exception
    {

        HealthData healthData;
        String scalarDBProperties = System.getProperty("user.dir") + File.separator + "scalardb.properties";
        healthData = new HealthData(scalarDBProperties);
        String start_time = "2023-07-11 23:30:29";
        String end_time = "2023-07-11 23:30:31";
        String id = "1";
        List<Integer> number_of_steps_list = healthData.getNumberOfSteps(id, start_time, end_time);
        System.out.println("Number of steps for id " + id + " is " + number_of_steps_list);
        SwingUtilities.invokeLater
        (() -> 
            {
                GUI frame = new GUI("MyTitle",scalarDBProperties);
                frame.setVisible(true);
                  
            }
        );



    }
}