package com.project.innoutburger.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.innoutburger.dto.DishDto;
import com.project.innoutburger.entity.Dish;
import com.project.innoutburger.entity.DishFlavor;
import com.project.innoutburger.mapper.DishMapper;
import com.project.innoutburger.service.DishFlavorService;
import com.project.innoutburger.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    /*
    * 新增菜品, 同時保存對應的口味數據
    * @param dishDto
    * */
    @Transactional
    @Override
    public void saveWithFlavor(DishDto dishDto) {
        // 保存菜品的基本信息到菜表 dish
        this.save(dishDto);

        // 菜品 id
        Long dish_id = dishDto.getId();

        // 菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dish_id);
            return item;
        }).toList();

        // 保存菜品口味的數據到菜品口味表 dish_flavor
        dishFlavorService.saveBatch(flavors);

    }
}
