package com.project.innoutburger.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.project.innoutburger.dto.DishDto;
import com.project.innoutburger.entity.Dish;

public interface DishService extends IService<Dish> {

    // 新增菜品, 同時插入菜品對應的口味數據, 需要操作兩張表: dish, dish_flavor
    public void saveWithFlavor(DishDto dishDto);

}
