package com.snapit.application.interfaces;

public interface FramesExtractionEventSender {

    void sendFinishedEvent(String id, String filename);

    void sendFailedEvent(String id);
}
