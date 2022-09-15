package cn.ictt.zhanghui.springboot_test.base.exception.assertion;

import cn.hutool.core.util.ArrayUtil;
import cn.ictt.zhanghui.springboot_test.base.exception.BaseException;
import cn.ictt.zhanghui.springboot_test.base.exception.BusinessException;
import cn.ictt.zhanghui.springboot_test.base.exception.assertion.common.Assert;
import cn.ictt.zhanghui.springboot_test.base.exception.assertion.common.IResponseEnum;

import java.text.MessageFormat;

/**
 * <p>业务异常断言</p>
 *
 * @author sprainkle
 * @date 2019/5/2
 */
public interface BusinessExceptionAssert extends IResponseEnum, Assert {

    @Override
    default BaseException newException(Object... args) {
        String msg = this.getMessage();
        if (ArrayUtil.isNotEmpty(args)) {
            msg = MessageFormat.format(this.getMessage(), args);
        }

        return new BusinessException(this, args, msg);
    }

    @Override
    default BaseException newException(Throwable t, Object... args) {
        String msg = this.getMessage();
        if (ArrayUtil.isNotEmpty(args)) {
            msg = MessageFormat.format(this.getMessage(), args);
        }

        return new BusinessException(this, args, msg, t);
    }

}
