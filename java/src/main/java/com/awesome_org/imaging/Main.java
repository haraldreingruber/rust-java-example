package com.awesome_org.imaging;

import com.awesome_org.imaging.filters.BlurFilter;
import com.awesome_org.imaging.filters.GrayscaleFilter;
import com.awesome_org.imaging.filters.JavaAverageBlurFilter;
import com.awesome_org.imaging.filters.RustGrayscaleFilter;

import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.nio.ByteBuffer;

public class Main {
    public static final String FILENAME_WITHOUT_EXTENSION = "./baboon";

    private static BlurFilter blurFilter = new JavaAverageBlurFilter();

    private static GrayscaleFilter grayscaleFilter = new RustGrayscaleFilter();

    public static void main(String... args) {
        var image = ImageIO.readImage(FILENAME_WITHOUT_EXTENSION + ".png");
        DataBuffer dataBuffer = image.getRaster().getDataBuffer();
        int[] pixelsInt = ((DataBufferInt) dataBuffer).getData();
        var byteBuffer = ByteBuffer.allocate(pixelsInt.length * 4);
        byteBuffer.asIntBuffer().put(pixelsInt);
        byte[] pixels = byteBuffer.array();

        var result = new byte[pixels.length];
        var nanoTimeBefore = System.nanoTime();
        blurFilter.processBlurFilter(pixels, result, image.getWidth(), image.getHeight());
        var processingTime = System.nanoTime() - nanoTimeBefore;
        System.arraycopy(result, 0, pixels, 0, pixels.length);
        System.out.println("Blur filter took " + processingTime / 1000000.0 + "ms");

        nanoTimeBefore = System.nanoTime();
        grayscaleFilter.processGrayscaleFilter(pixels);
        processingTime = System.nanoTime() - nanoTimeBefore;
        System.out.println("Grayscale filter took " + processingTime / 1000000.0 + "ms");

        byteBuffer.asIntBuffer().get(pixelsInt);
        ImageIO.writeImage(FILENAME_WITHOUT_EXTENSION + "-blurred.png", image);
    }
}
