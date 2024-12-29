use utils::input_path;
use std::fs;

use regex::Regex;

fn product_sum(data : &String) -> u64 {
    let re = Regex::new("mul\\([0-9]+,[0-9]+\\)").unwrap();
    re.captures_iter(&data)
        .map(|c| c.get(0).unwrap().as_str())
        .map(
            |s| s[4..s.len() - 1].split(',')
            .map(|n| n.parse::<u64>())
            .flatten().product::<u64>()
        ).sum::<u64>()
}

fn main() {
    let mut data = fs::read_to_string(input_path(2024, 3)).unwrap();
    println!("{}", product_sum(&data));
    while let Some(i1) = data.find("don't()") {
        let i2 = data[i1 + 2..].find("do()").map_or(data.len(),|i| i + i1 + 2);
        data.replace_range(i1..i2, "");
    }
    println!("{}", product_sum(&data));
}