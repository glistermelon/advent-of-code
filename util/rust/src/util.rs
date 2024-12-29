use std::env;
use std::fmt::Debug;
use std::path::Path;
use std::path::PathBuf;
use std::fs::File;
use std::io;
use std::io::BufReader;
use std::io::BufRead;
use std::io::Result;
use core::iter::Flatten;
use ndarray::Dimension;
use ndarray::Array;

pub mod board;

#[allow(dead_code)]
pub fn input_path(year : i32, day: i8) -> PathBuf {
    let mut path : PathBuf = env::current_exe().expect("current_exe error");
    for _ in 0..3 { path.pop(); }
    path.push(std::format!("{}/inputs/{}.txt", year, day));
    path
}

#[allow(dead_code)]
pub fn read_lines<P>(path : P) -> Result<Flatten<io::Lines<BufReader<File>>>>
where P: AsRef<Path> {
    Ok(BufReader::new(File::open(path)?).lines().flatten())
}

pub struct Windows<It, T, const N: usize>
where It: Iterator<Item = T>
{
    iter: It,
    buf: Vec<T>
}

impl<It, T, const N: usize> Windows<It, T, N>
where It: Iterator<Item = T>
{
    pub fn new(iter: It) -> Self {
        Self {
            iter,
            buf: vec![]
        }
    }
}

impl<It, T, const N: usize> Iterator for Windows<It, T, N>
where It: Iterator<Item = T>, T: Debug + Clone
{
    type Item = [T; N];

    fn next(&mut self) -> Option<Self::Item> {
        if self.buf.is_empty() {
            for _ in 0..N {
                self.buf.push(self.iter.next().unwrap());
            }   
        }
        else {
            if let Some(next) = self.iter.next() {
                self.buf.rotate_left(1);
                *self.buf.last_mut().unwrap() = next;
            }
            else {
                return None;
            }
        }
        Some(Self::Item::try_from(self.buf.clone()).unwrap())
    }
}

pub struct WindowsDyn<It, T>
where It: Iterator<Item = T>
{
    iter: It,
    size: usize,
    buf: Vec<T>
}

impl<It, T> WindowsDyn<It, T, >
where It: Iterator<Item = T>
{
    pub fn new(iter: It, size : usize) -> Self {
        Self {
            iter,
            size,
            buf: vec![]
        }
    }
}

impl<It, T> Iterator for WindowsDyn<It, T>
where It: Iterator<Item = T>, T: Debug + Clone
{
    type Item = Vec<T>;

    fn next(&mut self) -> Option<Self::Item> {
        if self.buf.is_empty() {
            for _ in 0..self.size {
                self.buf.push(self.iter.next().unwrap());
            }   
        }
        else {
            if let Some(next) = self.iter.next() {
                self.buf.rotate_left(1);
                *self.buf.last_mut().unwrap() = next;
            }
            else {
                return None;
            }
        }
        Some(self.buf.clone())
    }
}

pub fn find_nd<A, D>(array: &Array<A, D>, target: A) -> Vec<D::Pattern>
where A: PartialEq, D: Dimension
{
    array
        .indexed_iter()
        .filter_map(|(index, value)| if *value == target { Some(index) } else { None })
        .collect()
}