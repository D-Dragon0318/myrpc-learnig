package io.spridra.rpc.serialization.api;

/**
 * @Author: Spridra
 * @CreateTime: 2025-05-28 09:45
 * @Describe: 序列化和反序列化
 * @Version: 1.0
 */
public interface Serialization {

    /**
     * 序列化
     */
    <T> byte[] serialize(T obj);

    /**
     * 反序列化
     */
    <T> T deserialize(byte[] data, Class<T> cls);
}
