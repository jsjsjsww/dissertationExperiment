package handler;
import combinatorial.CTModel;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Vector;
import java.util.List;
/**
 * Use the minimum forbidden tuple (MFT) as the constraint handling technique.
 * Given a test model, it firstly calculates the MFS, then a solution can be
 * verified against the MFS.
 */
public class CH_MFTVerifier implements ValidityChecker {
    int[][] relation;
    private Vector<Constraint> hardConstraint;   // user specified constraints

    private int numCall; //number of calls of the isValid() method
    private long time;

    public CH_MFTVerifier() {
        hardConstraint = new Vector<>();
        numCall = 0;

    }

    public void init(CTModel model) {
        relation = model.relation;
        if (model.constraint != null){
            Vector<Constraint> originalConstraint = new Vector<>();

        for (int[] x : model.constraint) {
            int[] y = sort(x);
            originalConstraint.add(new Constraint(y));
        }
        // System.out.println(model.constraint.size());

        //minimal the original constraints
        minimalConstrains(originalConstraint);
        // System.out.println("after minimal :"+originalConstraint.size());

        /**
         *get all constraint parameters
         *store in consParameter
         */
        int i;
        Vector<Integer> consParameter = new Vector<Integer>();
        for (i = 0; i < relation.length; i++) {
            boolean isConsParameter = true;
            //boolean getMatchCons = false;
            for (int j = 0; j < relation[i].length; j++) {
                boolean getMatchCons = false;
                for (int k = 0; k < originalConstraint.size(); k++) {
                    for (int kk = 0; kk < originalConstraint.get(k).disjunction.length; kk++)
                        if (relation[i][j] == Math.abs(originalConstraint.get(k).disjunction[kk])) {
                            //System.out.println(relation[i][j]+":"+Math.abs(originalConstraint.get(k).disjunction[kk]));
                            getMatchCons = true;
                            break;
                        }
                    if (getMatchCons)
                        break;
                }

                if (!getMatchCons) {
                    isConsParameter = false;
                    break;
                }
            }
            if (isConsParameter)
                consParameter.add(i);
        }


        Vector<Integer> consParameter1 = new Vector<Integer>();
        for (i = 0; i < consParameter.size(); i++) {
            consParameter1.add(consParameter.get(i));
        }


        Vector<Constraint> NewCons = new Vector<Constraint>();
        while (consParameter.size() != 0) {
            Vector<Integer> tempConsParameter = new Vector<Integer>();
            NewCons.clear();
            for (i = 0; i < consParameter.size(); i++) {
                Vector<Constraint> dikaer = new Vector<>();
               // System.out.println("round for parameter "+ consParameter.get(i));
                for (int ii = 0; ii < relation[consParameter.get(i)].length; ii++) {
                    //System.out.println(relation[consParameter.get(i)][ii]);
                    Vector<Constraint> tmpCons = new Vector<>();
                    for (int j = 0; j < originalConstraint.size(); j++) {
                        for (int k = 0; k < originalConstraint.get(j).disjunction.length; k++)
                            if (Math.abs(originalConstraint.get(j).disjunction[k]) == relation[consParameter.get(i)][ii]) {
                                // System.out.println(relation[i][ii]);
                                int[] tmpArray = new int[originalConstraint.get(j).disjunction.length - 1];
                                int tmpI = 0, tmp1 = 0;

                                while (tmpI < originalConstraint.get(j).disjunction.length) {
                                    if (tmpI != k) {
                                        tmpArray[tmp1] = originalConstraint.get(j).disjunction[tmpI];
                                        tmp1++;
                                    }
                                    tmpI++;
                                }
                                tmpCons.add(new Constraint(tmpArray));
                                break;

                            }

                    }

                    if (ii == 0) {
                        for (Constraint x : tmpCons) {
                            dikaer.add(x);
                        }
                    } else {
                        Vector<Constraint> tmp = new Vector<>();
                        for (Constraint x : dikaer) {
                            tmp.add(x);
                        }
                        dikaer.clear();
                        for (int j = 0; j < tmp.size(); j++)
                            for (int k = 0; k < tmpCons.size(); k++) {
                                int x = 0, y = 0, z = 0;
                                int[] mergeCons = new int[tmp.get(j).disjunction.length + tmpCons.get(k).disjunction.length];
                                while (x < tmp.get(j).disjunction.length || y < tmpCons.get(k).disjunction.length) {
                                    if (x >= tmp.get(j).disjunction.length) {
                                        mergeCons[z] = tmpCons.get(k).disjunction[y];
                                        y++;
                                    } else if (y >= tmpCons.get(k).disjunction.length) {
                                        mergeCons[z] = tmp.get(j).disjunction[x];
                                        x++;
                                    } else if (tmp.get(j).disjunction[x] > tmpCons.get(k).disjunction[y]) {
                                        mergeCons[z] = tmp.get(j).disjunction[x];
                                        x++;
                                    } else {
                                        mergeCons[z] = tmpCons.get(k).disjunction[y];
                                        y++;
                                    }
                                    z++;
                                }

                                y = 0;
                                boolean drop = false;
                                for (x = 0; x < mergeCons.length - 1; x++) {
                                    while (Math.abs(mergeCons[x]) > relation[y][relation[y].length - 1])
                                        y++;
                                    if (Math.abs(mergeCons[x + 1]) <= relation[y][relation[y].length - 1] && mergeCons[x] != mergeCons[x + 1]) {
                                        drop = true;
                                        break;
                                    }

                                }
                                //if(consParameter.get(i) == 4 && ii == relation[consParameter.get(i)].length - 1){
                               // for(int ix = 0; ix < mergeCons.length; ix++) {
                                 //   System.out.print(mergeCons[ix] + " ");
                                 //   System.out.println();
                              //  }
                                //    System.out.println();
                               // }

                                if (!drop) {
                                    List<Integer> list = new ArrayList<Integer>();
                                    for (x = 0; x < mergeCons.length; x++) {
                                        if (!list.contains(mergeCons[x])) {
                                            list.add(mergeCons[x]);
                                        }
                                    }
                                    int[] deducedCons = new int[list.size()];
                                    for (x = 0; x < list.size(); x++) {
                                        //System.out.println(list.get(i));
                                        deducedCons[x] = list.get(x);
                                    }
                                    //Constraint c1 = new Constraint((deducedCons));
                                    if(!isDublicated(dikaer, deducedCons))
                                        dikaer.add(new Constraint((deducedCons)));
                                }

                            }
                    }
                }
                //System.out.println("dikaer size = "+dikaer.size());
                for (int x = 0; x < dikaer.size(); x++)
                   // if (!NewCons.contains(dikaer.get(x))) {
                    //if(!isDublicated(NewCons, dikaer.get(x).disjunction)){
                        NewCons.add(dikaer.get(x));
                  //  }
            }

            /**
             * add new constraints
             * and get constraint parameters for next round
             */
            for (int x = 0; x < NewCons.size(); x++) {
                boolean isNewConstraint = true;
                for (int y = 0; y < originalConstraint.size(); y++)
                    if (originalConstraint.get(y).isSuperior(NewCons.get(x))) {
                        isNewConstraint = false;
                        break;
                    }
                if (isNewConstraint) {
                    originalConstraint.add(NewCons.get(x));
                    int k = 0;
                    for (int y = 0; y < NewCons.get(x).disjunction.length; y++) {

                        while (Math.abs(NewCons.get(x).disjunction[y]) > relation[k][relation[k].length - 1])
                            k++;
                        if (consParameter1.contains(k) && !tempConsParameter.contains(k))
                            tempConsParameter.add(k);
                    }

                }
            }
            consParameter.clear();

            //ready the new constraint parameters
            for (int x = 0; x < tempConsParameter.size(); x++)
                consParameter.add(tempConsParameter.get(x));

            /**
             * minimal constraints
             */
            minimalConstrains(originalConstraint);
        }


        // set hard constraints
        ArrayList<int[]> cons = new ArrayList<>();
        hardConstraint = new Vector<>();
        for (int x = 0; x < originalConstraint.size(); x++) {
            int[] sort = sort(originalConstraint.get(x).disjunction);
            //for(int i1 = 0; i1 < sort.length; i1++)
            //  System.out.print(sort[i1]+" ");
            // System.out.println();
            hardConstraint.add(new Constraint(sort));
            cons.add(sort);
        }
         System.out.println("constraint size:"+hardConstraint.size());
        model.constraint = cons;
    }

    }

