#include "DaySolver.hpp"

#include "Array2D.hpp"
#include "utils.hpp"

#include <deque>
#include <array>
#include <map>

enum Tile {
    Empty,
    Wall,
    Goblin,
    Elf
};

struct Point {

    int x, y;

    bool operator==(const Point& other) const {
        return x == other.x && y == other.y;
    }

    bool operator<(const Point& other) const {
        if (y < other.y) return true;
        if (y > other.y) return false;
        return x < other.x;
    }

    std::array<Point, 4> adj() const {
        return {
            Point { x, y - 1 },
            Point { x + 1, y },
            Point { x, y + 1 },
            Point { x - 1, y }
        };
    }

};

struct Entity {

    bool elf;
    Point pos;
    int hp;
    int ap;

    bool operator<(const Entity& other) const {
        return pos < other.pos;
    }

};

class Game {

    Array2D<Tile> map;
    std::vector<Entity*> entities;
    int rounds_completed = 0;
    bool any_elf_died = false;

    std::optional<Point> choose_path(Point root, const std::set<Point>& targets) const {

        std::deque<Point> queue = { root };
        std::map<Point, int> distances = { { root, 0 } };
        std::map<Point, std::set<Point>> first_steps = {};

        for (Point p : root.adj()) {
            first_steps[p] = { p };
        }

        std::set<Point> explored_targets;
        int min_target_distance = INT_MAX;

        while (!queue.empty()) {
            const Point& p = queue.front();
            const int p_dist = distances[p];
            if (p_dist > min_target_distance) break;
            if (targets.contains(p)) {
                explored_targets.insert(p);
                min_target_distance = p_dist;
            }
            const auto& p_first_steps = first_steps[p];
            for (const Point& a : p.adj()) {
                if (map[a.y][a.x] != Empty) continue;
                if (distances.find(a) == distances.end()) {
                    queue.push_back(a);
                    distances[a] = p_dist + 1;
                    if (p_dist != 0) first_steps[a] = p_first_steps;
                }
                first_steps[a].insert(p_first_steps.begin(), p_first_steps.end());
            }
            queue.pop_front();
        }

        if (explored_targets.empty()) {
            return { };
        }

        Point choice = *std::min_element(explored_targets.begin(), explored_targets.end());
        const auto& choice_first_steps = first_steps[choice];
        return *std::min_element(choice_first_steps.begin(), choice_first_steps.end());

    }

    Entity* select_target(const Entity& entity) {
        Entity* target = nullptr;
        for (Point adj_pos : entity.pos.adj()) {
            if (map[adj_pos.y][adj_pos.x] != (entity.elf ? Goblin : Elf)) {
                continue;
            }
            for (Entity* other : entities) {
                if (other->pos != adj_pos) continue;
                if (
                    !target || other->hp < target->hp ||
                    (other->hp == target->hp && other->pos < target->pos)
                ) {
                    target = other;
                }
            }
        }
        return target;
    }

    bool do_turn() {

        std::sort(entities.begin(), entities.end(), [](Entity* a, Entity* b) {
            return a->pos < b->pos;
        });

        std::deque<Entity*> entity_queue(entities.begin(), entities.end());

        while (!entity_queue.empty()) {

            Entity* entity = entity_queue.front();

            if (std::all_of(
                entities.begin(), entities.end(),
                [&](const auto& e) { return e->elf == entities.front()->elf; }
            )) {
                return false;
            }

            Entity* target = select_target(*entity);

            if (!target) {

                std::set<Point> target_adj;
                for (const auto& other : entities) {
                    if (entity->elf == other->elf) continue;
                    for (auto p : other->pos.adj()) {
                        if (map[p.y][p.x] == Empty) {
                            target_adj.insert(p);
                        }
                    }
                }

                std::optional<Point> move_choice = choose_path(entity->pos, target_adj);
                if (move_choice.has_value()) {
                    map[entity->pos.y][entity->pos.x] = Empty;
                    entity->pos = move_choice.value();
                    map[entity->pos.y][entity->pos.x] = entity->elf ? Elf : Goblin;
                }

                target = select_target(*entity);

            }

            if (target) {
                target->hp -= entity->ap;
                if (target->hp <= 0) {
                    if (target->elf) any_elf_died = true;
                    for (auto it = entities.begin(), end = entities.end(); it != end; it++) {
                        if (*it == target) {
                            entities.erase(it);
                            break;
                        }
                    }
                    for (auto it = entity_queue.begin(), end = entity_queue.end(); it != end; it++) {
                        if (*it == target) {
                            entity_queue.erase(it);
                            break;
                        }
                    }
                    map[target->pos.y][target->pos.x] = Empty;
                }
            }

            entity_queue.pop_front();

        }

        return true;

    }

    int total_hp_left() {
        int sum = 0;
        for (const auto& entity : entities) {
            sum += entity->hp;
        }
        return sum;
    }

protected:

    void print_map() {
        for (int y = 0; y < map.height(); y++) {
            auto row = map[y];
            for (int x = 0; x < map.width(); x++) {
                char c;
                switch (row[x]) {
                    case Empty:
                        c = '.';
                        break;
                    case Wall:
                        c = '#';
                        break;
                    case Elf:
                        c = 'E';
                        break;
                    case Goblin:
                        c = 'G';
                        break;
                    default:
                        c = '?';
                        break;
                }
                std::cout << c;
            }
            std::cout << std::endl;
        }
    }

public:

    Game(std::string puzzle_input, int elf_ap) {

        std::vector<std::string> lines = split(puzzle_input, '\n');

        map = Array2D<Tile>(lines.size(), lines[0].size());
        size_t y = 0;
        for (const auto& line : lines) {
            for (size_t x = 0; x < line.length(); x++) {
                Tile tile;
                switch (line[x]) {
                    case '.':
                        tile = Empty;
                        break;
                    case '#':
                        tile = Wall;
                        break;
                    case 'G':
                        tile = Goblin;
                        break;
                    case 'E':
                        tile = Elf;
                        break;
                }
                map[y][x] = tile;
            }
            y++;
        }

        const int w = (int)map.width();
        const int h = (int)map.height();
        for (int y = 0; y < h; y++) {
            auto row = map[y];
            for (int x = 0; x < w; x++) {
                Tile tile = row[x];
                if (tile == Empty || tile == Wall) {
                    continue;
                }
                entities.push_back(new Entity {
                    tile == Elf,
                    { x, y },
                    200,
                    tile == Elf ? elf_ap : 3
                });
            }
        }

    }

    void simulate_until_end() {
        while (do_turn()) rounds_completed++;
    }

    bool does_elf_die() {
        while (!any_elf_died && do_turn()) rounds_completed++;
        return any_elf_died;
    }

    int outcome() {
        return rounds_completed * total_hp_left();
    }

};

class Day15Solver : public DaySolver {

protected:

    DaySolution solve_part_1() {
        Game g(puzzle_input, 3);
        g.simulate_until_end();
        return g.outcome();
    }

    DaySolution solve_part_2() {
        int ap = 3;
        while (true) {
            Game g(puzzle_input, ap);
            if (g.does_elf_die()) {
                ap++;
                continue;
            }
            return g.outcome();
        }
    }

};

DaySolver* DaySolver::get_current_day_solver() {
    return new Day15Solver();
}