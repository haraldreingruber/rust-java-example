use jni::JNIEnv;
use jni::objects::{AutoPrimitiveArray, JObject, ReleaseMode};
use jni::sys::jbyteArray;
use jni_fn::jni_fn;

#[jni_fn("com.awesome_org.imaging.filters.RustGrayscaleFilter")]
pub fn nativeProcessGrayscaleFilter(env: JNIEnv, _: JObject, j_pixels: jbyteArray) {
    let pixels_java_array = env
        .get_primitive_array_critical(j_pixels, ReleaseMode::CopyBack)
        .expect("couldn't get java primitive array");

    let pixels = get_slice_from_primitve_array(&pixels_java_array);

    convert_to_grayscale(pixels);
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
