package com.neo.controller;
import com.neo.service.pair;
import com.neo.service.updateSeed;
import com.neo.service.updateWeight;
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
        JSONArray testsuiteJSONArray = (JSONArray) jsonObject.get("oldWeight");
        List testsuiteList = testsuiteJSONArray.toList();
        int num = testsuiteList.size();
        ArrayList<int[]> weight = new ArrayList<>();
        for(int i = 0; i < num; i++){
            JSONArray tmp = (JSONArray)(testsuiteJSONArray.get(i));
            List tmpList = tmp.toList();
            int[] testcase = new int[tmpList.size()];
            for(int j = 0; j < testcase.length; j++)
                testcase[j] = (Integer)tmpList.get(j);
            weight.add(testcase);
        }
        JSONArray jsonArray = (JSONArray)jsonObject.get("test");
        List valueList = jsonArray.toList();
        int[] test = new int[valueList.size()];
        for(int i = 0; i < test.length; i++)
            test[i] = (Integer)valueList.get(i);
        updateWeight.updateWeight(weight,test);
        JSONObject res = new JSONObject();
        res.put("weight", weight);
        return res.toString();
    }

}