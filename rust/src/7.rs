mod util;
use util::input_path;
use util::read_lines;

use std::vec::Vec;

use convert_base::Convert;

fn solve(nums : &Vec<u64>, opps : &mut Vec<u8>) -> u64 {
    nums.iter().copied().reduce(
        |n1, n2| match opps.pop().unwrap() {
            0 => n1 + n2,
            1 => n1 * n2,
            2 => (n1.to_string() + &n2.to_string()).parse::<u64>().unwrap(),
            x => panic!("yikes {}", x)
        }
    ).unwrap()
}

fn main() {
    let mut outputs : [u64; 2] = [0, 0];
    for ln in read_lines(input_path(7)).unwrap() {
        let colon_i = ln.find(':').unwrap();
        let targ = ln[0..colon_i].parse::<u64>().unwrap();
        let nums = ln[colon_i + 2..].split(' ').map(|n| n.parse::<u64>()).flatten().collect::<Vec<u64>>();
        let num_opps = (nums.len() - 1) as u32;
        for part in 0..2 {
            for pattern in 0..((if part == 0 {2} else {3}) as i32).pow(num_opps) {
                let mut opps = pattern.to_string().chars().rev().map(|c| c.to_digit(10).unwrap() as u8).collect::<Vec<u8>>();
                opps = Convert::new(10, if part == 0 {2} else {3}).convert::<u8, u8>(&opps);
                let missing_opps = num_opps as usize - opps.len();
                if missing_opps != 0 {
                    opps.splice(opps.len()..opps.len(), std::iter::repeat(0).take(missing_opps));
                }
                if solve(&nums, &mut opps) == targ {
                    outputs[part] += targ;
                    break;
                }
            }
        }
    }
    println!("{}\n{}", outputs[0], outputs[1]);
}