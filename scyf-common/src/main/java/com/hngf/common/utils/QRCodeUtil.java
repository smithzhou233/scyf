package com.hngf.common.utils;

import cn.hutool.core.util.RandomUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.hngf.common.exception.ScyfException;
import org.apache.commons.lang.StringUtils;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Hashtable;
import java.util.Random;

/**
 * 二维码工具类
 */
public class QRCodeUtil {

    private static final String CHARSET = "utf-8";//字符集
    private static final String FORMAT_NAME = "JPG";//文件类型
    private static final int QRCODE_SIZE = 300;//文件大小
    private static final int WIDTH = 60;//宽度
    private static final int HEIGHT = 60;//高度

    private static BufferedImage createImage(String content, String imgPath, boolean needCompress, String bottomDes) throws Exception {
        Hashtable hints = new Hashtable();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = (new MultiFormatWriter()).encode(content, BarcodeFormat.QR_CODE, 300, 300, hints);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        int tempHeight = height;
        boolean needDescription = false;
        if (null != bottomDes && !"".equals(bottomDes)) {
            needDescription = true;
        }

        if (needDescription) {
            tempHeight = height + 20;
        }

        BufferedImage image = new BufferedImage(width, tempHeight, 1);

        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? -16777216 : -1);
            }
        }

        if (needDescription) {
            addFontImage(image, bottomDes);
            return image;
        } else if (imgPath != null && !"".equals(imgPath)) {
            insertImage(image, imgPath, needCompress);
            return image;
        } else {
            return image;
        }
    }

    private static void addFontImage(BufferedImage source, String declareText) {
        BufferedImage textImage = getImage(declareText, 300, 50);
        Graphics2D graph = source.createGraphics();
        int width = textImage.getWidth((ImageObserver)null);
        int height = textImage.getHeight((ImageObserver)null);
        graph.drawImage(textImage, 0, 280, width, height, (ImageObserver)null);
        graph.dispose();
    }

    private static BufferedImage createImage(String content, Font font, Integer width, Integer height) {
        BufferedImage bi = new BufferedImage(width, height, 1);
        Graphics2D g2 = (Graphics2D)bi.getGraphics();
        g2.setBackground(Color.WHITE);
        g2.clearRect(0, 0, width, height);
        g2.setPaint(Color.BLACK);
        g2.setFont(font);
        FontRenderContext context = g2.getFontRenderContext();
        Rectangle2D bounds = font.getStringBounds(content, context);
        double x = ((double)width - bounds.getWidth()) / 2.0D;
        double y = ((double)height - bounds.getHeight()) / 2.0D;
        double ascent = -bounds.getY();
        double baseY = y + ascent;
        g2.drawString(content, (int)x, (int)baseY);
        return bi;
    }

    public static BufferedImage getImage(String content, Integer width, Integer height) {
        width = width == null ? 60 : width;
        height = height == null ? 60 : height;
        Font font = new Font("宋体", 0, 12);
        return createImage(content, font, width, height);
    }

    public static InputStream getInternetPic(String filePath) {
        InputStream inStream = null;

        try {
            URL url = new URL(filePath);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            inStream = conn.getInputStream();
        } catch (Exception var4) {
            System.out.println(var4.getMessage());
        }

        return inStream;
    }

    private static void insertImage(BufferedImage source, String imgPath, boolean needCompress) throws Exception {
        Image src = null;
        if (imgPath.contains("http")) {
            InputStream InputStream = getInternetPic(imgPath);
            if (InputStream == null) {
                throw new ScyfException("" + imgPath + "   该文件不存在！");
            }

            src = ImageIO.read(InputStream);
        } else {
            File file = new File(imgPath);
            if (!file.exists()) {
                throw new ScyfException("" + imgPath + "   该文件不存在！");
            }

            src = ImageIO.read(new File(imgPath));
        }

        int width = ((Image)src).getWidth((ImageObserver)null);
        int height = ((Image)src).getHeight((ImageObserver)null);
        if (needCompress) {
            if (width > 60) {
                width = 60;
            }

            if (height > 60) {
                height = 60;
            }

            Image image = ((Image)src).getScaledInstance(width, height, 4);
            BufferedImage tag = new BufferedImage(width, height, 1);
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, (ImageObserver)null);
            g.dispose();
            src = image;
        }

        Graphics2D graph = source.createGraphics();
        int x = (300 - width) / 2;
        int y = (300 - height) / 2;
        graph.drawImage((Image)src, x, y, width, height, (ImageObserver)null);
        Shape shape = new RoundRectangle2D.Float((float)x, (float)y, (float)width, (float)width, 6.0F, 6.0F);
        graph.setStroke(new BasicStroke(3.0F));
        graph.draw(shape);
        graph.dispose();
    }

    public static String encode(String content, String imgPath, String destPath, boolean needCompress, String saveName, String bottomDes) throws Exception {
        BufferedImage image = createImage(content, imgPath, needCompress, bottomDes);
        mkdirs(destPath);
        String file = (new Random()).nextInt(99999999) + ".png";
        if (StringUtils.isNotEmpty(saveName)) {
            file = saveName + ".png";
        }
        String outfile = destPath + "/" + file;
        ImageIO.write(image, "JPG", new File(outfile));
        return outfile;
    }

    public static void mkdirs(String destPath) {
        File file = new File(destPath);
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
    }

    public static void main(String[] args) throws Exception {
        for(int i = 0; i <= 2000; ++i) {
            encode(RandomUtil.randomUUID(), "", "/qread", false, String.valueOf(i), "");
        }
    }
}
