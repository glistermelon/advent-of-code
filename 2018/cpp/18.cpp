#include "DaySolver.hpp"

enum class Tile {
    Open,
    Wooded,
    Lumberyard
};

class Day18Solver : public DaySolver {

    Tile map[300][300] = { };
    int max_x = 0;
    int max_y = 0;

    bool check_adj(int x, int y, Tile t, int c) {
        int counter = 0;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                if (x + dx < 0 || x + dx > max_x) continue;
                if (y + dy < 0 || y + dy > max_y) continue;
                if (map[x + dx][y + dy] == t) {
                    counter++;
                    if (counter == c) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    void tick() {
        Tile new_map[300][300];
        for (int x = 0; x <= max_x; x++) {
            for (int y = 0; y <= max_y; y++) {
                switch (map[x][y]) {
                    case Tile::Open:
                        if (check_adj(x, y, Tile::Wooded, 3)) {
                            new_map[x][y] = Tile::Wooded;
                        }
                        else {
                            new_map[x][y] = Tile::Open;
                        }
                        break;
                    case Tile::Wooded:
                        if (check_adj(x, y, Tile::Lumberyard, 3)) {
                            new_map[x][y] = Tile::Lumberyard;
                        }
                        else {
                            new_map[x][y] = Tile::Wooded;
                        }
                        break;
                    case Tile::Lumberyard:
                        if (!(
                            check_adj(x, y, Tile::Lumberyard, 1) &&
                            check_adj(x, y, Tile::Wooded, 1)
                        )) {
                            new_map[x][y] = Tile::Open;
                        }
                        else {
                            new_map[x][y] = Tile::Lumberyard;
                        }
                        break;
                }
            }
        }
        memcpy(map, new_map, sizeof(map));
    }

    std::string map_string() {
        std::stringstream ss;
        for (int y = 0; y <= max_y; y++) {
            for (int x = 0; x <= max_x; x++) {
                char c = '?';
                switch (map[x][y]) {
                    case Tile::Open:
                        c = '.';
                        break;
                    case Tile::Wooded:
                        c = '|';
                        break;
                    case Tile::Lumberyard:
                        c = '#';
                        break;
                }
                ss << c;
            }
        }
        return ss.str();
    }

protected:

    void process_input() {

        std::istringstream input_stream(puzzle_input);
        std::string line;
        int y = 0;
        while (std::getline(input_stream, line, '\n')) {
            max_y = std::max(max_y, y);
            for (int x = 0; x < line.length(); x++) {
                Tile t;
                switch (line[x]) {
                    case '.':
                        t = Tile::Open;
                        break;
                    case '|':
                        t = Tile::Wooded;
                        break;
                    case '#':
                        t = Tile::Lumberyard;
                        break;
                }
                map[x][y] = t;
                max_x = std::max(max_x, x);
            }
            y++;
        }

    }

    DaySolution solve_part_1() {

        process_input();

        for (int i = 0; i < 10; i++) {
            tick();
        }

        int wooded = 0;
        int lumberyards = 0;
        for (int x = 0; x <= max_x; x++) {
            for (int y = 0; y <= max_y; y++) {
                 if (map[x][y] == Tile::Wooded) {
                    wooded++;
                 }
                 else if (map[x][y] == Tile::Lumberyard) {
                    lumberyards++;
                 }
            }
        }

        return wooded * lumberyards;
        
    }

    DaySolution solve_part_2() {

        process_input();

        std::vector<std::string> history = { map_string() };
        size_t cycle_i;
        size_t cycle_j;
        int rem_ticks;

        for (int i = 0; i < 1000000000; i++) {
            tick();
            std::string s = map_string();
            if (auto it = std::find(history.begin(), history.end(), s); it != history.end()) {
                cycle_i = it - history.begin();
                cycle_j = i;
                rem_ticks = 1000000000 - (i + 1);
                break;
            }
            history.push_back(s);
        }

        rem_ticks %= cycle_j - cycle_i + 1;
        for (int i = 0; i < rem_ticks; i++) {
            tick();
        }

        int wooded = 0;
        int lumberyards = 0;
        for (int x = 0; x <= max_x; x++) {
            for (int y = 0; y <= max_y; y++) {
                 if (map[x][y] == Tile::Wooded) {
                    wooded++;
                 }
                 else if (map[x][y] == Tile::Lumberyard) {
                    lumberyards++;
                 }
            }
        }

        return wooded * lumberyards;

    }

};

DaySolver* DaySolver::get_current_day_solver() {
    return new Day18Solver();
}