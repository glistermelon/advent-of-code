require_relative 'solver'

class Day12 < Solver

  def init_shared

    @graph = Hash.new { |m, k| m[k] = [] }
    get_input_lines() do |line|
      line.slice! '<-> '
      line.gsub! ',', ''
      nums = line.split(' ').map(&:to_i)
      @graph[nums[0]] += nums[1..]
    end
  
  end

  def get_group(pipe)

    group = Set.new
    buffer = [pipe]
    until buffer.empty?
      @graph[buffer.pop].each do |n|
        next if group.include? n
        group.add n
        buffer << n
      end
    end
    group

  end

  def solve_part_1 = get_group(0).size

  def solve_part_2

    pipes = Set.new @graph.keys
    groups = 0
    until pipes.empty?
      pipes.subtract get_group pipes.first
      groups += 1
    end

    groups

  end

end