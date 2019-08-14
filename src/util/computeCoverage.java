package util;

import combinatorial.CTModel;
import handler.CH_MFTVerifier;

import java.util.ArrayList;

/**
 * compute coverage of a testsuite
 */
public class computeCoverage {
  CTModel model;
  long uncovered;
  ArrayList<int[]> ts;
  long covered;

  public computeCoverage(CTModel model, ArrayList<int[]> ts){
    this.model = model;
    this.model.initialization();
    uncovered = this.model.getCombUncovered();
    //System.out.println(uncovered);
    this.ts = ts;
    for(int[] test: ts){
      this.model.updateCombination(test);
	}

	covered = uncovered - this.model.getCombUncovered();
    //System.out.println(covered);
  }

  public long getCovered(){
    return covered;
  }

  public static void main(String[] args){
    int parameter = 8;
    int[] values = new int[]{4,4,4,4,4,4,4,4};
    int[][] seed = new int[][]{{0,0,0,0,0,0,0,0}};
    ArrayList<int[]> weight = new ArrayList<>();
    for(int i = 0; i < 8; i++)
      weight.add(new int[]{0,0,0,0});
    CTModel model = new CTModel(parameter,values,2,null, new ArrayList<>(),weight, new CH_MFTVerifier());
    ArrayList<int[]> ts = new ArrayList<>();
    ts.add(new int[]{0,0,0,0,0,0,0,0});
    ts.add(new int[]{1,1,1,1,1,1,1,-1});
    computeCoverage cc = new computeCoverage(model, ts);
    System.out.println(cc.getCovered());
  }
}
