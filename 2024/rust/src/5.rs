mod util;
use util::input_path;
use util::read_lines;

use std::vec::Vec;
use std::cmp::Ordering;

struct Rules {
    rules : Vec<(u32, u32)>
}

impl Rules {
    fn cmp(&self, n1 : &u32, n2 : &u32) -> Ordering {
        for (r1, r2) in &self.rules {
            if r1 == n1 && r2 == n2 {
                return Ordering::Less;
            }
            if r1 == n2 && r2 == n1 {
                return Ordering::Greater;
            }
        }
        return n1.cmp(&n2);
    }
}

fn main() {
    let mut rules = Rules {
        rules: Vec::<(u32, u32)>::new(),
    };
    let mut rules_parsed : bool = false;
    let mut output1 : u32 = 0;
    let mut output2 : u32 = 0;
    for ln in read_lines(input_path(5)).unwrap() {
        if ln.is_empty() {
            rules_parsed = true;
        }
        else if !rules_parsed {
            let mut rule = ln.split('|').map(|n| n.parse::<u32>()).flatten();
            rules.rules.push((rule.next().unwrap(), rule.next().unwrap()));
        }
        else {
            let mut nums = ln.split(',').map(|n| n.parse::<u32>()).flatten().collect::<Vec<u32>>();
            if nums.is_sorted_by(|n1, n2| rules.cmp(n1, n2) == Ordering::Less) {
                output1 += nums[nums.len() / 2];
                continue;
            }
            nums.sort_by(|n1, n2| rules.cmp(n1, n2));
            output2 += nums[nums.len() / 2];
        }
    }
    println!("{}\n{}", output1, output2);
}