#include "DaySolver.hpp"

#include "utils.hpp"

class Day9Solver : public DaySolver {

    size_t players;
    size_t marbles;

protected:

    void process_input() {
        std::vector<std::string> parts = split(puzzle_input, ' ');
        players = std::stoi(parts[0]);
        marbles = std::stoi(parts[6]);
    }

    void advance_iter(std::list<size_t>& circle, std::list<size_t>::iterator& it, size_t steps) {
        for (auto i = 0; i < steps; i++) {
            if (++it == circle.end()) {
                it = circle.begin();
            }
        }
    }

    void reverse_iter(std::list<size_t>& circle, std::list<size_t>::iterator& it, size_t steps) {
        for (auto i = 0; i < steps; i++) {
            if (it == circle.begin()) {
                it = circle.end();
            }
            --it;
        }
    }

    size_t calc_high_score(size_t marbles) {

        std::vector<size_t> scores(players);
        std::list<size_t> circle = { 0 };
        auto current_it = circle.begin();
        int player = 0;
        
        for (int marble = 1; marble <= marbles; marble++) {

            if (marble % 23 != 0) {
                advance_iter(circle, current_it, 2);
                current_it = circle.insert(current_it, marble);
            }
            else {
                scores[player] += marble;
                reverse_iter(circle, current_it, 7);
                scores[player] += *current_it;
                current_it = circle.erase(current_it);
                if (current_it == circle.end()) current_it = circle.begin();
            }

            player++;
            if (player == players) player = 0;

        }

        return *std::max_element(scores.begin(), scores.end());

    }

    DaySolution solve_part_1() {
        return calc_high_score(marbles);
    }

    DaySolution solve_part_2() {
        return calc_high_score(marbles * 100);
    }

};

DaySolver* DaySolver::get_current_day_solver() {
    return new Day9Solver();
}