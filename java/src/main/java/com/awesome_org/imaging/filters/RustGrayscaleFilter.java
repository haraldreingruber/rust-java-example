package com.awesome_org.imaging.filters;

public class RustGrayscaleFilter implements GrayscaleFilter {
    private native void nativeProcessGrayscaleFilter(byte[] pixels);

    @Override
    public void processGrayscaleFilter(byte[] pixels) {
        nativeProcessGrayscaleFilter(pixels);
    }
}
