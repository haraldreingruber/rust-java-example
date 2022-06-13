package com.awesome_org.imaging;

import com.awesome_org.imaging.filters.*;

import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.nio.ByteBuffer;

public class Main {
    public static final String FILENAME_WITHOUT_EXTENSION = "./baboon";

    private static final GrayscaleFilter javaGrayscaleFilter = new JavaGrayscaleFilter();
    private static final GrayscaleFilter rustGrayscaleFilter = new RustGrayscaleFilter();
    private static final GrayscaleFilter grayscaleFilter = rustGrayscaleFilter;


    private static final BlurFilter javaBlurFilter = new JavaAverageBlurFilter();
    private static final BlurFilter rustBlurFilter = new RustAverageBlurFilter();
    private static final BlurFilter blurFilter = rustBlurFilter;


    static {
        System.loadLibrary("jni_image_filter");
    }

    public static void main(String... args) {
        // loading image
        var image = ImageIO.readImage(FILENAME_WITHOUT_EXTENSION + ".png");
        DataBuffer dataBuffer = image.getRaster().getDataBuffer();
        int[] pixelsInt = ((DataBufferInt) dataBuffer).getData();
        var byteBuffer = ByteBuffer.allocate(pixelsInt.length * 4);
        byteBuffer.asIntBuffer().put(pixelsInt);
        byte[] pixels = byteBuffer.array();

        // grayscale filter
        var nanoTimeBefore = System.nanoTime();

        grayscaleFilter.processGrayscaleFilter(pixels);

        var processingTime = System.nanoTime() - nanoTimeBefore;
        System.out.println("Grayscale filter took " + processingTime / 1000000.0 + "ms");

        // blur filter
        var result = new byte[pixels.length];
        nanoTimeBefore = System.nanoTime();

        blurFilter.processBlurFilter(pixels, result, image.getWidth(), image.getHeight());

        processingTime = System.nanoTime() - nanoTimeBefore;
        System.arraycopy(result, 0, pixels, 0, pixels.length);
        System.out.println("Blur filter took " + processingTime / 1000000.0 + "ms");

        // saving image
        byteBuffer.asIntBuffer().get(pixelsInt);
        ImageIO.writeImage(FILENAME_WITHOUT_EXTENSION + "-blurred.png", image);
    }
}
