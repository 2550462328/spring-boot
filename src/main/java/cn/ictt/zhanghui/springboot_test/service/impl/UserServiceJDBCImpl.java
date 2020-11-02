//package cn.ictt.zhanghui.springboot_test.service.impl;
//
//import cn.ictt.zhanghui.springboot_test.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Service;
//
///*
//   用户对象接口实现类
//   基于JDBC实现
// */
//@Service
//public class UserServiceJDBCImpl implements UserServiceJDBC {
//    @Autowired
//    @Qualifier("primaryJdbcTemplate")
//    private JdbcTemplate jdbcTemplate;
//
//    @Override
//    public void create(String name, Integer age) {
//        jdbcTemplate.update("insert into USER(NAME, AGE) values(?, ?)", name, age);
//    }
//
//    @Override
//    public void deleteByName(String name) {
//        jdbcTemplate.update("delete from USER where NAME = ?", name);
//    }
//
//    @Override
//    public Integer getAllUsers() {
//        return jdbcTemplate.queryForObject("select count(1) from USER", Integer.class);
//    }
//
//    @Override
//    public void deleteAllUsers() {
//        jdbcTemplate.update("delete from USER");
//    }
//}
