package com.snapit.application.interfaces;

public interface FramesExtractionEventSender {

    void sendFinishedEvent(String filename, String bucketPath, String userEmail);

    void sendFailedEvent(String filename, String userEmail);
}
