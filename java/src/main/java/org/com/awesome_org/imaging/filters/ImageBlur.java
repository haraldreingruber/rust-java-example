package org.com.awesome_org.imaging.filters;

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

public class ImageBlur {

    private static JavaAverageBlurFilter blurFilter = new JavaAverageBlurFilter();
    private static JavaGrayscaleFilter grayscaleFilter = new JavaGrayscaleFilter();

    public static void blurImage() {
        var filenameWithoutExtension = "./baboon";
        var image = readImage(filenameWithoutExtension + ".png");
        DataBuffer dataBuffer = image.getRaster().getDataBuffer();
        int[] pixelsInt = ((DataBufferInt) dataBuffer).getData();
        var byteBuffer = ByteBuffer.allocate(pixelsInt.length*4);
        byteBuffer.asIntBuffer().put(pixelsInt);
        byte[] pixels = byteBuffer.array();

        var result = new byte[pixels.length];
        var nanoTimeBefore = System.nanoTime();
        blurFilter.processBlurFilter(pixels, result, image.getWidth(), image.getHeight());
        var processingTime = System.nanoTime() - nanoTimeBefore;
        System.out.println("Blur filter took " + processingTime / 1000000.0 + "ms");
        System.arraycopy(result, 0, pixels, 0, pixels.length);

        nanoTimeBefore = System.nanoTime();
        grayscaleFilter.processGrayscaleFilter(pixels);
        processingTime = System.nanoTime() - nanoTimeBefore;
        System.out.println("Grayscale filter took " + processingTime / 1000000.0 + "ms");

        byteBuffer.asIntBuffer().get(pixelsInt);
        writeImage(filenameWithoutExtension + "-blurred.png", image);
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
