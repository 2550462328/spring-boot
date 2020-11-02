package cn.ictt.zhanghui.springboot_test.common.socket.netty;


import io.netty.channel.Channel;

/**
 * netty 执行业务处理接口
 *
 * @author nickle
 */
public interface ICommandHandler {
    void handleCommand(HandleBean handleBean, Channel channel) throws Exception;
}
