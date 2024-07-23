package com.project.innoutburger.dto;

import com.project.innoutburger.entity.Dish;
import com.project.innoutburger.entity.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();
    private String categoryName;
    private Integer copies;
}
