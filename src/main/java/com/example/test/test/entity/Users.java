package com.example.test.test.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author jobob
 * @since 2019-03-07
 */
@Data
@Accessors(chain = true)
@TableName("users")
public class Users {

    private static final long serialVersionUID = 1L;
    @TableId(
            value = "ID",
            type = IdType.INPUT
    )
    private Integer id;

    private String username;

    private String pwd;


}
