use std::env;
use std::path::Path;
use std::path::PathBuf;
use std::fs::File;
use std::io;
use std::io::BufReader;
use std::io::BufRead;
use std::io::Result;
use core::iter::Flatten;

pub fn input_path(day: i8) -> PathBuf {
    let mut path : PathBuf = env::current_exe().expect("current_exe error");
    for _ in 0..4 { path.pop(); }
    path.push(std::format!("inputs/{}.txt", day));
    path
}

pub fn read_lines<P>(path : P) -> Result<Flatten<io::Lines<BufReader<File>>>>
where P: AsRef<Path> {
    Ok(BufReader::new(File::open(path)?).lines().flatten())
}