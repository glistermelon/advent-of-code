#include "DaySolver.hpp"

std::ostream& operator<<(std::ostream& os, const DaySolution& sol) {
    switch (sol.type) {
        case DaySolutionType::INT64:
            os << sol.int64_val;
            break;
        case DaySolutionType::STR:
            os << sol.str_val;
            break;
    }
    return os;
}

void DaySolver::process_input() { }
void DaySolver::do_shared_work() { }
DaySolution DaySolver::solve_part_1() { return ""; }
DaySolution DaySolver::solve_part_2() { return ""; }

void DaySolver::run() {
    std::cout << "Running solver for day " << this->get_day_number() << std::endl;
    puzzle_input = get_puzzle_input(this->get_day_number());
    puzzle_input.erase(std::remove(puzzle_input.begin(), puzzle_input.end(), '\r'));
    this->process_input();
    this->do_shared_work();
    std::cout << "Part 1: " << this->solve_part_1() << std::endl;
    std::cout << "Part 2: " << this->solve_part_2() << std::endl;
}

void DaySolver::run_current_day() {
    get_current_day_solver()->run();
}