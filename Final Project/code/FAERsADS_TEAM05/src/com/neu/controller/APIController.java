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
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author vishakha
 */
@RequestMapping("/apicall.htm")
public class APIController {

    @RequestMapping(value = "/apicall.htm", method = RequestMethod.POST, headers = "Accept=*/*", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String searchresult(HttpServletRequest request) throws Exception {
        Map pmap = request.getParameterMap();
        System.out.println("in drugsearch controller");
        String action = request.getParameter("action");
        String urlRestWebService = "https://ussouthcentral.services.azureml.net/workspaces/168eb4a3d72e4b078160213fcdaa4333/services/ee080088b48141e0af10c9913067689d/execute?api-version=2.0&details=true";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer AJacooh+WG4WeJTntLNn/E3A3E4yQELYY8S6/2sbpcsBvNFSCabuopvuiqlnrd47a0qBf4Coj1LMcEhBUm0Ujw==");
        headers.add("Content-Length", "100000");
        headers.add("Content-Type", "application/json");

        String requestJson = "{'Inputs': {'input1': {'ColumnNames': [ 'drugname','route','dose_amt','dose_unit','dose_form', 'dose_freq','mfr_sndr','pt'],'Values':[['" + pmap.get("drugname") + "','" + pmap.get("reactionlist") + "','" + pmap.get("doseform") + "','" + pmap.get("doseunit") + "','" + pmap.get("dosefreq") + "','" + pmap.get("mfndetails") + "','" + pmap.get("route") + "','" + pmap.get("doseamt") + "']]}},'GlobalParameters': {}}";

        HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
        System.out.println("entity" + entity);
        RestTemplate restTemplate = new RestTemplate();

        String restData = restTemplate.postForObject(urlRestWebService, entity, String.class);

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);
        mapper.writeValue(System.out, restData);

//            Drugs drug=new Drugs("AZ","sn");
        return mapper.writeValueAsString(restData);
    }

}
