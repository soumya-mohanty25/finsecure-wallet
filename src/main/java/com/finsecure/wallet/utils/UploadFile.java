package com.finsecure.wallet.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ResourceBundle;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.jboss.logging.Logger;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
public class UploadFile {
    static ResourceBundle rb = ResourceBundle.getBundle("application");
    private static final String UPLOAD_DIRECTORY;

    public static String getPathToStoreDocument(String purpose, String id, String module) {
        String rootpath = UPLOAD_DIRECTORY;
        File rootDir = new File(rootpath);
        if (!rootDir.exists()) {
            rootDir.mkdir();
        }

        String uploadPath = rootpath + File.separator + module + "_" + purpose + "_" + id;
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        return uploadPath;
    }

    public static String upload(MultipartFile multipartFile, String purpose, String id, String module) throws IOException {
        BufferedOutputStream stream = null;
        boolean bool = false;
        String uploadPath = getPathToStoreDocument(purpose, id, module);
        String filePath = uploadPath + File.separator + multipartFile.getOriginalFilename();
        File storeFile = new File(filePath);
        bool = storeFile.exists();
        if (bool) {
            return filePath;
        } else {
            byte[] bytes = multipartFile.getBytes();
            stream = new BufferedOutputStream(new FileOutputStream(storeFile));
            stream.write(bytes);
            stream.close();
            String docname = multipartFile.getOriginalFilename();
            if (docname != null) {
                filePath = addCurrenDateTimeToDocAndRenameIt(docname, uploadPath, filePath);
            }

            return filePath;
        }
    }

    public static String uploadVideo(MultipartFile multipartFile, String purpose, String id, String module) throws IOException {
        BufferedOutputStream stream = null;
        boolean bool = false;
        String uploadPath = getPathToStoreDocument(purpose, id, module);
        String filePath = uploadPath + File.separator + multipartFile.getOriginalFilename();
        File storeFile = new File(filePath);
        bool = storeFile.exists();
        if (bool) {
            return null;
        } else {
            byte[] bytes = multipartFile.getBytes();
            stream = new BufferedOutputStream(new FileOutputStream(storeFile));
            stream.write(bytes);
            stream.close();
            String docname = multipartFile.getOriginalFilename();
            if (docname != null) {
                filePath = addCurrenDateTimeToDocAndRenameIt(docname, uploadPath, filePath);
            }

            return filePath;
        }
    }

    public static String addCurrenDateTimeToDocAndRenameIt(String docname, String uploadPath, String filePath) throws IOException {
        String extension = FilenameUtils.getExtension(docname);
        String newFileName = UUID.randomUUID().toString();
        if (extension.equals("")) {
            extension = "file";
        }

        String filePathNew = uploadPath + File.separator + newFileName + "." + extension;
        filePath = renameFile(filePath, filePathNew);
        return filePath;
    }

    public static String newChangeFileName(String docname) throws IOException {
        String extension = FilenameUtils.getExtension(docname);
        String newFileName = UUID.randomUUID().toString();
        if (extension.equals("")) {
            extension = "file";
        }

        return newFileName + "." + extension;
    }

    public static String renameFile(String filePath, String filePathNew) throws IOException {
        File srcFile = new File(filePath);
        boolean bSucceeded = false;

        try {
            File destFile = new File(filePathNew);
            if (destFile.exists() && !destFile.delete()) {
                throw new IOException(filePath + " could not be renamed to " + filePathNew);
            }

            if (!srcFile.renameTo(destFile)) {
                throw new IOException(filePath + " could not be renamed to " + filePathNew);
            }

            bSucceeded = true;
            if (bSucceeded) {
                log.debug(filePath + " is successfully renamed to " + filePathNew);
                String var5 = filePathNew;
                return var5;
            }
        } finally {
            if (bSucceeded) {
            }

        }

        return null;
    }

    public static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];

        int read;
        while((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }

    }

    static {
        UPLOAD_DIRECTORY = rb.getString("UPLOAD.FILE.PATH");
    }
}