package com.io.file;

import java.io.*;

public class IODemo {

    /**
     * 拷贝文件
     * @param sourcePath 源文件路径
     * @param targetPath 目标文件路径
     */
    public static void copyFile(String sourcePath,String targetPath){
        File source = new File(sourcePath);
        if(!source.exists() || source.isDirectory()) {
            throw new RuntimeException("file not exist");
        }
        try (FileInputStream inputStream = new FileInputStream(source);
             FileOutputStream outputStream = new FileOutputStream(targetPath)){
             inputStream.transferTo(outputStream);
             outputStream.flush();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 拷贝文件夹下所有文件
     * @param sourcePath 源文件路径
     * @param targetPath 目标文件路径
     */
    public static void copyDirectory(String sourcePath,String targetPath){
        File source = new File(sourcePath);
        if(!source.exists() || source.isFile()) {
            throw new RuntimeException("file not exist");
        }

    }
}
