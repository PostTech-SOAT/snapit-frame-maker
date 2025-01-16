package com.snapit.framework.camel;

import com.snapit.framework.aws.S3Service;
import com.snapit.framework.javacv.FramesExtractorService;
import com.snapit.framework.rabbitmq.FramesExtractionSenderService;
import com.snapit.interfaceadaptors.controller.ProcessController;
import lombok.AllArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Map;

import static java.lang.Integer.parseInt;

@Component
@AllArgsConstructor
public class CamelRoute extends RouteBuilder {

    private S3Service s3Service;

    private FramesExtractionSenderService senderService;

    @Override
    public void configure() {

        from("aws2-s3://snapit-uploads?configuration=#originBucket")
                .process(exchange -> {
                    Map<String, String> metadata =
                            (Map<String, String>) exchange.getMessage().getHeader("CamelAwsS3Metadata");
                    String userEmail = metadata.get("email");
                    int frameInterval = parseInt(metadata.get("frameinterval"));
                    InputStream video = exchange.getMessage().getBody(InputStream.class);
                    String filename = exchange.getProperty("CamelAwsS3Key").toString()
                            .replaceAll("(?<!^)[.][^.]*$", "");
                    ProcessController controller = new ProcessController();
                    controller.processVideoToFrames(new FramesExtractorService(), s3Service, senderService, video,
                            userEmail, filename, frameInterval);
                });
    }
}
