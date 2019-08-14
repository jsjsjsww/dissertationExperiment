import combinatorial.CTModel;
import combinatorial.TestSuite;
import generator.AETG;
import handler.CH_MFTVerifier;
import util.faultCoverage;
import util.pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class Adaptive {
  private Model[] models;

  public Adaptive(Model[] models) {
	this.models = models;
  }

  public static void run() {
	ArrayList<pair[]> list = new ArrayList<>();

	// read file that contains fault tuples
	try {
	  BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("fault.txt")),
			  "UTF-8"));
	  String lineTxt;
	  while ((lineTxt = br.readLine()) != null) {
		if (!lineTxt.equals("")) {
		  String[] split = lineTxt.split(",");
		  pair[] tmp = new pair[split.length];
		  for (int i = 0; i < split.length; i++) {
			String[] pv = split[i].split("=");
			tmp[i] = new pair(Integer.parseInt(pv[0]), Integer.parseInt(pv[1]));
		  }
		  list.add(tmp);
		}
	  }
	  br.close();
	} catch (Exception e) {
	  System.err.println("read errors :" + e);
	}

	// the number of fault tuples of each CT model
	int[] split = new int[]{2, 10, 20, 2, 10, 20, 2, 10, 20, 40};
	Model[] models = new Model[10];
	int[] values1 = new int[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2};
	int[] values2 = new int[]{4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4};
	int[] values3 = new int[]{5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5};
	int index = 0;

	// set SUT
	pair[][] fault1 = new pair[split[0]][];
	for (int i = 0; i < split[0]; i++)
	  fault1[i] = list.get(index + i);
	index += split[0];
	models[0] = new Model(10, values1, fault1);
	//System.out.println(models[0].fault.length);

	pair[][] fault2 = new pair[split[1]][];
	for (int i = 0; i < split[1]; i++)
	  fault2[i] = list.get(index + i);
	index += split[1];
	models[1] = new Model(10, values1, fault2);

	pair[][] fault3 = new pair[split[2]][];
	for (int i = 0; i < split[2]; i++)
	  fault3[i] = list.get(index + i);
	index += split[2];
	models[2] = new Model(10, values1, fault3);

	pair[][] fault4 = new pair[split[3]][];
	for (int i = 0; i < split[3]; i++)
	  fault4[i] = list.get(index + i);
	index += split[3];
	models[3] = new Model(20, values2, fault4);

	pair[][] fault5 = new pair[split[4]][];
	for (int i = 0; i < split[4]; i++)
	  fault5[i] = list.get(index + i);
	index += split[4];
	models[4] = new Model(20, values2, fault5);


	pair[][] fault6 = new pair[split[5]][];
	for (int i = 0; i < split[5]; i++)
	  fault6[i] = list.get(index + i);
	index += split[5];
	models[5] = new Model(20, values2, fault6);

	pair[][] fault7 = new pair[split[6]][];
	for (int i = 0; i < split[6]; i++)
	  fault7[i] = list.get(index + i);
	index += split[6];
	models[6] = new Model(30, values3, fault7);


	pair[][] fault8 = new pair[split[7]][];
	for (int i = 0; i < split[7]; i++)
	  fault8[i] = list.get(index + i);
	index += split[7];
	models[7] = new Model(30, values3, fault8);

	pair[][] fault9 = new pair[split[8]][];
	for (int i = 0; i < split[8]; i++)
	  fault9[i] = list.get(index + i);
	index += split[8];
	models[8] = new Model(30, values3, fault9);

	pair[][] fault10 = new pair[split[9]][];
	for (int i = 0; i < split[9]; i++)
	  fault10[i] = list.get(index + i);
	index += split[9];
	models[9] = new Model(30, values3, fault10);

	for (int i = 0; i < 10; i++) {

	  System.out.println("M" + (i + 1));
	  System.out.println("Non-adaptive");
	  ArrayList<int[]> weight = new ArrayList<>();
	  for (int j = 0; j < models[i].parameter; j++) {
		int[] tmp = new int[models[i].values[j]];
		Arrays.fill(tmp, 0);
		weight.add(tmp);
	  }
	  CTModel model = new CTModel(models[i].parameter, models[i].values, 2, null, new ArrayList<>(), weight, new CH_MFTVerifier());
	  int totalDiscoveredFault = 0;
	  for (int j = 0; j < 30; j++) {
		TestSuite ts = new TestSuite();
		// len must bigger then t-way covering array's size
		int len = 500;
		AETG gen = new AETG(len);
		((AETG) gen).setCANDIDATE(50);
		gen.generation(model, ts);
		ArrayList<int[]> ts1 = new ArrayList<>();
		for (int k = 0; k < ts.suite.size(); k++)
		  ts1.add(ts.suite.get(k).test);
		//System.out.println("l " + i + " "+ models[i].fault.length);
		// compute coverage
		faultCoverage fc = new faultCoverage(models[i].fault);
		fc.checkTestSuite(ts1);
		totalDiscoveredFault += fc.getCoveredFault();
	  }
	  System.out.println("found faults (mean):" + (float) totalDiscoveredFault / 30);

	  //System.out.println("model" + i);
	  System.out.println("Adaptive");
	  //ArrayList<int[]> weight = new ArrayList<>();

	  totalDiscoveredFault = 0;
	  for (int j = 0; j < 30; j++) {
		weight = new ArrayList<>();
		for (int j1 = 0; j1 < models[i].parameter; j1++) {
		  int[] tmp = new int[models[i].values[j1]];
		  Arrays.fill(tmp, 0);
		  weight.add(tmp);
		}
		ArrayList<int[]> seed = new ArrayList<>();
		//ArrayList<int[]> ts1 = new ArrayList<>();
		while (true) {
		  model = new CTModel(models[i].parameter, models[i].values, 2, null, seed, weight, new CH_MFTVerifier());
		  TestSuite ts = new TestSuite();
		  // generate a test at a time
		  int len = 1;
		  AETG gen = new AETG(len);
		  ((AETG) gen).setCANDIDATE(50);
		  gen.generation(model, ts);
		  if (ts.suite.size() == 0)
			break;
		  int[] test = ts.suite.get(0).test;
		  faultCoverage f1 = new faultCoverage(models[i].fault);
		  if (f1.isFault(test)) {
			updateWeight(weight, test);
		  }
		  // seed contains generated tests
		  seed.add(test);
		  //System.out.println("l " + i + " "+ models[i].fault.length);
		}
		// calculate coverage
		faultCoverage fc = new faultCoverage(models[i].fault);
		fc.checkTestSuite(seed);
		totalDiscoveredFault += fc.getCoveredFault();
	  }
	  System.out.println("found faults (mean):" + (float) totalDiscoveredFault / 30);
	}
  }

  // update weight
  private static void updateWeight(ArrayList<int[]> weight, int[] test) {
    // first let all weight--
	for (int[] aWeight : weight)
	  for (int j = 0; j < aWeight.length; j++) {
		if (aWeight[j] > 0)
		  aWeight[j]--;
	  }
	// add the weights of values in failed test
	// weight must be lower then 3
	// if weight is too large, the new generated test may not cover mush uncovered tuples, and may require mush more tests to cover all tuples
	for (int i = 0; i < test.length; i++) {
	  weight.get(i)[test[i]] += 2;
	  if (weight.get(i)[test[i]] > 3)
		weight.get(i)[test[i]] = 3;
	}
  }
}

class Model {
  int parameter;
  int[] values;
  pair[][] fault;

  Model(int parameter, int[] values, pair[][] fault) {
	this.parameter = parameter;
	this.values = values;
	this.fault = fault;
  }
}
