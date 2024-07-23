package com.project.innoutburger.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.innoutburger.common.R;
import com.project.innoutburger.dto.DishDto;
import com.project.innoutburger.entity.Category;
import com.project.innoutburger.entity.Dish;
import com.project.innoutburger.service.CategoryService;
import com.project.innoutburger.service.DishFlavorService;
import com.project.innoutburger.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/*
 * 菜品管理
 * */
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    /*
    * 新增菜品
    * @param dishdto
    * @return
    * */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());

        dishService.saveWithFlavor(dishDto);

        return R.success("新增菜品成功");
    }

    /*
     * 菜品信息分頁查詢
     * @param page
     * @param pageSize
     * @param name
     * @return
     * */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {

        // 構造分頁構造器
        Page<Dish> pageInfo = new Page<Dish>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        // 條件構造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        // 添加過濾條件
        queryWrapper.like(name != null, Dish::getName, name);
        // 添加排序條件
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        // 執行分頁查訊
        dishService.page(pageInfo, queryWrapper);

        // 對象拷貝
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");

        List<Dish> records = pageInfo.getRecords();

        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item, dishDto);

            // 分類 id
            Long categoryId = item.getCategoryId();
            // 根據 id 查詢分類對象
            Category category = categoryService.getById(categoryId);

            if(category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }
}