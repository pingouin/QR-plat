package cn.com.pingouin.qr_plat.server.helpers;

import java.util.Hashtable;

import com.google.zxing.EncodeHintType;

public class QRCodeConfigHelper {

	private Integer width;
	private Integer height;
	private Integer onColor;
	private Integer offColor;
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
	public Integer getOnColor() {
		return onColor;
	}
	public void setOnColor(Integer onColor) {
		this.onColor = onColor;
	}
	public Integer getOffColor() {
		return offColor;
	}
	public void setOffColor(Integer offColor) {
		this.offColor = offColor;
	}
	public String getOutImageFormat() {
		return outImageFormat;
	}
	public void setOutImageFormat(String outImageFormat) {
		this.outImageFormat = outImageFormat;
	}
	
}
