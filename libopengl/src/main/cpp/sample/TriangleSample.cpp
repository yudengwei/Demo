//
// Created by Dengwei Yu 俞登炜 on 2021/1/26.
//

#include "TriangleSample.h"
#include "LogUtil.h"
#include "GLUtils.h"

TriangleSample::TriangleSample() {

}

TriangleSample::~TriangleSample() {

}

void TriangleSample::Init(const char* vertex, const char* frag) {
    LOGCATE("TriangleSample::Init");
    m_ProgramObj = GLUtils::CreateProgram(vertex, frag, m_VertexShader, m_FragmentShader);
    LOGCATE("m_ProgramObj, %d", m_ProgramObj);
    if (m_ProgramObj == 0) {
        return;
    }
    GLfloat vVertices[] = {
            0.0f,0.5f,0.0f,
            -0.5f,-0.5f,0.0f,
            0.5f,-0.6f,0.0f,
    };
    glUseProgram(m_ProgramObj);
    glVertexAttribPointer(0,3,GL_FLOAT,GL_FALSE,0,vVertices);
    glEnableVertexAttribArray(0);
}

void TriangleSample::OnSizeChange(int width, int height) {
    LOGCATE("TriangleSample::OnSizeChange");
}

void TriangleSample::Draw(int screenW, int screenH) {
    glClear(GL_STENCIL_BUFFER_BIT | GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    glClearColor(1.0, 0.0, 0.0, 1.0);
    glDrawArrays(GL_TRIANGLES, 0, 3);
    //glUseProgram(GL_NONE);
}

void TriangleSample::Destroy() {
    if (m_ProgramObj) {
        glDeleteProgram(m_ProgramObj);
        m_ProgramObj = GL_NONE;
    }
}

