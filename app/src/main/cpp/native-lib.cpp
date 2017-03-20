#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_mundobabushka_magnus_bambushka_1pov_JanelaPrincipal_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
