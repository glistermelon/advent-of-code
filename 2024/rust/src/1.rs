mod util;
use util::input_path;
use util::read_lines;

use std::collections::HashMap;
use std::vec::Vec;

fn main() {
    
    let mut list1 : Vec<u32> = Vec::new();
    let mut list2 : Vec<u32> = Vec::new();

    for ln in read_lines(input_path(1)).unwrap() {
        let mut split = ln.split_whitespace();
        list1.push(split.next().unwrap().parse::<u32>().unwrap());
        list2.push(split.next().unwrap().parse::<u32>().unwrap());
    }

    list1.sort();
    list2.sort();

    let mut counts = HashMap::new();
    for n1 in list1.iter() {
        if counts.contains_key(n1) { continue; }
        counts.insert(n1, list2.iter().filter(|&&n2| n2 == *n1).count() as u32);
    }

    let mut output1 : u32 = 0;
    let mut output2 : u32 = 0;

    for (n1, n2) in list1.iter().zip(&list2) {
        output1 += n1.abs_diff(*n2);
        output2 += n1 * counts.get(n1).unwrap();
    }

    println!("{}\n{}", output1, output2);

}

