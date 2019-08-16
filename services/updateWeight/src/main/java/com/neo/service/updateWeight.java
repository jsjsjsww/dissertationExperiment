package com.neo.service;

import java.util.ArrayList;

public class updateWeight {

  public static void updateWeight(ArrayList<int[]> weight, int[] test) {
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
