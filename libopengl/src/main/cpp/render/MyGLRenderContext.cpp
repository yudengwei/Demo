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

    if (m_pBeforeSample)
    {
        delete m_pBeforeSample;
        m_pBeforeSample = nullptr;
    }

}

void MyGLRenderContext::OnSurfaceCreated() {
    LOGCATE("MyGLRenderContext::OnSurfaceCreated");
    glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
}

void MyGLRenderContext::OnSurfaceChanged(int width, int height) {
    LOGCATE("MyGLRenderContext::OnSurfaceChanged [w, h] = [%d, %d]", width, height);
    glViewport(0, 0, width, height);
    m_ScreenW = width;
    m_ScreenH = height;
}

void MyGLRenderContext::OnDrawFrame() {
    LOGCATE("MyGLRenderContext::OnDrawFrame");
    glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
}

void MyGLRenderContext::DestroyInstance(){
    LOGCATE("MyGLRenderContext::DestroyInstance");
    if (m_pContext) {
        delete m_pContext;
        m_pContext = nullptr;
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
    m_pBeforeSample = nullptr;
}