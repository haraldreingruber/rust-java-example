use image::{ImageBuffer, Rgba};
use jni::objects::{AutoPrimitiveArray, JObject, ReleaseMode};
use jni::sys::{jbyteArray, jint};
use jni::JNIEnv;
use jni_fn::jni_fn;

#[jni_fn("com.awesome_org.imaging.filters.RustGrayscaleFilter")]
pub fn nativeProcessGrayscaleFilter(env: JNIEnv, _: JObject, j_pixels: jbyteArray) {
    let pixels_java_array = env
        .get_primitive_array_critical(j_pixels, ReleaseMode::CopyBack)
        .expect("couldn't get java primitive array");

    let pixels = get_slice_from_primitve_array(&pixels_java_array);

    convert_to_grayscale(pixels);
}

#[jni_fn("com.awesome_org.imaging.filters.RustAverageBlurFilter")]
pub fn nativeProcessBlurFilter(
    env: JNIEnv,
    _: JObject,
    j_pixels: jbyteArray,
    j_result: jbyteArray,
    width: jint,
    height: jint,
) {
    let pixels_java_array = env
        .get_primitive_array_critical(j_pixels, ReleaseMode::NoCopyBack)
        .expect("couldn't get java primitive array");

    let pixels = get_slice_from_primitve_array(&pixels_java_array);

    let result_java_array = env
        .get_primitive_array_critical(j_result, ReleaseMode::CopyBack)
        .expect("couldn't get java primitive array");

    let result = get_slice_from_primitve_array(&result_java_array);

    let source = ImageBuffer::<Rgba<u8>, &mut [u8]>::from_raw(width as u32, height as u32, pixels)
        .expect("Couldn't convert pixel array to image");
    let mut destination =
        ImageBuffer::<Rgba<u8>, &mut [u8]>::from_raw(width as u32, height as u32, result)
            .expect("Couldn't convert pixel array to image");

    blur(&source, &mut destination);
}

fn get_slice_from_primitve_array<'java_array>(
    java_array: &'java_array AutoPrimitiveArray,
) -> &'java_array mut [u8] {
    let pixels_java_array_ptr = java_array.as_ptr().cast::<u8>();

    let array_length: usize = java_array
        .size()
        .expect("couldn't get array size")
        .try_into()
        .expect("couldn't convert to usize");

    unsafe { std::slice::from_raw_parts_mut(pixels_java_array_ptr, array_length) }
}

fn convert_to_grayscale(pixels: &mut [u8]) {
    for index in (0..pixels.len()).step_by(4) {
        let gray_color = (0.299 * pixels[index + 1] as f32
            + 0.587 * pixels[index + 2] as f32
            + 0.114 * pixels[index + 3] as f32) as u8;
        pixels[index + 1] = gray_color;
        pixels[index + 2] = gray_color;
        pixels[index + 3] = gray_color;
    }
}

fn blur(
    source: &ImageBuffer<Rgba<u8>, &mut [u8]>,
    destination: &mut ImageBuffer<Rgba<u8>, &mut [u8]>,
) {
    for y in 1..(source.height() - 1) as i16 {
        for x in 1..(source.width() - 1) as i16 {
            for color in 1..4 {

                let mut sum = 0_u32;
                for y_window_offset in -1_i16..=1_i16 {
                    for x_window_offset in -1_i16..=1_i16 {
                        sum += source
                            .get_pixel((x + x_window_offset) as u32, (y + y_window_offset) as u32)
                            [color] as u32;
                    }
                }

                destination.get_pixel_mut(x as u32, y as u32)[color] = (sum as f32 / 9.0) as u8;
            }
        }
    }
}
