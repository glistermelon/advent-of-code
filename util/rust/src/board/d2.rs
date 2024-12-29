use num::{PrimInt, Signed};
use std::fmt::{Debug, Display};

use crate::board::nd;

pub fn in_bounds<T>(bounds : (T, T), p : (T, T)) -> bool
where T: PrimInt
{
    nd::in_bounds([bounds.0, bounds.1], [p.0, p.1])
}

#[derive(Copy, Clone, Debug)]
pub enum Dir2D {
    Up,
    Right,
    Down,
    Left
}

impl Dir2D {

    pub fn all_dirs() -> [Dir2D; 4] {
        [Dir2D::Up, Dir2D::Right, Dir2D::Down, Dir2D::Left]
    }

    pub fn as_int(&self) -> i8
    {
        match self {
            Dir2D::Up => 0,
            Dir2D::Right => 1,
            Dir2D::Down => 2,
            Dir2D::Left => 3
        }
    }

    pub fn from_int<T>(i : T) -> Dir2D
    where T: Into<i32>, T: Display
    {
        match i.into() {
            0 => Dir2D::Up,
            1 => Dir2D::Right,
            2 => Dir2D::Down,
            3 => Dir2D::Left,
            x => { panic!("No Dir associaated with int {}", x); }
        }
    }

    pub fn from_char(c : char) -> Dir2D {
        match c {
            '^' | 'U' => Dir2D::Up,
            '>' | 'R' => Dir2D::Right,
            'v' | 'D' => Dir2D::Down,
            '<' | 'L' => Dir2D::Left,
            x => { panic!("No Dir associaated with char {}", x); }
        }
    }

    pub fn from_delta<T>(delta : (T, T)) -> (Option<Dir2D>, Option<Dir2D>)
    where T: PartialOrd<i32>
    {
        let mut dirs = vec![];
        if delta.0 > 0 {
            dirs.push(Dir2D::Down);
        }
        else if delta.0 < 0 {
            dirs.push(Dir2D::Up);
        }
        if delta.1 > 0 {
            dirs.push(Dir2D::Right);
        }
        else if delta.1 < 0 {
            dirs.push(Dir2D::Left);
        }
        let ret1 = if dirs.len() > 0 { Some(dirs[0]) } else { None };
        let ret2 = if dirs.len() > 1 { Some(dirs[1]) } else { None };
        return (ret1, ret2);
    }

    pub fn turn_right(&self) -> Dir2D {
        Dir2D::from_int((self.as_int() + 1) % 4)
    }

    pub fn turn_left(&self) -> Dir2D {
        Dir2D::from_int((self.as_int() + 3) % 4)
    }

    pub fn flip(&self) -> Dir2D {
        Dir2D::from_int((self.as_int() + 2) % 4)
    }

    pub fn advance<T>(&self, p : (T, T), n : T) -> (T, T)
    where T: PrimInt
    {
        match self {
            Dir2D::Up => (p.0 - n, p.1),
            Dir2D::Down => (p.0 + n, p.1),
            Dir2D::Right => (p.0, p.1 + n),
            Dir2D::Left => (p.0, p.1 - n)
        }
    }

}

pub fn delta_taxi<T>(p0 : (T, T), p1 : (T, T)) -> (T, T)
where T: PrimInt
{
    (p1.0 - p0.0, p1.1 - p0.1)
}

pub fn dist_taxi<T>(p0 : (T, T), p1 : (T, T)) -> (T, T)
where T: PrimInt + Signed
{
    ((p1.0 - p0.0).abs(), (p1.1 - p0.1).abs())
}

// The following aren't iterators because it would be a giant pain in the ass to make them iterators
// Hopefully I'll never pass a huge radius to any of them...

pub fn adj_points<T>(point : (T, T), radius : T, inclusive : bool) -> Vec<(T, T)>
where i64: TryFrom<T>, T: TryFrom<i64>, <i64 as TryFrom<T>>::Error: Debug, <T as TryFrom<i64>>::Error: Debug
{
    let point_i64 = (i64::try_from(point.0).unwrap(), i64::try_from(point.1).unwrap());
    let radius_i64 = i64::try_from(radius).unwrap();
    let mut points = vec![];
    for dx in (-radius_i64)..(radius_i64 + 1) {
        let abs_dy = radius_i64 - dx.abs();
        let mut iter_over = vec![];
        if inclusive {
            iter_over = ((-abs_dy)..(abs_dy + 1)).collect();
        }
        else {
            iter_over.push(abs_dy);
            if abs_dy != 0 {
                iter_over.push(-abs_dy);
            }
        }
        for dy in iter_over.into_iter() {
            if dx != 0 || dy != 0 {
                points.push((
                    T::try_from(point_i64.0 + dy).unwrap(),
                    T::try_from(point_i64.1 + dx).unwrap()
                ));
            }
        }
    }
    points
}

pub fn corner_points<T>(p : (T, T)) -> Vec<(T, T)>
where T: PrimInt
{
    let mut points = vec![];
    for dx in [-1, 1] {
        for dy in [-1, 1] {
            points.push((p.0 + T::from(dy).unwrap(), p.1 + T::from(dx).unwrap()));
        }
    }
    points
}

pub fn surrounding_points<T>(p : (T, T)) -> Vec<(T, T)>
where T: PrimInt
{
    let mut points = vec![];
    for dx in [-1, 0, 1] {
        for dy in [-1, 0, 1] {
            if dx == 0 && dy == 0 {
                continue;
            }
            points.push((p.0 + T::from(dy).unwrap(), p.1 + T::from(dx).unwrap()));
        }
    }
    points
}