package com.simisinc.platform.rest.services.carfix;

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

    public static String writeDataToFile(String base64PictureData) throws Exception {
        String fileName = UUID.randomUUID().toString()+".txt";
        String serverRootPath = FileSystemCommand.getFileServerRootPath();
        String serverCompletePath = serverRootPath + "/carfixPictureData/";

        File pictureFile = new File(serverCompletePath + fileName);
        FileOutputStream fos = new FileOutputStream(pictureFile);
        fos.write(base64PictureData.getBytes(StandardCharsets.UTF_8));
        fos.close();

        return fileName;
    }


    public static String readDataFromFile(String fileName) throws Exception {
        if(fileName == null) {
            return null;
        }
        String serverRootPath = FileSystemCommand.getFileServerRootPath();
        String serverCompletePath = serverRootPath + "/carfixPictureData/";
        File pictureFile = new File(serverCompletePath + fileName);
        FileInputStream fis = new FileInputStream(pictureFile);
        String fileData = new String(fis.readAllBytes());
        fis.close();
        return fileData;
    }

    public static void uploadImageFile(HttpServletRequest request) throws Exception {

        if (request.getPart("serviceRequestImage") != null && request.getHeader("Content-Type").contains("multipart/form-data")) {
            InputStream is = request.getPart("serviceRequestImage").getInputStream();
            BufferedImage uploadImage = ImageIO.read(new ByteArrayInputStream(is.readAllBytes()));
            //ImageIO.write(uploadImage, "jpg", new File("/home/webapps/connect/ROOT/images/carfixImages","snap.jpg"));
            ImageIO.write(uploadImage, "jpg", new File("/home/julius","snap.jpg"));
        }
    }
}
