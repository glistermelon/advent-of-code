use itertools::Itertools;
use utils::input_path;
use std::fs;
use utils::board::d2::*;
use ndarray::Array2;

#[derive(Clone, Debug)]
struct Blizzard {
    dir : Dir2D,
    pos : (usize, usize)
}

fn main() {

    let input : Vec<String> = fs::read_to_string(input_path(2022, 24)).unwrap()
        .replace("\r", "")
        .split("\n")
        .map(|s| s.to_string())
        .collect();

    let height = input.len();
    let width = input[0].len();

    let mut blizzards = vec![];
    for (y, row) in input.iter().enumerate() {
        for (x, c) in row.chars().enumerate() {
            if c != '.' && c != '#' {
                blizzards.push(Blizzard {
                    dir: Dir2D::from_char(c),
                    pos: (y, x)
                })
            }
        }
    }

    let mut queue = Array2::<bool>::from_shape_vec(
        [height as usize, width as usize], std::iter::repeat(false).take((height * width) as usize).collect::<Vec<bool>>()
    ).unwrap();
    queue[(0, 1)] = true;
    let end_pos = (height - 1, width - 2);
    let mut target = end_pos;
    let mut targets_reached = 0;
    let mut output1 = -1;
    let mut output2 = -1;
    'search: for step in std::iter::successors(Some(1), |&n| Some(n + 1)) {

        for b in blizzards.iter_mut() {
            let mut pos = b.dir.advance(b.pos, 1);
            if pos.0 == 0 {
                pos.0 = height - 2;
            }
            else if pos.1 == 0 {
                pos.1 = width - 2;
            }
            else if pos.0 == height - 1 {
                pos.0 = 1;
            }
            else if pos.1 == width - 1 {
                pos.1 = 1;
            }
            b.pos = pos;
        }

        let mut blizzard_grid = Array2::<bool>::from_shape_vec(
            [height as usize, width as usize], std::iter::repeat(false).take((height * width) as usize).collect::<Vec<bool>>()
        ).unwrap();
        for b in blizzards.iter() {
            blizzard_grid[b.pos] = true;
        }

        let mut next_queue = Array2::<bool>::from_shape_vec(
            [height as usize, width as usize], std::iter::repeat(false).take((height * width) as usize).collect::<Vec<bool>>()
        ).unwrap();

        'drain_queue: for ((y, x), &p) in queue.indexed_iter() {
            if !p {
                continue;
            }
            for (dy, dx) in (0..3).cartesian_product(0..3) {
                //println!("{} {} {} {}", y, x, dy, dx);
                if dx != 1 && dy != 1 {
                    //println!("c1");
                    continue;
                }
                if x + dx == 0 || y + dy == 0 {
                    //println!("c2");
                    continue;
                }
                let y = y + dy - 1;
                let x = x + dx - 1;
                if y >= queue.dim().0 || x >= queue.dim().1 {
                    //println!("c3");
                    continue;
                }
                //println!("passed");
                if (y, x) == target {
                    targets_reached += 1;
                    if targets_reached == 1 {
                        output1 = step;
                    }
                    else if targets_reached == 3 {
                        output2 = step;
                        break 'search;
                    }
                    target = if target == end_pos { (0, 1) } else { end_pos };
                    for (_, p) in next_queue.indexed_iter_mut() {
                        *p = false;
                    }
                    next_queue[(y, x)] = true;
                    break 'drain_queue;
                }
                if (y <= 0 && x != 1) || x <= 0 || (y >= height - 1 && x != end_pos.1) || x >= width - 1
                    || blizzard_grid[(y, x)] {
                    continue;
                }
                next_queue[(y, x)] = true;
            }
        }
        //println!("{:?}", next_queue);
        queue.assign(&next_queue);

    }
    println!("{}\n{}", output1, output2);

}