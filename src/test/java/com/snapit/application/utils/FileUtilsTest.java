package com.snapit.application.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipFile;

import static org.junit.jupiter.api.Assertions.*;

class FileUtilsTest {

    private static final String TEST_DIR = "testDir";
    private static final String TEST_FILE = "testFile.jpg";
    private static final String ZIP_FILE = "testFile.zip";

    @BeforeEach
    void setUp() throws IOException {
        Files.createDirectory(Paths.get(TEST_DIR));
        try (FileWriter writer = new FileWriter(TEST_DIR + File.separator + TEST_FILE)) {
            writer.write("This is a test file.");
        }
    }

    @AfterEach
    void tearDown() {
        FileUtils.deleteFiles(TEST_DIR);
    }

    @Test
    void testCreateZip() throws IOException {
        FileUtils.createZip(TEST_DIR, "testFile");
        File zipFile = new File(TEST_DIR + File.separator + ZIP_FILE);

        assertTrue(zipFile.exists(), "Zip file should be created");

        try (ZipFile zip = new ZipFile(zipFile)) {
            assertNotNull(zip.getEntry(TEST_FILE), "Zip file should contain the test file");
        }
    }

    @Test
    void testCreateZipWithNullFile() throws IOException {
        FileUtils.createZip("nullDir", "testFile");
        File folder = new File("nullDir");

        assertFalse(folder.exists(), "Null directory should not be created");
    }

    @Test
    void testCreateZipWithZeroFileLength() throws IOException {
        String emptyDir = "emptyDir";
        File folder = new File(emptyDir);
        FileUtils.createZip(emptyDir, "testFile");

        assertFalse(folder.exists(), "Empty directory should be created");
        assertTrue(folder.length() < 1, "Empty directory should be empty");
    }

    @Test
    void testDeleteFiles() {
        FileUtils.deleteFiles(TEST_DIR);
        File dir = new File(TEST_DIR);

        assertFalse(dir.exists(), "Directory should be deleted");
    }
}
