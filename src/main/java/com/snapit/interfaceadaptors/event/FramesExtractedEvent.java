package com.snapit.interfaceadaptors.event;

import lombok.AllArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
public class FramesExtractedEvent implements Serializable {

    private String id;

    private String filename;

}
