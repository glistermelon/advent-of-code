require_relative "solver"

class Day2 < Solver

  def init_shared

    @rows = get_input_lines.to_a.map { |line| line.strip.split(/\s+/).map(&:to_i) }

  end

  def solve_part_1()

    sum = 0

    @rows.each do |nums|
      sum += nums.max - nums.min
    end

    sum

  end

  def solve_part_2()

    sum = 0

    @rows.each do |nums|
      nums.combination(2) do |n1, n2|
        if n1 % n2 == 0
          sum += n1 / n2
          break
        end
        if n2 % n1 == 0
          sum += n2 / n1
          break
        end
      end
    end

    sum
    
  end

end