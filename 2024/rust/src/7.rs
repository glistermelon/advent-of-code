use utils::input_path;
use utils::read_lines;

use std::vec::Vec;

struct Solver {
    allow_concat : bool
}

impl Solver {
    fn solve(&mut self, nums : &[u64], targ : u64) -> bool {
        let n = nums[0];
        if nums.len() == 1 { return n == targ; }
        let next_slice = &nums[1..];
        if self.allow_concat {
            let digits_p10 = (10 as u64).pow(if n != 1 { ((n + 1) as f64).log10().ceil() as u32 } else { 1 });
            if targ % digits_p10 == n && self.solve(next_slice, targ / digits_p10) { return true; }
        }
        if targ % n == 0 && self.solve(next_slice, targ / n) { return true; }
        if n < targ && self.solve(next_slice, targ - n) { return true; }
        false
    }
}

fn main() {
    let mut outputs : [u64; 2] = [0, 0];
    let mut solver = Solver { allow_concat: false };
    for ln in read_lines(input_path(2024, 7)).unwrap() {
        let colon_i = ln.find(':').unwrap();
        let targ = ln[0..colon_i].parse::<u64>().unwrap();
        let nums = ln[colon_i + 2..].split_whitespace().map(|n| n.parse::<u64>()).flatten().rev().collect::<Vec<u64>>();
        if solver.solve(&nums, targ) {
            outputs[0] += targ;
        }
        else {
            solver.allow_concat = true;
            if solver.solve(&nums, targ) {
                outputs[1] += targ;
            }
            solver.allow_concat = false;
        }
    }
    println!("{}\n{}", outputs[0], outputs[0] + outputs[1]);
}