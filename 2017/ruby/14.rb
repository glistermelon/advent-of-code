require_relative 'solver'
require_relative '10'

class Day14 < Solver

  def solve_part_1

    input = get_input
    (0..127).map { |i| Day10.knot_hash("#{input}-#{i}").to_s(2).count('1') }.sum

  end

  def solve_part_2

    input = get_input
    mem = []
    (0..127).each do |r|
      bin = Day10.knot_hash("#{input}-#{r}").to_s(2).rjust(128, '0')
      mem << bin.chars.map { |bit| bit == '1' }
    end
    unvisited = Set.new mem.each_with_index.flat_map {
      |r, y| r.each_with_index.map { |c, x| c ? [y, x] : nil }.filter { |p| !p.nil? }
    }

    groups = 0
    until unvisited.empty?
      groups += 1
      buffer = [unvisited.first]
      unvisited.delete buffer[0]
      until buffer.empty?
        py, px = buffer.pop
        [[0, 1], [1, 0], [0, -1], [-1, 0]].each do |dy, dx|
          a = [py + dy, px + dx]
          if unvisited.include? a
            buffer << a
            unvisited.delete a
          end
        end
      end
    end

    groups

  end

end