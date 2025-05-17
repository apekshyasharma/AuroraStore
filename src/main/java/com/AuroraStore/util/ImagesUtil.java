package com.AuroraStore.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths; // Import Paths
import jakarta.servlet.http.Part;

public class ImagesUtil {
    
    /**
     * Extracts the file name from the uploaded file Part.
     * Uses Part.getSubmittedFileName() and sanitizes it.
     */
    public String getImageNameFromPart(Part part) {
        if (part == null) {
            System.err.println("ImagesUtil.getImageNameFromPart: Part is null.");
            return null; 
        }
        String submittedFileName = part.getSubmittedFileName();
        if (submittedFileName != null && !submittedFileName.trim().isEmpty()) {
            // Sanitize the filename to prevent directory traversal issues
            // and ensure it's just the base name.
            // Using Paths.get().getFileName() is a good way to get the actual file name
            String sanitizedName = Paths.get(submittedFileName).getFileName().toString();
            System.out.println("ImagesUtil.getImageNameFromPart: Extracted filename: " + sanitizedName);
            return sanitizedName;
        }
        
        // Fallback for older servlet containers or specific configurations if needed,
        // but getSubmittedFileName should be preferred.
        String contentDisp = part.getHeader("content-disposition");
        if (contentDisp != null) {
            String[] items = contentDisp.split(";");
            for (String s : items) {
                if (s.trim().startsWith("filename")) {
                    // Extract from "filename="foo.bar""
                    String nameFromHeader = s.substring(s.indexOf('=') + 1).trim().replace("\"", "");
                    String sanitizedName = Paths.get(nameFromHeader).getFileName().toString();
                    System.out.println("ImagesUtil.getImageNameFromPart: Extracted filename from header: " + sanitizedName);
                    return sanitizedName;
                }
            }
        }
        System.err.println("ImagesUtil.getImageNameFromPart: Could not determine filename from part name: " + part.getName());
        return null; // Return null if no filename could be reliably extracted
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
    public boolean uploadProductImage(Part part, String rootPath) { // rootPath is not used here due to hardcoded getProductSavePath
        if (part == null || part.getSize() == 0) {
            System.out.println("ImagesUtil.uploadProductImage: No image to upload - part is null or empty.");
            return false; // It's not an error, but nothing was uploaded.
        }
        
        String imageName = getImageNameFromPart(part);
        if (imageName == null || imageName.trim().isEmpty()) {
            System.err.println("ImagesUtil.uploadProductImage: Failed to get a valid image name from part. Upload aborted.");
            return false;
        }
        
        String savePath = getProductSavePath(); // This is your hardcoded path
        File fileSaveDir = new File(savePath);
        
        System.out.println("ImagesUtil.uploadProductImage: Attempting to save to directory: " + savePath);

        if (!fileSaveDir.exists()) {
            System.out.println("ImagesUtil.uploadProductImage: Directory does not exist, attempting to create: " + savePath);
            boolean dirCreated = fileSaveDir.mkdirs();
            if (dirCreated) {
                System.out.println("ImagesUtil.uploadProductImage: Directory created successfully: " + savePath);
            } else {
                System.err.println("ImagesUtil.uploadProductImage: Failed to create directory: " + savePath + ". Check permissions and path validity.");
                // Attempt to create parent directories if they don't exist
                File parentDir = fileSaveDir.getParentFile();
                if (parentDir != null && !parentDir.exists()) {
                    if (parentDir.mkdirs()) {
                        System.out.println("ImagesUtil.uploadProductImage: Parent directory created. Retrying to create target directory.");
                        if (!fileSaveDir.mkdirs()) {
                             System.err.println("ImagesUtil.uploadProductImage: Still failed to create target directory after creating parent.");
                             return false;
                        }
                    } else {
                        System.err.println("ImagesUtil.uploadProductImage: Failed to create parent directory.");
                        return false;
                    }
                } else if (parentDir == null) {
                     System.err.println("ImagesUtil.uploadProductImage: Parent directory is null, cannot create path.");
                     return false;
                } else {
                    // Parent exists but mkdirs failed for the target directory itself.
                    System.err.println("ImagesUtil.uploadProductImage: Directory creation failed even if parent might exist.");
                    return false;
                }
            }
        }

        try {
            String filePath = savePath + File.separator + imageName;
            System.out.println("ImagesUtil.uploadProductImage: Writing file to: " + filePath);
            
            part.write(filePath); // This is the actual write operation
            
            File uploadedFile = new File(filePath);
            if (uploadedFile.exists() && uploadedFile.length() > 0) {
                System.out.println("ImagesUtil.uploadProductImage: File successfully written to " + filePath + ", size: " + uploadedFile.length());
                return true;
            } else if (uploadedFile.exists()) {
                System.err.println("ImagesUtil.uploadProductImage: File exists at " + filePath + " but is empty (size 0).");
                return false;
            }
            else {
                System.err.println("ImagesUtil.uploadProductImage: File not found at " + filePath + " after write operation. Upload likely failed.");
                return false;
            }
        } catch (IOException e) {
            System.err.println("ImagesUtil.uploadProductImage: IO Error uploading image '" + imageName + "' to '" + savePath + "': " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println("ImagesUtil.uploadProductImage: Unexpected error uploading image '" + imageName + "': " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Gets the save path for uploaded user images
     */
    public String getSavePath() {
        // Consider making this configurable or relative to a known data directory
        return "C:/Users/ghimi/OneDrive/Documents/DSA/AuroraStore/src/main/webapp/resources/images/users/";
    }
    
    /**
     * Gets the save path for uploaded product images and ensures directory exists
     */
    public String getProductSavePath() {
        // Consider making this configurable or relative to a known data directory
        String path = "C:/Users/ghimi/OneDrive/Documents/DSA/AuroraStore/src/main/webapp/resources/images/products/";
        
        File dir = new File(path);
        if (!dir.exists()) {
            // This initial check is good, but mkdirs in uploadProductImage is more robust
            if (dir.mkdirs()) {
                System.out.println("ImagesUtil.getProductSavePath: Created product images directory: " + path);
            } else {
                System.err.println("ImagesUtil.getProductSavePath: Failed to create product images directory during getProductSavePath: " + path);
            }
        }
        return path;
    }
}
