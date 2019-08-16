package util;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * invoke PLEDGE tool to generate test cases
 */
public class forPLEDGE {

  // generate model file
  private static void genModelFile(int parameter, int[] values){
    StringBuilder par = new StringBuilder();
    StringBuilder cons = new StringBuilder();
    int index = 1, numOfCons = 0;
    for(int i = 0; i < parameter; i++){
      for(int j = 0; j < values[i]; j++) {
        par.append("c ").append(index + j).append(" ").append(i + 1).append(j).append("\n");
        cons.append(index + j).append(" ");
      }
      cons.append("0\n");
      numOfCons ++;
      for(int j = 0; j < values[i] - 1; j++)
        for(int k = j + 1; k < values[i]; k++) {
          cons.append("-").append(index + j).append(" -").append(index + k).append(" 0\n");
          numOfCons ++;
        }
      index += values[i];
    }
    //System.out.println(par);
    //System.out.println(cons);
    try {
      File writename = new File("model.dimacs");
      writename.createNewFile();
      BufferedWriter out = new BufferedWriter(new FileWriter(writename));
      String content = par.toString() + "p cnf " + (index - 1) + " " + numOfCons + "\n" + cons.toString();
      out.write(content);
      out.flush();
      out.close();
    }catch (Exception e){
      System.out.println(e.getMessage());
    }
  }

  // call PLEDGE tool through command line, and save result to out.txt
  private static void runPLEDGE(String modelFile){
    String command = "java -jar PLEDGE.jar generate_products -fm " + modelFile + " -o out.txt -timeAllowedMS 1000 -nbProds 10 -dimacs";
   // System.out.println(System.getProperty("user.dir"));
    Runtime runtime = Runtime.getRuntime();
    try{
      //System.out.println(command);
      runtime.exec(command).waitFor();
    }catch (Exception e){
      e.printStackTrace();
    }
  }

  // read file and transfer test cases
  private static ArrayList<int[]> readTS(String fileName){
    ArrayList<int[]> testsuite = new ArrayList<>();
    try {
      BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileName)),
              "UTF-8"));
      String lineTxt;
      Map<String,Integer> map = new HashMap<>();
      while ((lineTxt = br.readLine()) != null) {
        //System.out.println(lineTxt);
        if(lineTxt.contains("->")){
          String[] split = lineTxt.split("->");
          String tmp = split[1].substring(split[1].length() - 1, split[1].length());
          map.put(split[0], Integer.parseInt(tmp));
        }
        else{
          ArrayList<Integer> test = new ArrayList<>();
          String[] split = lineTxt.split(";");
          for(String s: split){
            if(!s.contains("-")){
              test.add(map.get(s));
            }
          }
          int[] newTest = test.stream().mapToInt(Integer::valueOf).toArray();
          testsuite.add(newTest);
        }
      }
      br.close();
    } catch (Exception e) {
      System.err.println("read errors :" + e);
    }
    return testsuite;
  }


  public static ArrayList<int[]> runPLEDGETool(int parameter, int[] values){
    genModelFile(parameter, values);
    runPLEDGE("model.dimacs");
    return readTS("out.txt");
  }

  public static void main(String[] args){
   ArrayList<int[]> res = forPLEDGE.runPLEDGETool(8, new int[]{4,4,4,4,4,4,4,4});
   for(int[] ts:res){
     for(int i = 0; i < ts.length; i++)
       System.out.print(ts[i] + " ");
     System.out.println();
   }
  }
}
