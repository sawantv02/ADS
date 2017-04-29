/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neu.controller;

import com.neu.pojo.CSV;
import com.neu.pojo.Drugs;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 *
 * @author vishakha
 */
@org.springframework.stereotype.Controller
public class FaersController {

    @RequestMapping(value = "/index.htm", method = RequestMethod.GET)
    public ModelAndView handleRequest(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        ModelAndView mv = new ModelAndView();

        System.out.print("in controller");

        mv.setViewName("overview");
        return mv;
    }

    @RequestMapping(value = "/drugsearch.htm", method = RequestMethod.GET)
    public ModelAndView handleSearchRequest(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        ModelAndView mv = new ModelAndView();

        System.out.print("in controller");

        mv.setViewName("drugsearch");
        return mv;
    }

    @RequestMapping(value = "/drug.htm", method = RequestMethod.GET)
    public ModelAndView handleDrugRequest(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        ModelAndView mv = new ModelAndView();

        System.out.print("in controller");

        mv.setViewName("drug");
        return mv;
    }
}
