package com.project.innoutburger.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.innoutburger.entity.Employee;
import com.project.innoutburger.mapper.EmployeeMapper;
import com.project.innoutburger.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

}
