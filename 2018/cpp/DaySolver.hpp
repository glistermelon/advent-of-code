#pragma once

#include <fstream>
#include <string>
#include <sstream>
#include <iostream>
#include <algorithm>
#include <set>
#include <vector>
#include <filesystem>

#ifdef _WIN32
#include <direct.h>
#endif

inline std::filesystem::path get_input_dir() {
#ifdef _WIN32
    std::string cwd(1024, 0);
    _getcwd((char*)cwd.data(), 1024);
#elif
#error get_input_dir only defined for windows
#endif
    std::filesystem::path path(cwd);
    while (path.filename() != "2018") {
        path = path.parent_path();
    }
    return path / "inputs";
}

inline std::string get_puzzle_input(int day) {

    auto path = get_input_dir() / (std::to_string(day) + ".txt");
    std::ifstream file(path);
    std::stringstream buffer;
    buffer << file.rdbuf();
    std::string str = buffer.str();
    str.erase(std::remove(str.begin(), str.end(), '\r'), str.end());
    while (str.ends_with('\n')) {
        str.pop_back();
    }
    return str;

}

enum class DaySolutionType {
    INT64,
    STR
} ;

struct DaySolution {

    DaySolutionType type;

    int64_t int64_val;
    std::string str_val;

    DaySolution(int64_t v) : int64_val(v), type(DaySolutionType::INT64) { }
    DaySolution(std::string v) : str_val(v), type(DaySolutionType::STR) { }  
    DaySolution(const char* v) : str_val(v), type(DaySolutionType::STR) { }        

    friend std::ostream& operator<<(std::ostream& os, const DaySolution& obj);

};

class DaySolver {

protected:

    std::string puzzle_input;

    static DaySolver* get_current_day_solver();

    inline int get_day_number() {
        return DAY_NUMBER;
    }

    virtual void process_input();
    virtual void do_shared_work();
    virtual DaySolution solve_part_1();
    virtual DaySolution solve_part_2();

public:

    void run();

    static void run_current_day();

};