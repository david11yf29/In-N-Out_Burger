package com.project.innoutburger.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.innoutburger.common.R;
import com.project.innoutburger.entity.Employee;
import com.project.innoutburger.service.EmployeeService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

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

        // 被 MetaObjectHandler 自動填充取代
        // employee.setCreateTime(LocalDateTime.now());
        // employee.setUpdateTime(LocalDateTime.now());

        // 獲得當前用戶的 id
        // Long empId = (Long) request.getSession().getAttribute("employee");

        // employee.setCreateUser(empId);
        // employee.setUpdateUser(empId);

        employeeService.save(employee);

        return R.success("新增員工成功");
    }

    /*
    * 員工信息分頁查詢
    * @param page
    * @param pageSize
    * @param name
    * @return
    * */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        log.info("page = {}, pageSize = {}, name = {}", page, pageSize, name);

        // 構造分頁構造器 page, pageSize
        Page pageInfo = new Page(page, pageSize);

        // 構造條件構造器 name (相當於 SQL WHERE)
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        // 添加過濾條件
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        // 添加排序條件
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        // 執行查詢
        employeeService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    /*
     * 根據 id 修改員工訊息
     * @param employee
     * @return
     * */
    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee) {
        log.info(employee.toString());

        long id = Thread.currentThread().threadId();
        log.info("線程 id 為: {}", id);

        // 被 MetaObjectHandler 自動填充取代
        // Long empId = (Long)request.getSession().getAttribute("employee");
        // employee.setUpdateTime(LocalDateTime.now());
        // employee.setUpdateUser(empId);
        employeeService.updateById(employee);

        return R.success("員工信息修改成功");
    }

    /*
     * 根據 id 查詢員工訊息
     * @param id
     * @return
     * */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id) {
        log.info("根據 id 查詢員工信息...");
        Employee employee = employeeService.getById(id);
        if(employee != null) {
            return R.success(employee);
        }
        return R.error("沒有查詢到對應員工信息");
    }
}
