require_relative 'solver'

require 'matrix'

STEPS = 1000

Particle = Struct.new(:id, :pos, :vel, :acc) do
  def step
    self.vel += self.acc
    self.pos += self.vel
  end
end

def distance(vec)
  vec.map(&:abs).sum
end

class Day20 < Solver

  def init_shared
    @particles = get_input_lines.each_with_index.map { |line, i|
      line = line.scan(/[^pva=<>,\s]+/).map(&:to_i)
      Particle.new i, Vector.elements(line[..2]), Vector.elements(line[3..5]), Vector.elements(line[6..8])
    }
  end

  def solve_part_1
    @particles.sort_by! { |p| [distance(p.acc), distance(p.vel), distance(p.pos)] }
    @particles.first.id
  end

  def solve_part_2

    remaining = @particles.clone
    (0..STEPS).each do |step|
      remaining.group_by { |p| p.pos }.each do |k, v|
        remaining -= v if v.length != 1
      end
      remaining.each do |p|
        p.step
      end
    end

    remaining.length

  end

end