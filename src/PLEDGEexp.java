import combinatorial.CTModel;
import handler.CH_MFTVerifier;
import util.computeCoverage;
import util.forPLEDGE;
import util.pair;
import util.updateSeed;

import java.util.ArrayList;

public class PLEDGEexp {

  public static void run() {

    // set SUT model
	stage stages1 = new stage(8, new int[]{4, 4, 4, 4, 4, 4, 4, 4}, false, new ArrayList<>(), new ArrayList<>(), 0);
	ArrayList<pair> removeVal = new ArrayList<>();
	removeVal.add(new pair(0, 3));
	stage stages2 = new stage(8, new int[]{3, 4, 4, 4, 4, 4, 4, 4}, true, removeVal, new ArrayList<>(), 0);
	stage stages3 = new stage(8, new int[]{3, 4, 4, 4, 4, 4, 4, 4}, false, new ArrayList<>(), new ArrayList<>(), 0);
	stage stages4 = new stage(8, new int[]{3, 5, 4, 4, 4, 4, 4, 4}, false, new ArrayList<>(), new ArrayList<>(), 0);
	stage stages5 = new stage(9, new int[]{3, 5, 4, 4, 4, 4, 4, 4, 4}, true, new ArrayList<>(), new ArrayList<>(), 1);
	ArrayList<Integer> removePar = new ArrayList<>();
	removePar.add(7);
	stage stages6 = new stage(8, new int[]{3, 5, 4, 4, 4, 4, 4, 4}, true, new ArrayList<>(), removePar, 0);
	stage stages61 = new stage(8, new int[]{3, 5, 4, 4, 4, 4, 4, 4}, false, new ArrayList<>(), new ArrayList<>(), 0);

	stage stages7 = new stage(20, new int[]{6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6}, false, new ArrayList<>(), new ArrayList<>(), 0);
	ArrayList<pair> removeVal1 = new ArrayList<>();
	removeVal1.add(new

			pair(0, 5));
	stage stages8 = new stage(20, new int[]{5, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6}, true, removeVal1, new ArrayList<>(), 0);
	stage stages9 = new stage(20, new int[]{5, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6}, false, new ArrayList<>(), new ArrayList<>(), 0);
	stage stages10 = new stage(20, new int[]{5, 7, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6}, false, new ArrayList<>(), new ArrayList<>(), 0);
	stage stages11 = new stage(21, new int[]{5, 7, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6}, true, new ArrayList<>(), new ArrayList<>(), 1);
	ArrayList<Integer> removePar1 = new ArrayList<>();
	removePar1.add(19);
	stage stages12 = new stage(20, new int[]{5, 7, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6}, true, new ArrayList<>(), removePar1, 0);
	stage stages121 = new stage(20, new int[]{5, 7, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6}, false, new ArrayList<>(), new ArrayList<>(), 0);

	stage[][] models = new stage[8][];
	// 第一个模型
	models[0] = new stage[2];
	models[0][0] = stages1;
	models[0][1] = stages1;

	// 第二个模型
	models[1] = new stage[2];
	models[1][0] = stages1;
	models[1][1] = stages2;

	// 第三个模型
	models[2] = new stage[6];
	models[2][0] = stages1;
	models[2][1] = stages2;
	models[2][2] = stages3;
	models[2][3] = stages3;
	models[2][4] = stages3;
	models[2][5] = stages3;

	// 第四个模型
	models[3] = new stage[6];
	models[3][0] = stages1;
	models[3][1] = stages2;
	models[3][2] = stages4;
	models[3][3] = stages5;
	models[3][4] = stages6;
	models[3][5] = stages61;

	// 第五个模型
	models[4] = new stage[2];
	models[4][0] = stages7;
	models[4][1] = stages7;

	// 第六个模型
	models[5] = new stage[2];
	models[5][0] = stages7;
	models[5][1] = stages8;

	// 第七个模型
	models[6] = new stage[6];
	models[6][0] = stages7;
	models[6][1] = stages8;
	models[6][2] = stages9;
	models[6][3] = stages9;
	models[6][4] = stages9;
	models[6][5] = stages9;

	// 第八个模型
	models[7] = new stage[6];
	models[7][0] = stages7;
	models[7][1] = stages8;
	models[7][2] = stages10;
	models[7][3] = stages11;
	models[7][4] = stages12;
	models[7][5] = stages121;

	for (int i = 0; i < 8; i++) {
	  System.out.println("M" + (i + 1));
	  System.out.println("PLEDGETool");
	  int totalTuple = 0, totalSize = 0;
	  for (int j = 0; j < 30; j++) {
		ArrayList<int[]> seed = new ArrayList<>();
		for (int k = 0; k < models[i].length; k++) {
		  // System.out.println("stage "+ k);
		  // if model is changed, seed need to update
		  if (models[i][k].isChanged) {
			updateSeed us = new updateSeed(models[i][k].removedVal, models[i][k].removedPar, models[i][k].newPar);
			seed = us.update(seed);
		  }
		  // invoke PLEDGE tool
		  ArrayList<int[]> ts = forPLEDGE.runPLEDGETool(models[i][k].parameter, models[i][k].values);
		  //System.out.println(ts.suite.size());
		  totalSize += ts.size();
		  //System.out.println(ts1.size());
		  // compute coverage gain
		  CTModel model1 = new CTModel(models[i][k].parameter, models[i][k].values, 2, null, seed, new ArrayList<>(), new CH_MFTVerifier());
		  computeCoverage cc = new computeCoverage(model1, ts);
		  totalTuple += cc.getCovered();
		  // add new tests to seed
		  seed.addAll(ts);
		}
	  }
	  System.out.println("size (mean):" + (float) totalSize / 30);
	  System.out.println("covered tuples (mean):" + (float) totalTuple / 30);
	}
  }
}

