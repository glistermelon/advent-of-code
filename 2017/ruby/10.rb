require_relative 'solver'

class Day10 < Solver

  def reverse(nums, pos, len)
    i1 = pos
    i2 = (pos + len - 1) % nums.length
    (1..len / 2).each do
      nums[i1], nums[i2] = nums[i2], nums[i1]
      i1 += 1
      if i1 == nums.length
        i1 = 0
      end
      i2 -= 1
      if i2 == -1
        i2 = nums.length - 1
      end
    end
  end

  def xor(nums)
    x = 0
    nums.each do |n|
      x ^= n
    end
    x
  end

  def solve_part_1
    
    nums = (0..255).to_a
    pos = 0
    skip = 0
    lengths = get_input.split(',').map(&:to_i)

    lengths.each do |len|
      reverse(nums, pos, len)
      pos = (pos + len + skip) % nums.length
      skip += 1
    end

    nums[0] * nums[1]

  end

  def solve_part_2
    
    nums = (0..255).to_a
    pos = 0
    skip = 0
    lengths = get_input.chars.map(&:ord) + [17, 31, 73, 47, 23]

    (1..64).each do
      lengths.each do |len|
        reverse(nums, pos, len)
        pos = (pos + len + skip) % nums.length
        skip += 1
      end
    end

    nums = nums.each_slice(16).map do |chunk|
      xor(chunk)
    end

    nums.map { |n| n.to_s 16 }.join ''

  end

end