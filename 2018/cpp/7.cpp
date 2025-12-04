#include "DaySolver.hpp"

#include <map>

struct Node {

    char name;
    std::vector<Node*> next;
    std::vector<Node*> prev;
    bool completed = false;

    bool in_progress = false;
    int time_left;

    bool available() {
        return std::all_of(
            prev.begin(), prev.end(),
            [](const auto& n) { return n->completed; }
        );
    }

};

class Day7Solver : public DaySolver {

    std::vector<Node*> root_nodes;
    size_t total_node_count;

    std::set<Node*> completed_nodes;

protected:

    std::set<Node*> get_available_nodes() {

        std::set<Node*> available;
        for (const auto& node : root_nodes) {
            if (!node->completed) {
                available.insert(node);
            }
        }
        for (const auto& node : completed_nodes) {
            available.insert(node->next.begin(), node->next.end());
        }

        std::erase_if(available, [](const auto& n) {
            return n->completed || !n->available() || n->in_progress;
        });

        return available;

    }

    void process_input() {

        root_nodes.clear();
        completed_nodes.clear();
        
        std::vector<std::pair<char, char>> data;

        std::istringstream input_stream(puzzle_input);
        std::string line;
        while (std::getline(input_stream, line, '\n')) {
            data.push_back(std::pair(line[5], line[36]));
        }

        std::map<char, Node*> nodes;
        for (const auto& [before, after] : data) {
            if (nodes.find(after) == nodes.end()) {
                nodes[after] = new Node();
            }
            if (nodes.find(before) == nodes.end()) {
                nodes[before] = new Node();
            }
            Node* after_node = nodes[after];
            Node* before_node = nodes[before];
            after_node->prev.push_back(before_node);
            before_node->next.push_back(after_node);
        }

        for (const auto& [name, node] : nodes) {
            node->name = name;
            node->time_left = name - 'A' + 1 + 60;
            if (node->prev.empty()) {
                root_nodes.push_back(node);
            }
        }

        total_node_count = nodes.size();

    }

    DaySolution solve_part_1() {

        std::string order = "";

        while (order.length() != total_node_count) {

            auto available = get_available_nodes();
            
            Node* choice = *std::min_element(
                available.begin(), available.end(),
                [](const auto& n1, const auto& n2) {
                    return n1->name < n2->name;
                }
            );

            choice->completed = true;
            completed_nodes.insert(choice);
            order.push_back(choice->name);

        }
        
        return order;

    }

    DaySolution solve_part_2() {

        if (!(root_nodes.empty() && completed_nodes.empty())) {
            process_input();
        }

        std::set<Node*> in_progress;

        int steps = -1;

        while (completed_nodes.size() != total_node_count) {

            steps++;

            for (Node* node : in_progress) {
                if (--node->time_left == 0) {
                    node->completed = true;
                    completed_nodes.insert(node);
                }
            }
            std::erase_if(in_progress, [](const auto& n) { return n->completed; });

            while (in_progress.size() < 5) {

                auto available = get_available_nodes();

                if (available.empty()) break;
                
                Node* choice = *std::min_element(
                    available.begin(), available.end(),
                    [](const auto& n1, const auto& n2) {
                        return n1->name < n2->name;
                    }
                );

                choice->in_progress = true;
                in_progress.insert(choice);

            }

        }
        
        return steps;

    }

};

DaySolver* DaySolver::get_current_day_solver() {
    return new Day7Solver();
}