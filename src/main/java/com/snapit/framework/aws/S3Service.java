package com.snapit.framework.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.snapit.application.interfaces.BucketService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@AllArgsConstructor
public class S3Service implements BucketService {

    private AmazonS3 s3Client;

    @Override
    public void sendToBucket(String outputDir, String filename, String s3Key) {
        String zipFilename = outputDir + File.separator + filename + ".zip";
        s3Client.putObject("snapit-frames", s3Key, new File(zipFilename));
    }

}
