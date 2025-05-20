require_relative 'solver'

class Day17 < Solver

  def solve_part_1

    step = get_input.to_i
    state = [0]
    i = 0
    (1..2017).each do |v|
      i = (i + step + 1) % state.length
      state.insert i, v
    end
    state[(i + 1) % state.length]

  end

  def solve_part_2

    step = get_input.to_i
    after_zero = nil
    i = 0
    (1..50000000).each do |v|
      # state.length == v
      i = (i + step + 1) % v
      after_zero = v if i == 0
    end
    after_zero

  end

end