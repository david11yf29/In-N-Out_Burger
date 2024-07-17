package com.project.innoutburger.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.project.innoutburger.common.R;
import com.project.innoutburger.entity.Employee;
import com.project.innoutburger.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired //由 Bean 來提供對象後面就不用自己 new 一個對象(IOC)
    private EmployeeService employeeService;


    /*
    * 員工登錄
    * @param request
    * @param employee
    * @return
    * */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {

        // 1. 頁面提交的密碼進行 md5 加密處裡
        String password = employee.getPassword();
        password = DigestUtils .md5DigestAsHex(password.getBytes());

        // 2. 頁面提交的用戶名 username 查詢數據庫
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        // 3. 如果沒查詢到則返回失敗
        if (emp == null) {
            return R.error("登錄失敗");
        }

        // 4. 有查詢到接著比對密碼
        if (!emp.getPassword().equals(password)) {
            return R.error("登錄失敗");
        }

        // 5. 用戶名和密碼皆正確, 查看員工狀態, 如果為已禁用狀態則返回員工已禁用
        if (emp.getStatus() == 0) {
            return R.error("帳號已禁用");
        }

        // 6. 登入成功, 將員工 id 存入 Session 並返回登錄成功結果
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }

    /*
     * 員工登出
     * @param request
     * @return
     * */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        // 清理 Session 中保存的當前登錄員工 id
        request.getSession().removeAttribute("employee");
        return R.success("退出成功！");
    }

    /*
     * 新增員工
     * @param employee
     * @return
     * */
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("新增員工, 員工信息: {}", employee.toString());

        // 設置初始密碼123456, 但是需要進行 md5 加密處理
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        // 獲得當前用戶的 id
        Long empId = (Long) request.getSession().getAttribute("employee");

        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);

        employeeService.save(employee);

        return R.success("新增員工成功");
    }

}
