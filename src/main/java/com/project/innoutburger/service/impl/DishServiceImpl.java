package com.project.innoutburger.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.innoutburger.entity.Dish;
import com.project.innoutburger.mapper.DishMapper;
import com.project.innoutburger.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

}
