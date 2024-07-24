package com.project.innoutburger.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.innoutburger.dto.SetmealDto;
import com.project.innoutburger.entity.Setmeal;
import com.project.innoutburger.entity.SetmealDish;
import com.project.innoutburger.mapper.SetmealMapper;
import com.project.innoutburger.service.SetmealDishService;
import com.project.innoutburger.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    /*
     * 新增套餐, 同時需要保存套餐和菜品的關聯關係
     * @param setmealDto
     * */
    @Transactional
    @Override
    public void saveWithDish(SetmealDto setmealDto) {

        // 保存套餐的基本信息, 操作 setmeal, 執行 insert 操作
        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).toList();

        // 保存套餐和菜品的關聯, 操作 setmeal_dish, 執行 insert 操作
        setmealDishService.saveBatch(setmealDishes);

    }
}
