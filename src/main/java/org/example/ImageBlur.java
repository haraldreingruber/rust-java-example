package org.example;

import org.apache.commons.imaging.*;
import org.apache.commons.imaging.formats.png.PngImageParser;
import org.apache.commons.imaging.formats.png.PngImagingParameters;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

public class ImageBlur {

    public static void blurImage() {
        var filenameWithoutExtension = "./baboon";
        var image = readImage(filenameWithoutExtension+".png");
        // var pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        writeImage(filenameWithoutExtension+"-blurred.png", image);
    }

    private static void writeImage(String path, BufferedImage image) {
        try {
            Imaging.writeImage(image, new File(path), ImageFormats.PNG);
        } catch (ImageWriteException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private static BufferedImage readImage(String path) {
        try {
            return new PngImageParser().getBufferedImage(new File(path), new PngImagingParameters());
        } catch (ImageReadException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String... args) {
        blurImage();
    }
}
