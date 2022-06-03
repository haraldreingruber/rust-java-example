package org.com.awesome_org.imaging.filters;

public interface BlurFilter {
    void processBlurFilter(byte[] pixels, byte[] result, int width, int height);
}
