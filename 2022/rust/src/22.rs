use cgmath::InnerSpace;
use ndarray::Array2;
use ndarray::Ix2;
use ndarray::s;
use utils::input_path;
use utils::board::nd::*;
use utils::board::d2::*;
use utils::ttoa2;
use std::fs;
use itertools::Itertools;
use cgmath::{Quaternion, Deg, Vector3, Rotation, Rotation3};
use bimap::{BiMap, BiHashMap};

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// This takes forever to run because I went for a mathematical solution that would work on almost any input
/// To avoid having to do low level math myself I just used cgmath which forces me to use floats
/// And doing anything with floats takes forever
/// The bijective hashmap probably slows it down too
/// I might optimize this later if I'm bored but right now I want to do the next day
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

#[derive(PartialEq, Eq)]
enum Side {
    FRONT,
    BACK,
    RIGHT,
    LEFT,
    BOTTOM,
    TOP
}

fn parse_face(
    arr : &Array2<char>,
    size : usize,
    pos : (usize, usize),
    rot : Quaternion<f32>,
    parsed_sides : &mut Vec<Side>,
    output : &mut BiHashMap<(usize, usize), (i32, i32, i32)>
) {

    let start_vec = Vector3::new(0.0, -1.0, 0.0);
    let face_vec = rot.rotate_vector(start_vec);
    let face_vec_elements = (face_vec.x.round() as i32, face_vec.y.round() as i32, face_vec.z.round() as i32);
    let face_dir = match face_vec_elements {
        x if x == (0, 0, 1) => Side::TOP,
        x if x == (0, 0, -1) => Side::BOTTOM,
        x if x == (0, 1, 0) => Side::BACK,
        x if x == (0, -1, 0) => Side::FRONT,
        x if x == (1, 0, 0) => Side::RIGHT,
        x if x == (-1, 0, 0) => Side::LEFT,
        _ => { panic!("Failed to match rotated start vector {:?} to a direction", face_vec); }
    };
    if parsed_sides.iter().contains(&face_dir) {
        return;
    }

    for x_usz in 0..size {
        for y_usz in 0..size {
            let mut x = x_usz as i32;
            let mut y = y_usz as i32;
            x = 2 * x - size as i32;
            y = -(2 * y - size as i32);
            let mut v = Vector3::new(x, -(size as i32), y);
            if size % 2 == 0 {
                v.x += 1;
                v.z -= 1;
            }
            let v = Vector3::new(v.x as f32, v.y as f32, v.z as f32);
            let mut v = rot.rotate_vector(v);
            round_vector3(&mut v);
            output.insert(
                (pos.0 + y_usz, pos.1 + x_usz),
                (v.x as i32, v.y as i32, v.z as i32)
            );
        }
    }
    parsed_sides.push(face_dir);

    for &(point, unit_vec) in [
        if pos.1 >= size { Some(((pos.0, pos.1 - size), (-1, 0, 0))) } else { None },
        if pos.1 + size < arr.dim().1 { Some(((pos.0, pos.1 + size), (1, 0, 0))) } else { None },
        if pos.0 >= size { Some(((pos.0 - size, pos.1), (0, 0, 1))) } else { None },
        if pos.0 + size < arr.dim().0 { Some(((pos.0 + size, pos.1), (0, 0, -1))) } else { None }
    ].iter().flatten() {
        if arr[ttoa2!(point)] != ' ' {
            let unit_vec = Vector3::new(unit_vec.0 as f32, unit_vec.1 as f32, unit_vec.2 as f32);
            let axis = rot.rotate_vector(start_vec.cross(unit_vec));
            let rot1 = Quaternion::<f32>::from_axis_angle(axis.normalize(), Deg(90.0));
            let rot1 = rot1 * rot;
            parse_face(arr, size, point, rot1, parsed_sides, output);
        }
    }

}

fn round_vector3(v : &mut Vector3<f32>) {
    v.x = v.x.round();
    v.y = v.y.round();
    v.z = v.z.round();
}

