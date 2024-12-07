mod util;
use util::input_path;
use std::fs;

use std::vec::Vec;

fn main() {

    let data_str = fs::read_to_string(input_path(4)).unwrap();
    let data = data_str.split("\n").map(|ln| ln.trim()).collect::<Vec<&str>>();

    let mut output : u32 = 0;
    output += data.iter().map(|ln| ln.as_bytes().windows(4).filter(|&w| w == "XMAS".as_bytes() || w == "SAMX".as_bytes()).count() as u32).sum::<u32>();
    output += (0..data.len() - 3).map(
        |r| (0..data[0].len()).map(
            |c| (0..4).map(|i| &data[r + i][c..c + 1]).collect::<String>()
        ).filter(|s| s == "XMAS" || s == "SAMX").count() as u32
    ).sum::<u32>();
    output += (0..data.len() - 3).map(
        |r| (0..data[0].len() - 3).map(
            |c| (0..4).map(|i| &data[r + i][c + i..c + i + 1]).collect::<String>()
        ).filter(|s| s == "XMAS" || s == "SAMX").count() as u32
    ).sum::<u32>();
    output += (0..data.len() - 3).map(
        |r| (3..data[0].len()).map(
            |c| (0..4).map(|i| &data[r + i][c - i..c - i + 1]).collect::<String>()
        ).filter(|s| s == "XMAS" || s == "SAMX").count() as u32
    ).sum::<u32>();
    println!("{}", output);

    output = (1..data.len() as i32 - 1).map(
        |r| (1..data[0].len() as i32 - 1).map(
            |c| [(-1..2).map(|i| &data[(r + i) as usize][(c + i) as usize..(c + i + 1) as usize]).collect::<String>(),
                (-1..2).map(|i| &data[(r + i) as usize][(c - i) as usize..(c - i + 1) as usize]).collect::<String>()
            ]
        ).filter(|[s1, s2]| (s1 == "MAS" || s1 == "SAM") && (s2 == "MAS" || s2 == "SAM")).count() as u32
    ).sum::<u32>();
    println!("{}", output);

}