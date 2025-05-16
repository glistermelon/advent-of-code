require_relative 'solver'

class Day7 < Solver

  def solve_part_1

    seen_once = Set.new
    seen_twice = Set.new

    get_input_lines do |line|

      line.slice!(/\s(\(.*\))/)
      line.slice! ' ->'
      line.split(/,*\s+/).each do |tower|

        unless seen_twice.include? tower
          if seen_once.include? tower
            seen_once.delete tower
            seen_twice.add tower
          else
            seen_once.add tower
          end
        end

      end

    end

    @base_tower_name = seen_once.first

  end

  def solve_part_2

    towers = []
    children = {}

    get_input_lines do |line|
      name = line[/^.+?\s/][..-2]
      weight = line[/\(.+?\)/][1..-2].to_i
      towers.append Tower.new name, weight
      i = line.index('->')
      children[name] = line[i + 3..].split ', ' unless i.nil?
    end

    towers.each do |tower|
      next unless children.include? tower.name
      children[tower.name].each do |name|
        tower.add_child Tower.get_instance name
      end
    end

    Tower.get_instance(@base_tower_name).fix_weights nil

  end

end

class Tower

  class << self

    def instances
      @instances ||= {}
    end

    def get_instance(name)
      @instances[name]
    end

  end

  attr_reader :name, :children

  def initialize(name, weight)
    @name = name
    @weight = weight
    @children = []
    self.class.instances[name] = self
  end

  def add_child(tower)
    @children.append tower
  end

  def weight
    @weight + @children.map(&:weight).sum
  end

  def fix_weights(delta)
    groups = @children.group_by(&:weight)
    if groups.size < 2
      @weight + delta
    else
      valid = groups.max_by { |_, v| v.length }[1].first
      invalid = groups.min_by { |_, v| v.length }[1].first
      invalid.fix_weights valid.weight - invalid.weight
    end
  end

end
