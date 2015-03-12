package cn.com.pingouin.qr_plat.server.helpers;

import java.util.Hashtable;

import com.google.zxing.EncodeHintType;

public class BarcodeConfigHelper {

	private Integer width;
	private Integer height;
	private Hashtable<EncodeHintType, Object> hints;
	private String outImageFormat;
	
	public Integer getWidth() {
		return width;
	}
	public void setWidth(Integer width) {
		this.width = width;
	}
	public Integer getHeight() {
		return height;
	}
	public void setHeight(Integer height) {
		this.height = height;
	}
	public Hashtable<EncodeHintType, Object> getHints() {
		return hints;
	}
	public void setHints(Hashtable<EncodeHintType, Object> hints) {
		this.hints = hints;
	}
	public String getOutImageFormat() {
		return outImageFormat;
	}
	public void setOutImageFormat(String outImageFormat) {
		this.outImageFormat = outImageFormat;
	}
	
}
