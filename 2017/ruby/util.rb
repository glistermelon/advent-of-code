module Util

  def get_input(day)
    return File.read("../inputs/#{day}.txt")
  end

  module_function :get_input

end