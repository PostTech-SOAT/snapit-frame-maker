package com.snapit.framework.javacv;

import com.snapit.application.interfaces.FramesExtractor;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;

import static java.lang.String.format;

public class FramesExtractorService implements FramesExtractor {

    @Override
    public void extractFrames(InputStream videoInputStream, String outputDir, int intervalSeconds) throws Exception {
        new File(outputDir).mkdirs();

        FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(videoInputStream);
        frameGrabber.start();

        Java2DFrameConverter converter = new Java2DFrameConverter();
        int frameRate = (int) frameGrabber.getFrameRate();
        int frameInterval = frameRate * intervalSeconds;

        int frameNumber = 0;
        Frame frame;
        while ((frame = frameGrabber.grabImage()) != null) {
            if (frameNumber % frameInterval == 0) {
                BufferedImage bufferedImage = converter.convert(frame);
                File outputFile = new File(outputDir, format("frame_%d.jpg", frameNumber));
                ImageIO.write(bufferedImage, "jpg", outputFile);
            }
            frameNumber++;
        }

        frameGrabber.stop();
    }

}
