#include "DaySolver.hpp"

#include <map>

struct VecZ2 {

    int x, y;

    bool operator<(const VecZ2& other) const {
        if (x < other.x) return true;
        if (x > other.x) return false;
        return y < other.y;
    }

};

struct Light {
    VecZ2 pos;
    VecZ2 vel;
};

class Day10Solver : public DaySolver {

    std::vector<Light> lights;
    size_t seconds = 0;

protected:

    void process_input() {
        std::istringstream input_stream(puzzle_input);
        std::string line;
        while (std::getline(input_stream, line, '\n')) {

            Light light;
            size_t i, j;

            i = line.find('<') + 1;
            j = line.find(',');
            light.pos.x = std::stoi(line.substr(i, j - i));

            i = j + 2;
            j = line.find('>');
            light.pos.y = std::stoi(line.substr(i, j - i));

            i = line.find('<', j) + 1;
            j = line.find(',', i);
            light.vel.x = std::stoi(line.substr(i, j - i));
            
            i = j + 2;
            j = line.find('>', i);
            light.vel.y = std::stoi(line.substr(i, j - i));

            lights.push_back(light);

        }
    }

    void advance_lights() {
        for (auto& light : lights) {
            light.pos.x += light.vel.x;
            light.pos.y += light.vel.y;
        }
    }

    void get_spread(int& min_x, int& max_x, int& min_y, int& max_y) {
        min_x = INT_MAX;
        max_x = INT_MIN;
        min_y = INT_MAX;
        max_y = INT_MIN;
        for (const auto& light : lights) {
            min_x = std::min(min_x, light.pos.x);
            max_x = std::max(max_x, light.pos.x);
            min_y = std::min(min_y, light.pos.y);
            max_y = std::max(max_y, light.pos.y);
        }
    }

    bool is_candidate() {
        constexpr int height_guess = 10;
        int min_x, max_x, min_y, max_y;
        get_spread(min_x, max_x, min_y, max_y);
        return max_y - min_y + 1 <= height_guess;
    }

    void do_shared_work() {
        while (!is_candidate()) {
            advance_lights();
            seconds++;
        }
    }

    DaySolution solve_part_1() {

        int min_x, max_x, min_y, max_y;
        get_spread(min_x, max_x, min_y, max_y);
        for (auto& light : lights) {
            light.pos.x -= min_x;
            light.pos.y -= min_y;
        }
        max_x -= min_x;
        max_y -= min_y;

        std::string sky = "";
        for (int y = 0; y <= max_y; y++) {
            sky.append(std::string(max_x + 1, '.'));
            sky.push_back('\n');
        }
        for (const auto& light : lights) {
            sky[light.pos.y * (max_x + 2) + light.pos.x] = '#';
        }
        sky.insert(sky.begin(), '\n');

        return sky;

    }

    DaySolution solve_part_2() {
        return seconds;
    }

};

DaySolver* DaySolver::get_current_day_solver() {
    return new Day10Solver();
}