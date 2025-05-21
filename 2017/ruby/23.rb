require_relative 'solver'

require 'prime'

def numeric?(s)
  s.match?(/[[:digit:]]/)
end

class Day23 < Solver

  def solve_part_1

    registers = Hash.new { |m, k| m[k] = 0 }
    instructions = get_input_lines.to_a
    pos = 0
    times = 0

    loop do

      instr = instructions[pos]
      return times if instr.nil?

      instr = instr.split ' '
      opcode = instr[0]
      reg = instr[1]
      val = nil
      val = numeric?(instr[2]) ? instr[2].to_i : registers[instr[2]] if opcode != 'snd' && opcode != 'rcv'

      case opcode
      when 'set'
        registers[reg] = val
      when 'sub'
        registers[reg] -= val
      when 'mul'
        registers[reg] *= val
        times += 1
      when 'jnz'
        pos += val - 1 unless (numeric?(reg) ? reg.to_i : registers[reg]).zero?
      end

      pos += 1

    end

  end

  def solve_part_2

    105_700.step(122_700, 17).map { |n| Prime.prime?(n) ? 0 : 1 }.sum

  end

end
