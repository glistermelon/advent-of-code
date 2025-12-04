#include "DaySolver.hpp"

class Day0Solver : public DaySolver {

protected:

    void process_input() {

        std::istringstream input_stream(puzzle_input);
        std::string line;
        while (std::getline(input_stream, line, '\n')) {

        }

    }

    void do_shared_work() {
        std::cout << "doing shared work for template day" << std::endl;
    }

    DaySolution solve_part_1() {
        return "part 1 solution";
    }

    DaySolution solve_part_2() {
        return 69420;
    }

};

DaySolver* DaySolver::get_current_day_solver() {
    return new Day0Solver();
}