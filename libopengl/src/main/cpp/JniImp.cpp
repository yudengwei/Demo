//
// Created by Dengwei Yu 俞登炜 on 2021/1/26.
//

#include "util/LogUtil.h"
#include <MyGLRenderContext.h>
#include "jni.h"

#define NATIVE_RENDER_CLASS_NAME "com/example/opengl/NativeRender"

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT void JNICALL native_init(JNIEnv *env, jobject instance, jstring vertex, jstring frag) {
    const char* c_vertex = env->GetStringUTFChars(vertex, JNI_FALSE);
    const char* c_frag = env->GetStringUTFChars(frag, JNI_FALSE);
    MyGLRenderContext::GetInstance()->Init(c_vertex);
}

JNIEXPORT void JNICALL native_onSurfaceCreated(JNIEnv *env, jobject instance) {
    MyGLRenderContext::GetInstance()->OnSurfaceCreated();
}

JNIEXPORT void JNICALL native_onSurfaceChanged(JNIEnv *env, jobject instance, jint width, jint height){
    MyGLRenderContext::GetInstance()->OnSurfaceChanged(width, height);
}

JNIEXPORT void JNICALL native_onDrawFrame(JNIEnv *env, jobject instance) {
    MyGLRenderContext::GetInstance()->OnDrawFrame();
}

JNIEXPORT void JNICALL native_unInit(JNIEnv *env, jobject instance) {
    MyGLRenderContext::GetInstance()->DestroyInstance();
}

#ifdef __cplusplus
}
#endif

static JNINativeMethod g_RenderMethods[] = {
        {"native_init", "()V", (void *)(native_init)},
        {"native_onSurfaceCreated", "()V", (void *)(native_onSurfaceCreated)},
        {"native_onSurfaceChanged", "(II)V", (void *)(native_onSurfaceChanged)},
        {"native_onDrawFrame", "()V", (void *)(native_onDrawFrame)},
        {"native_unInit","()V", (void *)(native_unInit)}
};

static int RegisterNativeMethods(JNIEnv *env, const char *className, JNINativeMethod *methods, int methodNum) {
    LOGCATE("RegisterNativeMethods");
    jclass clazz = env->FindClass(className);
    if (clazz == NULL) {
        LOGCATE("RegisterNativeMethods fail. clazz == NULL");
        return JNI_FALSE;
    }
    if (env->RegisterNatives(clazz, methods, methodNum) < 0) {
        LOGCATE("RegisterNativeMethods fail");
        return JNI_FALSE;
    }
    return JNI_TRUE;
}

static void UnregisterNativeMethods(JNIEnv *env, const char *className) {
    LOGCATE("UnregisterNativeMethods");
    jclass clazz = env->FindClass(className);
    if (clazz == NULL)
    {
        LOGCATE("UnregisterNativeMethods fail. clazz == NULL");
        return;
    }
    env->UnregisterNatives(clazz);
}

extern "C" jint JNI_OnLoad(JavaVM *jvm, void *p) {
    LOGCATE("-----JNI_Onload------");
    jint result = JNI_ERR;
    JNIEnv *env = NULL;
    if (jvm->GetEnv((void **)(&env), JNI_VERSION_1_6) != JNI_OK) {
        return result;
    }
    if (RegisterNativeMethods(env, NATIVE_RENDER_CLASS_NAME, g_RenderMethods, sizeof(g_RenderMethods) / sizeof(g_RenderMethods[0])) != JNI_TRUE) {
        return result;
    }
    return JNI_VERSION_1_6;
}

extern "C" void JNI_OnUnload(JavaVM *jvm, void *p) {
    JNIEnv *env = NULL;
    if (jvm->GetEnv((void **)(&env), JNI_VERSION_1_6) != JNI_OK) {
        return;
    }
    UnregisterNativeMethods(env, NATIVE_RENDER_CLASS_NAME);
}
