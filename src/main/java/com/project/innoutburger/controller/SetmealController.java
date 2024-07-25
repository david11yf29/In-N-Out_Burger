package com.project.innoutburger.controller;

import com.alibaba.druid.support.spring.stat.BeanTypeAutoProxyCreator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.innoutburger.common.R;
import com.project.innoutburger.dto.SetmealDto;
import com.project.innoutburger.entity.Category;
import com.project.innoutburger.entity.Setmeal;
import com.project.innoutburger.service.CategoryService;
import com.project.innoutburger.service.SetmealDishService;
import com.project.innoutburger.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
* 套餐管理
* */
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SetmealDishService setmealDishService;

    /*
    * 新增套餐
    * @param setmealDto
    * @return
    * */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        log.info("套餐信息: {}", setmealDto);

        setmealService.saveWithDish(setmealDto);

        return R.success("新增套餐成功");
    }

    /*
     * 套餐分頁查詢
     * @param page
     * @param pageSize
     * @param name
     * @return
     * */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        // 分頁構造器
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> dtoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        // 添加查詢條件, 根據 name 進行模糊查詢 like
        queryWrapper.like(name != null, Setmeal::getName, name);
        // 添加排序條件, 根據更新時間降序排列
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(pageInfo, queryWrapper);

        // 對象拷貝
        BeanUtils.copyProperties(pageInfo, dtoPage, "records");
        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            // 對象拷貝
            BeanUtils.copyProperties(item, setmealDto);
            // 分類 id
            Long categoryId = item.getCategoryId();
            // 根據 id 查詢分類對象
            Category category = categoryService.getById(categoryId);
            if(category != null) {
                // 分類名稱
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).toList();

        dtoPage.setRecords(list);
        return R.success(dtoPage);
    }

    /*
     * 刪除套餐
     * @param ids
     * @return
     * */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        log.info("ids: {}", ids);

        setmealService.removeWithDish(ids);

        return R.success("套餐數據刪除成功");
    }

}
