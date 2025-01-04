use utils::input_path;
use std::fs;
use itertools::Itertools;

fn adj_points(point : &[i32; 3]) -> std::array::IntoIter<[i32; 3], 6> {
    let mut result = [[0; 3]; 6];
    let mut i = 0;
    for delta in [-1, 1] {
        for axis in 0..3 {
            let mut adj = point.clone();
            adj[axis] += delta;
            result[i] = adj;
            i += 1;

        }
    }
    result.into_iter()
}

fn main() {
    let points = fs::read_to_string(input_path(2022, 18)).unwrap()
        .replace("\r", "")
        .split("\n")
        .map(|point_str| {
            let point : (i32, i32, i32) = point_str.split(",").map(|s| s.parse::<i32>().unwrap()).collect_tuple().unwrap();
            [point.0, point.1, point.2]
        })
        .collect_vec();

    let mut output1 = 0;
    for point in points.iter() {
        for adj in adj_points(point) {
            if !points.contains(&adj) {
                output1 += 1;
            }
        }
    }


    let start_point = [
        points.iter().map(|p| p[0]).min().unwrap() - 1,
        points.iter().map(|p| p[1]).min().unwrap() - 1,
        points.iter().map(|p| p[2]).min().unwrap() - 1
    ];
    let end_point = [
        points.iter().map(|p| p[0]).max().unwrap() + 1,
        points.iter().map(|p| p[1]).max().unwrap() + 1,
        points.iter().map(|p| p[2]).max().unwrap() + 1
    ];
    let mut queue = vec![start_point];
    let mut visited = vec![queue[0]];
    let mut output2 = 0;
    while !queue.is_empty() {
        let point = queue.remove(0);
        for adj in adj_points(&point) {
            if visited.contains(&adj)
            || adj.iter().enumerate().any(|(i, n)| *n < start_point[i])
                || adj.iter().enumerate().any(|(i, n)| *n > end_point[i])
            {
                continue;
            }
            if points.contains(&adj) {
                output2 += 1;
            }
            else {
                visited.push(adj);
                queue.push(adj);
            }
        }
    }


    println!("{}\n{}", output1, output2);
}