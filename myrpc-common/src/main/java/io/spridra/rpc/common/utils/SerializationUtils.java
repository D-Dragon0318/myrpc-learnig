package io.spridra.rpc.common.utils;

import java.util.stream.IntStream;

/**
 * @Author: Spridra
 * @CreateTime: 2025-05-28 09:41
 * @Describe: 序列化工具类
 * @Version: 1.0
 */

public class SerializationUtils {
    private static final String PADDING_STRING = "0";

    /**
     * 约定序列化类型最大长度为16
     */
    public static final int MAX_SERIALIZATION_TYPE_COUNR = 16;

    /**
     * 为长度不足16的字符串后面补0
     * @param str 原始字符串
     * @return 补0后的字符串
     */
    public static String paddingString(String str){
        str = transNullToEmpty(str);
        if (str.length() >= MAX_SERIALIZATION_TYPE_COUNR) return str;
        int paddingCount = MAX_SERIALIZATION_TYPE_COUNR - str.length();
        StringBuilder paddingString = new StringBuilder(str);
        IntStream.range(0, paddingCount).forEach((i) -> {
            paddingString.append(PADDING_STRING);
        });
        return paddingString.toString();
    }

    /**
     * 字符串去0 操作
     * @param str 原始字符串
     * @return 去0后的字符串
     */
    public static String subString(String str){
        str = transNullToEmpty(str);
        return str.replace(PADDING_STRING, "");
    }

    public static String transNullToEmpty(String str){
        return str == null ? "" : str;
    }
}
