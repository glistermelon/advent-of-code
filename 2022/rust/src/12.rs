use ndarray::Array2;
use utils::input_path;
use std::collections::HashSet;
use std::fs;
use utils::board::d2::{adj_points, in_bounds};
use utils::find_nd;

fn shortest_path(start : (i32, i32), end : (i32, i32), map : &Array2<char>, shape : (i32, i32)) -> Option<i32> {
    let mut visited = HashSet::new();
    visited.insert(start);
    let mut queue = vec![start];
    let mut step = 1;
    while !queue.is_empty() {
        let mut next_queue = vec![];
        for node in queue.drain(..).into_iter() {
            for adj in adj_points(node, 1, false) {
                if visited.contains(&adj) ||
                    !in_bounds(shape, adj) ||
                    (map[(adj.0 as usize, adj.1 as usize)] as i16) -
                    (map[(node.0 as usize, node.1 as usize)] as i16) > 1
                    { continue; }
                if adj == end {
                    return Some(step);
                }
                visited.insert(adj);
                next_queue.push(adj);
            }
        }
        queue.append(&mut next_queue);
        step += 1;
    }
    None
}

fn main() {
    let map = fs::read_to_string(input_path(2022, 12)).unwrap().replace("\r", "");
    let shape = (map.chars().filter(|&c| c == '\n').count() + 1, map.find("\n").unwrap());
    let mut map = Array2::<char>::from_shape_vec(
        shape,
        map.replace("\n", "").chars().collect::<Vec<char>>()
    ).unwrap();
    let shape = (shape.0 as i32, shape.1 as i32);
    let start = find_nd(&map, 'S')[0];
    let end = find_nd(&map, 'E')[0];
    map[start] = 'a';
    map[end] = 'z';
    let start = (start.0 as i32, start.1 as i32);
    let end = (end.0 as i32, end.1 as i32);
    let output1 = shortest_path(start, end, &map, shape).unwrap();
    let output2 = map.indexed_iter()
        .flat_map(|(p, &c)| if c != 'a' { None } else {
            let p = (p.0 as i32, p.1 as i32);
            shortest_path(p, end, &map, shape)
        })
        .min()
        .unwrap();
    println!("{}\n{}", output1, output2);
}