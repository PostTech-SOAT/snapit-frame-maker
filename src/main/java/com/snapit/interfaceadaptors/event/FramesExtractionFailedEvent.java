package com.snapit.interfaceadaptors.event;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class FramesExtractionFailedEvent implements Serializable {

    private String filename;

    private String userEmail;

}
