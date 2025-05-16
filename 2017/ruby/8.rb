require_relative 'solver'

require 'ostruct'

class Day8 < Solver

  def init_shared

    data = OpenStruct.new
    @max = 0

    get_input_lines() do |line|

      line = line.split ' '
      targ = line[0]
      inc = line[1] == 'inc'
      amount = line[2].to_i
      other = line[4]

      data[targ] = 0 if data[targ].nil?
      data[other] = 0 if data[other].nil?

      if eval("#{data[other]} #{line[5]} #{line[6]}")
        if inc
          data[targ] += amount
        else
          data[targ] -= amount
        end
        @max = data[targ] if data[targ] > @max
      end

    end

    @end_max = data.each_pair.max_by { |_, v| v }[1]

  end

  def solve_part_1
    @end_max
  end

  def solve_part_2
    @max
  end

end
