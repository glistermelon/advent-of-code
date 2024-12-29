use utils::input_path;
use std::fs;

enum RPS {
    Rock = 0,
    Paper = 1,
    Scissors = 2
}

impl RPS {
    fn from_char(c : char) -> RPS {
        return match c {
            'A' | 'X' => RPS::Rock,
            'B' | 'Y' => RPS::Paper,
            'C' | 'Z' => RPS::Scissors,
            _ => { panic!("RPS {}", c); }
        }
    }
    fn outcome(opp : RPS, you : RPS) -> (u32, u32) {

        let you_i = you as i8;
        let opp_i = opp as i8;

        let part1 = (you_i + 1 + match opp_i {
            x if x == you_i => 3,
            x if x == (you_i + 1 + 3) % 3 => 0,
            x if x == (you_i - 1 + 3) % 3 => 6,
            _ => { panic!("This will never happen"); }
        }) as u32;

        let part2 = (1 + match you_i {
            x if x == (RPS::from_char('X') as i8) => (opp_i - 1 + 3) % 3,
            x if x == (RPS::from_char('Y') as i8) => opp_i + 3,
            x if x == (RPS::from_char('Z') as i8) => ((opp_i + 1 + 3) % 3) + 6,
            _ => { panic!("This will never happen"); }
        }) as u32;

        (part1, part2)

    }
}

fn main() {
    let games = fs::read_to_string(input_path(2022, 2))
        .unwrap()
        .split("\r\n")
        .map(
            |s| RPS::outcome(
                RPS::from_char(s.chars().nth(0).unwrap()),
                RPS::from_char(s.chars().nth(2).unwrap())
            )
        )
        .fold((0, 0), |a, b| (a.0 + b.0, a.1 + b.1));
    println!("{}\n{}", games.0, games.1);
}