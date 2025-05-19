require_relative 'solver'

class Wall < Struct.new(:depth, :range, :scanner)

  attr_reader :period

  def initialize(depth, range, scanner = 0)
    super depth, range, scanner
    @forward = true
    @period = 2 * range - 2
  end

  def reset
    self.scanner = 0
  end

  def step(steps)
    steps %= @period
    (1..steps).each do
      if @forward
        self.scanner += 1
        @forward = !@forward if self.scanner == self.range - 1
      else
        self.scanner -= 1
        @forward = !@forward if self.scanner == 0
      end
    end
  end

end

class Day13 < Solver

  def init_shared

    @firewall = get_input_lines.map do |line|
      Wall.new *line.split(': ').map(&:to_i)
    end.to_a

  end

  def solve_part_1

    severity = 0

    @firewall.sort_by { |w| w.depth }.each_cons(2) do |w1, w2|
      steps = w2.depth - w1.depth
      @firewall.each do |w|
        w.step steps
      end
      severity += w2.depth * w2.range if w2.scanner == 0
    end

    severity

  end
  
  def solve_part_2

    # brute force
    period = @firewall.map(&:period).reduce(:lcm)
    tests = @firewall.map { |w| [w.period, (-w.depth) % w.period] }.to_a
    (1..period).each do |n|
      return n if tests.map { |t| n % t[0] != t[1] }.all?
    end

  end

end