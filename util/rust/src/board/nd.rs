use ndarray::{prelude::*, NdIndex, StrideShape};
use num::PrimInt;
use std::{fmt::Debug, ops::{Index, IndexMut}};

#[derive(Clone, Debug)]
pub struct Board<T, D>
where D: Dimension
{
    pub arr : Array<T, D>
}

impl<T, D> Board<T, D>
where D: Dimension, T : Debug
{

    pub fn from_shape_vec<Sh>(shape : Sh, vec : Vec<T>) -> Self
    where Sh: Into<StrideShape<D>> + Debug
    {
        Board {
            arr: Array::from_shape_vec(shape, vec).unwrap()
        }
    }

    pub fn get<I, const N : usize>(&self, i: [I; N]) -> Option<&T>
    where
        D: Dimension,
        [usize; N]: NdIndex<D>,
        usize: TryFrom<I>,
        I: PrimInt + Copy {
        let mut j = [0; N];
        for k in 0..N {
            let n = usize::try_from(i[k]);
            if n.is_err() {
                return None;
            }
            j[k] = n.ok().unwrap();
        }
        self.arr.get(j)
    }

    pub fn get_mut<I, const N : usize>(&mut self, i: [I; N]) -> Option<&mut T>
    where
        D: Dimension,
        [usize; N]: NdIndex<D>,
        usize: TryFrom<I>,
        I: PrimInt + Copy {
        let mut j = [0; N];
        for k in 0..N {
            let n = usize::try_from(i[k]);
            if n.is_err() {
                return None;
            }
            j[k] = n.ok().unwrap();
        }
        self.arr.get_mut(j)
    }

}

impl<T, D, const N : usize> Index<[usize; N]> for Board<T, D>
where
    D: Dimension,
    [usize; N]: NdIndex<D>
{
    type Output = T;
    fn index(&self, i: [usize; N]) -> &Self::Output {
        &self.arr[i]
    }
}

impl<T, D, const N : usize> IndexMut<[usize; N]> for Board<T, D>
where
    D: Dimension,
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