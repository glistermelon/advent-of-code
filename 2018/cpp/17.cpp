#include "DaySolver.hpp"

enum class Tile {
    Sand = 0,
    Clay,
    RunningWater,
    StillWater
};

struct WaterStats {

    int still;
    int running;

    WaterStats operator+(const WaterStats& other) const {
        return {
            still + other.still,
            running + other.running
        };
    }

    void operator+=(const WaterStats& other) {
        still += other.still;
        running += other.running;
    }

};

class Day17Solver : public DaySolver {

    Tile map[2000][2000] = { };

    int min_y = INT_MAX;
    int max_y = INT_MIN;

    WaterStats stats;

    WaterStats spread_water(int src_x, int src_y) {

        if (src_y > max_y) {
            return { };
        }

        map[src_x][src_y] = Tile::RunningWater;

        WaterStats water = { };

        if (map[src_x][src_y + 1] == Tile::Sand) {
            water += spread_water(src_x, src_y + 1);
        }

        if (map[src_x][src_y + 1] == Tile::Clay || map[src_x][src_y + 1] == Tile::StillWater) {

            bool closed[2] = { };
            
            int x = src_x;
            while (true) {
                if (map[x - 1][src_y] == Tile::Sand) {
                    x--;
                }
                else {
                    break;
                }
                if (map[x][src_y + 1] == Tile::Sand) {
                    water += spread_water(x, src_y + 1);
                    if (map[x][src_y + 1] != Tile::StillWater) {
                        break;
                    }
                }
            }
            closed[0] = map[x - 1][src_y] == Tile::Clay;
            int left_x = x;

            x = src_x;
            while (true) {
                if (map[x + 1][src_y] == Tile::Sand) {
                    x++;
                }
                else {
                    break;
                }
                if (map[x][src_y + 1] == Tile::Sand) {
                    water += spread_water(x, src_y + 1);
                    if (map[x][src_y + 1] != Tile::StillWater) {
                        break;
                    }
                }
            }
            closed[1] = map[x + 1][src_y] == Tile::Clay;
            int right_x = x;

            for (int x = left_x; x <= right_x; x++) {
                map[x][src_y] = closed[0] && closed[1] ? Tile::StillWater : Tile::RunningWater;
            }

            if (closed[0] && closed[1]) {
                water.still += right_x - left_x + 1;
            }
            else {
                water.running += right_x - left_x + 1;
            }

        }
        else {
            if (src_y >= min_y) {
                water.running++;
            }
        }

        return water;

    }

protected:

    void process_input() {

        std::istringstream input_stream(puzzle_input);
        std::string line;
        while (std::getline(input_stream, line, '\n')) {

            int nums[3];
            size_t i, j;

            i = 2;
            j = line.find(',');
            nums[0] = std::stoi(line.substr(i, j - i));

            i = line.find('=', j) + 1;
            j = line.find('.', i);
            nums[1] = std::stoi(line.substr(i, j - i));

            i = j + 2;
            nums[2] = std::stoi(line.substr(i));

            if (line[0] == 'x') {
                for (int y = nums[1]; y <= nums[2]; y++) {
                    map[nums[0]][y] = Tile::Clay;
                }
                min_y = std::min(min_y, nums[1]);
                max_y = std::max(max_y, nums[2]);
            }
            else {
                for (int x = nums[1]; x <= nums[2]; x++) {
                    map[x][nums[0]] = Tile::Clay;
                }
                min_y = std::min(min_y, nums[0]);
                max_y = std::max(max_y, nums[0]);
            }

        }

    }

    void do_shared_work() {
        stats = spread_water(500, 0);
    }

    DaySolution solve_part_1() {
        return stats.still + stats.running;
    }

    DaySolution solve_part_2() {
        return stats.still;
    }

};

DaySolver* DaySolver::get_current_day_solver() {
    return new Day17Solver();
}