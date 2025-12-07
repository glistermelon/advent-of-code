#include "DaySolver.hpp"

#include "utils.hpp"

#include <map>
#include <array>

enum class Opcode {
    addr,
    addi,
    mulr,
    muli,
    banr,
    bani,
    borr,
    bori,
    setr,
    seti,
    gtir,
    gtri,
    gtrr,
    eqir,
    eqri,
    eqrr
};

struct Device {

    int registers[4] = { };

    void op_addr(int A, int B, int C) {
        registers[C] = registers[A] + registers[B];
    }

    void op_addi(int A, int B, int C) {
        registers[C] = registers[A] + B;
    }

    void op_mulr(int A, int B, int C) {
        registers[C] = registers[A] * registers[B];
    }

    void op_muli(int A, int B, int C) {
        registers[C] = registers[A] * B;
    }

    void op_banr(int A, int B, int C) {
        registers[C] = registers[A] & registers[B];
    }

    void op_bani(int A, int B, int C) {
        registers[C] = registers[A] & B;
    }

    void op_borr(int A, int B, int C) {
        registers[C] = registers[A] | registers[B];
    }

    void op_bori(int A, int B, int C) {
        registers[C] = registers[A] | B;
    }

    void op_setr(int A, int B, int C) {
        registers[C] = registers[A];
    }

    void op_seti(int A, int B, int C) {
        registers[C] = A;
    }

    void op_gtir(int A, int B, int C) {
        registers[C] = A > registers[B];
    }

    void op_gtri(int A, int B, int C) {
        registers[C] = registers[A] > B;
    }

    void op_gtrr(int A, int B, int C) {
        registers[C] = registers[A] > registers[B];
    }

    void op_eqir(int A, int B, int C) {
        registers[C] = A == registers[B];
    }

    void op_eqri(int A, int B, int C) {
        registers[C] = registers[A] == B;
    }

    void op_eqrr(int A, int B, int C) {
        registers[C] = registers[A] == registers[B];
    }

    void do_instr(Opcode op, int in1, int in2, int out) {
        switch (op) {
            case Opcode::addr:
                op_addr(in1, in2, out);
                break;
            case Opcode::addi:
                op_addi(in1, in2, out);
                break;
            case Opcode::mulr:
                op_mulr(in1, in2, out);
                break;
            case Opcode::muli:
                op_muli(in1, in2, out);
                break;
            case Opcode::banr:
                op_banr(in1, in2, out);
                break;
            case Opcode::bani:
                op_bani(in1, in2, out);
                break;
            case Opcode::borr:
                op_borr(in1, in2, out);
                break;
            case Opcode::bori:
                op_bori(in1, in2, out);
                break;
            case Opcode::setr:
                op_setr(in1, in2, out);
                break;
            case Opcode::seti:
                op_seti(in1, in2, out);
                break;
            case Opcode::gtir:
                op_gtir(in1, in2, out);
                break;
            case Opcode::gtri:
                op_gtri(in1, in2, out);
                break;
            case Opcode::gtrr:
                op_gtrr(in1, in2, out);
                break;
            case Opcode::eqir:
                op_eqir(in1, in2, out);
                break;
            case Opcode::eqri:
                op_eqri(in1, in2, out);
                break;
            case Opcode::eqrr:
                op_eqrr(in1, in2, out);
                break;
        }
    }

};

struct Record {
    int before[4];
    int instr[4];
    int after[4];
};

class Day16Solver : public DaySolver {

    std::vector<Record> records;
    std::vector<std::array<int, 4>> instructions;

protected:

    void process_input() {

        std::vector<std::string> input_parts = split(puzzle_input, "\n\n\n\n");

        std::istringstream input_stream(input_parts[0]);
        std::string line;
        Record r;
        while (std::getline(input_stream, line, '\n')) {
            if (line.empty()) {
                records.push_back(r);
            }
            else if (line.at(0) == 'B' || line.at(0) == 'A') {
                bool before = line.at(0) == 'B';
                line = line.substr(9, line.length() - 10);
                line.erase(std::remove(line.begin(), line.end(), ' '));
                std::vector<std::string> nums = split(line, ',');
                for (int i = 0; i < 4; i++) {
                    int n = std::stoi(nums[i]);
                    if (before) r.before[i] = n;
                    else r.after[i] = n;
                }
            }
            else if (std::isdigit(line.at(0))) {
                std::vector<std::string> nums = split(line, ' ');
                for (int i = 0; i < 4; i++) {
                    r.instr[i] = std::stoi(nums[i]);
                }
            }
        }
        records.push_back(r);

        input_stream = std::istringstream(input_parts[1]);
        while (std::getline(input_stream, line, '\n')) {
            std::array<int, 4> instr;
            std::vector<std::string> nums = split(line, ' ');
            for (int i = 0; i < 4; i++) {
                instr[i] = std::stoi(nums[i]);
            }
            instructions.push_back(instr);
        }

    }

    DaySolution solve_part_1() {
        int count = 0;
        for (const auto& r : records) {
            Device dev;
            int matching = 0;
            for (Opcode op = Opcode::addr; op <= Opcode::eqrr; op = (Opcode)((int)op + 1)) {
                memcpy(dev.registers, r.before, sizeof(dev.registers));
                dev.do_instr(op, r.instr[1], r.instr[2], r.instr[3]);
                if (memcmp(dev.registers, r.after, sizeof(dev.registers)) == 0) {
                    if (++matching >= 3) {
                        count++;
                        break;
                    }
                }
            }
        }
        return count;
    }

    DaySolution solve_part_2() {
        
        std::map<int, Opcode> opcodes;

        std::set<int> opcode_range;
        for (const auto& r : records) {
            opcode_range.insert(r.instr[0]);
        }

        std::map<int, std::vector<Opcode>> possible_opcodes;

        for (int num : opcode_range) {
            for (Opcode op = Opcode::addr; op <= Opcode::eqrr; op = (Opcode)((int)op + 1)) {
                if (std::all_of(
                    records.begin(), records.end(),
                    [&](const Record& r) {
                        if (r.instr[0] != num) return true;
                        Device dev;
                        memcpy(dev.registers, r.before, sizeof(dev.registers));
                        dev.do_instr(op, r.instr[1], r.instr[2], r.instr[3]);
                        return memcmp(dev.registers, r.after, sizeof(dev.registers)) == 0;
                    }
                )) {
                    possible_opcodes[num].push_back(op);
                }
            }
        }

        while (std::any_of(
            opcode_range.begin(), opcode_range.end(),
            [&](int n) {
                return opcodes.find(n) == opcodes.end();
            }
        )) {
            int det_num;
            Opcode det_op;
            for (const auto& [n, ops] : possible_opcodes) {
                if (ops.size() == 1) {
                    det_num = n;
                    det_op = ops[0];
                    break;
                }
            }
            opcodes[det_num] = det_op;
            possible_opcodes.erase(det_num);
            for (auto& [n, ops] : possible_opcodes) {
                auto it = std::find(ops.begin(), ops.end(), det_op);
                if (it != ops.end()) {
                    ops.erase(it);
                }
            }
        }

        Device dev { };
        for (const std::array<int, 4>& instr : instructions) {
            dev.do_instr(opcodes[instr[0]], instr[1], instr[2], instr[3]);
        }

        return dev.registers[0];

    }



};

DaySolver* DaySolver::get_current_day_solver() {
    return new Day16Solver();
}