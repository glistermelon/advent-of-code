#include "DaySolver.hpp"

#include "Array2D.hpp"

struct Rect {
    int x, y, w, h;
};

class Day3Solver : public DaySolver {

    std::vector<Rect> rects;

    Array2D<int> map;

protected:

    void process_input() {
        std::istringstream input_stream(puzzle_input);
        std::string line;
        while (std::getline(input_stream, line, '\n')) {

            Rect r;
            size_t i, j;

            i = line.find('@') + 2;
            j = line.find(',');
            r.x = std::stoi(line.substr(i, j - i));

            i = j + 1;
            j = line.find(':');
            r.y = std::stoi(line.substr(i, j - i));

            i = j + 2;
            j = line.find('x');
            r.w = std::stoi(line.substr(i, j - i));

            i = j + 1;
            r.h = std::stoi(line.substr(i));
            
            rects.push_back(r);

        }
    }

    void do_shared_work() {

        int map_w = 0, map_h = 0;
        for (const auto& r : rects) {
            map_w = std::max(map_w, r.x + r.w);
            map_h = std::max(map_h, r.y + r.h);
        }

        map = Array2D<int>(map_h, map_w);

        for (const auto& r : rects) {
            for (auto x = r.x; x < r.x + r.w; x++) {
                for (auto y = r.y; y < r.y + r.h; y++) {
                    map[y][x]++;
                }
            }
        }

    }

    DaySolution solve_part_1() {
        int overlapping = 0;
        for (const auto& value : map) {
            if (value >= 2) {
                overlapping++;
            }
        }
        return overlapping;
    }

    DaySolution solve_part_2() {
        for (auto i = 0; i < rects.size(); i++) {
            auto r = rects[i];
            bool any_overlap = false;
            for (auto x = r.x; x < r.x + r.w && !any_overlap; x++) {
                for (auto y = r.y; y < r.y + r.h; y++) {
                     if (map[y][x] >= 2) {
                        any_overlap = true;
                        break;
                     }
                }
            }
            if (!any_overlap) {
                return i + 1;
            }
        }
        return "no solution found";
    }

};

DaySolver* DaySolver::get_current_day_solver() {
    return new Day3Solver();
}