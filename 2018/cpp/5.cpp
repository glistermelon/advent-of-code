#include "DaySolver.hpp"

struct Unit {
    char type;
    bool polarity;
};

class Day5Solver : public DaySolver {

    std::vector<Unit> polymer;

protected:

    void process_input() {
        for (char c : puzzle_input) {
            if (std::isalpha(c)) {
                polymer.push_back(Unit { (char)std::tolower(c), (bool)std::isupper(c) });
            }
        }
    }

    size_t optimize_polymer(char remove_type) {
        std::vector<Unit> polymer = this->polymer;
        std::erase_if(polymer, [&](Unit u) { return u.type == remove_type; });
        while (true) {
            bool any_reactions = false;
            for (int i = 0; i < polymer.size() - 1;) {
                const Unit& u1 = polymer[i];
                const Unit& u2 = polymer[i + 1];
                if (u1.type == u2.type && u1.polarity != u2.polarity) {
                    polymer.erase(polymer.begin() + i, polymer.begin() + i + 2);
                    any_reactions = true;
                }
                else {
                    i++;
                }
            }
            if (!any_reactions) {
                break;
            }
        }
        return polymer.size();
    }

    DaySolution solve_part_1() {
        return optimize_polymer(0);
    }

    DaySolution solve_part_2() {
        size_t optimal = UINT64_MAX;
        for (char type = 'a'; type <= 'z'; type++) {
            optimal = std::min(optimal, optimize_polymer(type));
        }
        return optimal;
    }

};

DaySolver* DaySolver::get_current_day_solver() {
    return new Day5Solver();
}