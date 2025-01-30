package com.snapit.framework.rabbitmq;

import com.snapit.interfaceadaptors.event.FramesExtractedEvent;
import com.snapit.interfaceadaptors.event.FramesExtractionFailedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.UUID;

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

        verify(rabbitTemplate).convertAndSend("frames-extraction-finished-exchange", "frames",
                new FramesExtractedEvent(id, "filename"));
    }

    @Test
    void shouldSendFailedEvent() {
        String id = UUID.randomUUID().toString();
        senderService.sendFailedEvent(id);

        verify(rabbitTemplate).convertAndSend("frames-extraction-failed-exchange", "frames",
                new FramesExtractionFailedEvent(id));
    }

}
