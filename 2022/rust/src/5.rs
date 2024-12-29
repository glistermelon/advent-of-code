use utils::input_path;
use std::fs;

fn main() {
    for part1 in [true, false] {
        let lines = fs::read_to_string(input_path(2022, 5)).unwrap()
            .split("\r\n").map(|s| String::from(s)).collect::<Vec<String>>();
        let num_stacks = (lines[0].len() + 1) / 4;
        let mut stacks : Vec<Vec<char>> = vec![vec![]; num_stacks];
        let instructions_i = lines.iter().position(|ln| ln.is_empty()).unwrap();
        for i in 0..num_stacks {
            let c_i = 4 * i + 1;
            stacks[i] = (0..instructions_i - 1)
                .map(|ln_i| lines[ln_i].chars().nth(c_i))
                .flatten()
                .filter(|&c| c != ' ')
                .rev()
                .collect::<Vec<char>>();
        }
        for ln in &lines[(instructions_i + 1)..] {
            let nums = ln.split_whitespace()
                .enumerate()
                .filter(|(i, _)| matches!(i, 1 | 3 | 5))
                .map(|(_, s)| s.parse::<usize>())
                .flatten()
                .collect::<Vec<usize>>();
            let mv = nums[0];
            let src = nums[1] - 1;
            let dst = nums[2] - 1;
            let i = ((stacks[src].len() - mv) as i64).max(0) as usize;
            let mut slice = stacks[src].drain(i..).collect::<Vec<char>>();
            if part1 {
                slice.reverse();
            }
            stacks[dst].extend(slice);
        }
        let output1 = stacks.iter().map(|s| s.last()).flatten().collect::<String>();
        println!("{}", output1);
    }
}