//
// Created by Dengwei Yu 俞登炜 on 2021/1/28.
//

#ifndef DEMO_TEXTUREMAPSAMPLE_H
#define DEMO_TEXTUREMAPSAMPLE_H
#include "GLSampleBase.h"
#include "ImageDef.h"

class TextureMapSample : public GLSampleBase{
public:
    TextureMapSample();
    virtual ~TextureMapSample();

    virtual void Init(const char* vertex, const char* frag);

    virtual void Draw(int screenW, int screenH);

    virtual void OnSizeChange(int width, int height);

    virtual void Destroy();

    virtual void LoadImage(NativeImage *pImage);

private:
    GLuint m_TextureId;
    GLint m_SampleLoc;
    NativeImage m_RenderImage;
};


#endif //DEMO_TEXTUREMAPSAMPLE_H
