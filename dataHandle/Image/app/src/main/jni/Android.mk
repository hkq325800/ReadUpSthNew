LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

OpenCV_INSTALL_MODULES := onOpenCV_CAMERA_MODULES := off
OPENCV_LIB_TYPE :=STATIC

ifeq ("$(wildcard $(OPENCV_MK_PATH))","")include ..\..\..\..\native\jni\OpenCV.mkelseinclude $(OPENCV_MK_PATH)
endif
LOCAL_MODULE := OpenCV
LOCAL_SRC_FILES :=
LOCAL_LDLIBS +=  -lm -llog
include $(BUILD_SHARED_LIBRARY)