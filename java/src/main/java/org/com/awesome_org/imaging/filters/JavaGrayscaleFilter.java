package org.com.awesome_org.imaging.filters;

public class JavaGrayscaleFilter implements GrayscaleFilter {
    public void processGrayscaleFilter(byte[] pixels) {
        for (int index = 0; index < pixels.length; index += 4) {
            var grayColor = (byte) (0.299 * Byte.toUnsignedInt(pixels[index + 1])
                    + 0.587 * Byte.toUnsignedInt(pixels[index + 2])
                    + 0.114 * Byte.toUnsignedInt(pixels[index + 3]));
            pixels[index + 1] = grayColor;
            pixels[index + 2] = grayColor;
            pixels[index + 3] = grayColor;
        }
    }
}