#include "DaySolver.hpp"

#include "utils.hpp"
#include "Array2D.hpp"

struct Point {

    int y, x;

    int distance_to(const Point& other) {
        return abs(x - other.x) + abs(y - other.y);
    }

    bool operator<(const Point& other) const {
        if (y != other.y) {
            return y < other.y;
        }
        return x < other.x;
    }

    Point operator+(const Point& other) const {
        return { y + other.y, x + other.x };
    }

};

class Area;

struct Claim {
    Area* area;
    int dist;
    bool tied;
};

using Map = Array2D<Claim*>;

class Area {

    Map& map;
    Point center;

    std::set<Point> inner;
    std::set<Point> outer;
    int outer_dist;

public:

    char label;

    Area(Map& map, Point center) : map(map), center(center) {
        outer.insert(center);
        outer_dist = 0;
    }

    void remove_point(Point p) {
        inner.erase(p);
        outer.erase(p);
    }

    bool can_expand() {
        return !outer.empty();
    }

    void expand() {

        outer_dist++;
        std::set<Point> new_outer;

        for (const Point& outer_p : outer) {

            inner.insert(outer_p);

            for (int d = -1; d <= 1; d += 2) {
                for (int target_x = 0; target_x < 2; target_x++) {

                    int y = outer_p.y, x = outer_p.x;
                    if (target_x) x += d;
                    else y += d;

                    if (y < 0 || x < 0 || y >= map.height() || x >= map.width()) {
                        continue;
                    }

                    Point new_p = { y, x };

                    Claim* claim = map[y][x];
                    
                    if (!claim) {
                        claim = new Claim { this, outer_dist, false };
                        map[y][x] = claim;
                        new_outer.insert(new_p);
                    }
                    else if (claim->area == this) {
                        continue;
                    }
                    else if (outer_dist < claim->dist) {
                        
                        if (claim->area) {
                            claim->area->remove_point(new_p);
                        }

                        claim->area = this;
                        claim->dist = outer_dist;
                        claim->tied = false;

                        new_outer.insert(new_p);

                    }
                    else if (outer_dist == claim->dist) {
                        if (claim->area) {
                            claim->area->remove_point(new_p);
                        }
                        claim->area = nullptr;
                        claim->tied = true;
                    }

                }
            }

        }

        outer = new_outer;

    }

    size_t size() {
        return inner.size() + outer.size();
    }

    Point get_center() {
        return center;
    }

};

class Day6Solver : public DaySolver {

    std::vector<Area*> areas;
    Map map;

protected:

    void print_map() {
        for (int y = 0; y < map.height(); y++) {
            for (int x = 0; x < map.width(); x++) {
                Claim* claim = map[y][x];
                char c = '_';
                if (!claim) c = '0';
                else if (claim->tied) c = '.';
                else if (!claim->area) c = '?';
                else if (claim->area) c = claim->area->label;
                else c = '!';
                std::cout << c;
            }
            std::cout << '\n';
        }
        std::cout << "\n\n\n" << std::endl;
    }

    void process_input() {

        std::vector<Point> points;

        int min_x = INT_MAX;
        int max_x = INT_MIN;
        int min_y = INT_MAX;
        int max_y = INT_MIN;

        std::istringstream input_stream(puzzle_input);
        std::string line;
        while (std::getline(input_stream, line, '\n')) {

            auto parts = split(line, ',');
            int x = std::stoi(parts[0]);
            int y = std::stoi(parts[1].substr(1));

            points.push_back({ y, x });

            min_x = std::min(min_x, x);
            max_x = std::max(max_x, x);
            min_y = std::min(min_y, y);
            max_y = std::max(max_y, y);

        }

        min_x--;
        min_y--;
        max_x++;
        max_y++;

        int w = max_x - min_x + 1;
        int h = max_y - min_y + 1;
        map = Map(h, w);
        for (auto& p : points) {
            p.x -= min_x;
            p.y -= min_y;
            areas.push_back(new Area(map, p));
        }

    }

    DaySolution solve_part_1() {

        for (int i = 0; i < areas.size(); i++) {
            Area* area = areas[i];
            area->label = 'a' + i;
            while (area->can_expand()) {
                area->expand();
            }
        }

        // print_map();

        std::set<Area*> noninfinite_areas(areas.begin(), areas.end());
        for (int y = 0; y < map.height(); y++) {
            noninfinite_areas.erase(map[y][0]->area);
            noninfinite_areas.erase(map[y][map.width() - 1]->area);
        }
        for (int x = 0; x < map.width(); x++) {
            noninfinite_areas.erase(map[0][x]->area);
            noninfinite_areas.erase(map[map.height() - 1][x]->area);
        }

        size_t result = 0;
        char label;
        for (Area* area : noninfinite_areas) {
            auto size = area->size();
            if (size > result) {
                result = size;
                label = area->label;
            }
        }

        return result;

    }

    DaySolution solve_part_2() {
        const auto h = map.height();
        const auto w = map.width();
        Array2D<int> map(h, w);
        for (Area* area : areas) {
            Point c = area->get_center();
            for (int y = 0; y < map.height(); y++) {
                for (int x = 0; x < map.width(); x++) {
                    map[y][x] += c.distance_to({ y, x });
                }
            }
        }
        int result = 0;
        for (int value : map) {
            if (value < 10000) {
                result++;
            }
        }
        return result;
    }

};

DaySolver* DaySolver::get_current_day_solver() {
    return new Day6Solver();
}