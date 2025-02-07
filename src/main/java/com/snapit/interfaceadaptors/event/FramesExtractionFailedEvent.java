package com.snapit.interfaceadaptors.event;

import lombok.AllArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
public class FramesExtractionFailedEvent implements Serializable {

    private String id;

}
