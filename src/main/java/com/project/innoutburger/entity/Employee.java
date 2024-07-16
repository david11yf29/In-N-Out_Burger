package com.project.innoutburger.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

// Lombok 提供的 @Data 注解自动生成常见的 Getter、Setter、toString、equals 和 hashCode 方法
@Data
public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String username;
    private String name;
    private String password;
    private String phone;
    private String sex;
    private String idNumber; //身分證號碼
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;
}
