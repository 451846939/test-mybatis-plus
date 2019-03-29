package com.example.test.test.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.test.test.entity.Users;
import com.example.test.test.mapper.UsersMapper;
import com.example.test.test.service.IUsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jobob
 * @since 2019-03-07
 */
@RestController
@Slf4j
//@RequestMapping("/test/users")
public class UsersController {
    @Resource
    private IUsersService iUsersService;
    @Resource
    private UsersMapper usersMapper;
    @GetMapping("/test")
    public Users test(){
        Users byId = iUsersService.getById(1);
        Users users = new Users();
        users.setId(1);
        LambdaQueryWrapper<Users> select = Wrappers.lambdaQuery(byId).eq(Users::getId,1);
        String sqlSelect = select.getSqlSelect();
        log.info(sqlSelect);
        Users users1 = usersMapper.selectOne(select);
        log.info(users1.toString());
        return byId;
    }
}
