package com.project.innoutburger.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.innoutburger.dto.DishDto;
import com.project.innoutburger.entity.Dish;
import com.project.innoutburger.entity.DishFlavor;
import com.project.innoutburger.mapper.DishMapper;
import com.project.innoutburger.service.DishFlavorService;
import com.project.innoutburger.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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

    /*
     * 根據 id 查詢菜品信息和對應的口味信息
     * @param id
     * @return
     * */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        // 查詢菜品信息, 從 dish 表查詢
        Dish dish = this.getById(id);

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);

        // 查詢當前菜品對應口味信息, 從 dish_flavor 表查詢
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavors);

        return dishDto;
    }

    @Transactional
    @Override
    public void updateWithFlavor(DishDto dishDto) {
        // 更新菜品信息
        this.updateById(dishDto);

        // 清理當前菜品對應口味數據 -> dish_flavor 的 delete 操作
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());

        dishFlavorService.remove(queryWrapper);


        // 添加當前提交的口味數據 -> dish_flavor 的 insert 操作
        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).toList();

        dishFlavorService.saveBatch(flavors);

    }


}
