package com.snapit.application.usecase;

import com.snapit.application.interfaces.BucketService;
import com.snapit.application.interfaces.FramesExtractionEventSender;
import com.snapit.application.interfaces.FramesExtractor;
import com.snapit.framework.javacv.FramesExtractorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ExtractFramesUseCaseTest {

    private ExtractFramesUseCase useCase;

    @Mock
    private BucketService bucketService;

    @Mock
    private FramesExtractionEventSender eventSender;

    AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        FramesExtractor framesExtractor = new FramesExtractorService();
        useCase = new ExtractFramesUseCase(framesExtractor, bucketService, eventSender);
    }

    @Test
    void shouldProcessVideoToFrames() throws FileNotFoundException {

        InputStream video = new FileInputStream("src/test/resources/video/dummy.mp4");

        doNothing().when(bucketService).sendToBucket(any(String.class), any(String.class), any(String.class));
        doNothing().when(eventSender).sendFinishedEvent(any(String.class), any(String.class));

        useCase.processVideoToFrames(UUID.randomUUID().toString(), video, "email@test.com", "dummy", 5);

        assertFalse(new File("email@test.com-dummy").exists());
        verify(eventSender).sendFinishedEvent(any(String.class), any(String.class));
        verify(eventSender, times(1)).sendFinishedEvent(any(String.class), any(String.class));
        verify(eventSender, times(0)).sendFailedEvent(any(String.class));
    }

    @Test
    void shouldSendFailedEventWhenExceptionIsThrown() throws FileNotFoundException {

        InputStream txt = new FileInputStream("src/test/resources/video/dummy.txt");

        doNothing().when(bucketService).sendToBucket(any(String.class), any(String.class), any(String.class));
        doNothing().when(eventSender).sendFailedEvent(any(String.class));

        useCase.processVideoToFrames
                (UUID.randomUUID().toString(), txt, "email@test.com", "dummy", 5);

        assertFalse(new File("email@test.com-dummy").exists());
        verify(eventSender).sendFailedEvent(any(String.class));
        verify(eventSender, times(0)).sendFinishedEvent(any(String.class), any(String.class));
        verify(eventSender, times(1)).sendFailedEvent(any(String.class));
    }

}
