package cn.ictt.zhanghui.springboot_test.business.batch.processor;

import cn.ictt.zhanghui.springboot_test.business.pojo.domain.UserOperate;
import org.springframework.batch.item.ItemProcessor;

/**
 * Description:
 *
 * @author createdBy huizhang43.
 * @date createdAt 2022/9/21 10:08
 **/

public class UserItemProcessor implements ItemProcessor<UserOperate,UserOperate> {

    @Override
    public UserOperate process(UserOperate item) throws Exception {

        UserOperate transferUser = new UserOperate();

        transferUser.setUsername(item.getUsername().toUpperCase());
        transferUser.setPassword(item.getPassword().toUpperCase());

        return transferUser;
    }
}
