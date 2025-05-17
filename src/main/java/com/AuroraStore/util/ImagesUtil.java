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
     * Uploads the product image file to server
     */
    public boolean uploadProductImage(Part part, String rootPath) {
        String savePath = getProductSavePath();
        File fileSaveDir = new File(savePath);
        
        System.out.println("Product image save path: " + savePath);

        if (!fileSaveDir.exists()) {
            if (!fileSaveDir.mkdirs()) {
                System.err.println("Failed to create directory: " + savePath);
                return false;
            }
        }

        try {
            String imageName = getImageNameFromPart(part);
            String filePath = savePath + File.separator + imageName;
            System.out.println("Writing file to: " + filePath);
            
            part.write(filePath);
            
            // Verify file was written
            File uploadedFile = new File(filePath);
            if (uploadedFile.exists()) {
                System.out.println("File successfully written, size: " + uploadedFile.length());
                return true;
            } else {
                System.err.println("File not found after write operation");
                return false;
            }
        } catch (IOException e) {
            System.err.println("IO Error uploading image: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Gets the save path for uploaded user images
     */
    public String getSavePath() {
        return "C:/Users/ghimi/OneDrive/Documents/DSA/AuroraStore/src/main/webapp/resources/images/users/";
    }
    
    /**
     * Gets the save path for uploaded product images and ensures directory exists
     */
    public String getProductSavePath() {
        String path = "C:/Users/ghimi/OneDrive/Documents/DSA/AuroraStore/src/main/webapp/resources/images/products/";
        
        // Ensure directory exists
        File dir = new File(path);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (created) {
                System.out.println("Created product images directory: " + path);
            } else {
                System.err.println("Failed to create product images directory: " + path);
            }
        }
        
        return path;
    }
}
