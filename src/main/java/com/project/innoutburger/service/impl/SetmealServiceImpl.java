package com.project.innoutburger.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.innoutburger.common.CustomException;
import com.project.innoutburger.common.R;
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

    /*
     * 刪除套餐, 同時需要刪除套餐和菜品的關聯數據
     * @param ids
     * */
    @Transactional
    @Override
    public void removeWithDish(List<Long> ids) {
        // 查詢套餐狀態, 確定是否可以刪除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(Setmeal::getId, ids);
        queryWrapper.eq(Setmeal::getStatus, 1);

        int count = (int) this.count(queryWrapper);

        // 如果不能刪除, 拋出業務異常
        if (count > 0) {
            throw new CustomException("套餐正在售賣中, 無法刪除");
        }

        // 如果可以刪除, 先刪除套餐表中的數據 -- setmeal
        this.removeByIds(ids);

        // DELETE FROM setmeal_dish WHERE setmeal_dish IN (1,2,3)
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId, ids);

        // 刪除關係表中的數據 -- setmeal_dish
        setmealDishService.remove(lambdaQueryWrapper);
    }
}
