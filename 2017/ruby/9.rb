require_relative 'solver'

require 'ostruct'

class Day9 < Solver

  def init_shared

    str = get_input

    i = 0
    depth = 0
    @score = 0
    @garbage = 0

    prev_len = 0
    while str.length != prev_len
      prev_len = str.length
      str = str.sub /\!./, ''
    end

    while i < str.length

      c = str[i]

      case c
      when '{'
        depth += 1
      when '}'
        @score += depth
        depth -= 1
      when '<'
        while str[i] != '>'
          i += 1
          @garbage += 1
        end
        @garbage -= 1
      end

      i += 1

    end

    @score

  end

  def solve_part_1
    @score
  end

  def solve_part_2
    @garbage
  end

end
