package com.snapit.framework.rabbitmq;

import com.snapit.interfaceadaptors.event.FramesExtractedEvent;
import com.snapit.interfaceadaptors.event.FramesExtractionFailedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

class FramesExtractionSenderServiceTest {

    private FramesExtractionSenderService senderService;

    @Mock
    private RabbitTemplate rabbitTemplate;

    AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        senderService = new FramesExtractionSenderService(rabbitTemplate);
    }

    @Test
    void shouldSendFinishedEvent() {
        String id = UUID.randomUUID().toString();
        senderService.sendFinishedEvent(id, "filename");

        verify(rabbitTemplate).convertAndSend(anyString(), anyString(), any(FramesExtractedEvent.class));
    }

    @Test
    void shouldSendFailedEvent() {
        String id = UUID.randomUUID().toString();
        senderService.sendFailedEvent(id, "test.mp4", "email@test.com");

        verify(rabbitTemplate).convertAndSend(anyString(), anyString(), any(FramesExtractionFailedEvent.class));
    }

}
