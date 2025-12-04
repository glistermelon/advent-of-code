cmake_minimum_required(VERSION 3.10.0)
project(advent-of-code-2018 VERSION 1.0.0 LANGUAGES C CXX)

set(CMAKE_CXX_STANDARD 23)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

set(DAY_NUMBER 4)
add_executable("Day${DAY_NUMBER}" "${DAY_NUMBER}.cpp" DaySolver.cpp main.cpp)
target_compile_definitions(Day${DAY_NUMBER} PRIVATE DAY_NUMBER=${DAY_NUMBER})