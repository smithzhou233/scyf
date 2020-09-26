package com.hngf.common.utils;

import com.hngf.common.exception.ScyfException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 文件工具类型
 */
public class FileUtils {

    private static Logger logger = LoggerFactory.getLogger(FileUtils.class);

    /**
     * 删除文件
     * @param dir
     * @return
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for(int i = 0; i < children.length; ++i) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    /**
     * 复制文件
     * @param oldPath
     * @param newPath
     * @throws IOException
     */
    public static void copyfile(String oldPath, String newPath) throws IOException {
        File oldFile = new File(oldPath);
        if (oldFile.exists()) {
            try {
                FileInputStream fis = new FileInputStream(oldFile);
                FileOutputStream fos = new FileOutputStream(newPath);
                byte[] buffer = new byte[1024];

                int hasRead;
                while((hasRead = fis.read(buffer)) != -1) {
                    fos.write(buffer, 0, hasRead);
                }
                fis.close();
            } catch (Exception e) {
                logger.error("copyfile", e);
                throw new IOException(e.getMessage());
            }
        }

    }

    /**
     * 复制文件夹
     * @param oldPath
     * @param newPath
     * @throws IOException
     */
    public static void copyDir(String oldPath, String newPath) throws IOException {
        if (!(new File(newPath)).exists()) {
            (new File(newPath)).mkdirs();
        }

        File f1 = new File(oldPath);
        File[] files = f1.listFiles();

        for(int i = 0; i < files.length; ++i) {
            if (files[i].isDirectory()) {
                File dirNew = new File(newPath + File.separator + files[i].getName());
                dirNew.mkdir();
                copyDir(oldPath + File.separator + files[i].getName(), newPath + File.separator + files[i].getName());
            } else {
                String filePath = newPath + File.separator + files[i].getName();
                copyfile(files[i].getAbsolutePath(), filePath);
            }
        }

    }

    /**
     * 文件压缩
     * @param target
     * @param filePath
     * @param fileName
     * @return
     */
    public static File zipFiles(String target, String filePath, String fileName) {
        File srcfile = new File(target);
        ZipOutputStream out = null;
        File targetFile = new File(filePath, fileName.concat(".zip"));

        try {
            if (targetFile.exists()) {
                targetFile.delete();
            } else if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }

            out = new ZipOutputStream(new FileOutputStream(targetFile));
            if (srcfile.isFile()) {
                zipFile(srcfile, out, "");
            } else {
                File[] list = srcfile.listFiles();

                for(int i = 0; i < list.length; ++i) {
                    compress(list[i], out, "");
                }
            }

            System.out.println("压缩完毕");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return targetFile;
    }

    //执行压缩
    private static void compress(File file, ZipOutputStream out, String basedir) {
        if (file.isDirectory()) {
            zipDirectory(file, out, basedir);
        } else {
            zipFile(file, out, basedir);
        }

    }

    /**
     * 文件压缩
     * @param srcfile
     * @param out
     * @param basedir
     */
    public static void zipFile(File srcfile, ZipOutputStream out, String basedir) {
        if (srcfile.exists()) {
            byte[] buf = new byte[1024];
            FileInputStream in = null;

            try {
                in = new FileInputStream(srcfile);
                out.putNextEntry(new ZipEntry(basedir + srcfile.getName()));

                int len;
                while((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.closeEntry();
                    }

                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
    }

    /**
     * 文件夹打包
     * @param dir
     * @param out
     * @param basedir
     */
    public static void zipDirectory(File dir, ZipOutputStream out, String basedir) {
        if (dir.exists()) {
            File[] files = dir.listFiles();
            for(int i = 0; i < files.length; ++i) {
                compress(files[i], out, basedir + dir.getName() + "/");
            }

        }
    }

    /**
     * 删除文件夹下所有文件
     * @param folderPath
     */
    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath);
            String filePath = folderPath.toString();
            File myFilePath = new File(filePath);
            myFilePath.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        } else if (!file.isDirectory()) {
            return flag;
        } else {
            String[] tempList = file.list();
            File temp = null;

            for(int i = 0; i < tempList.length; ++i) {
                if (path.endsWith(File.separator)) {
                    temp = new File(path + tempList[i]);
                } else {
                    temp = new File(path + File.separator + tempList[i]);
                }

                if (temp.isFile()) {
                    temp.delete();
                }

                if (temp.isDirectory()) {
                    delAllFile(path + "/" + tempList[i]);
                    delFolder(path + "/" + tempList[i]);
                    flag = true;
                }
            }

            return flag;
        }
    }

    /**
     * 前端文件下载
     * @param response
     * @param file
     * @throws Exception
     */
    public static void getOutputStream(HttpServletResponse response, File file) throws Exception{
        if (file.exists()) {
            String fileName = null;
            try {
                fileName = URLEncoder.encode(file.getName(), "UTF-8");//
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                throw new ScyfException("文件名称转码失败！");
            }

            response.setContentLength((int)file.length());
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.setContentType("application/octet-stream");

            FileInputStream fileInputStream = null;
            BufferedInputStream bufferedInputStream = null;
            OutputStream outputStream = null;
            try {
                fileInputStream = new FileInputStream(file);
                bufferedInputStream = new BufferedInputStream(fileInputStream);
                byte[] b = new byte[bufferedInputStream.available()];
                bufferedInputStream.read(b);
                outputStream = response.getOutputStream();
                outputStream.write(b);
            } catch (IOException e) {
                e.printStackTrace();
                throw new ScyfException("文件读取失败！");
            }finally {//发生异常时，一定要把流关掉
                if(null != fileInputStream){
                    try {
                        fileInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(null != bufferedInputStream){
                    try {
                        bufferedInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(null != outputStream){
                    try {
                        outputStream.flush();
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
