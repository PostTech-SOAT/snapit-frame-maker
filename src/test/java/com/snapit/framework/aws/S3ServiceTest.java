package com.snapit.framework.aws;

import com.amazonaws.services.s3.AmazonS3;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;

import static org.mockito.Mockito.*;

class S3ServiceTest {

    private S3Service s3Service;

    @Mock
    private AmazonS3 s3Client;

    AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        s3Service = new S3Service(s3Client);
    }

    @Test
    void shouldSendToBucket() {

        s3Service.sendToBucket("outputDir", "filename", "s3Key");


        verify(s3Client, times(1)).putObject("snapit-frames", "s3Key", new File("outputDir/filename.zip"));
        verifyNoMoreInteractions(s3Client);

    }

}
