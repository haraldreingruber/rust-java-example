package org.example;

import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.formats.png.PngImageParser;
import org.apache.commons.imaging.formats.png.PngImagingParameters;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class ImageBlur {

    public static void blurImage() {
        var filenameWithoutExtension = "./baboon";
        var image = readImage(filenameWithoutExtension + ".png");
        DataBuffer dataBuffer = image.getRaster().getDataBuffer();
        int[] pixelsInt = ((DataBufferInt) dataBuffer).getData();
        var byteBuffer = ByteBuffer.allocate(pixelsInt.length*4);
        byteBuffer.asIntBuffer().put(pixelsInt);
        byte[] pixels = byteBuffer.array();

        processGrayscaleFilter(pixels);
        //processBlurFilter(pixels);

        byteBuffer.asIntBuffer().get(pixelsInt);
        writeImage(filenameWithoutExtension + "-blurred.png", image);
    }

    private static void processBlurFilter(int[] pixels) {
        for (int index = 0; index < pixels.length; index++) {
            var pixel = new short[]{0, 0, 0, 0};
            //pixels[index] = ARGB2Int(pixel);
        }
    }

    private static void processGrayscaleFilter(byte[] pixels) {
        for (int index = 0; index < pixels.length; index+=4) {
            var grayColor = (byte)(0.299 * Byte.toUnsignedInt(pixels[index+1])
                    + 0.587 * Byte.toUnsignedInt(pixels[index+2])
                    + 0.114 * Byte.toUnsignedInt(pixels[index+3]));
            pixels[index+1] = grayColor;
            pixels[index+2] = grayColor;
            pixels[index+3] = grayColor;
        }
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