fn main() {

    let mut input : Vec<String> = fs::read_to_string(input_path(2022, 22)).unwrap()
        .replace("\r", "")
        .split("\n")
        .map(|s| s.to_string())
        .collect();
    let directions = input.pop().unwrap().trim().to_string();
    let directions_numbers : Vec<i32> = directions.split(&['L', 'R']).map(|s| s.parse().unwrap()).collect();
    let directions_turns : Vec<Dir2D> = directions.chars().filter_map(|c| match c {
        'L' => Some(Dir2D::Left),
        'R' => Some(Dir2D::Right),
        _ => None
    }).collect();
    input.pop();
    {
        let d2_width = input.iter().map(|r| r.len()).max().unwrap();
        for r in input.iter_mut() {
            r.extend(std::iter::repeat(' ').take(d2_width - r.len()));
        }
    }

    let board = Board::<char, Ix2>::from_shape_vec(
        [input.len(), input[0].len()],
        input.iter().flat_map(|r| r.chars()).collect_vec()
    );
    let mut pos : (i32, i32) = (0, board.arr.slice(s![0, ..]).iter().position(|c| *c == '.').unwrap() as i32);
    let mut prev_pos = pos;
    let mut dir = Dir2D::Right;
    for pair in directions_numbers.iter().zip_longest(directions_turns.iter()) {
        let (steps, turn) = pair.left_and_right();
        if let Some(&steps) = steps {
            for _ in 0..steps {
                pos = dir.advance(pos, 1);
                if *board.get(ttoa2!(pos)).unwrap_or(&' ') == ' ' {
                    loop {
                        pos = dir.rev_advance(pos, 1);
                        if *board.get(ttoa2!(pos)).unwrap_or(&' ') == ' ' {
                            pos = dir.advance(pos, 1);
                            break;
                        }
                    }
                }
                if *board.get(ttoa2!(pos)).unwrap() != '.' {
                    pos = prev_pos;
                    break;
                }
                prev_pos = pos;
            }
        }
        if let Some(&turn) = turn {
            dir = match turn {
                Dir2D::Left => dir.turn_left(),
                Dir2D::Right => dir.turn_right(),
                _ => { panic!("This will never happen"); }
            };
        }
    }
    let dir_number = match dir {
        Dir2D::Right => 0,
        Dir2D::Down => 1,
        Dir2D::Left => 2,
        Dir2D::Up => 3
    };
    let output1 = 1000 * (pos.0 + 1) + 4 * (pos.1 + 1) + dir_number;

    let mut transform : BiHashMap<(usize, usize), (i32, i32, i32)> = BiMap::new();
    let size = board.arr.dim().0.max(board.arr.dim().1) / 4;
    let pos = (0, board.arr.slice(s![0, ..]).iter().position(|c| *c == '.').unwrap());
    parse_face(
        &board.arr,
        size,
        pos,
        Quaternion::new(1.0, 0.0, 0.0, 0.0),
        &mut Vec::new(),
        &mut transform
    );
    let pos = *transform.get_by_left(&pos).unwrap();
    let mut pos = Vector3::new(pos.0 as f32, pos.1 as f32, pos.2 as f32);
    let mut prev_pos = pos;
    let mut dir : Vector3::<f32> = Vector3::new(1.0, 0.0, 0.0);
    let mut prev_dir = dir;
    let mut rot : Vector3::<f32> = Vector3::new(0.0, -1.0, 0.0);
    let mut prev_rot = rot;
    let size = size as f32;
    for pair in directions_numbers.iter().zip_longest(directions_turns.iter()) {
        let (steps, turn) = pair.left_and_right();
        if let Some(&steps) = steps {
            for _ in 0..steps {
                pos += 2.0 * dir;
                round_vector3(&mut pos);
                let rotate = !transform.right_values().contains(&(pos.x as i32, pos.y as i32, pos.z as i32));
                if rotate {
                    pos -= 2.0 * dir * size;
                    round_vector3(&mut pos);
                    let q = Quaternion::<f32>::from_axis_angle(rot.cross(dir).normalize(), Deg(90.0));
                    rot = dir;
                    dir = q.rotate_vector(dir);
                    pos = q.rotate_vector(pos);
                    round_vector3(&mut dir);
                    round_vector3(&mut pos);
                }
                let pos_2d = transform.get_by_right(&(pos.x as i32, pos.y as i32, pos.z as i32));
                if pos_2d.is_none() || board.arr[*pos_2d.unwrap()] != '.' {
                    pos = prev_pos;
                    if rotate {
                        dir = prev_dir;
                        rot = prev_rot;
                    }
                    break;
                }
                prev_pos = pos;
                prev_dir = dir;
                prev_rot = rot;
            }
        }
        if let Some(&turn) = turn {
            let q = Quaternion::<f32>::from_axis_angle(
                rot, Deg(if matches!(turn, Dir2D::Left) { 90.0 } else { -90.0 })
            );
            dir = q.rotate_vector(dir);
            round_vector3(&mut dir);
        }
    }

    let mut dp0 = pos;
    let mut dp1 = pos + 2.0 * dir;
    if transform.get_by_right(&(dp1.x.round() as i32, dp1.y.round() as i32, dp1.z.round() as i32)).is_none() {
        dp1 = dp0;
        dp0 -= 2.0 * dir;
    }
    let dp0 = *transform.get_by_right(&(dp0.x.round() as i32, dp0.y.round() as i32, dp0.z.round() as i32)).unwrap();
    let dp1 = *transform.get_by_right(&(dp1.x.round() as i32, dp1.y.round() as i32, dp1.z.round() as i32)).unwrap();
    let dir_number = match (dp1.0 as i32 - dp0.0 as i32, dp1.1 as i32 - dp0.1 as i32) {
        (0, 1) => 0,
        (1, 0) => 1,
        (0, -1) => 2,
        (-1, 0) => 3,
        _ => { panic!("Unable to match dir number to {:?}", dir); }
    };

    let pos = transform.get_by_right(&(pos.x.round() as i32, pos.y.round() as i32, pos.z.round() as i32)).unwrap();

    let output2 = 1000 * (pos.0 + 1) + 4 * (pos.1 + 1) + dir_number;
    println!("{}\n{}", output1, output2);

}