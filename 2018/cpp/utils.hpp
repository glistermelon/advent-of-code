#pragma once

#include <vector>
#include <string>
#include <sstream>
#include <functional>

std::vector<std::string> split(std::string s, char delim) {
    std::vector<std::string> parts;
    std::istringstream ss(s);
    std::string part;
    while (std::getline(ss, part, delim)) {
        parts.push_back(part);
    }
    return parts;
}

template <class To, class From, class Func>
auto map_to(const std::vector<From>& vec, Func func) {
    std::vector<To> result(vec.size());
    for (auto i = 0; i < vec.size(); i++) {
        result[i] = func(vec[i]);
    }
    return result;
}