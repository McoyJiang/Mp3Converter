# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
             mp3lame

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             src/main/cpp/native-lib.cpp
             src/main/cpp/libmp3lame/bitstream.c
             src/main/cpp/libmp3lame/encoder.c
             src/main/cpp/libmp3lame/fft.c
             src/main/cpp/libmp3lame/gain_analysis.c
             src/main/cpp/libmp3lame/id3tag.c
             src/main/cpp/libmp3lame/lame.c
             src/main/cpp/libmp3lame/mpglib_interface.c
             src/main/cpp/libmp3lame/newmdct.c
             src/main/cpp/libmp3lame/presets.c
             src/main/cpp/libmp3lame/psymodel.c
             src/main/cpp/libmp3lame/quantize.c
             src/main/cpp/libmp3lame/quantize_pvt.c
             src/main/cpp/libmp3lame/reservoir.c
             src/main/cpp/libmp3lame/set_get.c
             src/main/cpp/libmp3lame/tables.c
             src/main/cpp/libmp3lame/takehiro.c
             src/main/cpp/libmp3lame/util.c
             src/main/cpp/libmp3lame/vbrquantize.c
             src/main/cpp/libmp3lame/VbrTag.c
             src/main/cpp/libmp3lame/version.c
             src/main/cpp/wrapper.c

             src/main/cpp/soundtouch-jni.cpp
             src/main/cpp/soundtouch/source/SoundTouch/SoundTouch.cpp
             src/main/cpp/soundtouch/source/SoundTouch/RateTransposer.cpp
             src/main/cpp/soundtouch/source/SoundTouch/FIFOSampleBuffer.cpp
             src/main/cpp/soundtouch/source/SoundTouch/AAFilter.cpp
             src/main/cpp/soundtouch/source/SoundTouch/FIRFilter.cpp
             src/main/cpp/soundtouch/source/SoundTouch/cpu_detect_x86.cpp
             src/main/cpp/soundtouch/source/SoundTouch/BPMDetect.cpp
             src/main/cpp/soundtouch/source/SoundTouch/mmx_optimized.cpp
             src/main/cpp/soundtouch/source/SoundTouch/PeakFinder.cpp
             src/main/cpp/soundtouch/source/SoundTouch/sse_optimized.cpp
             src/main/cpp/soundtouch/source/SoundTouch/TDStretch.cpp
             src/main/cpp/soundtouch/source/SoundStretch/WavFile.cpp)

# Specifies a path to native header files.
include_directories(
            src/main/cpp/libmp3lame/
            src/main/cpp/soundtouch/include/
            src/main/cpp/soundtouch/source/SoundTouch/
            src/main/cpp/soundtouch/source/SoundStretch)

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
                       mp3lame

                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )