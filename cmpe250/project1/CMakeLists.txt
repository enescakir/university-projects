cmake_minimum_required(VERSION 2.8)
project(project1)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

set(SOURCE_FILES main.cpp Node.h Node.cpp LinkedList.h LinkedList.cpp BigInteger.h BigInteger.cpp)

SET( CMAKE_RUNTIME_OUTPUT_DIRECTORY ${PROJECT_SOURCE_DIR} )

add_executable(project1 ${SOURCE_FILES})