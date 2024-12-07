mod util;
use util::input_path;
use util::read_lines;

use std::vec::Vec;

fn check(list : &Vec<i32>) -> bool {
    let mut positive : Option<bool> = None;
    for i in 1..list.len() {
        let delta = list[i] - list[i - 1];
        let abs_delta = delta.abs();
        if abs_delta == 0 || abs_delta > 3 { return false; }
        match positive {
            None => { if delta != 0 { positive = Some(delta > 0); } },
            Some(true) => { if delta < 0 { return false; } },
            Some(false) => { if delta > 0 { return false; } }
        }
    }
    true
}

fn main() {
    let mut output1 : u32 = 0;
    let mut output2 : u32 = 0;
    for ln in read_lines(input_path(2)).unwrap() {
        let mut list = ln.split(" ").map(|x| x.parse::<i32>()).flatten().collect::<Vec<i32>>();
        if check(&list) {
            output1 += 1;
            output2 += 1;
        }
        else {
            for i in 0..list.len() {
                let n = list.remove(i);
                let safe = check(&list);
                list.insert(i, n);
                if safe {
                    output2 += 1;
                    break;
                }
            }
        }
    }
    println!("{}\n{}", output1, output2);
}

