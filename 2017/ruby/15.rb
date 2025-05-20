require_relative 'solver'

class Day15 < Solver

  def solve_part_1

    input = get_input_lines.map { |line| line.split(' ').last.to_i }
    a = input[0]
    b = input[1]

    count = 0
    (1..40000000).each do

      a = (a * 16807) % 2147483647
      b = (b * 48271) % 2147483647

      count += 1 if a & 0xffff == b & 0xffff

    end
    count

  end

  def solve_part_2

    input = get_input_lines.map { |line| line.split(' ').last.to_i }
    a = input[0]
    b = input[1]

    count = 0
    (1..5000000).each do

      while true
        a = (a * 16807) % 2147483647
        break if a % 4 == 0
      end

      while true
        b = (b * 48271) % 2147483647
        break if b % 8 == 0
      end

      count += 1 if a & 0xffff == b & 0xffff

    end
    count

  end

end