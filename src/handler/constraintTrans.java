package handler;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

/*
*transfer forbidden cons, requiring cons, numeric cons, shielding cons to forbidden cons
* a tuple = (p1.1, p2.1,...)
* forbidden cons is a set of tuples.
*
* requiring con: t -> R, t is a tuple and R is a tuple set, means if the
* existence of the t-tuple t in a test case requires at least one tuple in
* the tuple set R to appear in the same test case
* like (p1.1,p2.0) -> {(p3.1)}
*
*numeric cons: the values of p1, p2, ... have some arithmetical, relational, or logical relationship
* like p1 == p2
*
* shielding cons: the existence of a tuple t make a set of parameters disables
* like (p1.1,p2.2) / {p3}
*
* a constraint is transfered to a int array like:
* p1  p2  p3  p4  p5
*   1   4   7  10  13
*   2   5   8  11  14
*   3   6   9  12  15
*  {0,  -1, 0, -1, -1} as [-1, -7]
 */
public class constraintTrans {
    public static ArrayList<int[]> f2f(ArrayList<String> cons, int[] values){
        ArrayList<int[]> result = new ArrayList<>();
        for(int i = 0; i < cons.size(); i++){
            String temp = cons.get(i);
            temp = temp.substring(1, temp.length() - 1);
            String[] split = temp.split(",");
            int[] newCon = new int[values.length];
            for(int j = 0; j < newCon.length; j++)
                newCon[j] = -1;
            for(int j = 0; j < split.length; j++) {
                String[] pv = split[j].split("\\.");
                newCon[Integer.parseInt(pv[0].substring(1,pv[0].length())) - 1] = Integer.parseInt(pv[1]);
            }
            result.add(newCon);
        }
        return result;
    }

//requiring to forbidden
    public static ArrayList<int[]> r2f(ArrayList<String> cons, int[] values){
        ArrayList<int[]> result = new ArrayList<>();
        for(int i = 0; i < cons.size(); i++){
            System.out.println(cons.get(i));
            String[] split = cons.get(i).split("->");
            int[] condition = new int[values.length];
            for(int j = 0; j < condition.length; j++)
                condition[j] = -1;
            String[] condition_pv = split[0].substring(1,split[0].length() - 1).split(",");
            for(int j = 0; j < condition_pv.length; j++){
                String[] a = condition_pv[j].substring(1,condition_pv[j].length()).split("\\.");
                int para = Integer.parseInt(a[0]) - 1;
                int value = Integer.parseInt(a[1]);
                condition[para] = value;
            }
            String[] follows = split[1].substring(2,split[1].length() - 2).split("\\),\\(");
            ArrayList<Integer> par = new ArrayList<>();
            ArrayList<int[]> R = new ArrayList<>();
            for(int j = 0; j < follows.length; j++){
                int[] tmp = new int[values.length];
                for(int k = 0; k < tmp.length; k++)
                    tmp[k] = -1;
                String[] pv = follows[j].split(",");
                for(int k = 0; k < pv.length; k++){
                    String[] a = pv[k].substring(1,pv[k].length()).split("\\.");
                    int para = Integer.parseInt(a[0]) - 1;
                    int value = Integer.parseInt(a[1]);
                    tmp[para] = value;
                    if(!par.contains(para))
                        par.add(para);
                }
                R.add(tmp);
            }
            //System.out.println(par.size());
            ArrayList<int[]> Ra = getAllCom(par, values);
            for(int j = 0; j < R.size(); j++){
                for(int k = Ra.size() - 1; k >= 0; k--){
                    if(contain(R.get(j), Ra.get(k)))
                        Ra.remove(k);
                }
            }
            for(int j = 0; j < Ra.size(); j++){
                if(!isConflict(condition, Ra.get(j))) {
                    merge(Ra.get(j), condition);
                    result.add(Ra.get(j));
                }
            }
        }
        return result;
    }

    public static ArrayList<int[]> n2f(ArrayList<String> cons, ArrayList<Object> realValues){
        ArrayList<int[]> result = new ArrayList<>();
        for(int i = 0; i < cons.size(); i++){

        }
        return result;
    }

    //判断exp是否为true
    public boolean getValue(String exp){
        return value(exp.toCharArray(), 0)[0];
    }

    public boolean[] value(char[] chars, int i){
        Deque<String> deq = new LinkedList<String>();
        int pre = 0;
        String pre_S = "";
        boolean[] bra = null;
        while(i < chars.length && chars[i] != ')'){
            if(chars[i] >= '0' && chars[i] <= '9'){
                pre = pre * 10 + chars[i++] -'0';
            }
            else if(chars[i] == '\"'){
                i++;
                while(chars[i] != '\"'){
                    pre_S += chars[i];
                    i ++;
                }
                i++;
            }
            else if(chars[i] == '('){
                addNum(deq, pre);
                deq.addLast(String.valueOf(chars[i++]));
                pre = 0;
            }
            else{
                bra = value(chars, i + 1);
               // pre = bra[0];
                //i = bra[1] + 1;
            }
        }
        addNum(deq, pre);
       // return new int[] {getNum(deq), i};
        boolean[] result = new boolean[2];
        return result;
    }

    public void addNum(Deque<String> deq, int num){

    }

    public int getNum(Deque<String> deq){
        return 0;

    }

    public static ArrayList<int[]> s2f(ArrayList<String> cons){
        ArrayList<int[]> result = new ArrayList<>();
        return result;
    }

    public static ArrayList<int[]> getAllCom(ArrayList<Integer> par, int[] values){
        ArrayList<int[]> result = new ArrayList<>();
        int[] tmp = new int[par.size()];
        for(int i = 0; i < tmp.length; i++)
            tmp[i] = 0;
        do{
            int[] comb = new int[values.length];
            for(int i = 0; i < comb.length; i++)
                comb[i] = -1;
            for(int i = 0; i < par.size(); i++)
                comb[par.get(i)] = tmp[i];
            result.add(comb);
            tmp[tmp.length - 1]++;
            for(int i = tmp.length - 1; i >= 1; i--)
                if(tmp[i] == values[par.get(i)]){
                tmp[i] = 0;
                tmp[i - 1]++;
                }
        }
        while(tmp[0] < values[par.get(0)]);
        return result;
    }

    //判断a是否属于b
    public static boolean contain(int[] a, int[] b){
        for(int i = 0; i < a.length; i++){
            if(a[i] != b[i] && a[i] != -1)
                return false;
        }
        return true;
    }

    //判断a和b是否能合并
    public static boolean isConflict(int[] a, int[] b){
        for(int i = 0; i < a.length; i++){
            if(a[i] != b[i] && a[i] != -1 && b[i] != -1)
                return true;
        }
        return false;
    }

    public static void merge(int[] a, int[] b){
        for(int i = 0; i < a.length; i++){
            if(a[i] == -1)
                a[i] = b[i];
        }
    }

    public static void main(String[] args){
        int[] values = {2,2,2,2};
        ArrayList<String> cons1 = new ArrayList<>();
        cons1.add("(p1.1,p2.1)");
        cons1.add("(p2.1,p4.1)");
        ArrayList<int[]> result = f2f(cons1,values);
        for(int i = 0; i < result.size(); i++){
            for(int j = 0; j < result.get(i).length; j++)
                System.out.print(result.get(i)[j]+" ");
            System.out.println();
        }
    }

}

