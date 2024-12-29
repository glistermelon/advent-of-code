use std::collections::HashSet;

use utils::{input_path, read_lines};
use utils::board::d2::{surrounding_points, Dir2D, delta_taxi};

fn solve(num_knots : usize) -> usize {
    let mut knots : Vec<(i32, i32)> = std::iter::repeat((0, 0)).take(num_knots).collect();
    let mut visited = HashSet::new();
    visited.insert((0, 0));
    for (dir, n) in read_lines(input_path(2022, 9)).unwrap()
        .map(
            |ln| {
                let ln = ln.split_whitespace().collect::<Vec<_>>();
                (Dir2D::from_char(ln[0].chars().next().unwrap()), ln[1].parse::<usize>().unwrap())
            }
        ) {
        for _ in 0..n {
            knots[0] = dir.advance(knots[0], 1);
            for i in 0..(knots.len() - 1) {
                if knots[i] == knots[i + 1] || surrounding_points(knots[i]).contains(&knots[i + 1]) {
                    continue;
                }
                let dirs = Dir2D::from_delta(delta_taxi(knots[i + 1], knots[i]));
                knots[i + 1] = dirs.0.unwrap().advance(knots[i + 1], 1);
                if dirs.1.is_some() {
                    knots[i + 1] = dirs.1.unwrap().advance(knots[i + 1], 1);
                }
            }
            visited.insert(*knots.last().unwrap());
        }
    }
    visited.len()
}

fn main() {
    println!("{}\n{}", solve(2), solve(10));
}