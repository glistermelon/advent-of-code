require_relative 'solver'
require_relative '19'

class Day22 < Solver

  def solve_part_1

    input = get_input_lines.to_a
    infected = Set.new(input.each_with_index.flat_map{ |data, y|
      data.chars.each_with_index.filter { |c, _| c == '#' }.map { |_, x| [y, x] }
    })
    pos = [input.length / 2, input[0].length / 2]
    dir = Dir.new 0
    sum = 0

    10000.times do
      dir = infected.include?(pos) ? dir.turn_right : dir.turn_left
      if infected.include? pos
        infected.delete pos
      else
        infected.add pos
        sum += 1
      end
      pos = dir.advance pos
    end

    sum

  end

  def solve_part_2

    input = get_input_lines.to_a
    infected = Set.new(input.each_with_index.flat_map{ |data, y|
      data.chars.each_with_index.filter { |c, _| c == '#' }.map { |_, x| [y, x] }
    })
    weakened = Set.new
    flagged = Set.new
    pos = [input.length / 2, input[0].length / 2]
    dir = Dir.new 0
    sum = 0

    10_000_000.times do

      if infected.include? pos
        dir = dir.turn_right
        infected.delete pos
        flagged.add pos
      elsif weakened.include? pos
        weakened.delete pos
        infected.add pos
        sum += 1
      elsif flagged.include? pos
        dir = dir.reverse
        flagged.delete pos
      else
        dir = dir.turn_left
        weakened.add pos
      end

      pos = dir.advance pos

    end

    sum

  end

end
