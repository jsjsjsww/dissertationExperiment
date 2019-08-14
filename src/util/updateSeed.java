package util;

import java.util.ArrayList;
import java.util.Collections;

public class updateSeed {
  ArrayList<pair> removedVal;
  ArrayList<Integer> removedPar;
  int newPar;

  public updateSeed(ArrayList<pair> removedVal, ArrayList<Integer> removedPar, int newPar) {
	this.removedVal = removedVal;
	this.removedPar = removedPar;
	this.newPar = newPar;
  }

  public ArrayList<int[]> update(ArrayList<int[]> testsuite) {
	ArrayList<int[]> res = new ArrayList<>();
	for (int[] ts : testsuite) {
	  ArrayList<Integer> tmp = new ArrayList<>();
	  for (int i = 0; i < ts.length; i++)
		tmp.add(ts[i]);
	  // insert new parameters in the end of each test
	  for (int i = 0; i < newPar; i++)
		tmp.add(-1);
	  //System.out.println(tmp.toString());
	  // set invalid value to -1
	  for (pair p : removedVal) {
		if (tmp.get(p.par) == p.val)
		  tmp.set(p.par, -1);
	  }
	  // System.out.println(tmp.toString());
	  // drop invalid parameters
	  ArrayList<Integer> newTmp = new ArrayList<>();
	  for (int i = tmp.size() - 1; i >= 0; i--) {
		if (!removedPar.contains(i))
		  newTmp.add(tmp.get(i));
	  }
	  Collections.reverse(newTmp);
	  //System.out.println(tmp.toString());
	  res.add(newTmp.stream().mapToInt(Integer::valueOf).toArray());
	}
	return res;
  }

  public static void main(String[] args) {
	ArrayList<pair> removedVal = new ArrayList<>();
	removedVal.add(new pair(0, 3));
	ArrayList<Integer> removedPar = new ArrayList<>();
	removedPar.add(1);
	removedPar.add(3);
	updateSeed updateSeed = new updateSeed(removedVal, removedPar, 2);
	ArrayList<int[]> ts = new ArrayList<>();
	ts.add(new int[]{3, 1, 0, 1, 1, 1});
	ts.add(new int[]{2, 1, 1, 0, 1, 1});
	ts.add(new int[]{1, 1, 1, 1, 0, 1});
	ts.add(new int[]{0, 1, 1, 1, 1, 0});

	ArrayList<int[]> res = updateSeed.update(ts);
	for (int[] test : res) {
	  for (int i = 0; i < test.length; i++)
		System.out.print(test[i] + " ");
	  System.out.println();
	}
  }
}
