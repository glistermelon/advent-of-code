#include "DaySolver.hpp"

#include "utils.hpp"

class Day8Solver : public DaySolver {

    std::vector<int> input_data;

    std::vector<int>::iterator calc_metadata_sum(int& sum, std::vector<int>::iterator it) {

        int child_count = *(it++);
        int metadata_count = *(it++);

        for (int i = 0; i < child_count; i++) {
            it = calc_metadata_sum(sum, it);
        }

        for (int i = 0; i < metadata_count; i++) {
            sum += *(it++);
        }

        return it;

    }

    std::vector<int>::iterator calc_value_sum(int& sum, std::vector<int>::iterator it) {

        int child_count = *(it++);
        int metadata_count = *(it++);

        int* child_values = new int[child_count];
        memset(child_values, 0, child_count * sizeof(int));
        for (int i = 0; i < child_count; i++) {
            it = calc_value_sum(child_values[i], it);
        }

        if (child_count == 0) {
            for (int i = 0; i < metadata_count; i++) {
                sum += *(it++);
            }
        }
        else {
            for (int i = 0; i < metadata_count; i++) {
                int idx = *(it++) - 1;
                if (idx < child_count) {
                    sum += child_values[idx];
                }
            }
        }

        delete[] child_values;

        return it;

    }

protected:

    void process_input() {
        input_data = map_to<int>(split(puzzle_input, ' '), [](const auto& s) { return std::stoi(s); });
    }

    DaySolution solve_part_1() {
        int metadata_sum = 0;
        calc_metadata_sum(metadata_sum, input_data.begin());
        return metadata_sum;
    }

    DaySolution solve_part_2() {
        int value_sum = 0;
        calc_value_sum(value_sum, input_data.begin());
        return value_sum;
    }

};

DaySolver* DaySolver::get_current_day_solver() {
    return new Day8Solver();
}