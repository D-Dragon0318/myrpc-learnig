package io.spridra.rpc.protocol.header;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Spridra
 * @CreateTime: 2025-05-27 15:55
 * @Describe: 消息头类
 * @Version: 1.0
 */
@Data
public class RpcHeader implements Serializable {
    /**
     * 序列化
     */
    private static final long serialVersionUID = 6011436680686290298L;

    /*
    +---------------------------------------------------------------+
    | 魔数 2byte | 报文类型 1byte | 状态 1byte |     消息 ID 8byte      |
    +---------------------------------------------------------------+
    |           序列化类型 16byte      |        数据长度 4byte          |
    +---------------------------------------------------------------+
    */

    /**
     * 魔数 2字节
     */
    private short magic;
    /**
     * 报文类型 1字节
     */
    private byte msgType;
    /**
     * 状态 1字节
     */
    private byte status;

    /**
     * 消息 ID 8字节
     */
    private long requestId;

    /**
     * 序列化类型16字节，不足16字节后面补0，约定序列化类型长度最多不能超过16
     */
    private String serializationType;

    /**
     * 消息长度 4字节
     */
    private int msgLen;
}
