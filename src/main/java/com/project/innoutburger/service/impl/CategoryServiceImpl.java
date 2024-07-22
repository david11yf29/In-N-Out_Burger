package com.project.innoutburger.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.innoutburger.common.CustomException;
import com.project.innoutburger.entity.Category;
import com.project.innoutburger.entity.Dish;
import com.project.innoutburger.entity.Setmeal;
import com.project.innoutburger.mapper.CategoryMapper;
import com.project.innoutburger.service.CategoryService;
import com.project.innoutburger.service.DishService;
import com.project.innoutburger.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /*
    * 根據 id 刪除分類, 刪除前需要進行判斷
    * @param id
    * */
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 添加查詢條件, 根據分類 id 進行查詢
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        int count1 = (int) dishService.count(dishLambdaQueryWrapper);

        // 查詢當前分類是否關聯了菜品, 如果已經關聯, 直接拋出一個異常
        if(count1 > 0) {
            // 已經關聯菜品
            throw new CustomException("當前分類下關聯了菜品, 不能刪除");
        }

        // 查詢當前分類是否關聯了套餐, 如果已經關聯, 直接拋出一個異常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();

        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        int count2 = (int) setmealService.count(setmealLambdaQueryWrapper);
        if(count2 > 0) {
            // 已經關聯套餐
            throw new CustomException("當前分類下關聯了套餐, 不能刪除");
        }

        // 正常刪除分類
        super.removeById(id);
    }
}
