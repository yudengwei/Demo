
cmake_minimum_required(VERSION 3.4.1)

set(jnilibs "${CMAKE_SOURCE_DIR}/../jniLibs")

include_directories(render
        glm
        sample
        util)

file(GLOB src-files
        ${CMAKE_SOURCE_DIR}/*.cpp
        ${CMAKE_SOURCE_DIR}/sample/*.cpp
        ${CMAKE_SOURCE_DIR}/util/*.cpp
        ${CMAKE_SOURCE_DIR}/render/*.cpp)

add_library(native-render

        SHARED

        ${src-files})

find_library(log-lib

        log)

target_link_libraries(native-render
                       ${log-lib}
                       EGL
                       GLESv3
                       )
