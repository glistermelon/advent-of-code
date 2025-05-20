require_relative 'solver'

def alpha?(s)
  s.match? /[[:alpha:]]/
end

class Dir

  def initialize(num)
    @num = num
  end

  def advance(vec, step = 1)
    case @num
    when 0
      [vec[0] - step, vec[1]]
    when 1
      [vec[0], vec[1] + step]
    when 2
      [vec[0] + step, vec[1]]
    when 3
      [vec[0], vec[1] - step]
    end
  end

  def turn_right
    Dir.new((@num + 1) % 4)
  end

  def turn_left
    Dir.new((@num - 1) % 4)
  end

end

class Day19 < Solver

  def initialize(day)
    super day, false
  end

  def in_bounds(pos)
    pos[0] >= 0 && pos[1] >= 0 && pos[0] < @map.length && pos[1] < @map[0].length
  end

  def on_path(pos)
    in_bounds(pos) && @map[pos[0]][pos[1]] != ' '
  end

  def char_at(pos)
    @map[pos[0]][pos[1]]
  end

  def init_shared

    @map = get_input_lines.to_a
    pos = [0, @map[0].index('|')]
    dir = Dir.new 2 # down
    @letters = ''
    @steps = 1

    while true

      while true
        pos = dir.advance pos
        break unless on_path pos
        @steps += 1
        c = char_at pos
        @letters << c if alpha? c
      end

      pos = dir.advance pos, -1
      if on_path(dir.turn_right.advance pos)
        dir = dir.turn_right
      elsif on_path(dir.turn_left.advance pos)
        dir = dir.turn_left
      else
        break
      end

    end

  end

  def solve_part_1
    @letters
  end

  def solve_part_2
    @steps
  end

end