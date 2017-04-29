/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neu.controller;

import com.neu.pojo.Drugs;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author vishakha
 */
@RequestMapping("/search.htm")
public class SearchController {

    @RequestMapping(value = "/search.htm", method = RequestMethod.GET, headers = "Accept=*/*", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    String searchresult(HttpServletRequest request) throws Exception {
        System.out.println("in drugsearch controller");
        String action = request.getParameter("action");
        List<Drugs> drugs;
        String csvFile = "UserSearch.csv";
        String rpath = request.getRealPath("/");
        rpath=rpath+"/dataFiles/"+csvFile;
        

        Pattern pattern = Pattern.compile(",");

        BufferedReader in = new BufferedReader(new FileReader(rpath));
        drugs = in.lines().skip(1).map(line -> {
            String[] x = pattern.split(line);
            return new Drugs(x[0], x[3]);
        }).collect(Collectors.toList());
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);
        mapper.writeValue(System.out, drugs);

//            Drugs drug=new Drugs("AZ","sn");
        return mapper.writeValueAsString(drugs);
    }
}
