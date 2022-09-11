package com.zhangmingge.demo.javascanner.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class TestScanner {
    @RequestMapping(value = "/test", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String test(String params){
        System.out.println("params = " + params);
        String pattern1 = "(?<=vgdecoderesult=)(.*)(?=&&)";
        Pattern r1 = Pattern.compile(pattern1);
        Matcher m1 = r1.matcher(params);
        if (m1.find()) {
            System.out.println(m1.group(0));
        }
        String pattern2 = "(?<=&&devicenumber=)(.*)$";
        Pattern r2 = Pattern.compile(pattern2);
        Matcher m2 = r2.matcher(params);
        if (m2.find()) {
            System.out.println(m2.group(0));
        }
        return "code=0000";
    }
}
