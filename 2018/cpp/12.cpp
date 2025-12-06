#include "DaySolver.hpp"

#include <list>

class BinaryTreeNode {

    bool value;
    std::unique_ptr<BinaryTreeNode> left;
    std::unique_ptr<BinaryTreeNode> right;

public:

    BinaryTreeNode() { }

    BinaryTreeNode(bool value) : value(value) { }

    void set(bool value) {
        this->value = value;
    }

    operator bool() const {
        return value;
    }

    BinaryTreeNode& operator[](bool i) {
        if (!i) {
            if (!left) left = std::make_unique<BinaryTreeNode>();
            return *left;
        }
        else {
            if (!right) right = std::make_unique<BinaryTreeNode>();
            return *right;
        }
    }

};

using BinaryTree = BinaryTreeNode;

struct Pot {
    uint64_t num;
    bool current;
    bool next;
};

class Day12Solver : public DaySolver {
    
    std::list<Pot> start_state;

    BinaryTree tree;
    std::list<Pot> state;

    bool evaluate(const std::list<Pot>::iterator& it) {

        auto start = it;
        int empty_start_spaces = 2;
        for (auto i = 0; i < 2 && start != state.begin(); i++) {
            --start;
            --empty_start_spaces;
        }

        auto end = it;
        int empty_end_spaces = 3;
        for (auto i = 0; i < 3 && end != state.end(); i++) {
            ++end;
            --empty_end_spaces;
        }

        BinaryTreeNode* node = &tree;
        for (auto i = 0; i < empty_start_spaces; i++) {
            node = &(*node)[false];
        }
        for (auto it = start; it != end; ++it) {
            node = &(*node)[it->current];
        }
        for (auto i = 0; i < empty_end_spaces; i++) {
            node = &(*node)[false];
        }
        
        return *node;

    }

    void advance_gen() {

        for (auto i = 0; i < 2; i++) {
            state.push_front({ state.front().num - 1, false });
            state.push_back({ state.back().num + 1, false });
        }

        for (auto it = state.begin(), end = state.end(); it != end; ++it) {
            it->next = evaluate(it);
        }

        for (Pot& pot : state) {
            pot.current = pot.next;
        }

        while (!state.front().current) {
            state.pop_front();
        }
        while (!state.back().current) {
            state.pop_back();
        }

    }

    inline std::string serialize_state() {
        std::string s;
        for (const Pot& pot : state) {
            s.push_back(pot.current ? '#' : '.');
        }
        return s;
    }

    // inline size_t hash_state() {
    //     return std::hash<std::string>{}(serialize_state());
    // }

protected:

    void process_input() {

        std::istringstream input_stream(puzzle_input);
        std::string line;

        std::getline(input_stream, line, '\n');
        line = line.substr(line.find(':') + 2);
        for (uint64_t i = 0; i < line.length(); i++) {
            start_state.push_back({ i, line.at(i) == '#' });
        }

        std::getline(input_stream, line, '\n');
        while (std::getline(input_stream, line, '\n')) {
            BinaryTreeNode* node = &tree;
            for (int i = 0; i < 5; i++) {
                node = &(*node)[line[i] == '#'];
            }
            node->set(line.back() == '#');
        }

    }

    DaySolution solve_part_1() {

        state = start_state;

        for (int gen = 0; gen < 20; gen++) {
            advance_gen();
        }

        int sum = 0;
        for (const Pot& pot : state) {
            if (pot.current) {
                sum += pot.num;
            }
        }

        return sum;

    }

    DaySolution solve_part_2() {

        state = start_state;

        uint64_t rem_gen = 50000000000;
        while (true) {
            std::string prev = serialize_state();
            advance_gen();
            --rem_gen;
            if (serialize_state() == prev) {
                auto prev_pos = state.front().num;
                advance_gen();
                --rem_gen;
                auto delta_pos = state.front().num - prev_pos;
                for (Pot& pot : state) {
                    pot.num += delta_pos * rem_gen;
                }
                break;
            }
        }

        uint64_t sum = 0;
        for (const Pot& pot : state) {
            if (pot.current) {
                sum += pot.num;
            }
        }

        return sum;

    }

};

DaySolver* DaySolver::get_current_day_solver() {
    return new Day12Solver();
}