package io.spridra.rpc.codec;

import io.spridra.rpc.serialization.api.Serialization;
import io.spridra.rpc.serialization.jdk.JdkSerialization;

/**
 * @Author: Spridra
 * @CreateTime: 2025-05-28 09:57
 * @Describe: 获取JdkSerialization对象
 * @Version: 1.0
 */
public interface RpcCodec {
    default Serialization getJdkSerialization(){

        return new JdkSerialization();

    }
}
