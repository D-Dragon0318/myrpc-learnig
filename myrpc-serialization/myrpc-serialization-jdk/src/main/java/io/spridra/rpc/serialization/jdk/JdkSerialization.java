package io.spridra.rpc.serialization.jdk;


import io.spridra.rpc.common.exception.SerializerException;
import io.spridra.rpc.serialization.api.Serialization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * @Author: Spridra
 * @CreateTime: 2025-05-28 09:47
 * @Describe: 基于JDK实现数据的序列化和反序列化工程
 * @Version: 1.0
 */

public class JdkSerialization implements Serialization {
    private final Logger logger = LoggerFactory.getLogger(JdkSerialization.class);
    @Override
    public <T> byte[] serialize(T obj) {
        // logger.info("execute serialize...");
        logger.info("==编码==>>");
        if (obj == null){
            throw new SerializerException("serialize object is null");
        }
        try{
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(os);
            out.writeObject(obj);
            return os.toByteArray();
        }catch (IOException e){
            throw new SerializerException(e.getMessage(), e);
        }
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> cls) {
        // logger.info("execute deserialize...");
        logger.info("==解码==>>");
        if (data == null){
            throw new SerializerException("deserialize data is null");
        }
        try{
            ByteArrayInputStream is = new ByteArrayInputStream(data);
            ObjectInputStream in = new ObjectInputStream(is);
            return (T) in.readObject();
        }catch (Exception e){
            throw new SerializerException(e.getMessage(), e);
        }
    }
}
