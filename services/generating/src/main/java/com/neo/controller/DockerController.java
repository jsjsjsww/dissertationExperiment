package com.neo.controller;

import com.neo.domain.TestSuite;
import com.neo.service.combinatorial.CTModel;
import com.neo.service.generator.AETG;
import com.neo.service.handler.MFTVerifier;
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
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RestController
//@RequestMapping("/generation")
public class DockerController {
	
    @RequestMapping(value = "", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    // ACTS 3.0 version
    public TestSuite Generation(HttpServletRequest request) {
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
        int parameters = (Integer)jsonObject.get("parameters");
        int strength = (Integer)jsonObject.get("strength");
        int size = (Integer)jsonObject.get("size");
        JSONArray jsonArray = (JSONArray)jsonObject.get("values");
        List valueList = jsonArray.toList();
        int[] values = new int[valueList.size()];
        for(int i = 0; i < values.length; i++)
            values[i] = (Integer)valueList.get(i);
	  jsonArray = (JSONArray)jsonObject.get("constraint");
	  List constraintList = jsonArray.toList();
	  ArrayList<int[]> constraint = new ArrayList<>();
	  for (Object aConstraintList : constraintList) {
		String line = (String) aConstraintList;
		//constraint.add((String)constraintList.get(i));
		String[] split = line.trim().split(" ");
		int[] each = new int[split.length / 2];
		for (int j = 0; j < split.length; j += 2) {
		  // specific symbol, note that the representation in the file
		  // starts from 0 but the representation in the solver starts
		  // from 1.
		  int v = Integer.valueOf(split[j + 1]) + 1;
		  each[j / 2] = split[j].equals("-") ? -v : v;
		}
		constraint.add(each);

	  }
	  JSONArray testsuiteJSONArray = (JSONArray) jsonObject.get("weight");
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

	  testsuiteJSONArray = (JSONArray) jsonObject.get("seed");
	  testsuiteList = testsuiteJSONArray.toList();
	  num = testsuiteList.size();
	  ArrayList<int[]> seed = new ArrayList<>();
	  for(int i = 0; i < num; i++){
		JSONArray tmp = (JSONArray)(testsuiteJSONArray.get(i));
		List tmpList = tmp.toList();
		int[] testcase = new int[tmpList.size()];
		for(int j = 0; j < testcase.length; j++)
		  testcase[j] = (Integer)tmpList.get(j);
		seed.add(testcase);
	  }
        Instant start = Instant.now();
        CTModel model = new CTModel(parameters,  values,strength, constraint, seed, weight, new MFTVerifier());
        AETG aetg = new AETG(size);
        com.neo.service.combinatorial.TestSuite ts = new com.neo.service.combinatorial.TestSuite();
        aetg.generation(model, ts);
        Instant end = Instant.now();
        long time = Duration.between(start, end).toMillis();

        ArrayList<int[]> testsuite = new ArrayList<>();
        for(int i = 0; i < ts.suite.size(); i++)
            testsuite.add(ts.suite.get(i).test);
	  return new TestSuite(testsuite, time);
    }
}