    public int[] sort(int[] a){
        int[] b = new int[a.length];
        for(int i = 0; i < b.length; i++){
            int max = -10000;
            int maxi = -1;
            for(int j = 0; j < b.length; j++){
                if(a[j] > max){
                    max = a[j];
                    maxi = j;
                }
            }
            b[i] = max;
            a[maxi] = -10000;
        }
        return b;
    }

    public boolean isDublicated(Vector<Constraint> v, int[] a){
        for (int i = 0; i < v.size(); i++){
            int[] b = v.get(i).disjunction;
            boolean re;
            if(b.length == a.length){
                re = true;
                for(int j = 0; j <b.length; j++){
                    if(b[j] != a[j]){
                        re = false;
                        break;
                    }
                }
                if(re)
                    return true;
            }
        }
        return false;
    }

    public void minimalConstrains(Vector<Constraint> originalConstraint){

       // removeList.clear();
        ArrayList<Integer > removeList = new ArrayList<>();
        for (int i = 0; i < originalConstraint.size()-1; i++) {
            for (int j = i + 1; j < originalConstraint.size(); j++) {
                int tmp1 = originalConstraint.get(i).disjunction.length;
                int tmp2 = originalConstraint.get(j).disjunction.length;
                boolean isMatch = true;
                if (tmp1 <= tmp2) {
                    for (int k = 0; k < tmp1; k++) {
                        int kk;
                        for (kk = 0; kk < tmp2; kk++)
                            if (originalConstraint.get(i).disjunction[k] == originalConstraint.get(j).disjunction[kk])
                                break;
                        if (kk == tmp2 && originalConstraint.get(i).disjunction[k] != originalConstraint.get(j).disjunction[tmp2 - 1]) {
                            isMatch = false;
                            break;
                        }
                    }
                } else {
                    for (int k = 0; k < tmp2; k++) {
                        int kk;
                        for (kk = 0; kk < tmp1; kk++)
                            if (originalConstraint.get(j).disjunction[k] == originalConstraint.get(i).disjunction[kk])
                                break;
                        if (kk == tmp1 && originalConstraint.get(j).disjunction[k] != originalConstraint.get(i).disjunction[tmp1 - 1]) {
                            isMatch = false;
                            break;
                        }
                    }
                }

                if (isMatch) {
                    //System.out.println(isMatch);
                    if (tmp1 <= tmp2) {
                        if (!removeList.contains(j))
                            removeList.add(j);
                    }
                    else {

                        if (!removeList.contains(i))
                            removeList.add(i);

                    }
                }

            }
        }
        // System.out.println("rm:"+removeList.size());
        Collections.sort(removeList,Collections.reverseOrder());
        for (int i = 0; i < removeList.size(); i++) {

            //System.out.println(originalConstraint.size()+" rm at :" + removeList.get((i)));
            originalConstraint.removeElementAt(removeList.get(i));
        }

    }

