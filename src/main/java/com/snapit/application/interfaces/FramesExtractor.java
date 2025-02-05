package com.snapit.application.interfaces;

import java.io.IOException;
import java.io.InputStream;

public interface FramesExtractor {

    void extractFrames(InputStream videoInputStream, String outputDir, int intervalSeconds) throws IOException;

}
