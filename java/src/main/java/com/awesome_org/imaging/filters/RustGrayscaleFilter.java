package com.awesome_org.imaging.filters;

public class RustGrayscaleFilter implements GrayscaleFilter {

    @Override
    public void processGrayscaleFilter(byte[] pixels) {
        nativeProcessGrayscaleFilter(pixels);
    }

    private native void nativeProcessGrayscaleFilter(byte[] pixels);
}
