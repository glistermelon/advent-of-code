#include "DaySolver.hpp"

#include <array>

struct Square {
    int x, y;
    int power;
};

class Day11Solver : public DaySolver {

    int serial_number;

    int map[300][300] = { };

    int calc_square_power(int ox, int oy, int size) {
        int total_power = 0;
        for (int dx = 0; dx < size; dx++) {
            for (int dy = 0; dy < size; dy++) {
                total_power += map[ox + dx][oy + dy];
            }
        }
        return total_power;
    }

protected:

    void process_input() {
        serial_number = std::stoi(puzzle_input);
    }

    void do_shared_work() {
        for (int x = 0; x < 300; x++) {
            for (int y = 0; y < 300; y++) {
                
                x++;
                y++;

                int rack_id = x + 10;
                int power = rack_id * y;
                power += serial_number;
                power *= rack_id;
                power = (power / 100) % 10;
                power -= 5;

                x--;
                y--;

                map[x][y] = power;

            }
        }
    }

    Square move_right(const Square& sq, int size) {

        Square r_sq = sq;
        r_sq.x++;

        for (int dy = 0; dy < size; dy++) {
            r_sq.power -= map[sq.x][sq.y + dy];
            r_sq.power += map[sq.x + size][sq.y + dy];
        }

        return r_sq;

    }

    Square move_down(const Square& sq, int size) {

        Square d_sq = sq;
        d_sq.y++;

        for (int dx = 0; dx < size; dx++) {
            d_sq.power -= map[sq.x + dx][sq.y];
            d_sq.power += map[sq.x + dx][sq.y + size];
        }

        return d_sq;

    }

    Square solve(int size) {

        Square left_sq {
            .x = 0,
            .y = 0,
            .power = calc_square_power(0, 0, size)
        };

        int max_sq_power = left_sq.power;
        int max_sq_power_x = 0;
        int max_sq_power_y = 0;

        for (int dy = 0; dy < 301 - size; dy++) {
            Square sq = left_sq;
            for (int dx = 0; dx < 301 - size; dx++) {
                if (sq.power > max_sq_power) {
                    max_sq_power = sq.power;
                    max_sq_power_x = sq.x;
                    max_sq_power_y = sq.y;
                }
                sq = move_right(sq, size);
            }
            left_sq =  move_down(left_sq, size);
        }

        return { max_sq_power_x + 1, max_sq_power_y + 1, max_sq_power };

    }

    DaySolution solve_part_1() {
        auto sol = solve(3);
        return std::to_string(sol.x) + "," + std::to_string(sol.y);
    }

    DaySolution solve_part_2() {
        Square sol = { };
        int sol_size = 0;
        for (int size = 1; size <= 300; size++) {
            auto size_sol = solve(size);
            if (size_sol.power > sol.power) {
                sol = size_sol;
                sol_size = size;
            }
        }
        return std::to_string(sol.x) + "," + std::to_string(sol.y) + "," + std::to_string(sol_size);
    }

};

DaySolver* DaySolver::get_current_day_solver() {
    return new Day11Solver();
}