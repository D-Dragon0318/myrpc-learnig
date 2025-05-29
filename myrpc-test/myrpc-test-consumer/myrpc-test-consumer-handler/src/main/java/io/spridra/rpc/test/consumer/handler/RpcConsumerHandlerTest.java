package io.spridra.rpc.test.consumer.handler;

import io.spridra.rpc.consumer.common.RpcConsumer;
import io.spridra.rpc.protocol.RpcProtocol;
import io.spridra.rpc.protocol.header.RpcHeaderFactory;
import io.spridra.rpc.protocol.request.RpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: Spridra
 * @CreateTime: 2025-05-28 16:55
 * @Describe: 消费者处理器测试类
 * @Version: 1.0
 */

public class RpcConsumerHandlerTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcConsumerHandlerTest.class);
    public static void main(String[] args) throws Exception {
        RpcConsumer consumer = RpcConsumer.getInstance();
        Object result = consumer.sendRequest(getRpcRequestProtocol());
        LOGGER.info("从服务消费者获取到的数据===>>>{}",result.toString());
        Thread.sleep(2000);
        consumer.close();
    }

    private static RpcProtocol<RpcRequest> getRpcRequestProtocol(){
        //模拟发送数据
        RpcProtocol<RpcRequest> protocol = new RpcProtocol<RpcRequest>();
        protocol.setHeader(RpcHeaderFactory.getRequestHeader("jdk"));
        RpcRequest request = new RpcRequest();
        request.setClassName("io.spridra.rpc.test.api.DemoService");
        request.setGroup("spridra");
        request.setMethodName("hello");
        request.setParameters(new Object[]{"spridra"});
        request.setParameterTypes(new Class[]{String.class});
        request.setVersion("1.0.0");
        request.setAsync(false);
        request.setOneway(false);
        protocol.setBody(request);
        return protocol;
    }
}
