#pragma once

#include <vector>
#include <string>
#include <sstream>
#include <functional>

inline bool is_blank(std::string s) {
    for (char c : s) {
        if (c != ' ' || c != '\n' || c != '\r') {
            return false;
        }
    }
    return true;
}

std::vector<std::string> split(std::string s, char delim, bool remove_blank = true) {
    std::vector<std::string> parts;
    std::istringstream ss(s);
    std::string part;
    while (std::getline(ss, part, delim)) {
        if (!(remove_blank && is_blank(part))) {
            parts.push_back(part);
        }
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