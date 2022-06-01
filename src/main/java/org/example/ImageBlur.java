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

public class ImageBlur {

    public static void blurImage() {
        var filenameWithoutExtension = "./baboon";
        var image = readImage(filenameWithoutExtension+".png");
        DataBuffer dataBuffer = image.getRaster().getDataBuffer();
        int[] pixels = ((DataBufferInt) dataBuffer).getData();
        processBlurFilter(pixels);
        writeImage(filenameWithoutExtension+"-blurred.png", image);
    }

    private static void processBlurFilter(int[] pixels) {
        for (int index = 0; index < pixels.length; index++) {
            var pixel = new byte[] {Byte.MAX_VALUE, Byte.MIN_VALUE, Byte.MIN_VALUE, Byte.MIN_VALUE};
            pixels[index] = byteArray2Int(pixel);
        }
    }

    // from https://stackoverflow.com/questions/2183240/java-integer-to-byte-array
    public static byte[] intT2ByteArray(int value) {
        return new byte[] {
                (byte)(value >> 24),
                (byte)(value >> 16),
                (byte)(value >> 8),
                (byte)value};
    }

    public static int byteArray2Int(byte[] array) {
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
