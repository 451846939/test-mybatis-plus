package com.example.test.test.entity;

import com.example.test.test.annotation.Copy;
import com.example.test.test.annotation.CopyField;
import com.example.test.test.annotation.CopyFieldGroups;
import lombok.Data;

@Copy
@Data
public class UsersDTO {
    @CopyFieldGroups(groups = {@CopyField(value = "id",group = Users.class,copy = false),@CopyField(value = "id",group = UsersDTO.class,copy = true)})
    private Integer idd;
    @CopyField(copy = true)
    private String username;
}
