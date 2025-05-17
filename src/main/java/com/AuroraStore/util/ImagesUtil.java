package com.AuroraStore.util;

import java.io.File;
import java.io.IOException;
import jakarta.servlet.http.Part;

public class ImagesUtil {
    
    /**
     * Extracts the file name from the uploaded file Part
     */
    public String getImageNameFromPart(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        String imageName = null;

        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                imageName = s.substring(s.indexOf("=") + 2, s.length() - 1);
            }
        }

        if (imageName == null || imageName.isEmpty()) {
            imageName = "default.png";
        }

        return imageName;
    }

    /**
     * Uploads the image file to server
     */
    public boolean uploadImage(Part part, String rootPath) {
        String savePath = getSavePath();
        File fileSaveDir = new File(savePath);

        if (!fileSaveDir.exists()) {
            if (!fileSaveDir.mkdirs()) {
                return false;
            }
        }

        try {
            String imageName = getImageNameFromPart(part);
            String filePath = savePath + File.separator + imageName;
            part.write(filePath);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Gets the save path for uploaded images
     */
    public String getSavePath() {
        return "C:/Users/ghimi/OneDrive/Documents/DSA/AuroraStore/src/main/webapp/resources/images/users/";
    }
}
