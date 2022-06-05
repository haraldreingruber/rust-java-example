use jni::JNIEnv;
use jni::objects::JObject;
use jni::sys::jbyteArray;
use jni_fn::jni_fn;

#[jni_fn("com.awesome_org.imaging.filters.RustGrayscaleFilter")]
pub fn nativeProcessGrayscaleFilter(env: JNIEnv, _: JObject, pixels: jbyteArray) {
    println!("hello native world");
}