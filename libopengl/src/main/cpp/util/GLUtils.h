//
// Created by Dengwei Yu 俞登炜 on 2021/1/26.
//

#ifndef DEMO_GLUTILS_H
#define DEMO_GLUTILS_H

#include <GLES3/gl3.h>
#include <string>
#include <glm.hpp>

class GLUtils {
public:
    static GLuint LoadShader(GLenum shaderType, const char *pSource);

    static GLuint CreateProgram(const char *pVertexShaderSource, const char *pFragShaderSource,
                                GLuint &vertexShaderHandle,
                                GLuint &fragShaderHandle);

    static GLuint CreateProgram(const char *pVertexShaderSource, const char *pFragShaderSource);

    static void DeleteProgram(GLuint &program);

    static void CheckGLError(const char *pGLOperation);
};


#endif //DEMO_GLUTILS_H
