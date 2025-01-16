package com.snapit.application.usecase;

import com.snapit.application.interfaces.BucketService;
import com.snapit.application.interfaces.FramesExtractor;
import com.snapit.application.interfaces.FramesExtractionEventSender;

import java.io.InputStream;

import static com.snapit.application.utils.FileUtils.createZip;
import static com.snapit.application.utils.FileUtils.deleteFiles;

public class ExtractFramesUseCase {

    private final FramesExtractor framesExtractor;

    private final BucketService bucketService;

    private final FramesExtractionEventSender eventSender;

    public ExtractFramesUseCase(FramesExtractor framesExtractor, BucketService bucketService, FramesExtractionEventSender eventSender) {
        this.framesExtractor = framesExtractor;
        this.bucketService = bucketService;
        this.eventSender = eventSender;
    }

    public void processVideoToFrames (InputStream video, String userEmail, String filename, int frameInterval) {
        String outputDir = userEmail + "-" + filename;
        try {
            framesExtractor.extractFrames(video, outputDir, frameInterval);
            createZip(outputDir, filename);

            String bucketTarget = userEmail.concat("/").concat(filename).concat(".zip");
            bucketService.sendToBucket(outputDir, filename, bucketTarget);

            eventSender.sendFinishedEvent(filename, bucketTarget, userEmail);
        } catch (Exception e) {
            eventSender.sendFailedEvent(filename, userEmail);
        } finally {
            deleteFiles(outputDir);
        }
    }

}
