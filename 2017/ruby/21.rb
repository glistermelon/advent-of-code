require_relative 'solver'

class Square

  attr_reader :state

  def initialize(state)
    @state = state
  end

  def self.merge(chunks)
    chunk_size = chunks[0][0].state.length
    total_size = chunk_size * chunks.length
    Square.new((0...total_size).map { |y| chunks[y / chunk_size].map { |sq| sq.state[y % chunk_size] }.join })
  end

  def split
    chunks = []
    size = state.length.even? ? 2 : 3
    0.step(state.length - 1, size).each do |y|
      chunks << []
      0.step(state.length - 1, size).each do |x|
        chunks.last << Square.new((0...size).map { |dy| (0...size).map { |dx| @state[y + dy][x + dx] }.join })
      end
    end
    chunks
  end

  def transform(input)

    state = @state.map(&:clone)

    4.times do

      result = input[state.join '/']
      return Square.new(result.split('/')) unless result.nil?

      # flip 1
      result = input[state.reverse.join '/']
      return Square.new(result.split('/')) unless result.nil?

      # flip 2
      result = input[state.map(&:reverse).join '/']
      return Square.new(result.split('/')) unless result.nil?

      # rotate
      state = state.map(&:chars).transpose.map(&:reverse).map(&:join)

    end

    nil

  end

  def count_pixels
    @state.map { |r| r.count '#' }.sum
  end

end

class Day21 < Solver

  def solve(sq, iter)

    iter.times do
      sq = Square.merge(sq.split.map { |r| r.map { |s| s.transform @input } })
    end
    sq.count_pixels

  end

  def init_shared
    @input = get_input_lines.map { |line| line.split ' => ' }.to_h
  end

  def solve_part_1 = solve (Square.new ['.#.', '..#', '###']), 5

  def solve_part_2 = solve (Square.new ['.#.', '..#', '###']), 18

end
