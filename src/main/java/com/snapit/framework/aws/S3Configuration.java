package com.snapit.framework.aws;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.RequiredArgsConstructor;
import org.apache.camel.CamelContext;
import org.apache.camel.component.aws2.s3.AWS2S3Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.services.s3.S3Client;

import static software.amazon.awssdk.regions.Region.US_EAST_1;

@Configuration
@RequiredArgsConstructor
public class S3Configuration {

    @Value("${camel.component.aws2-s3.accessKey}")
    private String accessKey;

    @Value("${camel.component.aws2-s3.secretKey}")
    private String secretKey;

    @Value("${camel.component.aws2-s3.session-token}")
    private String sessionToken;

    @Value("${camel.component.aws2-s3.region}")
    private String region;

    @Bean("originBucket")
    public AWS2S3Configuration getOriginBucket() {
        AWS2S3Configuration configuration = new AWS2S3Configuration();
        configuration.setAccessKey(accessKey);
        configuration.setSecretKey(secretKey);
        configuration.setSessionToken(sessionToken);
        configuration.setRegion(region);
        configuration.setBucketName("snapit-uploads");
        return configuration;
    }

    @Bean("S3Client")
    public S3Client s3Client(CamelContext camelContext){
        ProfileCredentialsProvider credentialsProvider = ProfileCredentialsProvider.builder()
                .build();
        S3Client s3Client = S3Client.builder()
                .region(US_EAST_1)
                .credentialsProvider(credentialsProvider)
                .build();
        camelContext.getRegistry().bind("s3Client", s3Client);
        return s3Client;
    }

    @Bean
    public AmazonS3 getAmazonS3Client() {
        AWSCredentials credentials = new BasicSessionCredentials(accessKey, secretKey, sessionToken);
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.US_EAST_1)
                .build();
    }



}
