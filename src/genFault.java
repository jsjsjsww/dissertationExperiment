import util.pair;

import java.util.ArrayList;
import java.util.Random;

public class genFault {
  private ArrayList<int[]> model;
  private Random random = new Random();

  public genFault(ArrayList<int[]> model){
    this.model = model;
  }

  /*
  generate fault tuple for CT model
   */
  public pair[][] genFaultRandomly(int[] values, int num){
	pair[][] res = new pair[num][];
	res[0] = genARandomTuple(values, 3);
	int index = 1;
	while(index < num){
	  int choose = random.nextInt(index);
	  pair[] tmp = modifyAPosition(res[choose], values);
	  if(!containsTuple(res, index, tmp)){
	    res[index] = tmp;
	    index ++;
	  }
	}
	return res;
  }

  // return a random t-way tuple
  public pair[] genARandomTuple(int[] values, int t){
    pair[] res = new pair[t];
    ArrayList<Integer> loc = new ArrayList<>();
    int index = 0;
    while(index < t){
      int tmp = random.nextInt(values.length);
      if(!loc.contains(tmp)){
        loc.add(tmp);
        index++;
	  }
	}
	for(int i = 0; i < t; i++)
      res[i] = new pair(loc.get(i), random.nextInt(values[loc.get(i)]));
	return res;
  }

  // randomly modify a tuple
  public pair[] modifyAPosition(pair[] p, int[] values){
    int parameter = values.length;
    pair[] res = new pair[p.length];
    int index = random.nextInt(p.length);
    int j = 0;
    for(int i = 0; i < p.length; i++){
      if(i != index){
        res[j] = new pair(p[i].par, p[i].val);
        j++;
	  }
	}
	int par = random.nextInt(parameter);
    while(containsPar(res, res.length - 1, par))
	  par = random.nextInt(parameter);
    res[res.length - 1] = new pair(par, random.nextInt(values[par]));
    return res;
  }

  public boolean containsPar(pair[] p, int n, int par){
    for(int i = 0; i < n; i++){
      if(p[i].par == par)
        return true;
	}
	return false;
  }

  public boolean containsTuple(pair[][] tuple, int n, pair[] a){
    for(int i = 0; i < n; i++){
      if(isEqual(tuple[i], a))
        return true;
	}
	return false;
  }

  public boolean isEqual(pair[] a, pair[] b){
    if(a == null && b == null)
      return true;
    if(a == null || b == null)
      return false;
    if(a.length != b.length)
      return false;
    for(pair p1 : a) {
      boolean got = false;
	  for (pair p2 : b) {
		if (p1.equals(p2)) {
		  got = true;
		  break;
		}
	  }
	  if(!got)
	    return false;
	}
	return true;
  }
  public static void main(String[] args){
    int[] values = new int[]{5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5};
    genFault a = new genFault(new ArrayList<>());
    pair[][] tuples = a.genFaultRandomly(values, 40);
    for(pair[] fault: tuples){
      for(pair p: fault){
        System.out.print(+ p.par + "=" + p.val + ",");
	  }
	  System.out.println();
	}
  }
}
