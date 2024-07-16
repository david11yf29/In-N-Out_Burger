package com.project.innoutburger.controller;

import com.project.innoutburger.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired //由 Bean 來提供對象後面就不用自己 new 一個對象(IOC)
    private EmployeeService employeeService;
}
