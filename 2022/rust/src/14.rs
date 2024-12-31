use ndarray::Array2;
use utils::input_path;
use std::fs;

#[derive(Clone)]
enum State {
    Stone,
    Sand,
    Open
}

// returns true if abyss is reached
fn drop_sand(map : &mut Array2<State>, source : [usize; 2]) -> bool {
    
    let mut source = source;
    let limit = source;
    let abyss_y = map.shape()[0];
    while matches!(map[source], State::Open) {
        source[0] += 1;
        if source[0] == abyss_y {
            return true;
        }
    }
    source[0] -= 1;

    loop {

        let mut left = source;
        while !matches!(map[(left[0] + 1, left[1])], State::Open) && matches!(map[(left[0] + 1, left[1] - 1)], State::Open)  {
            left = [left[0] + 1, left[1] - 1];
        }
        if matches!(map[(left[0] + 1, left[1])], State::Open) && drop_sand(map, left) {
            return true;
        }
        while left != source {
            map[left] = State::Sand;
            left = [left[0] - 1, left[1] + 1];
        }

        let mut right = source;
        while !matches!(map[(right[0] + 1, right[1])], State::Open) && matches!(map[(right[0] + 1, right[1] + 1)], State::Open)  {
            right = [right[0] + 1, right[1] + 1];
        }
        if matches!(map[(right[0] + 1, right[1])], State::Open) && drop_sand(map, right) {
            return true;
        }
        while right != source {
            map[right] = State::Sand;
            right = [right[0] - 1, right[1] - 1];
        }

        map[source] = State::Sand;
        if source == limit {
            break;
        }
        source[0] -= 1;

    }

    false
}

fn main() {

    let shape = (200, 1000);
    let mut map = Array2::<State>::from_shape_vec(
        shape, std::iter::repeat(State::Open).take(shape.0 * shape.1).collect()
    ).unwrap();
    for ln in fs::read_to_string(input_path(2022, 14)).unwrap()
        .replace("-> ", "")
        .replace("\r", "")
        .split("\n") {
        let vertices = ln.split_whitespace()
            .map(
                |s| {
                    let mut vertex = s.split(",")
                        .map(|s| s.parse::<usize>())
                        .flatten()
                        .collect::<Vec<usize>>();
                    vertex.reverse();
                    vertex
                }
            ).collect::<Vec<Vec<usize>>>();
        for i in 0..(vertices.len() - 1) {
            let v0 = &vertices[i];
            let v1 = &vertices[i + 1];
            if v0[0] != v1[0] {
                for y in *[v0[0], v1[0]].iter().min().unwrap()..(*[v0[0], v1[0]].iter().max().unwrap() + 1) {
                    map[[y, v0[1]]] = State::Stone;
                }
            }
            else {
                for x in *[v0[1], v1[1]].iter().min().unwrap()..(*[v0[1], v1[1]].iter().max().unwrap() + 1) {
                    map[[v0[0], x]] = State::Stone;
                }
            }
        }
    }

    drop_sand(&mut map, [0, 500]);
    let output1 = map.iter().filter(|s| matches!(s, State::Sand)).count();

    for s in map.iter_mut() {
        if matches!(s, State::Sand) {
            *s = State::Open;
        }
    }
    let floor = 2 + map.indexed_iter()
        .filter_map(|(i, s)| match s { State::Stone => Some(i.0), _ => None } )
        .max()
        .unwrap();
    for x in 0..shape.1 {
        map[(floor, x)] = State::Stone;
    }

    drop_sand(&mut map, [0, 500]);
    let output2 = map.iter().filter(|s| matches!(s, State::Sand)).count();

    println!("{}\n{}", output1, output2);
}