package com.awesome_org.imaging;

import com.awesome_org.imaging.filters.GrayscaleFilter;
import com.awesome_org.imaging.filters.JavaGrayscaleFilter;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.nio.ByteBuffer;

public class Main {
    public static final String BABOON_FILE_NAME = "./baboon";

    private static final GrayscaleFilter grayscaleFilter = new JavaGrayscaleFilter();

    public static void main(String... args) {
        byte[] pixels = loadImage(BABOON_FILE_NAME + ".png");

        // grayscale filter
        var nanoTimeBefore = System.nanoTime();

        grayscaleFilter.processGrayscaleFilter(pixels);

        var processingTime = System.nanoTime() - nanoTimeBefore;
        System.out.println("Grayscale filter took " + processingTime / 1000000.0 + "ms");

        // saving image
        saveImage(BABOON_FILE_NAME + "-blurred.png");
    }

    private static BufferedImage image;
    private static int[] pixelsInt;
    private static ByteBuffer byteBuffer;
    
    private static byte[] loadImage(String path) {
        image = ImageIO.readImage(path);
        DataBuffer dataBuffer = image.getRaster().getDataBuffer();
        pixelsInt = ((DataBufferInt) dataBuffer).getData();
        byteBuffer = ByteBuffer.allocate(pixelsInt.length * 4);
        byteBuffer.asIntBuffer().put(pixelsInt);
        byte[] pixels = byteBuffer.array();
        return pixels;
    }

    private static void saveImage(String path) {
        byteBuffer.asIntBuffer().get(pixelsInt);
        ImageIO.writeImage(path, image);
    }
}
