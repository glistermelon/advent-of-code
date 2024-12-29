use ndarray::Array2;
use utils::{board::{d2::Dir2D, nd::{in_bounds, points_range}}, input_path};
use std::{collections::HashSet, fs};

fn main() {
    let map = fs::read_to_string(input_path(2022, 8)).unwrap()
            .split("\r\n")
            .map(|s| s.to_string())
            .collect::<Vec<String>>();
    let dim = (map.len(), map[0].len());
    let map = Array2::<char>::from_shape_vec(
        dim,
        map.into_iter().flat_map(|s| s.chars().collect::<Vec<char>>()).collect()
    ).unwrap();
    let mut visible = HashSet::new();
    for dir in Dir2D::all_dirs() {
        let mut start_p : (i32, i32) = match dir {
            Dir2D::Up => ((dim.0 - 1) as i32, 0),
            Dir2D::Right => (0, 0),
            Dir2D::Down => (0, (dim.1 - 1) as i32),
            Dir2D::Left => ((dim.0 - 1) as i32, (dim.1 - 1) as i32)
        };
        while in_bounds([dim.0 as i32, dim.1 as i32], <[i32; 2]>::from(start_p)) {
            let mut p = start_p;
            let mut max_h = -1;
            while in_bounds([dim.0 as i32, dim.1 as i32], <[i32; 2]>::from(p)) {
                let h= map[(p.0 as usize, p.1 as usize)].to_digit(10).unwrap() as i32;
                if h > max_h {
                    max_h = h;
                    visible.insert(p);
                }
                p = dir.advance(p, 1);
            }
            start_p = dir.turn_right().advance(start_p, 1);
        }
    }
    let output1 = visible.len();
    let output2 = points_range([dim.0 as i32, dim.1 as i32])
        .map(|start_p| {
            let start_p = (start_p[0], start_p[1]);
            let h = map[[start_p.0 as usize, start_p.1 as usize]];
            let mut score = 1;
            for dir in Dir2D::all_dirs() {
                let mut trees = 0;
                let mut p = dir.advance(start_p, 1);
                while in_bounds([dim.0 as i32, dim.1 as i32], <[i32; 2]>::from(p)) {
                    trees += 1;
                    if map[[p.0 as usize, p.1 as usize]] >= h {
                        break;
                    }
                    p = dir.advance(p, 1);
                }
                score *= trees;
            }
            score
        })
        .max().unwrap();
    println!("{}\n{}", output1, output2);
}