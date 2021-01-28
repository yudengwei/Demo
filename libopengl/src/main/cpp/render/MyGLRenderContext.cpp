//
// Created by Dengwei Yu 俞登炜 on 2021/1/26.
//

#include "MyGLRenderContext.h"
#include "LogUtil.h"
#include "TriangleSample.h"
#include "TextureMapSample.h"
#include "ImageDef.h"

MyGLRenderContext* MyGLRenderContext::m_pContext = nullptr;

MyGLRenderContext::MyGLRenderContext()
{


}


MyGLRenderContext::~MyGLRenderContext()
{
    if (m_pCurSample)
    {
        delete m_pCurSample;
        m_pCurSample = nullptr;
    }
}

void MyGLRenderContext::OnSurfaceCreated() {
    LOGCATE("MyGLRenderContext::OnSurfaceCreated");
    //只有在这里调用才有效
    glClearColor(1.0, 1.0, 0.0, 1.0);
}

void MyGLRenderContext::OnSurfaceChanged(int width, int height) {
    LOGCATE("MyGLRenderContext::OnSurfaceChanged [w, h] = [%d, %d]", width, height);
    glViewport(0, 0, width, height);
    m_ScreenW = width;
    m_ScreenH = height;
    if (m_pCurSample != nullptr) {
        m_pCurSample->OnSizeChange(width, height);
    }
}

void MyGLRenderContext::OnDrawFrame() {
    LOGCATE("MyGLRenderContext::OnDrawFrame");
    if (m_pCurSample != nullptr) {
        m_pCurSample->Draw(m_ScreenW, m_ScreenH);
    }
}

void MyGLRenderContext::DestroyInstance(){
    LOGCATE("MyGLRenderContext::DestroyInstance");
    if (m_pContext) {
        delete m_pContext;
        m_pContext = nullptr;
    }
    if (m_pCurSample != nullptr) {
        m_pCurSample->Destroy();
    }
}

MyGLRenderContext *MyGLRenderContext::GetInstance()
{
    LOGCATE("MyGLRenderContext::GetInstance");
    if (m_pContext == nullptr)
    {
        m_pContext = new MyGLRenderContext();
    }
    return m_pContext;
}

void MyGLRenderContext::Init(const char* vertex, const char* frag) {
    m_pCurSample = new TextureMapSample();
    m_pCurSample->Init(vertex, frag);
}

void MyGLRenderContext::SetImageData(int format, int imageWidth, int imageHeight, uint8_t *pData) {
    LOGCATE("MyGLRenderContext::SetImageData format=%d, width=%d, height=%d, pData=%p", format, imageWidth, imageHeight, pData);
    NativeImage nativeImage;
    nativeImage.format = format;
    nativeImage.width = imageWidth;
    nativeImage.height = imageHeight;
    nativeImage.ppPlane[0] = pData;

    switch (format) {
        case IMAGE_FORMAT_NV12:
        case IMAGE_FORMAT_NV21:
            nativeImage.ppPlane[1] = nativeImage.ppPlane[0] + imageWidth * imageHeight;
            break;
        case IMAGE_FORMAT_I420:
            nativeImage.ppPlane[1] = nativeImage.ppPlane[0] + imageWidth * imageHeight;
            nativeImage.ppPlane[2] = nativeImage.ppPlane[1] + imageWidth * imageHeight / 4;
            break;
        default:
            break;
    }
    if (m_pCurSample) {
        m_pCurSample->LoadImage(&nativeImage);
    }
}