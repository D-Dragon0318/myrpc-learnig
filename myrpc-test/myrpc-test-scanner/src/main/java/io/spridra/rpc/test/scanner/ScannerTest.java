package io.spridra.rpc.test.scanner;

import io.spridra.rpc.common.scanner.ClassScanner;
import io.spridra.rpc.common.scanner.reference.RpcReferenceScanner;
import io.spridra.rpc.common.scanner.server.RpcServiceScanner;
import org.junit.Test;

import java.util.List;

/**
 * @Author: Spridra
 * @CreateTime: 2025-05-27 09:47
 * @Describe: 测试类
 * @Version: 1.0
 */

public class ScannerTest {
    /**
     * 扫描io.binghe.rpc.test.scanner包下所有的类
     */
    @Test
    public void testScannerClassNameList() throws Exception {
        List<String> classNameList = ClassScanner.getClassNameList("io.spridra.rpc.test.scanner");
        classNameList.forEach(System.out::println);
    }

    /**
     * 扫描io.binghe.rpc.test.scanner包下所有标注了@RpcService注解的类
     */
    @Test
    public void testScannerClassNameListByRpcService() throws Exception {
        RpcServiceScanner.doScannerWithRpcServiceAnnotationFilterAndRegistryService("io.spridra.rpc.test.scanner");
    }
    /**
     * 扫描io.binghe.rpc.test.scanner包下所有标注了@RpcReference注解的类
     */
    @Test
    public void testScannerClassNameListByRpcReference() throws Exception {
        RpcReferenceScanner.doScannerWithRpcReferenceAnnotationFilter("io.spridra.rpc.test.scanner");
    }
}
