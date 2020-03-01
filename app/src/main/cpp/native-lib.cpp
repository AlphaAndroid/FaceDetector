#include <jni.h>
#include <string>
#include <android/log.h>
#include <opencv2/opencv.hpp>

using namespace std;
using namespace cv;
typedef unsigned char byte;

extern "C" JNIEXPORT jlong JNICALL
Java_com_example_myndkapplication_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {

    Mat *mat = new Mat();
    *mat = imread("/sdcard/Download/image1_large.png", IMREAD_COLOR);

    if (mat->empty()) {
        return NULL;
    }

    return (jlong) mat;
}