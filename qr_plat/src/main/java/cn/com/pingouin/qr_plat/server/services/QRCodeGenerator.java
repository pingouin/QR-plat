package cn.com.pingouin.qr_plat.server.services;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

import com.google.zxing.BarcodeFormat;

import cn.com.pingouin.qr_plat.server.helpers.QRCodeConfigHelper;


public interface QRCodeGenerator {

		// 二维码默认编码格式
		public static final BarcodeFormat BARCODE_FORMAT_QRCODE = BarcodeFormat.QR_CODE;

		// 图像色彩类型RGB
		public static final int BUFFERED_IMAGE_TYPE_RGB = BufferedImage.TYPE_INT_RGB;
		
		// 默认二维码宽度
		public static final int DEFAULT_QRCODE_IMAGE_WIDTH = 300;
		
		// 默认二维码高度
		public static final int DEFAULT_QRCODE_IMAGE_HEIGHT = 300;
		
		/**
		 * 生成二维码图片BufferedImage
		 * @param toEncode
		 * @param qrCodeConfigHelper
		 * @return
		 */
		BufferedImage generateQRCodeBufferedImage(String toEncode, QRCodeConfigHelper qrCodeConfigHelper);
		
		/**
		 * 生成二维码图片输出流ByteArrayOutputStream
		 * @param toEncode
		 * @param qrCodeConfigHelper
		 * @return
		 */
		ByteArrayOutputStream generateQRCodeOutputStream(String toEncode, QRCodeConfigHelper qrCodeConfigHelper);
		
		/**
		 * 
		 * @param toEncode
		 * @param qrCodeConfigHelper
		 */
		void generateQRCodeIntoFile(String toEncode, QRCodeConfigHelper qrCodeConfigHelper, File fileToSave);
	
}
