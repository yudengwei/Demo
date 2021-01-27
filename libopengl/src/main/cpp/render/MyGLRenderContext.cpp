//
// Created by Dengwei Yu 俞登炜 on 2021/1/26.
//

#include "MyGLRenderContext.h"
#include "LogUtil.h"
#include "TriangleSample.h"

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
        m_pCurSample->Draw(0, 0);
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
    m_pCurSample = new TriangleSample();
    m_pCurSample->Init(vertex, frag);
}