/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neu.controller;

import com.neu.pojo.DrugOutcome;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author vishakha
 */
@RequestMapping("/outcome.htm")
public class OutcomeController {

    @RequestMapping(value = "/outcome.htm", method = RequestMethod.GET, headers = "Accept=*/*", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    String searchresult(HttpServletRequest request) throws Exception {
        System.out.println("in drugsearch controller");
        String action = request.getParameter("action");
        List<DrugOutcome> outcomes;
        String csvFile = "Merge.csv";
        String rpath = request.getRealPath("/");
        rpath=rpath+"/dataFiles/"+csvFile;
        Pattern pattern = Pattern.compile(",");

        BufferedReader in = new BufferedReader(new FileReader(rpath));
        outcomes = in.lines().skip(1).map(line -> {
            String[] x = pattern.split(line);
            return new DrugOutcome(x[3], x[20],x[7],x[6],x[8],x[13],x[4],x[5]);
        }).collect(Collectors.toList());
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);
        mapper.writeValue(System.out, outcomes);

//            Drugs drug=new Drugs("AZ","sn");
        return mapper.writeValueAsString(outcomes);
    }
}
