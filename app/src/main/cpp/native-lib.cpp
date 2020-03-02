#include <jni.h>
#include <string>
#include <android/log.h>
#include <android/bitmap.h>
#include <opencv2/opencv.hpp>

#define LOGTAG "Alpha"
#define LOG(...) __android_log_print(ANDROID_LOG_ERROR, LOGTAG, __VA_ARGS__)

using namespace std;
using namespace cv;

extern "C" JNIEXPORT jlong JNICALL
Java_com_example_myndkapplication_MainActivity_getImageFromPath(
        JNIEnv *env,
        jobject /* this */) {
    Mat *mat = new Mat();
    *mat = imread("/sdcard/Download/image1_large.png", IMREAD_COLOR);

    if (mat->empty()) {
        return 0;
    }

    return (jlong) mat;
}

extern "C" JNIEXPORT void JNICALL
Java_com_example_myndkapplication_MainActivity_setNativeMat(
        JNIEnv *env,
        jobject thiz,
        jlong inputMat,
        jint width,
        jint height,
        jboolean isIncrease
) {

    Mat &input = *(Mat *) inputMat;

    if (isIncrease) {
        resize(input, input, Size(width, height), 0, 0, INTER_LINEAR);
    } else {
        resize(input, input, Size(width, height), 0, 0, INTER_AREA);
    }
}