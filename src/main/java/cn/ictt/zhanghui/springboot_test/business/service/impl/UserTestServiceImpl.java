package cn.ictt.zhanghui.springboot_test.business.service.impl;

import cn.ictt.zhanghui.springboot_test.business.pojo.domain.Role;
import cn.ictt.zhanghui.springboot_test.business.pojo.domain.RoleRepository;
import cn.ictt.zhanghui.springboot_test.business.pojo.domain.UserOpeareRepository;
import cn.ictt.zhanghui.springboot_test.business.pojo.domain.UserOperate;
import cn.ictt.zhanghui.springboot_test.business.service.UserTestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author ZhangHui
 * @version 1.0
 * @className UserTestServiceImpl
 * @description UserOperate的服务类
 * @date 2020/9/17
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserTestServiceImpl implements UserTestService {

    private final UserOpeareRepository userRepository;

    private final RoleRepository roleRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserOperate findByName(String name) {
        return userRepository.findByUsername(name);
    }

    @Override
    public UserOperate saveUser(UserOperate userOperate) {
        userOperate.setPassword(bCryptPasswordEncoder.encode(userOperate.getPassword()));
        return userRepository.save(userOperate);
    }

    /**
     * Spring事务的模拟测试
     */
    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.READ_COMMITTED)
    public String addUser() {

        log.info("准备插入user数据");
        userRepository.save(new UserOperate("zhanghui", "123"));
        addRole();

        return "ok";
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public void addRole() {
        log.info("准备插入role数据");

        roleRepository.save(new Role("guest", "访客"));

        // 模拟报错 测试事务回滚
//        System.out.println(1/0);
    }
}
