package com.AuroraStore.util;

import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;
import jakarta.servlet.http.Part;

public class ValidationUtil {

    // 1. Validate if a field is null or empty
    public static boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    // 2. Validate if a string contains only letters
    public static boolean isAlphabetic(String value) {
        return value != null && value.matches("^[a-zA-Z]+$");
    }

    // 3. Validate if a string starts with a letter and is composed of letters and numbers
    public static boolean isAlphanumericStartingWithLetter(String value) {
        return value != null && value.matches("^[a-zA-Z][a-zA-Z0-9]*$");
    }

    // 4. Validate if a string is "male" or "female" (case insensitive)
    public static boolean isValidGender(String value) {
        return value != null && (value.equalsIgnoreCase("male") || value.equalsIgnoreCase("female"));
    }

    // 5. Validate if a string is a valid email address
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return email != null && Pattern.matches(emailRegex, email);
    }

    // 6. Validate if a number is of 10 digits and starts with 98
    public static boolean isValidPhoneNumber(String number) {
        return number != null && number.matches("^98\\d{8}$");
    }

    // Add new method to check if phone number has exactly 10 digits
    public static boolean isPhoneNumberLength10(String number) {
        return number != null && number.length() == 10;
    }

    // Add new method to check if phone number contains only digits
    public static boolean isNumericOnly(String number) {
        return number != null && number.matches("^[0-9]+$");
    }

    // 7. Validate if a password is composed of at least 1 capital letter, 1 number, and 1 symbol
    public static boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return password != null && password.matches(passwordRegex);
    }

    // Add these new methods to ValidationUtil class
    public static boolean hasMinimumLength(String password) {
        return password != null && password.length() >= 8;
    }

    public static boolean hasSpecialCharacter(String password) {
        return password != null && password.matches(".*[@!#$%&].*");
    }

    public static boolean hasUpperCase(String password) {
        return password != null && password.matches(".*[A-Z].*");
    }

    /**
     * Validates if the uploaded file is an image with allowed extensions
     */
    public static boolean isValidImageExtension(Part part) {
        if (part == null || part.getSize() == 0) {
            return true; // No image is considered valid for optional images
        }
        
        String fileName = part.getSubmittedFileName();
        if (fileName == null || fileName.isEmpty()) {
            return true;
        }
        
        fileName = fileName.toLowerCase();
        return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || 
               fileName.endsWith(".png") || fileName.endsWith(".gif");
    }
    
    // Alternative method for required image uploads
    public static boolean isRequiredValidImageExtension(Part imagePart) {
        if (imagePart == null || imagePart.getSize() == 0 || isNullOrEmpty(imagePart.getSubmittedFileName())) {
            return false; // No file or empty file is considered invalid for required images
        }
        String fileName = imagePart.getSubmittedFileName().toLowerCase();
        return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png") || fileName.endsWith(".gif");
    }

    /**
     * Validates that an image file has an allowed extension and size
     */
    public static boolean isValidImageFile(Part filePart) {
        // Check if part exists and has content
        if (filePart == null || filePart.getSize() == 0) {
            return true; // No file is valid (not required for this method's logic, could be handled elsewhere if required)
        }
        
        // Check file size (max 10MB)
        if (filePart.getSize() > 10 * 1024 * 1024) { // Updated to 10MB
            System.out.println("ValidationUtil: Image size exceeds 10MB. Size: " + filePart.getSize());
            return false;
        }
        
        // Check file extension
        return isValidImageExtension(filePart);
    }

    // 9. Validate if password and retype password match
    public static boolean doPasswordsMatch(String password, String retypePassword) {
        return password != null && password.equals(retypePassword);
    }

    // 10. Validate if the date of birth is at least 16 years before today
    public static boolean isAgeAtLeast16(LocalDate dob) {
        if (dob == null) {
            return false;
        }
        LocalDate today = LocalDate.now();
        return Period.between(dob, today).getYears() >= 16;
    }
}