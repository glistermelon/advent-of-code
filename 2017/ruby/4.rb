require_relative "solver"

class Day4 < Solver

  def solve_part_1()

    get_input_lines.to_a.map { |ln|
      ln = ln.split " "
      if ln.uniq.length == ln.length then 1 else 0 end
    }.sum

  end

  def solve_part_2()

    get_input_lines.to_a.map { |ln|

      valid = true
      history = Set.new

      ln.split(" ").each do |s|
        info = s.chars.group_by { |c| c }.transform_values(&:count).freeze
        if history.include? info
          valid = false
          break
        end
        history.add info
      end

      valid ? 1 : 0

    }.sum

  end

end