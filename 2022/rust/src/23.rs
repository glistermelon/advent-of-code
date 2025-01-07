use utils::input_path;
use std::{collections::HashMap, fs};

fn main() {
    let input : Vec<String> = fs::read_to_string(input_path(2022, 23)).unwrap()
        .replace("\r", "")
        .split("\n")
        .map(|s| s.to_string())
        .collect();
    let mut elves : Vec<_> = input.iter().enumerate().flat_map(
        |(y, row)| row.chars()
                                        .enumerate()
                                        .filter_map(move |(x, c)| if c == '#' { Some((y as i32, x as i32)) } else { None } )
    ).collect();
    let mut actions : [(Vec<i32>, Vec<i32>); 4] = [
        (vec![-1], vec![0, -1, 1]),
        (vec![1], vec![0, -1, 1]),
        (vec![0, -1, 1], vec![-1]),
        (vec![0, -1, 1], vec![1])
    ];

    let mut output2 = -1;
    for round in std::iter::successors(Some(1), |&n| Some(n + 1)) {
        let mut claims = HashMap::new();
        for &(y, x) in elves.iter() {
            let mut skip_elf = true;
            'surrounding_elves: for dy in 0..3 {
                for dx in 0..3 {
                    if (dx != 1 || dy != 1) && elves.contains(&(y + dy - 1, x + dx - 1)) {
                        skip_elf = false;
                        break 'surrounding_elves;
                    }
                }
            }
            if skip_elf {
                continue;
            }
            'action_loop: for a in actions.iter() {
                for dy in a.0.iter() {
                    for dx in a.1.iter() {
                        if elves.contains(&(y + dy, x + dx)) {
                            continue 'action_loop;
                        }
                    }
                }
                claims.insert((y, x), (y + a.0[0], x + a.1[0]));
                break;
            }
        }
        actions.rotate_left(1);
        let claims : HashMap<_, _> = claims.iter()
            .filter_map(|(a, b)|{
                if claims.values().filter(|v| **v == *b).count() == 1 {
                    Some((*a, *b))
                }
                else {
                    None
                }
            })
            .collect();
        if claims.is_empty() {
            output2 = round;
            break;
        }
        for c in elves.iter_mut() {
            if claims.contains_key(c) {
                *c = claims[c];
            }
        }
    }

    let min_x = *elves.iter().map(|(_y, x)| x).min().unwrap();
    let max_x = *elves.iter().map(|(_y, x)| x).max().unwrap();
    let min_y = *elves.iter().map(|(y, _x)| y).min().unwrap();
    let max_y = *elves.iter().map(|(y, _x)| y).max().unwrap();

    let mut output1 = 0;
    for x in min_x..(max_x + 1) {
        for y in min_y..(max_y + 1) {
            if !elves.contains(&(y, x)) {
                output1 += 1;
            }
        }
    }
    println!("{}\n{}", output1, output2);

}