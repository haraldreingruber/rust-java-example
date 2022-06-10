package com.awesome_org.imaging;

import com.awesome_org.imaging.filters.GrayscaleFilter;
import com.awesome_org.imaging.filters.JavaGrayscaleFilter;

import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.nio.ByteBuffer;

public class Main {
    public static final String BABOON_FILE_NAME = "./baboon";

    private static final GrayscaleFilter grayscaleFilter = new JavaGrayscaleFilter();

    public static void main(String... args) {
        var image = ImageIO.readImage(BABOON_FILE_NAME + ".png");
        DataBuffer dataBuffer = image.getRaster().getDataBuffer();
        int[] pixelsInt = ((DataBufferInt) dataBuffer).getData();
        var byteBuffer = ByteBuffer.allocate(pixelsInt.length * 4);
        byteBuffer.asIntBuffer().put(pixelsInt);
        byte[] pixels = byteBuffer.array();

  
        var nanoTimeBefore = System.nanoTime();

        grayscaleFilter.processGrayscaleFilter(pixels);

        var processingTime = System.nanoTime() - nanoTimeBefore;
        System.out.println("Grayscale filter took " + processingTime / 1000000.0 + "ms");

        byteBuffer.asIntBuffer().get(pixelsInt);
        ImageIO.writeImage(BABOON_FILE_NAME + "-blurred.png", image);
    }
}
