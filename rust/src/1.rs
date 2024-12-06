mod util;
use util::input_path;
use util::read_lines;

use std::vec::Vec;

fn main() {
    let mut list1 : Vec<u32> = Vec::new();
    let mut list2 : Vec<u32> = Vec::new();
    for ln in read_lines(input_path(1)).unwrap() {
        let mut split = ln.split("   ");
        list1.push(split.next().unwrap().parse::<u32>().unwrap());
        list2.push(split.next().unwrap().parse::<u32>().unwrap());
    }
    list1.sort();
    list2.sort();
    let mut output1 : u32 = 0;
    let mut output2 : u32 = 0;
    for (i, v1) in list1.iter().enumerate() {
        output1 += v1.abs_diff(list2[i]);
        output2 += v1 * (list2.iter().filter(|&&n| n == *v1).count() as u32);
    }
    println!("{}\n{}", output1, output2);
}

