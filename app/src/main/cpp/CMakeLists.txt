cmake_minimum_required(VERSION 3.4.1)

add_library(
        cpp_code
        SHARED
        cpp_code.cpp)
find_library(
        cpp_code
        log)

set(OBOE_DIR ./oboe)

add_subdirectory(${OBOE_DIR} ./oboe)

include_directories(${OBOE_DIR}/include)

target_link_libraries(cpp_code log oboe)
