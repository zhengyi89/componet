package com.zbjdl.common.utils;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

/**
 * @author njgnini
 * 
 */
public class ImageUtils {

	/**
	 * 剪切图片
	 * @param srcFile 要剪切的图片文件
	 * @param destFile 剪切后的图片文件
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @throws IOException
	 */
	public static void cut(File srcFile, File destFile, int x, int y,
			int width, int height) throws IOException {
		ImageInputStream iis = null;
		FileInputStream is = null;
		try {
			is = new FileInputStream(srcFile);
			/*
			 * 返回包含所有当前已注册 ImageReader 的 Iterator，这些 ImageReader 声称能够解码指定格式。
			 * 参数：formatName - 包含非正式格式名称 .（例如 "jpeg" 或 "tiff"）等 。
			 */
			Iterator<ImageReader> it = ImageIO
					.getImageReadersByFormatName("jpg");
			ImageReader reader = it.next();
			// 获取图片流
			iis = ImageIO.createImageInputStream(is);

			/*
			 * <p>iis:读取源.true:只向前搜索 </p>.将它标记为 ‘只向前搜索’。
			 * 此设置意味着包含在输入源中的图像将只按顺序读取，可能允许 reader 避免缓存包含与以前已经读取的图像关联的数据的那些输入部分。
			 */
			reader.setInput(iis, true);

			/*
			 * <p>描述如何对流进行解码的类<p>.用于指定如何在输入时从 Java Image I/O
			 * 框架的上下文中的流转换一幅图像或一组图像。用于特定图像格式的插件 将从其 ImageReader 实现的
			 * getDefaultReadParam 方法中返回 ImageReadParam 的实例。
			 */
			ImageReadParam param = reader.getDefaultReadParam();

			/*
			 * 图片裁剪区域。Rectangle 指定了坐标空间中的一个区域，通过 Rectangle 对象
			 * 的左上顶点的坐标（x，y）、宽度和高度可以定义这个区域。
			 */
			Rectangle rect = new Rectangle(x, y, width, height);

			// 提供一个 BufferedImage，将其用作解码像素数据的目标。
			param.setSourceRegion(rect);

			/*
			 * 使用所提供的 ImageReadParam 读取通过索引 imageIndex 指定的对象，并将 它作为一个完整的
			 * BufferedImage 返回。
			 */
			BufferedImage bi = reader.read(0, param);
			// 保存新图片
			ImageIO.write(bi, "jpg", destFile);
		} finally {
			if (is != null)
				is.close();
			if (iis != null)
				iis.close();
		}
	}

	/**
	 * 图片缩放
	 * @param srcFile 要剪切的图片文件
	 * @param destFile 剪切后的图片文件
	 * @param width
	 * @param heigth
	 * @throws IOException
	 */
	public static void resize(File srcFile, File destFile, int width, int heigth) throws IOException {
		FileOutputStream fos = null;
		try {

			// 构造Image对象
			Image imageSrc = ImageIO.read(srcFile);
			BufferedImage bi = new BufferedImage(width, heigth, BufferedImage.TYPE_INT_RGB);
			// 绘制缩小后的图
			bi.getGraphics().drawImage(imageSrc.getScaledInstance(width, heigth,  
                    Image.SCALE_SMOOTH), 0, 0, null);
			// 输出到文件流
			ImageIO.write(bi, "jpg", destFile);
		} finally {
			if(fos != null) {
				fos.close();
			}
		}
	}
	
	/**
	 * 图片缩放
	 * @param input 要剪切的图片文件流
	 * @param output 剪切后的图片文件流
	 * @param width
	 * @param heigth
	 * @throws IOException
	 */
	public static void resize(InputStream input, OutputStream output, int width, int heigth) throws IOException {
		try{
			// 构造Image对象
			Image imageSrc = ImageIO.read(input);
			BufferedImage bi = new BufferedImage(width, heigth, BufferedImage.TYPE_INT_RGB);
			// 绘制缩小后的图
			bi.getGraphics().drawImage(imageSrc.getScaledInstance(width, heigth,  
	                Image.SCALE_SMOOTH), 0, 0, null);
			// 输出到文件流
			ImageIO.write(bi, "jpg", output);
		}finally{
			try {
	            if (input != null) {
	                input.close();
	            }
	        } catch (IOException ioe) {
	            // ignore
	        }
	        try {
	            if (output != null) {
	                output.close();
	            }
	        } catch (IOException ioe) {
	            // ignore
	        }
		}
	}

	/**
	 * 是否为图片
	 * @param file 文件
	 * @return 是否为图片
	 */
	public static boolean isImage(File file) {
		try {
			Image imageSrc = ImageIO.read(file);
			imageSrc.getWidth(null);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
