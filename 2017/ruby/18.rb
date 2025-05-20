require_relative 'solver'

def numeric?(s)
  s.match? /[[:digit:]]/
end

class Day18 < Solver

  def solve_part_1

    registers = Hash.new { |m, k| m[k] = 0 }
    sound = nil
    instructions = get_input_lines.to_a
    pos = 0

    while true

      instr = instructions[pos]

      instr = instr.split ' '
      opcode = instr[0]
      reg = instr[1]
      val = nil
      val = numeric?(instr[2]) ? instr[2].to_i : registers[instr[2]] if opcode != 'snd' && opcode != 'rcv'

      case opcode
      when 'snd'
        sound = registers[reg]
      when 'set'
        registers[reg] = val
      when 'add'
        registers[reg] += val
      when 'mul'
        registers[reg] *= val
      when 'mod'
        registers[reg] %= val
      when 'rcv'
        return sound if registers[reg] != 0
      when 'jgz'
        pos += val if registers[reg] > 0
      end

      pos += 1 unless opcode == 'jgz' && registers[reg] > 0

    end

  end

  def solve_part_2

    registers = [Hash.new { |m, k| m[k] = 0 }, Hash.new { |m, k| m[k] = 0 }]
    registers[0]['p'] = 0
    registers[1]['p'] = 1

    queues = [[], []]
    instructions = get_input_lines.to_a
    pos = [0, 0]
    thread = 0
    other_thread = 1
    send_count = 0
    other_waiting = false
    other_terminated = false

    while true

      instr = instructions[pos[thread]]

      instr = instr.split ' '
      opcode = instr[0]
      reg = instr[1]
      val = nil
      val = numeric?(instr[2]) ? instr[2].to_i : registers[thread][instr[2]] if opcode != 'snd' && opcode != 'rcv'

      case opcode
      when 'snd'
        queues[other_thread].append registers[thread][reg]
        send_count += 1 if thread == 1
        other_waiting = false
      when 'rcv'
        if queues[thread].empty?
          return send_count if other_terminated || other_waiting
          other_waiting = true
          thread, other_thread = other_thread, thread
          next
        else
          registers[thread][reg] = queues[thread].shift
        end
      when 'set'
        registers[thread][reg] = val
      when 'add'
        registers[thread][reg] += val
      when 'mul'
        registers[thread][reg] *= val
      when 'mod'
        registers[thread][reg] %= val
      when 'jgz'
        # -1 to cancel out next increment
        pos[thread] += val - 1 if (numeric?(reg) ? reg.to_i : registers[thread][reg]) > 0
      end

      pos[thread] += 1

      if pos[thread] < 0 || pos[thread] >= instructions.length
        return send_count if other_terminated
        other_terminated = true
        thread, other_thread = other_thread, thread
      end

    end

  end

end