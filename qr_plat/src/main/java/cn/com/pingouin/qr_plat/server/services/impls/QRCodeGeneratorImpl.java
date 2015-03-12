package cn.com.pingouin.qr_plat.server.services.impls;


import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Hashtable;

import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import cn.com.pingouin.qr_plat.server.enums.ImageFileFormatEnum;
import cn.com.pingouin.qr_plat.server.helpers.ARGBColorHelper;
import cn.com.pingouin.qr_plat.server.helpers.QRCodeConfigHelper;
import cn.com.pingouin.qr_plat.server.services.QRCodeGenerator;

public class QRCodeGeneratorImpl implements QRCodeGenerator {

public BufferedImage generateQRCodeBufferedImage(String toEncode, QRCodeConfigHelper qrCodeConfigHelper) {
		
		checkQRCodeConfig(qrCodeConfigHelper);
		
		BufferedImage image = new BufferedImage(qrCodeConfigHelper.getWidth(), qrCodeConfigHelper.getHeight(), BUFFERED_IMAGE_TYPE_RGB);
		
		MatrixToImageConfig config = new MatrixToImageConfig(qrCodeConfigHelper.getOnColor(), qrCodeConfigHelper.getOffColor());
		
		try {
			// encode
			BitMatrix matrix = new QRCodeWriter().encode(toEncode, BARCODE_FORMAT_QRCODE, qrCodeConfigHelper.getWidth(), qrCodeConfigHelper.getHeight(), qrCodeConfigHelper.getHints());
			// to bufferedImage
			image = MatrixToImageWriter.toBufferedImage(matrix, config);
		} catch (Exception e) {
			//
		}
		
		return image;
	}
	
	public ByteArrayOutputStream generateQRCodeOutputStream(String toEncode, QRCodeConfigHelper qrCodeConfigHelper) {
		
		checkQRCodeConfig(qrCodeConfigHelper);
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		MatrixToImageConfig config = new MatrixToImageConfig(qrCodeConfigHelper.getOnColor(), qrCodeConfigHelper.getOffColor());
		
		try {
			// encode
			BitMatrix matrix = new QRCodeWriter().encode(toEncode, BARCODE_FORMAT_QRCODE, qrCodeConfigHelper.getWidth(), qrCodeConfigHelper.getHeight(), qrCodeConfigHelper.getHints());
			// to byteArrayOutputStream
			MatrixToImageWriter.writeToStream(matrix, qrCodeConfigHelper.getOutImageFormat(), outputStream, config);
		} catch (Exception e) {
			//
		}
		
		return outputStream;
	}
	
	public void generateQRCodeIntoFile(String toEncode, QRCodeConfigHelper qrCodeConfigHelper, File fileToSave) {
		checkQRCodeConfig(qrCodeConfigHelper);
		
		MatrixToImageConfig config = new MatrixToImageConfig(qrCodeConfigHelper.getOnColor(), qrCodeConfigHelper.getOffColor());
		
		try {
			// encode
			BitMatrix matrix = new QRCodeWriter().encode(toEncode, BARCODE_FORMAT_QRCODE, qrCodeConfigHelper.getWidth(), qrCodeConfigHelper.getHeight(), qrCodeConfigHelper.getHints());
			// save to file
			MatrixToImageWriter.writeToFile(matrix, qrCodeConfigHelper.getOutImageFormat(), fileToSave, config);
		} catch (Exception e) {
			//
		}
	}
	
    // -------------------------------------------------//
    // ------------ Supernatural Separator -------------//
    // -------------------------------------------------//
	
	private void checkQRCodeConfig(QRCodeConfigHelper qrCodeConfigHelper) {
		// 未指定属性使用默认值
		
		if(qrCodeConfigHelper.getWidth() == null ||  qrCodeConfigHelper.getWidth() <= 0) {
			qrCodeConfigHelper.setWidth(DEFAULT_QRCODE_IMAGE_WIDTH);
		}
		
		if(qrCodeConfigHelper.getHeight() == null ||  qrCodeConfigHelper.getHeight() <= 0) {
			qrCodeConfigHelper.setHeight(DEFAULT_QRCODE_IMAGE_HEIGHT);
		}
		
		if(qrCodeConfigHelper.getOnColor() == null) {
			qrCodeConfigHelper.setOnColor(ARGBColorHelper.Black);
		}
		
		if(qrCodeConfigHelper.getOffColor() == null) {
			qrCodeConfigHelper.setOffColor(ARGBColorHelper.White);
		}
		
		if(qrCodeConfigHelper.getHints() == null) {
			Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
			hints.put(EncodeHintType.MARGIN, 0);
			hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
			qrCodeConfigHelper.setHints(hints);
		}
		
		if (qrCodeConfigHelper.getOutImageFormat() == null) {
			qrCodeConfigHelper.setOutImageFormat(ImageFileFormatEnum.PNG.getKey());
		}
	}

}
