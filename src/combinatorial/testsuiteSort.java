package combinatorial;

import java.util.ArrayList;

/**
 * sort test case according to the  number of free entries in each row from small to lareg
 */
public class testsuiteSort {
    public static ArrayList<int[]> sort(CTModel model, ArrayList<int[]> TS, int[][] cover){
        int t = model.t_way;
        int n = model.parameter;
        double[] point = new double[TS.size()];
        for(int i = 0; i < TS.size(); i++){
            int[] test = TS.get(i);
            int[] pos = new int[t];
            for(int j = 0; j < t; j++)
                pos[j] = j;

            for (int j = 0; j < cover.length; j++) {
                if (pos[0] != n - t + 1) {
                    int colum = 0;
                    int sd = 1;
                    for(int k = t-1; k >= 0; k--) {
                        colum += sd * test[pos[k]];
                        sd *= model.value[pos[k]];
                    }
                    //System.out.println("j = " + j + " colum = " + colum);
                    if(cover[j][colum] != -1)
                        ;//point[i] += 1.0 / (double)cover[j][colum];
                    else
                        point[i] += 1;
                        //point[i] -= 1;
                    pos[t - 1]++;
                    for (int ii = t - 1; ii > 0; ii--) {
                        if (pos[ii] == n - t + 1 + ii) {
                            pos[ii - 1]++;
                            for (int j1 = 0; j1 < t - ii; j1++)
                                pos[ii + j1] = pos[ii - 1] + j1 + 1;
                        }

                    }
                }
            }
        }
        ArrayList<int[]> res = new ArrayList<>();
        while(res.size() != TS.size()){
            double max = Double.NEGATIVE_INFINITY;
            int index = -1;
            for(int i = 0; i < point.length; i++){
                if(point[i] > max){
                    max = point[i];
                    index = i;
                }
            }

            res.add(TS.get(index));
            point[index] = Double.NEGATIVE_INFINITY;
        }
        return res;
    }
}
