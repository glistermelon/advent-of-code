#include "DaySolver.hpp"

#include "Array2D.hpp"
#include "utils.hpp"

#include <map>
#include <optional>

enum class Dir {
    Up,
    Right,
    Down,
    Left
};

std::optional<Dir> dirFromChar(char c) {
    switch (c) {
        case '^':
            return Dir::Up;
        case '>':
            return Dir::Right;
        case 'v':
        case 'V':
            return Dir::Down;
        case '<':
            return Dir::Left;
        default:
            return { };
    }
}

void turnRight(Dir& dir) {
    dir = (Dir)(((int)dir + 1) % 4);
}

void turnLeft(Dir& dir) {
    dir = (Dir)(((int)dir + 3) % 4);
}

struct Pos {

    int x, y;

    bool operator==(const Pos& other) const {
        return x == other.x && y == other.y;
    }

    bool operator<(const Pos& other) const {
        if (y < other.y) return true;
        if (y > other.y) return false;
        return x < other.x;
    }

    operator std::string() const {
        return std::to_string(x) + "," + std::to_string(y);
    }

};

struct Cart {

    Pos pos;
    Dir dir;
    uint8_t counter = 0;

    bool operator<(const Cart& other) const {
        return pos < other.pos;
    }

};

class Day13Solver : public DaySolver {

    std::vector<std::string> map;

    std::vector<Cart> carts;
    Array2D<bool> cart_map;

    void reset_carts() {

        cart_map = Array2D<bool>(map.size(), map.at(0).size());
        carts.clear();
        
        for (int y = 0; y < map.size(); y++) {
            const auto& row = map[y];
            for (int x = 0; x < row.size(); x++) {
                if (auto dir = dirFromChar(row[x]); dir.has_value()) {
                    carts.push_back({ { x, y }, dir.value() });
                    cart_map[y][x] = true;
                }
            }
        }

    }

    std::vector<Pos> tick() {

        std::vector<Pos> crashes;

        std::sort(carts.begin(), carts.end());

        std::vector<Cart> queue(carts.rbegin(), carts.rend());
        carts.clear();

        while (!queue.empty()) {

            Cart cart = queue.back();
            queue.pop_back();

            cart_map[cart.pos.y][cart.pos.x] = false;

            const char& tile = map[cart.pos.y][cart.pos.x];

            if (tile == '/') {
                if (cart.dir == Dir::Up || cart.dir == Dir::Down) {
                    turnRight(cart.dir);
                }
                else {
                    turnLeft(cart.dir);
                }
            }
            else if (tile == '\\') {
                if (cart.dir == Dir::Up || cart.dir == Dir::Down) {
                    turnLeft(cart.dir);
                }
                else {
                    turnRight(cart.dir);
                }
            }
            else if (tile == '+') {
                if (cart.counter == 0) {
                    turnLeft(cart.dir);
                }
                else if (cart.counter == 2) {
                    turnRight(cart.dir);
                }
                if (++cart.counter == 3) {
                    cart.counter = 0;
                }
            }

            switch (cart.dir) {
                case Dir::Up:
                    cart.pos.y--;
                    break;
                case Dir::Right:
                    cart.pos.x++;
                    break;
                case Dir::Down:
                    cart.pos.y++;
                    break;
                case Dir::Left:
                    cart.pos.x--;
                    break;
            }

            if (cart_map[cart.pos.y][cart.pos.x]) {
                crashes.push_back(cart.pos);
                std::erase_if(carts, [&](const Cart& c) { return c.pos == cart.pos; });
                std::erase_if(queue, [&](const Cart& c) { return c.pos == cart.pos; });
                cart_map[cart.pos.y][cart.pos.x] = false;
            }
            else {
                cart_map[cart.pos.y][cart.pos.x] = true;
                carts.push_back(cart);
            }

        }

        return crashes;

    }

protected:

    void process_input() {
        map = split(puzzle_input, '\n');
    }

    DaySolution solve_part_1() {
        reset_carts();
        while (true) {
            auto crashes = tick();
            if (!crashes.empty()) {
                return (std::string)crashes[0];
            }   
        }
    }

    DaySolution solve_part_2() {
        reset_carts();
        while (carts.size() != 1) {
            tick();
        }
        return (std::string)carts[0].pos;
        
    }

};

DaySolver* DaySolver::get_current_day_solver() {
    return new Day13Solver();
}