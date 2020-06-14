package com.samplejsonschema.controller;

import com.samplejsonschema.util.Validator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {
    @RequestMapping("/")
    String jsonval() {
        final Validator sv = new Validator();
        return sv.isValid();
    }
}