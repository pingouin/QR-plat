package cn.com.pingouin.qr_plat.server.servlets;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;

import cn.com.pingouin.qr_plat.server.enums.ImageFileFormatEnum;
import cn.com.pingouin.qr_plat.server.enums.RequestParameterEnum;
import cn.com.pingouin.qr_plat.server.helpers.QRCodeConfigHelper;
import cn.com.pingouin.qr_plat.server.helpers.ServletConstantsHelper;
import cn.com.pingouin.qr_plat.server.services.impls.QRCodeGeneratorImpl;

public class EncodeRequestServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//用于判断logo是否存在且是否符合要求
    private Boolean LOGO_URL_VALIDATION = false;
    private Boolean LOGO_UPLOADED_VALIDATION = false;
    
    private final static String IMAGE_CONTENT_TYPE = "image/";
    
    private String outImageFormat = ImageFileFormatEnum.PNG.getKey();
    
    private File servletContextFilePath;
    
    private int defaultWidth = 0;
    private int defaultHeight = 0;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	
    	if (request.getParameter(RequestParameterEnum.REQUEST_URL.getKey()) == null || request.getParameter(RequestParameterEnum.REQUEST_URL.getKey()).trim().length() <= 0) {
        	response.sendError(503, "Could not get request URL");
            return;
        }
    	
    	String requestText = new String(request.getParameter(RequestParameterEnum.REQUEST_URL.getKey()).getBytes("ISO-8859-1"), "UTF-8");
    	String requestWidth = request.getParameter(RequestParameterEnum.ENCODE_WIDTH.getKey());
        String requestHeight = request.getParameter(RequestParameterEnum.ENCODE_HEIGHT.getKey());
        String requestColor = request.getParameter(RequestParameterEnum.ENCODE_COLOR.getKey());
        String requestLogoUrl = "";
        if(request.getParameter(RequestParameterEnum.ENCODE_LOGO_URL.getKey()) != null && request.getParameter(RequestParameterEnum.ENCODE_LOGO_URL.getKey()).trim().length() > 0) {
        	requestLogoUrl = new String(request.getParameter(RequestParameterEnum.ENCODE_LOGO_URL.getKey()).getBytes("ISO-8859-1"), "UTF-8");
        }
    
        String destFolder = servletContextFilePath.getPath();
        
        String requestTextMD5 = DigestUtils.md5Hex(requestText);
        String intermediateRequestText = requestText;
        
        // 获得上传文件的servlet传递的参数
        String requestAttribute_UploadLogoPath = "";
        if (request.getSession().getAttribute(ServletConstantsHelper.ATTRIBUTE_UPLOADFILEPATH) != null) {
        	//
            requestAttribute_UploadLogoPath = (String) request.getSession().getAttribute(ServletConstantsHelper.ATTRIBUTE_UPLOADFILEPATH);
            request.getSession().removeAttribute(ServletConstantsHelper.ATTRIBUTE_UPLOADFILEPATH);
        }
        
        // 宽高正则验证是否为数字;颜色正则验证是否为不大于六位的16进制
        Pattern requestColorPattern = Pattern.compile("^[0-9a-fA-F]{0,6}$");
        Pattern requestSizePattern = Pattern.compile("^[0-9]+$");
        
        // 
        int mWidth = defaultWidth;
        int mHeight = defaultHeight;
        if (requestWidth != null && requestWidth.trim().length() > 0 
        		&& requestHeight != null && requestHeight.trim().length() > 0 
        		&& requestSizePattern.matcher(requestWidth).matches() && requestSizePattern.matcher(requestHeight).matches()) {
        	//
            mWidth = Integer.parseInt(requestWidth);
            mHeight = Integer.parseInt(requestHeight);
        }
        
        //验证logo的存在与是否符合系统要求
        URL logoUrl = null;
        try {
        	logoUrl = new URL(requestLogoUrl);
        	URLConnection connection = logoUrl.openConnection();
        	if (connection.getContentType().startsWith("image/")) {
        		int midWidth = (int) (mWidth/2);
        		int midHeight = (int) (mHeight/2);
        		int midSquare = (int) ((mWidth*mHeight)*0.15);
        		if (ImageIO.read(logoUrl).getWidth() < midWidth && 
        				ImageIO.read(logoUrl).getHeight() < midHeight && 
        				(ImageIO.read(logoUrl).getWidth()*ImageIO.read(logoUrl).getHeight()) <= midSquare) 
        		{
        			LOGO_URL_VALIDATION = true;
        		}
        	}
		} catch (Exception e) {
			log("URLConnection Exception : " + e.getMessage());
		}
        
        //
        String uploadLogoPath = "";
        File uploadedLogoImage = null;
        //
        if (requestAttribute_UploadLogoPath != null && requestAttribute_UploadLogoPath.trim().length() > 0) {
        	//
            uploadLogoPath = requestAttribute_UploadLogoPath;
            uploadedLogoImage = new File(uploadLogoPath);
            //
            if (uploadedLogoImage.exists()) {
                int midWidth = (int) (mWidth/2);
                int midHeight = (int) (mHeight/2);
                int midSquare = (int) ((mWidth*mHeight)*0.15);
                if (ImageIO.read(uploadedLogoImage).getWidth() < midWidth && 
                        ImageIO.read(uploadedLogoImage).getHeight() < midHeight && 
                        (ImageIO.read(uploadedLogoImage).getWidth()*ImageIO.read(uploadedLogoImage).getHeight()) <= midSquare) 
                {
                    LOGO_UPLOADED_VALIDATION = true;
                }
            }
        }
        
      //--------输出图片流--------
        
        //有否logo及有否颜色的六种情况
        //既有logo网络地址又有上传的logo时，使用网络地址
        //有logo的话，先存储无logo的图片，再将logo画到已有图片上，并覆盖原图
        //解析接收的颜色代码字符串的方法：转换为color，再解析出int类型的RGB
        {
	        QRCodeConfigHelper qrCodeConfigHelper = new QRCodeConfigHelper();
	        BufferedImage logoImage = null;
	        
	        if (LOGO_URL_VALIDATION) {
	            if (requestColor != null && requestColor.length() == 6 && requestColorPattern.matcher(requestColor).matches()) {
	            	Color color = new Color((int) Long.parseLong(requestColor, 16), false);
	            	qrCodeConfigHelper.setOnColor(color.getRGB());
	            	logoImage = ImageIO.read(logoUrl);
	            }
	        } else if (LOGO_UPLOADED_VALIDATION) {
	        	if (requestColor != null && requestColor.length() == 6 && requestColorPattern.matcher(requestColor).matches()) {
	        		if (uploadedLogoImage != null && uploadedLogoImage.exists()) {
	        			Color color = new Color((int) Long.parseLong(requestColor, 16), false);
	        			qrCodeConfigHelper.setOnColor(color.getRGB());
	        			logoImage = ImageIO.read(uploadedLogoImage);
	        		}
	        	} else {
	        		if (uploadedLogoImage != null && uploadedLogoImage.exists()) {
	        			logoImage = ImageIO.read(uploadedLogoImage);
	        		}
	        	}
	        } else {
	        	if (requestColor != null && requestColor.length() == 6 && requestColorPattern.matcher(requestColor).matches()) {
	        		Color color = new Color((int) Long.parseLong(requestColor, 16), false);
	        		qrCodeConfigHelper.setOnColor(color.getRGB());
	        	}
	        }
	        
	        ByteArrayOutputStream outputStream = null;
	        
	    	BufferedImage image = new BufferedImage(mWidth, mHeight, BufferedImage.TYPE_INT_RGB);
	        qrCodeConfigHelper.setWidth(mWidth);
	        qrCodeConfigHelper.setHeight(mHeight);
	        QRCodeGeneratorImpl qrCodeGeneratorImpl = new QRCodeGeneratorImpl();
	        
	        // 
	        File fileToSave = createFile(requestTextMD5, destFolder, outImageFormat);
        	qrCodeGeneratorImpl.generateQRCodeIntoFile(requestText, qrCodeConfigHelper, fileToSave);
	        
	        if(logoImage != null) {
	        	outputStream = new ByteArrayOutputStream();
	        	//
	        	BufferedImage qrImage = qrCodeGeneratorImpl.generateQRCodeBufferedImage(requestText, qrCodeConfigHelper);
	        	
	        	Graphics2D graphics = image.createGraphics();
	        	graphics.drawImage(qrImage, null, 0, 0);
	        	graphics.drawImage(logoImage, null, (mWidth-logoImage.getWidth())/2, (mHeight - logoImage.getHeight())/2);
	        	graphics.dispose();
	        	ImageIO.write(image, outImageFormat, outputStream);
	        	
	        	//
	        	if (!ImageIO.write(image, outImageFormat, fileToSave)) {
                    throw new IOException("Could not write an image of format " + outImageFormat + " to " + fileToSave);
                }
	        } else {
	        	outputStream = qrCodeGeneratorImpl.generateQRCodeOutputStream(requestText, qrCodeConfigHelper);
	        }
	        
	        response.setContentType(IMAGE_CONTENT_TYPE + outImageFormat);
	        response.setContentLength(outputStream.size());
	        OutputStream outStream = response.getOutputStream();
	        outStream.write(outputStream.toByteArray());
	        outStream.flush();
	        outStream.close();
        }
        
        //---------输出图片文件---------
        
        //通过前两级域名来确定存入的文件路径destFolder
        if (! requestText.endsWith("/")) {
            intermediateRequestText = requestText + "/";
        }   
        
        String urlRootFolder = "";
        String urlSecondFolder = "";
        if (intermediateRequestText.substring(intermediateRequestText.indexOf("//") + 2) != null && intermediateRequestText.substring(intermediateRequestText.indexOf("//") + 2).length() != 0) {
            urlRootFolder = intermediateRequestText.substring(intermediateRequestText.indexOf("//") + 2);
            //avoiding the error, confirm whether it ends with "/" one more time
            if (!urlRootFolder.endsWith("/")) {urlRootFolder = urlRootFolder + "/";}
            if (urlRootFolder.substring(urlRootFolder.indexOf("/") + 1) != null && urlRootFolder.substring(urlRootFolder.indexOf("/") + 1).length() != 0) {
                urlSecondFolder = urlRootFolder.substring(urlRootFolder.indexOf("/") + 1);
                //avoiding the error, confirm whether it ends with "/" one more time
                if (!urlSecondFolder.endsWith("/")) {urlSecondFolder = urlSecondFolder + "/";}
                urlSecondFolder = urlSecondFolder.replaceAll(urlSecondFolder.substring(urlSecondFolder.indexOf("/")), "");
            } else {
                urlSecondFolder = "";
            }
            urlRootFolder = urlRootFolder.replaceAll(urlRootFolder.substring(urlRootFolder.indexOf("/")), "");
        }
            
        //
        File urlRootFolderFilePath;
        File urlSecondFolderFilePath;
        if (urlRootFolder != null && urlRootFolder.length() != 0) {
            urlRootFolderFilePath = new File(servletContextFilePath + "/" + DigestUtils.md5Hex(urlRootFolder));
            if (! urlRootFolderFilePath.exists()) {
                urlRootFolderFilePath.mkdir();
            }
            destFolder = urlRootFolderFilePath.getPath();
            if (urlSecondFolder != null && urlSecondFolder.length() != 0) {
                urlSecondFolderFilePath = new File(urlRootFolderFilePath.getPath() + "/" + DigestUtils.md5Hex(urlSecondFolder));
                if (! urlSecondFolderFilePath.exists()) {
                    urlSecondFolderFilePath.mkdir();
                }
                destFolder = urlSecondFolderFilePath.getPath();
            }
        }
        
        LOGO_URL_VALIDATION = false;
        LOGO_UPLOADED_VALIDATION = false;
        
    }
    
  //initial
    public void init() throws ServletException {
        servletContextFilePath = new File(getServletContext().getRealPath("QRCode"));
        if (!servletContextFilePath.exists()) {
            servletContextFilePath.mkdir();
        }
        defaultWidth = Integer.parseInt(this.getInitParameter("encodeWidth"));
        defaultHeight = Integer.parseInt(this.getInitParameter("encodeHeight"));
    }
    
    private File createFile(String fileName, String filePath, String outImageFormat) throws IOException {
        File file = new File(filePath, fileName + "." + outImageFormat);
        if (file.exists()) {
            file.delete();
            file.createNewFile();
        } else {
            file.createNewFile();
        }
        return file;
    }
    
}
