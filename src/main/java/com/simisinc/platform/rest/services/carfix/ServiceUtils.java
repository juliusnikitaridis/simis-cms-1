package com.simisinc.platform.rest.services.carfix;

import com.simisinc.platform.application.admin.LoadSitePropertyCommand;
import com.simisinc.platform.application.filesystem.FileSystemCommand;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
 * Julius Nikitaridis
 * 24/05/2023.
 * Utils for services. Images can not be saved directly in the DB. write the
 * base64 encoded data to the file system and store the file name in the DB field picture_data
 */

public class ServiceUtils {

//    public static String writeDataToFile(String base64PictureData) throws Exception {
//        String fileName = UUID.randomUUID().toString()+".txt";
//        String serverRootPath = FileSystemCommand.getFileServerRootPath();
//        String serverCompletePath = serverRootPath + "/carfixPictureData/";
//
//        File pictureFile = new File(serverCompletePath + fileName);
//        FileOutputStream fos = new FileOutputStream(pictureFile);
//        fos.write(base64PictureData.getBytes(StandardCharsets.UTF_8));
//        fos.close();
//
//        return fileName;
//    }


//    public static String readDataFromFile(String fileName) throws Exception {
//        if(fileName == null) {
//            return null;
//        }
//        String serverRootPath = FileSystemCommand.getFileServerRootPath();
//        String serverCompletePath = serverRootPath + "/carfixPictureData/";
//        File pictureFile = new File(serverCompletePath + fileName);
//        FileInputStream fis = new FileInputStream(pictureFile);
//        String fileData = new String(fis.readAllBytes());
//        fis.close();
//        return fileData;
//    }

    public static String uploadImageFile(HttpServletRequest request) throws Exception {

        if  (request.getHeader("Content-Type").contains("multipart/form-data") && request.getPart("serviceRequestImage") != null) {
            InputStream is = request.getPart("serviceRequestImage").getInputStream();
            BufferedImage uploadImage = ImageIO.read(new ByteArrayInputStream(is.readAllBytes()));
            String imageFileName = UUID.randomUUID()+".jpg";
            String imageDirPath = LoadSitePropertyCommand.loadByName("site.carfix.images.base.dir");
            String siteUrl = LoadSitePropertyCommand.loadByName("site.registration.confirm.link");
            if(siteUrl == null || imageDirPath == null) {
                throw new Exception("[site.registration.confirm.link] and [site.carfix.images.base.dir] has not been set in properties table");
            }
            ImageIO.write(uploadImage, "jpg", new File(imageDirPath,imageFileName));
            //images are rendered through the following path : https://carfix.connectmobiles24.com/images/apple-touch-icon.png
            return siteUrl+"images/carfixImages/"+imageFileName; //todo configure the /images/carfixImages prefix
        } else {
            return null; //means that this SR is created without images.
        }
    }
}
