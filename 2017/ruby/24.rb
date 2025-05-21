require_relative 'solver'

Part = Struct.new(:port1, :port2) do
  def strength = (port1 + port2)
end

class Day24 < Solver

  def get_matches(port, used)
    @parts.filter { |p| (p.port1 == port || p.port2 == port) && !used.include?(p) }
  end

  def find_strongest(port, used, max_length, cache)

    key = [port, used.clone.freeze].freeze
    return cache[key] if cache.key? key

    strongest = [max_length ? used.length : 0, used.map(&:strength).sum]

    get_matches(port, used).each do |part|
      next if used.include? part
      if part.port1 == port
        used << part
        s = find_strongest part.port2, used, max_length, cache
        used.delete part
        strongest = s if (s <=> strongest) == 1
      elsif part.port2 == port
        used << part
        s = find_strongest part.port1, used, max_length, cache
        used.delete part
        strongest = s if (s <=> strongest) == 1
      end
    end

    cache[key] = strongest

    strongest

  end

  def init_shared
    @parts = get_input_lines.map { |line| Part.new(*line.split('/').map(&:to_i)) }
  end

  def solve_part_1 = find_strongest(0, Set.new, false, {})[1]

  def solve_part_2 = find_strongest(0, Set.new, true, {})[1]

end
