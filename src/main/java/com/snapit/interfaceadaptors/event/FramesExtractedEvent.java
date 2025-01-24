package com.snapit.interfaceadaptors.event;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class FramesExtractedEvent implements Serializable {

    String id;

    private String filename;

}
