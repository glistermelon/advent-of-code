use utils::input_path;
use std::fs;

fn mix(mut data : Vec<i64>, mixes : usize) -> i64 {
    let size = data.len();
    let mut indices : Vec<i64> = (0..size).map(|n| n as i64).collect();
    for _ in 0..mixes {
        for src in 0..size {
            let src = indices.iter().position(|i| *i == src as i64).unwrap();
            let mut dst = src;
            dst = (dst as i64 + data[src]).rem_euclid(size as i64 - 1) as usize;
            if dst == 0 && data[src] < 0 {
                dst = size - 1;
            }
            for vec in [&mut data, &mut indices] {
                let n = vec.remove(src);
                vec.insert(dst, n);
            }
        }
    }
    let zero = data.iter().position(|n| *n == 0).unwrap();
    (1..4).map(|n| data[(zero + 1000 * n).rem_euclid(data.len())]).sum()
}

fn main() {
    let data : Vec<i64> = fs::read_to_string(input_path(2022, 20)).unwrap()
        .replace("\r", "")
        .split("\n")
        .map(|ln| ln.parse().unwrap())
        .collect();
    let output1 = mix(data.clone(), 1);
    let output2 = mix(data.iter().map(|n| 811589153 * n).collect(), 10);
    println!("{}\n{}", output1, output2);
}