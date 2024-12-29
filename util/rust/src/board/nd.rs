use ndarray::{prelude::*, Data, NdIndex};
use num::PrimInt;
use std::ops::{Index, IndexMut};

pub struct Board<T, D>
where T: Data, D: Dimension
{
    pub arr : Array<T, D>
}

impl<T, D> Board<T, D>
where T: Data, D: Dimension
{
    
}

impl<T, D, const N : usize> Index<[usize; N]> for Board<T, D>
where
    T: Data, D: Dimension,
    [usize; N]: NdIndex<D>
{
    type Output = T;
    fn index(&self, i: [usize; N]) -> &Self::Output {
        &self.arr[i]
    }
}

impl<T, D, const N : usize> IndexMut<[usize; N]> for Board<T, D>
where
    T: Data, D: Dimension,
    [usize; N]: NdIndex<D>
{
    fn index_mut(&mut self, i: [usize; N]) -> &mut T {
        &mut self.arr[i]
    }
}

pub fn in_bounds<T, const N : usize>(bounds : [T; N], p : [T; N]) -> bool
where T: PrimInt
{
    let zero = T::from(0).unwrap();
    for (&n, &b) in p.iter().zip(bounds.iter()) {
        if n < zero || n >= b { return false; }
    }
    true
}

pub struct PointsIt<T, const N: usize>
where T: PrimInt
{
    coords: [T; N],
    bounds: [T; N],
}

impl<T, const N: usize> PointsIt<T, N>
where T: PrimInt + std::ops::AddAssign<i32>
{
    fn new(bounds: [T; N]) -> Self {
        PointsIt { coords: [T::from(0).unwrap(); N], bounds }
    }
}

impl<T, const N: usize> Iterator for PointsIt<T, N>
where T: PrimInt + std::ops::AddAssign<i32>
{
    type Item = [T; N];
    fn next(&mut self) -> Option<Self::Item> {
        if self.coords[0] == self.bounds[0] {
            return None;
        }
        let ret= self.coords;
        for i in (1..N).rev() {
            self.coords[i] += 1;
            if self.coords[i] == self.bounds[i] {
                self.coords[i] = T::from(0).unwrap();
                self.coords[i - 1] += 1;
            }
            else {
                break;
            }
        }
        Some(ret)
    }
}

pub fn points_range<T, const N: usize>(bounds : [T; N]) -> PointsIt<T, N>
where T: PrimInt + std::ops::AddAssign<i32>
{
    PointsIt::new(bounds)
}