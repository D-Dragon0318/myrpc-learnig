package io.spridra.rpc.common.exception;

/**
 * @Author: Spridra
 * @CreateTime: 2025-05-28 09:39
 * @Describe: 序列化异常类
 * @Version: 1.0
 */

public class SerializerException extends RuntimeException {
    private static final long serialVersionUID = -6783134254669118520L;

    public SerializerException(final Throwable e) {

        super(e);

    }

    public SerializerException(final String message) {

        super(message);

    }

    public SerializerException(final String message, final Throwable throwable) {

        super(message, throwable);

    }
}
