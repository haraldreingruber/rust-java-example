package com.awesome_org.imaging.filters;

public class RustAverageBlurFilter implements BlurFilter {
    private native void nativeProcessBlurFilter(byte[] pixels, byte[] result, int width, int height);

    @Override
    public void processBlurFilter(byte[] pixels, byte[] result, int width, int height) {
        //nativeProcessBlurFilter(pixels, result, width, height);
    }
}
