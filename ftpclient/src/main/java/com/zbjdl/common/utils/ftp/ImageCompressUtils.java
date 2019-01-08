package com.zbjdl.common.utils.ftp;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

//import com.sun.image.codec.jpeg.JPEGCodec;
//import com.sun.image.codec.jpeg.JPEGImageEncoder; 

public class ImageCompressUtils {
	public static InputStream compressImage(InputStream is, int outputWidth,
			int outputHeight) {
		InputStream result = null;
		try {
			Image img = ImageIO.read(is);
			// 判断图片格式是否正确
			if (img.getWidth(null) == -1) {
				System.out.println("can't read,retry!" + "<BR>");
				result = is;
			} else {
				int newWidth;
				int newHeight;
				// 为等比缩放计算输出的图片宽度及高度
				int width = img.getWidth(null);
				int height = img.getHeight(null);
				double rate = (double) (outputWidth * outputHeight)
						/ (double) (width * height);
				if (rate >= 1) {
					rate = 1;
				}
				// 根据缩放比率大的进行缩放控制
				double rate1 = Math.sqrt(rate);
				newWidth = (int) (width * rate1);
				newHeight = (int) (height * rate1);

				BufferedImage tag = new BufferedImage((int) newWidth,
						(int) newHeight, BufferedImage.TYPE_INT_RGB);

				/*
				 * Image.SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的 优先级比速度高 生成的图片质量比较好 但速度慢
				 */
				tag.getGraphics().drawImage(
						img.getScaledInstance(newWidth, newHeight,
								Image.SCALE_SMOOTH), 0, 0, null);
				ByteArrayOutputStream out = new ByteArrayOutputStream();
//				// JPEGImageEncoder可适用于其他图片类型的转换
//				JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//				// JPEGEncodeParam jep=JPEGCodec.getDefaultJPEGEncodeParam(tag);
//				/* 压缩质量 */
//				// jep.setQuality(0, true);
//				encoder.encode(tag);
				
				ImageIO.write(tag,"jpeg",out);

				out.close();
				result = new ByteArrayInputStream(out.toByteArray());
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex.getMessage(), ex);
		}finally{
			try{
				is.close();
			}catch(Exception e){
				
			}
		}
		return result;
	}
}
