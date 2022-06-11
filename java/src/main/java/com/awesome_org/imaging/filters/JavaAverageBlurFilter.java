package com.awesome_org.imaging.filters;

public class JavaAverageBlurFilter implements BlurFilter {
    public void processBlurFilter(byte[] pixels, byte[] result, int width, int height) {
        for (int yDestinationIndex = 1; yDestinationIndex < height - 1; yDestinationIndex++) {
            for (int xDestinationIndex = 1; xDestinationIndex < width - 1; xDestinationIndex++) {
                for (int color = 1; color < 4; color++) {
                    var sum = 0;
                    for (int yWindowOffset = -1; yWindowOffset <= 1; yWindowOffset++) {
                        for (int xWindowOffset = -1; xWindowOffset <= 1; xWindowOffset++) {
                            sum += Byte.toUnsignedInt(pixels[getLinearIndex(xDestinationIndex + xWindowOffset, yDestinationIndex + yWindowOffset, color, width)]);
                        }
                    }
                    result[getLinearIndex(xDestinationIndex, yDestinationIndex, color, width)] = (byte) (sum / 9.0);
                }
            }
        }
    }

    private int getLinearIndex(int x, int y, int color, int width) {
        return y * width * 4 + x * 4 + color;
    }
}