package com.snapit.interfaceadaptors.controller;

import com.snapit.application.interfaces.BucketService;
import com.snapit.application.interfaces.FramesExtractionEventSender;
import com.snapit.application.interfaces.FramesExtractor;
import com.snapit.framework.javacv.FramesExtractorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

class ProcessControllerTest {

    private ProcessController processController;

    private FramesExtractor framesExtractor;

    @Mock
    private BucketService bucketService;

    @Mock
    private FramesExtractionEventSender eventSender;

    AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        processController = new ProcessController();
        framesExtractor = new FramesExtractorService();
    }

    @Test
    void shouldProcessVideoToFrames() throws FileNotFoundException {
        InputStream video = new FileInputStream("src/test/resources/video/dummy.mp4");

        processController.processVideoToFrames(framesExtractor, bucketService, eventSender,
                UUID.randomUUID().toString(), video, "email@test.com", "dummy", 5);

        verify(bucketService).sendToBucket(any(String.class), any(String.class), any(String.class));
        verify(eventSender).sendFinishedEvent(any(String.class), any(String.class));
    }
}
