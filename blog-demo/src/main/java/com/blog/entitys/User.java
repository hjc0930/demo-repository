package com.blog.entitys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("用户实体类")
@Data
@TableName
public class User {

    @ApiModelProperty("用户Id")
    @TableId(type = IdType.AUTO, value = "id")
    Integer id;

    @ApiModelProperty("firstName")
    @TableField("firstName")
    String firstName;

    @ApiModelProperty("lastName")
    @TableField("lastName")
    String lastName;

    @ApiModelProperty("age")
    @TableField("age")
    int age;
}
