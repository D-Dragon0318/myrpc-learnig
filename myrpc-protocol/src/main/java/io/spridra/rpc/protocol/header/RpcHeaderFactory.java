package io.spridra.rpc.protocol.header;

import io.spridra.rpc.common.scanner.id.IdFactory;
import io.spridra.rpc.constants.RpcConstants;
import io.spridra.rpc.protocol.enumeration.RpcType;

/**
 * @Author: Spridra
 * @CreateTime: 2025-05-27 15:58
 * @Describe: 消息头工厂类
 * @Version: 1.0
 */

public class RpcHeaderFactory {
    public static RpcHeader getRequestHeader(String serializationType/* , int messageType */){

        RpcHeader header = new RpcHeader();

        long requestId = IdFactory.getId();

        header.setMagic(RpcConstants.MAGIC);

        header.setRequestId(requestId);

        header.setMsgType((byte) RpcType.REQUEST.getType());

        header.setStatus((byte) 0x1);

        header.setSerializationType(serializationType);

        return header;

    }
}
