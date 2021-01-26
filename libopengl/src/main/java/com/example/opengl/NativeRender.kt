package com.example.opengl

class NativeRender{

    companion object {
        init {
            System.loadLibrary("native-render")
        }
    }

    external fun native_init(vertex : String, frag : String)

    external fun native_onSurfaceCreated()

    external fun native_onSurfaceChanged(width : Int, height : Int)

    external fun native_onDrawFrame()

    external fun native_unInit()
}