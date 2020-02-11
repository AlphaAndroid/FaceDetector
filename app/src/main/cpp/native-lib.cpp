#include <jni.h>
#include <string>
#include <android/log.h>

using namespace std;

extern "C" JNIEXPORT jbyteArray JNICALL
Java_com_example_myndkapplication_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    FILE *fp = fopen("/proc/cpuinfo", "r");
    string resultFileStr = "";
    char symbol;
    int rawChar;
    do {
        rawChar = fgetc(fp);
        symbol = static_cast<char>(rawChar);
        resultFileStr.append(1, symbol);
    } while (!feof(fp));
    fclose(fp);

    jbyteArray array = env->NewByteArray(resultFileStr.length());
    env->SetByteArrayRegion(array, 0, resultFileStr.length(), (jbyte *) resultFileStr.c_str());
    return array;
}
