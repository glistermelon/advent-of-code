require_relative "solver"

class Day3 < Solver

  def solve_part_1()

    input = get_input.to_i

    ring = 1
    cap = 9
    prev_cap = nil
    while input > cap
      prev_cap = cap
      ring += 1
      cap += 8 * ring
    end

    distance = (1..7).step(2).map { |n| (prev_cap + n * ring - input).abs }.min

    return ring + distance

  end

  def solve_part_2()
    
    input = get_input.to_i

    inner = [1, 2, 4, 5, 10, 11, 23, 25]
    
    while true

      corners = (inner.length - 1).step(0, -inner.length / 4).to_a
      outer = [
        inner.first + inner.last,
        2 * (inner.first + inner.last) + inner[1]
      ]
      
      (1..inner.length - 2).each do |i|

        if corners.include?(i)
          outer.append outer.last + inner[i - 1] + inner[i]
          outer.append outer.last + inner[i]
          outer.append outer.last + outer[-2] + inner[i] + inner[i + 1]
        else
          outer.append outer.last + inner[i - 1 ] + inner[i] + inner[i + 1]
        end

      end

      outer.append outer.last + inner.last + outer.first + inner[-2]
      outer.append outer.last + inner.last + outer.first

      inner = outer;

      inner.each do |n|
        if n > input
          return n
        end
      end

    end

  end

end