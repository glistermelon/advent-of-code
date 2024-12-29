use utils::{input_path, WindowsDyn};
use std::fs;

fn find_marker(s : &str, n : usize) -> usize {
    WindowsDyn::new(s.chars(), n)
    .position(
        |w| w.iter()
            .enumerate()
            .all(|(i, c)| !w[0..i].contains(c))
    )
    .unwrap() + n
}

fn main() {
    let input = fs::read_to_string(input_path(2022, 6)).unwrap();
    let output1 = find_marker(&input, 4);
    let output2 = find_marker(&input, 14);
    println!("{}\n{}", output1, output2);
}