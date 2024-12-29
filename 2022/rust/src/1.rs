use utils::input_path;
use std::fs;

fn main() {
    let mut elves = fs::read_to_string(input_path(2022, 1))
                                .unwrap().split("\r\n\r\n")
                                .map(
                                    |s| s.split_whitespace()
                                                .map(|s| s.parse::<u32>())
                                                .flatten()
                                                .collect::<Vec<u32>>()
                                )
                                .map(|cl| cl.iter().sum::<u32>())
                                .collect::<Vec<u32>>();
    elves.sort();
    elves.reverse();
    let output1 = elves[0];
    let output2 = elves[0..3].iter().sum::<u32>();
    println!("{}\n{}", output1, output2);
}