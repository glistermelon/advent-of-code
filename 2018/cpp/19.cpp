#include "DaySolver.hpp"

#include <cmath>

class Day19Solver : public DaySolver {

    int solve(int n) {
        int sum = 0;
        for (int d1 = 1, sqrt_n = sqrt(n); d1 <= sqrt_n; d1++) {
            if (n % d1 != 0) continue;
            sum += d1;
            int d2 = n / d1;
            if (d2 != d1) sum += d2;
        }
        return sum;
    }

protected:

    DaySolution solve_part_1() {
        return solve(978);
    }

    DaySolution solve_part_2() {
        return solve(10551378);
    }

};

DaySolver* DaySolver::get_current_day_solver() {
    return new Day19Solver();
}