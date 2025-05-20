require_relative 'solver'

BILLION = 1000000000

class Day16 < Solver

  def reset_state
    @state = (0..15).map { |i| ('a'.ord + i).chr }
  end

  def dance_once
    
    get_input.split(',').each do |instr|
      case instr[0]
      when 's'
        n = instr[1..].to_i
        @state.rotate! -n
      when 'x'
        indices = instr[1..].split('/').map(&:to_i)
        i1 = indices[0]
        i2 = indices[1]
        @state[i1], @state[i2] = @state[i2], @state[i1]
      when 'p'
        targets = instr[1..].split('/')
        i1 = @state.index targets[0]
        i2 = @state.index targets[1]
        @state[i1], @state[i2] = @state[i2], @state[i1]
      end
    end

  end

  def solve_part_1
    reset_state
    dance_once
    @state.join ''
  end

  def solve_part_2

    reset_state
    start_state = @state.clone.freeze
    period = 1
    dance_once
    until @state.clone.freeze == start_state
      dance_once
      period += 1
    end

    (1..BILLION % period).each do
      dance_once
    end

    @state.join ''

  end

end