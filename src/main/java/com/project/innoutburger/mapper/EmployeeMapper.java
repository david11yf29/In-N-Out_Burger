package com.project.innoutburger.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.project.innoutburger.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}
