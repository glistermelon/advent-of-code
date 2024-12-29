use utils::input_path;
use utils::read_lines;

fn priority(c : char) -> u32 {
    if c == c.to_ascii_uppercase() {
        return (c as u32) - ('A' as u32) + 27;
    }
    else {
        return (c as u32) - ('a' as u32) + 1;
    }
}

fn main() {
    let mut output1 = 0;
    for ln in read_lines(input_path(2022, 3)).unwrap() {
        let i = ln.len() / 2;
        for c in ln[0..i].chars() {
            if ln[i..].contains(c) {
                output1 += priority(c);
                break;
            }
        }
    }
    let mut output2 = 0;
    for lines in read_lines(input_path(2022, 3)).unwrap().collect::<Vec<_>>().chunks(3) {
        for c in lines[0].chars() {
            if lines[1].contains(c) && lines[2].contains(c) {
                output2 += priority(c);
                break;
            }
        }
    }
    println!("{}\n{}", output1, output2);
}