#include "include.hpp"

std::string solve_part_1(std::string puzzle_input) {

    std::istringstream puzzle_input_istream(puzzle_input);
    std::string line;
    int sum = 0;
    while (std::getline(puzzle_input_istream, line, '\n')) {
        sum += std::stoi(line);
    }
    return std::to_string(sum);
    
}

std::string solve_part_2(std::string puzzle_input) {

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

int main() {
    
    std::string puzzle_input = get_puzzle_input(1);
    std::cout << solve_part_1(puzzle_input) << std::endl;
    std::cout << solve_part_2(puzzle_input) << std::endl;
    
}