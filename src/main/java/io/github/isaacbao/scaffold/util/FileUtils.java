package io.github.isaacbao.scaffold.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URL;
import org.apache.commons.io.IOUtils;

/**
 * Created by rongyang_lu on 2017/8/21.
 */
public class FileUtils {
    static final Logger logger = LogManager.getLogger();

    /**
     * 判断文件夹是否存在，不存在则创建一个
     */
    private static String checkAndCreate(String path) {
        File file = new File(path);
        if (!file.isDirectory()) {
            file.mkdir();
        }
        return path;
    }


    public static String readFile(String file_name, String charset) throws IOException {
        File file = new File(file_name);
        return readFile(file);
    }

    public static String readFile(String file_name) throws IOException {
        String charset = getCharset(file_name);
        return readFile(file_name, charset);
    }

    public static String readFile(File file) throws IOException {
        String result = "";
        try {
            if (file.isFile() && file.exists()) {
                //获取字符串
                String file_charset = getCharset(file.getPath());
                logger.debug("读文件：" + file.getAbsoluteFile());
                logger.debug("编码：" + file_charset);
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), file_charset);
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTXT;
                while ((lineTXT = bufferedReader.readLine()) != null) {
                    result += lineTXT.trim();
                }
                read.close();
                bufferedReader.close();
            } else {
                logger.error("找不到指定的文件:" + file.getAbsoluteFile());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static byte[] getBytesFromFile(String file_name) {
        byte[] bytes = null;
        try {
            if (file_name == null) {
                return null;
            }
            File file = new File(file_name);
            FileInputStream in = new FileInputStream(file);
            ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
            byte[] b = new byte[4096];
            int n;
            while ((n = in.read(b)) != -1) {
                out.write(b, 0, n);
            }
            in.close();
            out.close();
            bytes = out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public static byte[] getBytesFromFile(File file) {
        byte[] ret = null;
        try {
            if (file == null) {
                return null;
            }
            FileInputStream in = new FileInputStream(file);
            ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
            byte[] b = new byte[4096];
            int n;
            while ((n = in.read(b)) != -1) {
                out.write(b, 0, n);
            }
            in.close();
            out.close();
            ret = out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static String getCharset(File file) {
        try {
            //获取文件的编码
            String file_charset = null;
            BufferedInputStream bin = new BufferedInputStream(new FileInputStream(file));
            int p = (bin.read() << 8) + bin.read();
            switch (p) {
                case 0xefbb:
                    file_charset = "UTF-8";
                    break;
                case 0xfffe:
                    file_charset = "Unicode";
                    break;
                case 0xfeff:
                    file_charset = "UTF-16BE";
                    break;
                default:
                    file_charset = "GBK";
            }
            bin.close();

            //获取字符串编码（通过前20行内的文本来判断）
            int line = 0;
            String content = "";
            InputStreamReader read = new InputStreamReader(new FileInputStream(file), file_charset);
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTXT;
            while ((lineTXT = bufferedReader.readLine()) != null) {
                line++;
                content += lineTXT.trim();
                if (line >= 20) break;
            }

            //矫正文件编码
            String str_charset = getStringCode(content);
            read.close();
            bufferedReader.close();
            return str_charset;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getCharset(String fileName) {
        File file = new File(fileName);
        return getCharset(file);
    }

    private static String getStringCode(String str) {
        try {
            String encode = "GB2312";
            if (str.equals(new String(str.getBytes(encode), encode))) {
                return encode;
            }
            encode = "ISO-8859-1";
            if (str.equals(new String(str.getBytes(encode), encode))) {
                return encode;
            }
            encode = "UTF-8";
            if (str.equals(new String(str.getBytes(encode), encode))) {
                return encode;
            }
            encode = "GBK";
            if (str.equals(new String(str.getBytes(encode), encode))) {
                return encode;
            }
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void writeFile(String content, String path)
            throws FileNotFoundException {
        String dir = getDir(path);
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        OutputStream output = new FileOutputStream(path);
        try {
            output.write(content.getBytes());
        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * 获取文件路径的目录地址
     */
    public static String getDir(String path) {
        String dir = path.substring(0, getLastSeparator(path));
        logger.debug("File dir:" + dir);
        return dir;
    }

    private static int getLastSeparator(String path) {
        return path.lastIndexOf('/') > path.lastIndexOf('\\') ? path.lastIndexOf('/') : path.lastIndexOf('\\');
    }

    public static void saveImage(String imageUrl, String destinationFile)
            throws IOException {
        URL url = new URL(imageUrl);
        InputStream is = url.openStream();
        OutputStream os = new FileOutputStream(destinationFile);
        byte[] b = new byte[2048];
        int length;
        while ((length = is.read(b)) != -1) {
            os.write(b, 0, length);
        }
        is.close();
        os.close();
    }

    public static String[] getAllFileName(String dir_path) {
        File dir = new File(dir_path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        if (!dir.isDirectory()) {
            return null;
        }
        return dir.list();
    }

    public static InputStream readFileAsStream(String file_path) throws FileNotFoundException {
        File file = new File(file_path);
        return readFileAsStream(file_path);
    }

    public static InputStream readFileAsStream(File file) throws FileNotFoundException {
        logger.debug("Read file:" + file.getAbsolutePath());
        InputStream inputStream = new FileInputStream(file);
        return inputStream;
    }

    public static void writeFile(InputStream is, String path) throws IOException {
        String dir = getDir(path);
        makeInexistentDirs(dir);
        try (OutputStream output = new FileOutputStream(path)) {
            output.write(IOUtils.toByteArray(is));
        }
    }

    public static boolean deleteFile(String path) throws FileNotFoundException {
        File file = new File(path);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }


    public static void makeInexistentDirs(String dir) {
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
    }
}
