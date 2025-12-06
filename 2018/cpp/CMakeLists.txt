cmake_minimum_required(VERSION 3.10.0)
project(advent-of-code-2018 VERSION 1.0.0 LANGUAGES C CXX)

set(CMAKE_CXX_STANDARD 23)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

set(DAY_NUMBER 14)
set(TEMPLATE_DAY OFF)

if(NOT EXISTS "${CMAKE_CURRENT_SOURCE_DIR}/${DAY_NUMBER}.cpp")
set(TEMPLATE_DAY ON)
endif()

if(TEMPLATE_DAY)
set(DAY_SRC "template.cpp")
else()
set(DAY_SRC "${DAY_NUMBER}.cpp")
endif()

add_executable("Day${DAY_NUMBER}" "${DAY_SRC}" DaySolver.cpp main.cpp)
target_compile_definitions(Day${DAY_NUMBER} PRIVATE DAY_NUMBER=${DAY_NUMBER})