import combinatorial.CTModel;
import combinatorial.TestSuite;
import generator.AETG;
import handler.CH_MFTVerifier;
import util.computeCoverage;
import java.util.ArrayList;
import java.util.Arrays;

public class OneTestSuite {

  public static void run() {
	int[][] values = new int[2][];
	values[0] = new int[]{4, 4, 4, 4, 4, 4, 4, 4};
	values[1] = new int[]{6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6};
	for (int k = 0; k < values.length; k++) {
	  System.out.print("M");
	  if(k==0)
	    System.out.println(1);
	  else
	    System.out.println(5);
	  ArrayList<int[]> weight = new ArrayList<>();
	  for (int i = 0; i < values[k].length; i++) {
	    int[] tmp = new int[values[k][i]];
		Arrays.fill(tmp, 0);
		weight.add(tmp);
	  }

	  CTModel model = new CTModel(values[k].length, values[k], 2, null, new ArrayList<>(), weight, new CH_MFTVerifier());
	  int totalSize = 0;
	  int totalTuple = 0;
	  for (int j = 0; j < 30; j++) {
		TestSuite ts = new TestSuite();
		int len = 500;
		AETG gen = new AETG(len);
		((AETG) gen).setCANDIDATE(50);
		gen.generation(model, ts);
		ArrayList<int[]> ts1 = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
		  ts1.add(ts.suite.get(i).test);
		}
		computeCoverage cc = new computeCoverage(model, ts1);


		totalTuple += cc.getCovered();
		totalSize += ts.getTestSuiteSize();
	  }
	  System.out.println("size (mean): " + (float) totalSize / 30);
	  System.out.println("covered tuples (mean): " + (float) totalTuple / 30);
	}
  }
}
