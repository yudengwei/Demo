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
    m_ProgramObj = GLUtils::CreateProgram(vertex, frag, m_VertexShader, m_FragmentShader);
}

void TriangleSample::Draw(int screenW, int screenH) {

}

void TriangleSample::Destroy() {

}

