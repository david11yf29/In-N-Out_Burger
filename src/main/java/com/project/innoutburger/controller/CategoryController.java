package com.project.innoutburger.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.innoutburger.common.R;
import com.project.innoutburger.entity.Category;
import com.project.innoutburger.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
* 分類管理
* */
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /*
    * 新增分類
    * @param category
    * @return
    * */
    @PostMapping
    public R<String> save(@RequestBody Category category){
        log.info("category: {}", category);
        categoryService.save(category);
        return R.success("新增分類成功");
    }

    /*
     * 分頁查詢
     * @param page
     * @param pageSize
     * @return
     * */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize){
        // 分頁構造器
        Page<Category> pageInfo = new Page<>(page, pageSize);
        // 條件構造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        // 添加排序條件, 根據 sort 進行排序
        queryWrapper.orderByAsc(Category::getSort);

        // 進行分頁查詢
        categoryService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    /*
     * 根據 id 刪除分類
     * @param id
     * @return
     * */
    @DeleteMapping
    public R<String> delete(Long id){
        log.info("刪除分類: {}", id);

        // categoryService.removeById(id);
        categoryService.remove(id);

        return R.success("分類信息刪除成功");
    }

    /*
     * 根據 id 修改分類信息
     * @param category
     * @return
     * */
    @PutMapping
    public R<String> update(@RequestBody Category category) {
        log.info("修改分類信息: {}", category);

        categoryService.updateById(category);
        return R.success("修改分類信息成功");
    }


    /*
    * 根據條件查詢分類數據
    * @param category
    * @return
    * */
    @GetMapping("/list")
    public R<List<Category>> list(Category category) {

        // 條件構造器 SELECT * FROM Category
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        // 添加條件 WHERE type = #{category.type} -- 如果 category.getType() 不为空
        queryWrapper.eq(category.getType() != null, Category::getType,category.getType());
        // 添加排序條件 ORDER BY sort ASC, update_time DESC;
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }
}
