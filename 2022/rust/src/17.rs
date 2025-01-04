use utils::input_path;
use std::{collections::HashSet, fs};
use itertools::Itertools;

#[derive(Debug)]
struct Landing {
    x : i64,
    height : i64,
    chars_i : usize
}

struct Tetris<CharIt>
where CharIt: Iterator<Item = char>
{
    rocks : [Vec<(i64, i64)>; 5],
    chars : CharIt,
    chars_len : usize,
    chars_i : usize,
    first_cycle : bool,
    height : i64,
    occupied : HashSet<(i64, i64)>,
    landings : Vec<Landing>,
    landings_offset : usize
}

impl<CharIt> Tetris<CharIt>
where CharIt: Iterator<Item = char>
{

    fn drop_block(&mut self, block_num : usize) {

        let mut rock = self.rocks[block_num].clone().into_iter()
            .map(|(y, x)| (self.height + 4 + y, 2 + x))
            .collect_vec();
        loop {
            let right = self.chars.next().unwrap() == '>';
            self.chars_i += 1;
            if self.chars_i == self.chars_len {
                self.chars_i = 0;
            }
            let mut rock1 = rock.iter()
                .map(|&(y, x)| if right { (y, x + 1) } else { (y, x - 1) })
                .collect_vec();
            if rock1[0].1 < 0 || rock1[2].1 >= 7 || rock1.iter().any(|p| self.occupied.contains(p)) {
                rock1 = rock.iter().map(|p| *p).collect_vec();
            }
            else {
                rock = rock1.iter().map(|p| *p).collect_vec();
            }
            let rock1 = rock1.iter()
                .map(|&(y, x)| (y - 1, x))
                .collect_vec();
            let can_go_down = !rock1.iter().any(|p| self.occupied.contains(p));
            if can_go_down {
                rock = rock1;
            }
            else {
                let y = rock[1].0;
                if y > self.height {
                    self.height = y;
                }
                for p in rock.iter() {
                    self.occupied.insert(*p);
                }
                if !self.first_cycle {
                    self.landings.push(Landing { x: rock[0].1, height: self.height, chars_i: self.chars_i });
                }
                else {
                    self.landings_offset += 1;
                }
            }
            if self.chars_i == 0 {
                self.first_cycle = false;
            }
            if !can_go_down {
                break;
            }
        }

    }

    fn new(chars : CharIt, chars_len : usize) -> Self {
        Tetris {
            rocks: [
                vec![(0, 0), (0, 1), (0, 3), (0, 2)],
                vec![(1, 0), (2, 1), (1, 2), (1, 1), (0, 1)],
                vec![(0, 0), (2, 2), (0, 2), (0, 1), (1, 2)],
                vec![(0, 0), (3, 0), (1, 0), (2, 0)],
                vec![(0, 0), (1, 0), (0, 1), (1, 1)]
            ],
            chars,
            chars_len,
            chars_i: 0,
            first_cycle: true,
            height: -1,
            occupied: HashSet::from_iter((0..7).map(|x| (-1, x))),
            landings: vec![],
            landings_offset: 0
        }
    }

}

fn main() {

    let input_str = fs::read_to_string(input_path(2022, 17)).unwrap();

    let chars = input_str.chars().cycle();
    let mut tetris = Tetris::new(chars, input_str.len());
    for i in 0..2022 {
        tetris.drop_block(i % 5);
    }
    let output1 = tetris.height + 1;

    let chars = input_str.chars().cycle();
    let mut tetris = Tetris::new(chars, input_str.len());
    let mut i_generator = (0..5 as usize).cycle();
    let offset_height;
    let pattern_height;
    let offset_length;
    let pattern_length;
    'outer: loop {
        let i = i_generator.next().unwrap();
        tetris.drop_block(i);
        if let Some(last) = tetris.landings.last() {
            for (i, landing) in tetris.landings.iter().enumerate().skip(i + (-(tetris.landings_offset as i64)).rem_euclid(5) as usize).step_by(5) {
                if !(landing.x == last.x && landing.chars_i == last.chars_i && !std::ptr::eq(landing, last)) {
                    continue;
                }
                offset_height = landing.height;
                pattern_height = last.height - offset_height;
                offset_length = 1 + i as i64 + tetris.landings_offset as i64;
                pattern_length = (tetris.landings.len() - i - 1) as i64;
                break 'outer
            }
        }
    }
    let offset_i = offset_length as usize - 1 - tetris.landings_offset;
    let pattern_segments = (1000000000000 - offset_length) / pattern_length;
    let pattern_blocks = pattern_segments * pattern_length;
    let extra_blocks = 1000000000000 - offset_length - pattern_blocks;
    let output2 = 1 + offset_height + pattern_segments * pattern_height + (tetris.landings[offset_i + extra_blocks as usize].height - offset_height);

    println!("{}\n{}", output1, output2);
}