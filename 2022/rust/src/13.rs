use itertools::Itertools;
use utils::input_path;
use std::{cmp::Ordering, fs};
use serde_json::{self, Value};

fn compare_packets(p1 : &Vec<Value>, p2 : &Vec<Value>) -> Option<bool> {
    for (n1, n2) in p1.iter().zip(p2.iter()) {
        if n1.is_i64() && n2.is_i64() {
            let n1 = n1.as_i64().unwrap();
            let n2 = n2.as_i64().unwrap();
            if n1 > n2 {
                return Some(false);
            }
            else if n1 < n2 {
                return Some(true);
            }
        }
        else {
            let n1 = if n1.is_i64() { &vec![Value::from(n1.as_i64().unwrap())] } else { n1.as_array().unwrap() };
            let n2 = if n2.is_i64() { &vec![Value::from(n2.as_i64().unwrap())] } else { n2.as_array().unwrap() };
            if let Some(ordered) = compare_packets(n1, n2) {
                return Some(ordered);
            }
        }
    }
    if p1.len() < p2.len() {
        Some(true)
    }
    else if p1.len() > p2.len() {
        Some(false)
    }
    else {
        None
    }
}

fn main() {
    let packets = fs::read_to_string(input_path(2022, 13)).unwrap()
        .replace("\r", "")
        .replace("\n\n", "\n")
        .split("\n")
        .map(|ln| serde_json::from_str::<Vec<Value>>(ln).unwrap())
        .collect_vec();
    let output1 = packets.iter()
        .chunks(2)
        .into_iter()
        .map(|chunk| {
            let chunk = chunk.into_iter().collect_vec();
            compare_packets(chunk[0], chunk[1])
        })
        .enumerate()
        .filter_map(|(i, ordered)| if ordered.unwrap() { Some(i + 1) } else { None })
        .sum::<usize>();
    let mut packets = packets.iter().map(|p| p).collect_vec();
    let key1 = serde_json::from_str::<Vec<Value>>("[[2]]").unwrap();
    let key2 = serde_json::from_str::<Vec<Value>>("[[6]]").unwrap();
    packets.push(&key1);
    packets.push(&key2);
    packets.sort_by(|p1, p2|
        if compare_packets(p1, p2).unwrap() { Ordering::Less } else { Ordering::Greater }
    );
    let output2 = packets.iter()
        .enumerate()
        .filter_map(|(i, &p)|
            if std::ptr::eq(p, &key1) || std::ptr::eq(p, &key2) {
                Some(i + 1)
            }
            else {
                None
            }
        )
        .product::<usize>();
    println!("{}\n{}", output1, output2);
}