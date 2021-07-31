//
// Created by reddy on 7/30/2021.
//

#include <jni.h>
#include <string>
#include <iostream>

#include <oboe/Oboe.h>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_sachinreddy_feature_MainActivity_Method(JNIEnv *env, jobject thiz) {
    std::string hello = "Hello from C++";

    oboe::AudioStreamBuilder builder;

    return env->NewStringUTF(hello.c_str());
}