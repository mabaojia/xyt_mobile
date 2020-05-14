package com.project.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
public class ZipUtils {

    private static final int  BUFFER_SIZE = 2 * 1024;
    
    /**
     * 
     * 
     * 青岛小道福利信息技术服务有限公司
     * http://www.xiaodaofuli.com
     * 联系方式：137-9192-7167
     * 技术QQ：2511251392
     */
    public static void toZip(String srcDir, OutputStream out, boolean KeepDirStructure) throws RuntimeException{
 
        long start = System.currentTimeMillis();
        ZipOutputStream zos = null ;
        try {
            zos = new ZipOutputStream(out);
            File sourceFile = new File(srcDir);
            compress(sourceFile,zos,sourceFile.getName(),KeepDirStructure);
            long end = System.currentTimeMillis();
            System.out.println("压缩完成，耗时：" + (end - start) +" ms");
        } catch (Exception e) {
            throw new RuntimeException("zip error from ZipUtils",e);
        }finally{
            if(zos != null){
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * 
     * 
     * 青岛小道福利信息技术服务有限公司
     * http://www.xiaodaofuli.com
     * 联系方式：137-9192-7167
     * 技术QQ：2511251392
     */
    private static void compress(File sourceFile, ZipOutputStream zos, String name, boolean KeepDirStructure) throws Exception{
 
        byte[] buf = new byte[BUFFER_SIZE];
        if(sourceFile.isFile()){
            zos.putNextEntry(new ZipEntry(name));
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1){
                zos.write(buf, 0, len);
            }
            zos.closeEntry();
            in.close();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if(listFiles == null || listFiles.length == 0){
                if(KeepDirStructure){
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    zos.closeEntry();
                }
            }else {
                for (File file : listFiles) {
                    if (KeepDirStructure) {
                        compress(file, zos, name + "/" + file.getName(),KeepDirStructure);
                    } else {
                        compress(file, zos, file.getName(),KeepDirStructure);
                    }
                }
            }
        }
    }
    /**
     * 
     * 
     * 青岛小道福利信息技术服务有限公司
     * http://www.xiaodaofuli.com
     * 联系方式：137-9192-7167
     * 技术QQ：2511251392
     */
    public static void copyFileUsingFileStreams(File source, File dest) throws IOException {
    	
        InputStream input = null;    
        OutputStream output = null;    
        try {
               input = new FileInputStream(source);
               output = new FileOutputStream(dest);        
               byte[] buf = new byte[1024];        
               int bytesRead;        
               while ((bytesRead = input.read(buf)) > 0) {
                   output.write(buf, 0, bytesRead);
               }
        } finally {
            input.close();
            output.close();
        }
    }
    /**
     * 
     * 
     * 青岛小道福利信息技术服务有限公司
     * http://www.xiaodaofuli.com
     * 联系方式：137-9192-7167
     * 技术QQ：2511251392
     */
    public static void main(String[] args) throws Exception {

    	FileOutputStream fos = new FileOutputStream(new File("d:/test.zip"));
        ZipUtils.toZip("d:/test", fos, true);
    }
}