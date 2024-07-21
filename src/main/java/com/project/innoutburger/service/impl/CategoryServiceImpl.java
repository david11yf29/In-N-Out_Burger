package com.project.innoutburger.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.innoutburger.entity.Category;
import com.project.innoutburger.mapper.CategoryMapper;
import com.project.innoutburger.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

}