    public boolean isValid(final int[] test) {
       /*for(int i = 0;i<test.length;i++)
            System.out.print(test[i]+",");
        System.out.println("");*/
        numCall ++;
        if (hardConstraint.size() == 0)
            return true;
        Instant start = Instant.now();
        long begintime = System.nanoTime();

        /**
         * transfer the testcase to constraint's form
         * for example: value = [2,2,2] and a TC = [0, 0, -1]
         * transfer it to [1, 3, -1]
         */
        int []transferTC = new int[test.length];
        int baseNum = 0;
        for(int i=0; i<test.length; i++){
            if(test[i]!= -1)
                transferTC[i] = test[i] + baseNum + 1;
            else
                transferTC[i] = -1;
            baseNum += relation[i].length;

        }
        /**
         *
         * find if the input TC contains any constraint
         */
        for(int i = 0; i < hardConstraint.size();i++){
            boolean matched = true;
            int k = 0;
            int j = 0;
            while(j < hardConstraint.get(i).disjunction.length){

                while(k < transferTC.length && (transferTC[k] == -1 ||Math.abs(hardConstraint.get(i).disjunction[j]) > transferTC[k])) {
                    k++;
                    if (k == transferTC.length)
                        matched = false;
                }
                if(!matched)
                    break;
                //System.out.println(transferTC.length+" "+k);
                if(Math.abs(hardConstraint.get(i).disjunction[j]) == transferTC[k])
                        //break;
                {
                    k++;
                    j++;
                }
                    else {
                        matched = false;
                        break;
                    }

                //if(k == transferTC.length && Math.abs(hardConstraint.get(i).disjunction[j]) != transferTC[k-1]) {
                   // matched = false;
                  //  break;
               // }
            }
            if(matched) {
               /* for(int i1 = 0; i1 < hardConstraint.get(i).disjunction.length; i1++)
                    System.out.print(hardConstraint.get(i).disjunction[i1]+" ");
                System.out.println();
                for(int i1 = 0; i1 < transferTC.length; i1++)
                    System.out.print(transferTC[i1]+" ");
                System.out.println();
                for(int i1 = 0; i1 < test.length; i1++)
                    System.out.print(test[i1]+" ");
                System.out.println();
                */

                //Instant end = Instant.now();
                //time += Duration.between(start, end).getSeconds();
               // time += Duration.between(start, end).getNano();
                long endtime = System.nanoTime();
                //time += Duration.between(start, end).getSeconds();
                time += (endtime - begintime);
                return false;
            }

        }
        //Instant end = Instant.now();
        long endtime = System.nanoTime();
        //time += Duration.between(start, end).getSeconds();
        time += (endtime - begintime);
        //time += Duration.between(start, end).getNano();
        return true;
    }


    public void showStatistic() {
        System.out.println("number of calls: " + numCall);
        time = time / (int)Math.pow(10,3);
        System.out.println("total time(Millis):      " + time);
        System.out.println("average time:    " + (double)time / (double)numCall);
    }


    /**
     * a test
     * @param args
     */


}