package com.snapit.framework.rabbitmq;

import com.snapit.application.interfaces.FramesExtractionEventSender;
import com.snapit.interfaceadaptors.event.FramesExtractedEvent;
import com.snapit.interfaceadaptors.event.FramesExtractionFailedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FramesExtractionSenderService implements FramesExtractionEventSender {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void sendFinishedEvent(String id, String filename) {
        rabbitTemplate.convertAndSend("frames-extraction-finished-exchange", "frames",
                new FramesExtractedEvent(id, filename));
    }

    @Override
    public void sendFailedEvent(String id) {
        rabbitTemplate.convertAndSend("frames-extraction-failed-exchange", "frames",
                new FramesExtractionFailedEvent(id));
    }

}
