package util;

import java.util.ArrayList;
import java.util.Arrays;

public class faultCoverage {
  pair[][] faultTuples;
  boolean[] state;
  int coveredFault;

  public faultCoverage(pair[][] pairs){
    this.faultTuples = pairs;
    coveredFault = 0;
   // System.out.println(faultTuples.length);
    state = new boolean[faultTuples.length];
	Arrays.fill(state, false);
  }

  // if test contains any fault tuple
  public boolean isFault(int[] test){
    boolean res = false;
    for(int i = 0; i < faultTuples.length; i++){
      pair[] tuple = faultTuples[i];
      boolean isMatch = true;
      for(pair p: tuple){
        if(test[p.par] != p.val){
          isMatch = false;
          break;
		}
	  }
	  if(isMatch) {
		res = true;
		if(!state[i]){
		  state[i] = true;
		  coveredFault ++;
		}
	  }
	}
	return res;
  }

  public int getCoveredFault(){
    return coveredFault;
  }

  // go through tests in ts
  public void checkTestSuite(ArrayList<int[]> ts){
    for(int[] test: ts)
      isFault(test);
  }

  public static void main(String[] args){
    pair[][] faults = new pair[2][];
    faults[0] = new pair[2];
    faults[0][0] = new pair(0,1);
    faults[0][1] = new pair(1,1);

	faults[1] = new pair[2];
	faults[1][0] = new pair(0,2);
	faults[1][1] = new pair(1,2);

	faultCoverage fc = new faultCoverage(faults);
	ArrayList<int[]> ts = new ArrayList<>();
	ts.add(new int[]{2,2,1,1});
	ts.add(new int[]{1,1,1,1});

	fc.checkTestSuite(ts);
	System.out.println(fc.getCoveredFault());
  }
}
