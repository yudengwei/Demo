//
// Created by Dengwei Yu 俞登炜 on 2021/1/26.
//

#ifndef DEMO_TRIANGLESAMPLE_H
#define DEMO_TRIANGLESAMPLE_H
#include "GLSampleBase.h"

class TriangleSample : public GLSampleBase{
public:
    TriangleSample();
    virtual ~TriangleSample();

    virtual void Init(const char* vertex, const char* frag);

    virtual void Draw(int screenW, int screenH);

    virtual void OnSizeChange(int width, int height);

    virtual void Destroy();

};


#endif //DEMO_TRIANGLESAMPLE_H
