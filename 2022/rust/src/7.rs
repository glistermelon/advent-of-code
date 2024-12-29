use utils::{input_path, read_lines};

fn main() {
    let mut size_stack : Vec<u64> = vec![];
    let mut all_sizes : Vec<u64> = vec![];
    let mut output1 = 0;
    for ln in read_lines(input_path(2022, 7)).unwrap() {
        if ln.starts_with("$ cd") {
            let d = &ln[5..];
            if d == ".." {
                let size = *size_stack.last().unwrap();
                all_sizes.push(size);
                if size <= 100000 {
                    output1 += size;
                }
                size_stack.pop();
                *size_stack.last_mut().unwrap() += size;
            }
            else {
                size_stack.push(0);
            }
        }
        else if !(ln.starts_with("d") || ln.starts_with("$")) {
            *size_stack.last_mut().unwrap() += ln[0..ln.find(" ").unwrap()].parse::<u64>().unwrap();
        }
    }
    for s in size_stack.iter().rev().scan(0, |sum, &size| {
        *sum += size;
        Some(*sum)
    }) {
        all_sizes.push(s);
    }
    let required_space = 30000000 - (70000000 - all_sizes.last().unwrap());
    let output2 = all_sizes.iter().filter(|&&s| s >= required_space).min().unwrap();
    println!("{}\n{}", output1, output2);
}