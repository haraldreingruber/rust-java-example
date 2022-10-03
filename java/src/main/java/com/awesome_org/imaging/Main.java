package com.awesome_org.imaging;

import com.awesome_org.imaging.filters.*;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.nio.ByteBuffer;

public class Main {
    public static final String IMAGE_FILE_NAME = "./image";

    private static final GrayscaleFilter javaGrayscaleFilter = new JavaGrayscaleFilter();
    private static final GrayscaleFilter rustGrayscaleFilter = new RustGrayscaleFilter();
    private static final GrayscaleFilter grayscaleFilter = rustGrayscaleFilter;


    private static final BlurFilter javaBlurFilter = new JavaAverageBlurFilter();
    private static final BlurFilter rustBlurFilter = new RustAverageBlurFilter();
    private static final BlurFilter blurFilter = rustBlurFilter;

    private static BufferedImage image;
    private static int[] pixelsInt;
    private static ByteBuffer byteBuffer;


    static {
        System.loadLibrary("jni_image_filter");
    }

    public static void main(String... args) {
        byte[] pixels = loadImage(IMAGE_FILE_NAME + ".png");

        // grayscale filter
        var nanoTimeBefore = System.nanoTime();

        grayscaleFilter.processGrayscaleFilter(pixels);

        var processingTime = System.nanoTime() - nanoTimeBefore;
        System.out.println("Grayscale filter took " + processingTime / 1000000.0 + "ms");

        // saving image
        saveImage(IMAGE_FILE_NAME + "-grayscale.png");
        pixels = loadImage(IMAGE_FILE_NAME + ".png");

        // blur filter
        var result = new byte[pixels.length];
        nanoTimeBefore = System.nanoTime();

        blurFilter.processBlurFilter(pixels, result, image.getWidth(), image.getHeight());

        processingTime = System.nanoTime() - nanoTimeBefore;
        System.arraycopy(result, 0, pixels, 0, pixels.length);
        System.out.println("Blur filter took " + processingTime / 1000000.0 + "ms");

        // saving image
        saveImage(IMAGE_FILE_NAME + "-blurred.png");
    }

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
