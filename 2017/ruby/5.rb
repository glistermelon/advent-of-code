require_relative "solver"

class Day5 < Solver

  def solve_part_1()

    jumps = get_input_lines.map(&:to_i)
    steps = 0
    i = 0
    while i >= 0 && i < jumps.length
      jump = jumps[i]
      jumps[i] += 1
      i += jump
      steps += 1
    end

    steps

  end

  def solve_part_2()

    jumps = get_input_lines.map(&:to_i)
    steps = 0
    i = 0
    while i >= 0 && i < jumps.length
      jump = jumps[i]
      if jump >= 3
        jumps[i] -= 1
      else
        jumps[i] += 1
      end
      i += jump
      steps += 1
    end

    steps

  end

end