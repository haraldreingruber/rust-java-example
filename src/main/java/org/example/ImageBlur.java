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
import java.util.Arrays;

public class ImageBlur {

    public static void blurImage() {
        var filenameWithoutExtension = "./baboon";
        var image = readImage(filenameWithoutExtension + ".png");
        DataBuffer dataBuffer = image.getRaster().getDataBuffer();
        int[] pixels = ((DataBufferInt) dataBuffer).getData();
        processGrayscaleFilter(pixels);
        writeImage(filenameWithoutExtension + "-blurred.png", image);
    }

    private static void processBlurFilter(int[] pixels) {
        for (int index = 0; index < pixels.length; index++) {
            var pixel = new short[]{0, 0, 0, 255};
            pixels[index] = ARGB2Int(pixel);
        }
    }

    private static void processGrayscaleFilter(int[] pixels) {
        for (int index = 0; index < pixels.length; index++) {
            var pixel = int2ARGB(pixels[index]);
            var grayColor = 0.299 * pixel[1]
                    + 0.587 * pixel[2]
                    + 0.114 * pixel[3];
            Arrays.fill(pixel, (short) grayColor);
            pixels[index] = ARGB2Int(pixel);
        }
    }

    // from https://stackoverflow.com/questions/2183240/java-integer-to-byte-array
    public static short[] int2ARGB(int value) {
        return new short[]{
                (short) Byte.toUnsignedInt((byte) (value >>> 24)),
                (short) Byte.toUnsignedInt((byte) (value >>> 16)),
                (short) Byte.toUnsignedInt((byte) (value >>> 8)),
                (short) Byte.toUnsignedInt((byte) value)};
    }

    public static int ARGB2Int(short[] array) {
        return array[0] << 24
                | array[1] << 16
                | array[2] << 8
                | array[3];
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
