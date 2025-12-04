#include "DaySolver.hpp"

class Day2Solver : public DaySolver {

protected:

    DaySolution solve_part_1() {

        std::istringstream istream(puzzle_input);
        std::string line;
        int doubles = 0, triples = 0;
        while (std::getline(istream, line, '\n')) {
            int counts[26] = {};
            for (char c : line) {
                counts[c - 'a']++;
            }
            bool has_double = false, has_triple = false;
            for (int i = 0; i < 26; i++) {
                if (counts[i] == 2) has_double = true;
                if (counts[i] == 3) has_triple = true;
            }
            if (has_double) doubles++;
            if (has_triple) triples++;
        }
        return std::to_string(doubles * triples);

    }

    DaySolution solve_part_2() {

        std::istringstream istream(puzzle_input);
        std::string line;
        std::vector<std::string> lines;
        while (std::getline(istream, line, '\n')) {
            lines.push_back(line);
        }

        for (auto it1 = lines.begin() + 1; it1 != lines.end(); it1++) {
            std::string line1 = *it1;
            for (auto it2 = lines.begin(); it2 < it1; it2++) {
                std::string line2 = *it2;
                std::vector<int> differ_indices;
                for (int i = 0; i < line1.length(); i++) {
                    if (line1[i] != line2[i]) {
                        differ_indices.push_back(i);
                    }
                }
                if (differ_indices.size() == 1) {
                    auto erase_it = line1.begin() + differ_indices[0];
                    line1.erase(erase_it, erase_it + 1);
                    return line1;
                }
            }
        }

        return "";

    }

};

DaySolver* DaySolver::get_current_day_solver() {
    return new Day2Solver();
}