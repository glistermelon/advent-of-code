use utils::input_path;
use std::fs;

#[derive(Clone, Debug)]
struct ObsidianRobotCost {
    ore : i32,
    clay : i32
}

#[derive(Clone, Debug)]
struct GeodeRobotCost {
    ore : i32,
    obsidian : i32
}

#[derive(Clone, Debug)]
struct Blueprint {
    ore_robot : i32,
    clay_robot : i32,
    obsidian_robot : ObsidianRobotCost,
    geode_robot : GeodeRobotCost
}

#[derive(Debug)]
struct Simulation {
    bp : Blueprint,
    ore : i32,
    clay : i32,
    obsidian: i32,
    geodes: i32,
    ore_robots : i32,
    clay_robots : i32,
    obsidian_robots: i32,
    geode_robots : i32,
    minute : i32,
    total_minutes : i32,
    current_max_geodes : i32
}

#[derive(Clone, Debug)]
enum RobotType {
    ORE,
    CLAY,
    OBSIDIAN,
    GEODE
}

impl Simulation {

    fn saving_time(&self, robot : RobotType) -> i32 {
        match robot {
            RobotType::ORE => {
                ((self.bp.ore_robot - self.ore + self.ore_robots - 1) / self.ore_robots).max(0)
            },
            RobotType::CLAY => {
                ((self.bp.clay_robot - self.ore + self.ore_robots - 1) / self.ore_robots).max(0)
            },
            RobotType::OBSIDIAN => {
                if self.clay_robots != 0 {
                    ((self.bp.obsidian_robot.ore - self.ore + self.ore_robots - 1) / self.ore_robots).max(
                        (self.bp.obsidian_robot.clay - self.clay + self.clay_robots - 1) / self.clay_robots
                    ).max(0)
                }
                else {
                    -1
                }
            },
            RobotType::GEODE => {
                if self.obsidian_robots != 0 {
                    ((self.bp.geode_robot.ore - self.ore + self.ore_robots - 1) / self.ore_robots).max(
                        (self.bp.geode_robot.obsidian - self.obsidian + self.obsidian_robots - 1) / self.obsidian_robots
                    ).max(0)
                }
                else {
                    -1
                }
            }
        }
    }

    fn buy_robot(&mut self, robot : RobotType) -> Option<(i32, i32)> {
        let delay = self.saving_time(robot.clone());
        if delay == -1 {
            return None;
        }
        let delay = delay + 1;
        if self.minute + delay > self.total_minutes {
            None
        } else {
            let prev_time = self.minute;
            self.minute += delay;
            self.ore += self.ore_robots * delay;
            self.clay += self.clay_robots * delay;
            self.obsidian += self.obsidian_robots * delay;
            self.geodes += self.geode_robots * delay;
            match robot {
                RobotType::ORE => {
                    self.ore_robots += 1;
                    self.ore -= self.bp.ore_robot;
                },
                RobotType::CLAY => {
                    self.clay_robots += 1;
                    self.ore -= self.bp.clay_robot;
                },
                RobotType::OBSIDIAN => {
                    self.obsidian_robots += 1;
                    self.ore -= self.bp.obsidian_robot.ore;
                    self.clay -= self.bp.obsidian_robot.clay;
                }
                RobotType::GEODE => {
                    self.geode_robots += 1;
                    self.ore -= self.bp.geode_robot.ore;
                    self.obsidian -= self.bp.geode_robot.obsidian;
                }
            }
            Some((prev_time, delay))
        }
    }

    fn sell_robot(&mut self, robot : RobotType, info : (i32, i32)) {
        self.minute = info.0;
        let delay = info.1;
        match robot {
            RobotType::ORE => {
                self.ore_robots -= 1;
                self.ore += self.bp.ore_robot;
            },
            RobotType::CLAY => {
                self.clay_robots -= 1;
                self.ore += self.bp.clay_robot;
            },
            RobotType::OBSIDIAN => {
                self.obsidian_robots -= 1;
                self.ore += self.bp.obsidian_robot.ore;
                self.clay += self.bp.obsidian_robot.clay;
            }
            RobotType::GEODE => {
                self.geode_robots -= 1;
                self.ore += self.bp.geode_robot.ore;
                self.obsidian += self.bp.geode_robot.obsidian;
            }
        }
        self.ore -= self.ore_robots * delay;
        self.clay -= self.clay_robots * delay;
        self.obsidian -= self.obsidian_robots * delay;
        self.geodes -= self.geode_robots * delay;
    }

    fn max_geodes(&mut self) -> i32 {

        if self.geodes + self.geode_robots * (self.total_minutes + 1 - self.minute) + (self.total_minutes - self.minute - 1) * (self.total_minutes - self.minute) / 2 < self.current_max_geodes {
            return 0;
        }

        let mut max_geodes = 0;
        for robot in [RobotType::GEODE, RobotType::OBSIDIAN, RobotType::CLAY, RobotType::ORE] {
            if let Some(info) = self.buy_robot(robot.clone()) {
                let geodes = self.max_geodes();
                if geodes > max_geodes {
                    max_geodes = geodes;
                }
                if geodes > self.current_max_geodes {
                    self.current_max_geodes = geodes;
                }
                self.sell_robot(robot, info);
            }
        }

        max_geodes.max(self.geodes + self.geode_robots * (self.total_minutes + 1 - self.minute))

    }

}

impl Blueprint {
    fn max_geodes(&self, minutes : i32) -> i32 {
        (Simulation {
            bp: self.clone(),
            ore: 0,
            clay: 0,
            obsidian: 0,
            geodes: 0,
            ore_robots: 1,
            clay_robots: 0,
            obsidian_robots: 0,
            geode_robots: 0,
            minute: 1,
            total_minutes: minutes,
            current_max_geodes: 0
        }).max_geodes()
    }
}

fn main() {
    let blueprints : Vec<Blueprint> = fs::read_to_string(input_path(2022, 19)).unwrap()
        .replace("\r", "")
        .replace(".", "")
        .split("\n")
        .map(|ln| {
            let ln : Vec<_> = ln.split_whitespace().map(|s| s.to_string()).collect();
            Blueprint {
                ore_robot: ln[6].parse().unwrap(),
                clay_robot: ln[12].parse().unwrap(),
                obsidian_robot: ObsidianRobotCost {
                    ore: ln[18].parse().unwrap(),
                    clay: ln[21].parse().unwrap()
                },
                geode_robot: GeodeRobotCost {
                    ore: ln[27].parse().unwrap(),
                    obsidian: ln[30].parse().unwrap()
                }
            }
        })
        .collect();
    let output1 = blueprints.iter()
        .enumerate()
        .map(|(i, bp)| (i as i32 + 1) * bp.max_geodes(24))
        .sum::<i32>();
    let output2 = blueprints.iter()
        .take(3)
        .map(|bp| bp.max_geodes(32))
        .product::<i32>();
    println!("{}\n{}", output1, output2);
}