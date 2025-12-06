#include "DaySolver.hpp"

#include <optional>

class Day14Solver : public DaySolver {

    int offset;

    std::vector<uint8_t> scores = { 3, 7 };
    size_t i0 = 0, i1 = 1;

    uint8_t sequence_at(size_t idx) {

        while (idx >= scores.size()) {

            int sum = scores[i0] + scores[i1];

            if (sum > 9) {
                scores.push_back(sum / 10);
                scores.push_back(sum % 10);
            }
            else {
                scores.push_back(sum);
            }

            i0 = (i0 + 1 + scores[i0]) % scores.size();
            i1 = (i1 + 1 + scores[i1]) % scores.size();
        }

        return scores[idx];

    }

    bool window_matches_input(const std::vector<uint8_t>& window) {
        for (int i = 0; i < puzzle_input.size(); i++) {
            if ('0' + window[i] != puzzle_input[i]) {
                return false;
            }
        }
        return true;
    }

protected:

    void process_input() {
        offset = std::stoi(puzzle_input);
    }

    DaySolution solve_part_1() {
        std::string result = "";
        for (auto i = 0; i < 10; i++) {
            result.push_back('0' + sequence_at(offset + i));
        }
        return result;
    }

    DaySolution solve_part_2() {

        std::vector<uint8_t> window;

        int i;
        
        for (i = 0; i < puzzle_input.length(); i++) {
            window.push_back(sequence_at(i));
        }

        int recipes = 0;

        while (!window_matches_input(window)) {
            window.erase(window.begin());
            window.push_back(sequence_at(i++));
            recipes++;
        }

        return recipes;

    }

};

DaySolver* DaySolver::get_current_day_solver() {
    return new Day14Solver();
}