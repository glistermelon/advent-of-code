require_relative 'solver'

class Day11 < Solver

  def distance(x, y)
    x = x.abs
    y = y.abs
    x >= y ? x : x + (y - x + 1) / 2
  end

  def init_shared

    x = 0
    y = 0
    @furthest = 0

    get_input.split(',').each do |instr|

      case instr
      when 'n'
        y += 2
      when 'ne'
        x += 1
        y += 1
      when 'se'
        x += 1
        y -= 1
      when 's'
        y -= 2
      when 'sw'
        x -= 1
        y -= 1
      when 'nw'
        x -= 1
        y += 1
      end

      d = distance(x, y)
      @furthest = d if d > @furthest

    end

    @end_distance = distance x, y

  end

  def solve_part_1 = @end_distance

  def solve_part_2 = @furthest

end