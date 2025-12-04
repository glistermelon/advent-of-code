#include "DaySolver.hpp"

class Day1Solver : public DaySolver {

protected:

    DaySolution solve_part_1() {

        std::istringstream puzzle_input_istream(puzzle_input);
        std::string line;
        int sum = 0;
        while (std::getline(puzzle_input_istream, line, '\n')) {
            sum += std::stoi(line);
        }
        return std::to_string(sum);
        
    }

    DaySolution solve_part_2() {

        std::istringstream puzzle_input_istream(puzzle_input);
        std::string line;
        std::vector<int> deltas;
        while (std::getline(puzzle_input_istream, line, '\n')) {
            deltas.push_back(std::stoi(line));
        }

        int sum = 0;
        std::set<int> history = { sum };
        while (true) {
            for (int delta : deltas) {
                sum += delta;
                if (history.count(sum)) {
                    return std::to_string(sum);
                }
                history.insert(sum);
            }
        }

    }

};

DaySolver* DaySolver::get_current_day_solver() {
    return new Day1Solver();
}