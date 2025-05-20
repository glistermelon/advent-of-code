require_relative "util"

class Solver

  def initialize(day, strip_input = true)
    @input = Util.get_input(day)
    @input.strip! if strip_input
    @strip_input = strip_input
  end

  def get_input
    return @input
  end

  # @yieldparam [String]
  # @return [Enumerator::Yielder<String>]
  def get_input_lines

    return enum_for(:get_input_lines) unless block_given?

    @input.each_line do |line|
      yield @strip_input ? line.strip : line
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
