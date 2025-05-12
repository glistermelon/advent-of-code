require_relative "solver"
require_relative "util"

class Day1 < Solver
    
    def solve_part_1()

        data = get_input

        data += data[0]
        
        sum = 0
        
        data.chars.each_cons(2) do |c1, c2|
            if c1 == c2
                sum += c1.to_i
            end
        end
        
        return sum

    end

    def solve_part_2()

        data = get_input

        sum = 0

        data.chars.each_with_index do |c1, i|
            c2 = data[(i + data.length / 2) % data.length]
            if c1 == c2
                sum += c1.to_i
            end
        end

        return sum

    end

end