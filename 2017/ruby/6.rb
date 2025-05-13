require_relative "solver"

class Day6 < Solver

  def solve_part_1()

    @banks = get_input.strip.split(/\s+/).map(&:to_i)
    steps = 0
    history = Set.new
    while !history.include? @banks.dup.freeze
      history.add @banks.dup.freeze
      i = @banks.each_with_index.max_by { |v, i| [v, -i] }[1]
      value = @banks[i]
      @banks[i] = 0
      (0..value - 1).each do
        i += 1
        if i == @banks.length
          i = 0
        end
        @banks[i] += 1
      end
      steps += 1
    end

    steps

  end

  def solve_part_2

    first = @banks.dup.freeze
    steps = 0

    loop do
      i = @banks.each_with_index.max_by { |v, i| [v, -i] }[1]
      value = @banks[i]
      @banks[i] = 0
      (0..value - 1).each do
        i += 1
        if i == @banks.length
          i = 0
        end
        @banks[i] += 1
      end
      steps += 1
      break if @banks == first
    end

    steps

  end

end