//
// Created by Dengwei Yu 俞登炜 on 2021/1/26.
//

#ifndef DEMO_MYGLRENDERCONTEXT_H
#define DEMO_MYGLRENDERCONTEXT_H

#include "stdint.h"
#include "GLSampleBase.h"
#include <GLES3/gl3.h>

class MyGLRenderContext
{

    //构造函数
    MyGLRenderContext();

    //析构函数
    ~MyGLRenderContext();

public:
    static MyGLRenderContext* GetInstance();
    void OnSurfaceCreated();
    void OnSurfaceChanged(int width, int height);
    void OnDrawFrame();
    void DestroyInstance();
    void Init(const char* vertex, const char* frag);

private:
    static MyGLRenderContext *m_pContext;
    GLSampleBase *m_pCurSample;
    int m_ScreenW;
    int m_ScreenH;
};


#endif //DEMO_MYGLRENDERCONTEXT_H
