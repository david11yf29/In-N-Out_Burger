package com.project.innoutburger.dto;

import com.project.innoutburger.entity.Setmeal;
import com.project.innoutburger.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;
    private String categoryName;
}
