package io.spridra.rpc.protocol.base;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Author: Spridra
 * @CreateTime: 2025-05-27 15:43
 * @Describe: 基础信息类
 * @Version: 1.0
 */
public class RpcMessage implements Serializable {
    /**
     * 单向传输
     */
    private boolean oneway;
    /**
     * 异步传输
     */
    private boolean async;

    public boolean getOneway() {
        return oneway;
    }

    public void setOneway(boolean oneway) {
        this.oneway = oneway;
    }

    public boolean getAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }
}
