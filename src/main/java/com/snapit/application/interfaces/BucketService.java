package com.snapit.application.interfaces;

public interface BucketService {

    void sendToBucket(String outputDir, String filename, String s3Key);

}
