package com.snapit.interfaceadaptors.controller;

import com.snapit.application.interfaces.BucketService;
import com.snapit.application.interfaces.FramesExtractor;
import com.snapit.application.interfaces.FramesExtractionEventSender;
import com.snapit.application.usecase.ExtractFramesUseCase;

import java.io.InputStream;

public class ProcessController {

    public void processVideoToFrames(FramesExtractor framesExtractor, BucketService bucketService, FramesExtractionEventSender sender,
                                     InputStream video, String userEmail, String filename, int frameInterval) {
        ExtractFramesUseCase useCase = new ExtractFramesUseCase(framesExtractor, bucketService, sender);
        useCase.processVideoToFrames(video, userEmail, filename, frameInterval);
    }

}
