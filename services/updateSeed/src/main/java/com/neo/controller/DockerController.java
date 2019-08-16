package com.neo.controller;
import com.neo.service.pair;
import com.neo.service.updateSeed;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

@RestController
//@RequestMapping("/generation")
public class DockerController {
	
    @RequestMapping(value = "", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String ACTSGeneration(HttpServletRequest request) {
        BufferedReader br;
        StringBuilder sb;
        String reqBody = null;
        try {
            br = new BufferedReader(new InputStreamReader(
                    request.getInputStream()));
            String line;
            sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            reqBody = URLDecoder.decode(sb.toString(), "UTF-8");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject(reqBody);
        int newPara = (Integer)jsonObject.get("newPara");
        JSONArray jsonArray = (JSONArray)jsonObject.get("deletedPara");
        List valueList = jsonArray.toList();
        ArrayList<Integer> deletedPara = new ArrayList<>();
        //int[] values = new int[valueList.size()];
        for(int i = 0; i < valueList.size(); i++)
            deletedPara.add( (Integer)valueList.get(i));
        jsonArray = (JSONArray)jsonObject.get("modifiedPara");
        List constraintList = jsonArray.toList();
        ArrayList<pair> modifiedPara = new ArrayList<>();
        for (Object aConstraintList : constraintList) {
            String pv = (String) aConstraintList;
            String[] split = pv.split(":");
            pair tmp = new pair(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
            modifiedPara.add(tmp);
            //constraint.add((String) aConstraintList);
        }
        JSONArray testsuiteJSONArray = (JSONArray) jsonObject.get("oldSeed");
        List testsuiteList = testsuiteJSONArray.toList();
        int num = testsuiteList.size();
        ArrayList<int[]> testsuite = new ArrayList<>();
        for(int i = 0; i < num; i++){
            JSONArray tmp = (JSONArray)(testsuiteJSONArray.get(i));
            List tmpList = tmp.toList();
            int[] testcase = new int[tmpList.size()];
            for(int j = 0; j < testcase.length; j++)
                testcase[j] = (Integer)tmpList.get(j);
            testsuite.add(testcase);
        }
        updateSeed us = new updateSeed(modifiedPara, deletedPara, newPara);
        ArrayList<int[]> ts = us.update(testsuite);
        JSONObject res = new JSONObject();
        res.put("seed", ts);
        return res.toString();
    }

}