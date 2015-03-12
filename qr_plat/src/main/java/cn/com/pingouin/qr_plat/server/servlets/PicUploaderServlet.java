package cn.com.pingouin.qr_plat.server.servlets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import cn.com.pingouin.qr_plat.server.helpers.ServletConstantsHelper;

public class PicUploaderServlet extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = -7129417361849887564L;
    
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        File uploadLogoFolder;
        File dateFolder;
        File tempFile;
        File uploadFile;
        
        String explicitFilePath = "";
        String uploadLogoFolderPath = getServletContext().getRealPath("UploadLogo");
        String dateFolderPath = DateFormat.getDateInstance(DateFormat.MEDIUM).format(new Date());
        String tempFilePath = "Logo_Temp";
        String uploadFilePath = "Logo_Upload";
        
        uploadLogoFolder = new File(uploadLogoFolderPath);
        if (!uploadLogoFolder.exists()) {
            uploadLogoFolder.mkdir();
        }
        dateFolder = new File(uploadLogoFolderPath + "/" + dateFolderPath);
        if (!dateFolder.exists()) {
            dateFolder.mkdir();
        }
        tempFile = new File(uploadLogoFolderPath + "/" + dateFolderPath + "/" + tempFilePath);
        if (!tempFile.exists()) {
            tempFile.mkdir();
        }
        uploadFile = new File(uploadLogoFolderPath + "/" + dateFolderPath + "/" + uploadFilePath);
        if (!uploadFile.exists()) {
            uploadFile.mkdir();
        }

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (isMultipart == true) {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            // 设置最多只允许在内存中存储的数据,单位:字节
            factory.setSizeThreshold(1024 * 10);
            factory.setRepository(tempFile);
            ServletFileUpload upload = new ServletFileUpload(factory);
            // 设置允许用户上传文件大小,单位:字节
            upload.setFileSizeMax(2 * 1024 * 1024);

            // 开始读取上传信息
            List<FileItem> fileItemList = null;
            try {
                fileItemList = upload.parseRequest(request);
//                System.out.println("---fileItemList---" + fileItemList);
            } catch (FileUploadException e) {
                e.printStackTrace();
            }

            Iterator<FileItem> iterator = fileItemList.iterator();
            while (iterator.hasNext()) {
                FileItem fileItem = (FileItem) iterator.next();
                if (!fileItem.isFormField()) {
                    String itemName = fileItem.getName();

                    long itemSize = fileItem.getSize();
                    if ((itemName == null || itemName.equals("")) && itemSize == 0)
                        continue;
                    int dot = itemName.indexOf(".");
                    itemName = DateFormat.getDateInstance(DateFormat.MEDIUM).format(new Date())
                            + itemName.substring(dot, itemName.length());
                    explicitFilePath = uploadLogoFolderPath + "/" + dateFolderPath + "/" + uploadFilePath + "/" + itemName;
                    
                    File serverFile = new File(uploadFile, itemName);
                    try {
                        fileItem.write(serverFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            System.out.println("enctype must be multipart");
        }

        request.getSession().setAttribute(ServletConstantsHelper.ATTRIBUTE_UPLOADFILEPATH, explicitFilePath);
//        request.getRequestDispatcher("/generateQRCode").forward(request, response);
        
        PrintWriter out = response.getWriter();
        String responseString = "Upload Sucessfully";
        out.println(responseString);
        out.flush();
        out.close();
    }

    public void init() throws ServletException {
        
    }
}

