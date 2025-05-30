package io.spridra.rpc.common.scanner;

import io.spridra.rpc.annotation.RpcService;
import io.spridra.rpc.common.helper.RpcServiceHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Spridra
 * @CreateTime: 2025-05-26 17:42
 * @Describe: @RpcService注解的扫描类
 * @Version: 1.0
 */

public class RpcServiceScanner extends ClassScanner {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServiceScanner.class);

    /**
     * 扫描指定包下的类，并筛选使用@RpcService注解标注的类
     */
    public static Map<String, Object> doScannerWithRpcServiceAnnotationFilterAndRegistryService(
            String host, int port,
            String scanPackage
            /*, RegistryService registryService*/
    ) throws Exception{
        Map<String, Object> handlerMap = new HashMap<>();
        List<String> classNameList = getClassNameList(scanPackage);
        if (classNameList == null || classNameList.isEmpty()){
            return handlerMap;
        }
        classNameList.stream().forEach((className) -> {
            try {
                Class<?> clazz = Class.forName(className);
                RpcService rpcService = clazz.getAnnotation(RpcService.class);
                // 判断是否是标注了@RpcService注解的类
                if (rpcService != null){
                    //优先使用interfaceClass, interfaceClass的name为空，再使用interfaceClassName
                    //TODO 后续逻辑向注册中心注册服务元数据，同时向handlerMap中记录标注了RpcService注解的类实例
                    LOGGER.info("当前标注了@RpcService注解的类实例名称===>>> " + clazz.getName());
                    LOGGER.info("@RpcService注解上标注的属性信息如下：");
                    LOGGER.info("interfaceClass===>>> " + rpcService.interfaceClass().getName());
                    LOGGER.info("interfaceClassName===>>> " + rpcService.interfaceClassName());
                    LOGGER.info("version===>>> " + rpcService.version());
                    LOGGER.info("group===>>> " + rpcService.group());

                    String serviceName = getServiceName(rpcService);
                    // String key = serviceName.concat(rpcService.version()).concat(rpcService.group());
                    String key = RpcServiceHelper.buildServiceKey(serviceName, rpcService.version(), rpcService.group());
                    //服务启动后，将服务构建
                    handlerMap.put(key, clazz.newInstance());
                }
            } catch (Exception e) {
                LOGGER.error("scan classes throws exception: {}", e.toString());
            }
        });
        return handlerMap;
    }

    /**
     * 获取serviceName
     */
    private static String getServiceName(RpcService rpcService){
        //优先使用interfaceClass
        Class<?> clazz = rpcService.interfaceClass();
        if (clazz == void.class){
            return rpcService.interfaceClassName();
        }
        String serviceName = clazz.getName();
        if (serviceName == null || serviceName.trim().isEmpty()){
            serviceName = rpcService.interfaceClassName();
        }
        return serviceName;
    }
}
