package io.spridra.rpc.common.scanner;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @Author: Spridra
 * @CreateTime: 2025-05-26 16:20
 * @Describe: 类扫描类
 * @Version: 1.0
 */

public class ClassScanner {
    /**
     * 文件
     * 扫描当前工程中指定包下的所有类信息
     */
    private static final String PROTOCOL_FILE = "file";
    /**
     * jar包
     * 扫描Jar文件中指定包下的所有类信息
     */
    private static final String PROTOCOL_JAR = "jar";
    /**
     * class文件的后缀
     * 扫描的过程中指定需要处理的文件的后缀信息
     */
    private static final String CLASS_FILE_SUFFIX = ".class";


    /**
     * 扫描当前工程中指定包下的所有类信息
     * @param packageName 扫描的包名
     * @param packagePath 包在磁盘上的完整路径
     * @param recursive 是否递归调用
     * @param classNameList 类名称的集合
     */
    private static void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive, List<String> classNameList){
        //获取此包的目录 建立一个File
        File dir = new File(packagePath);
        //如果不存在或者 也不是目录就直接返回
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        //如果存在 就获取包下的所有文件 包括目录
        //也就是目录和class文件
        File[] dirfiles = dir.listFiles(new FileFilter() {
            //自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
            public boolean accept(File file) {
                return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
            }
        });
        //循环所有文件
        for (File file : dirfiles) {
            //如果是目录 则继续扫描
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(packageName + "." + file.getName(),
                        file.getAbsolutePath(),
                        recursive,
                        classNameList);
            }else {
                //如果是java类文件 去掉后面的.class 只留下类名
                //减去五是class，再减去一是去掉点
                String className = file.getName().substring(0, file.getName().length() - 6);
                //添加到集合中去
                classNameList.add(packageName + '.' + className);
            }
        }
    }


}
