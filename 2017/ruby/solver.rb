require_relative "util"

class Solver

  def initialize(day)
    @input = Util.get_input(day).strip
  end

  def get_input
    return @input
  end

  def get_input_lines

    return enum_for(:get_input_lines) unless block_given?

    @input.each_line do |line|
      yield line.strip
    end

  end

  def init_shared
  end

  def solve_part_1
    raise NotImplementedError, "moron"
  end

  def solve_part_2
    raise NotImplementedError, "moron"
  end

end