#include <fstream>
#include <string>
#include <sstream>
#include <iostream>
#include <algorithm>
#include <set>
#include <vector>

inline std::string get_puzzle_input(int day) {

    std::string path = "../../2018/inputs/" + std::to_string(day) + ".txt";
    std::ifstream file(path);
    std::stringstream buffer;
    buffer << file.rdbuf();
    std::string str = buffer.str();
    str.erase(std::remove(str.begin(), str.end(), '\r'), str.end());
    return str;

}