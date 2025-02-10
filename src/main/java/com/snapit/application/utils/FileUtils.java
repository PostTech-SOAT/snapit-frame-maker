package com.snapit.application.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtils {

    private static final Logger LOGGER = Logger.getLogger(FileUtils.class.getName());

    private FileUtils() {
    }

    public static void createZip(String outputDir, String originalFilename) throws IOException {
        String zipFilename = outputDir + File.separator + originalFilename + ".zip";
        File dir = new File(outputDir);
        File[] files = dir.listFiles((d, name) -> name.endsWith(".jpg"));

        if (files != null) {
            try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFilename))) {
                for (File file : files) {
                    try (FileInputStream fis = new FileInputStream(file)) {
                        ZipEntry zipEntry = new ZipEntry(file.getName());
                        zos.putNextEntry(zipEntry);

                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = fis.read(buffer)) > 0) {
                            zos.write(buffer, 0, length);
                        }
                        zos.closeEntry();
                    }
                    deleteFile(file);
                }
            }
        }
    }

    public static void deleteFiles(String dirToDelete) {
        File file = new File(dirToDelete);
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteFile(f);
            }
        }
        deleteFile(file);
    }

    private static void deleteFile(File file) {
        if (!file.delete()) {
            LOGGER.severe("Failed to delete file: " + file.getAbsolutePath());
        }
    }

}
