require_relative 'solver'

Action = Struct.new(:write, :move, :next_state)

class Day25 < Solver

  def init_shared

    lines = get_input_lines.to_a

    @states = 3.step(lines.length - 1, 10).map { |i|
      name = lines[i][9]
      action0 = Action.new(
        lines[i + 2][18] == '1',
        lines[i + 3].include?('right') ? 1 : -1,
        lines[i + 4][22]
      )
      action1 = Action.new(
        lines[i + 6][18] == '1',
        lines[i + 7].include?('right') ? 1 : -1,
        lines[i + 8][22]
      )
      [name, [action0, action1]]
    }.to_h

    @state = @states[lines[0][15]]
    @steps = lines[1].split[5].to_i

  end

  def solve_part_1

    tape = Set.new
    x = 0

    @steps.times do
      action = @state[tape.include?(x) ? 1 : 0]
      if action.write
        tape.add x
      else
        tape.delete x
      end
      x += action.move
      @state = @states[action.next_state]
    end

    tape.length

  end

  def solve_part_2 = nil

end
