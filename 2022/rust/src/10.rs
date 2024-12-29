use utils::{read_lines, input_path};

fn main() {
    let mut cycle = 0;
    let mut targ_cycle = 20;
    let mut reg : i32 = 1;
    let mut output1 = 0;
    let mut output2 = std::iter::repeat(' ').take(40 * 6).collect::<Vec<char>>();
    for ln in read_lines(input_path(2022, 10)).unwrap() {
        if ((reg - 1)..(reg + 2)).contains(&(cycle % 40)) {
            output2[cycle as usize] = '#';
        }
        let noop = ln == "noop";
        if noop {
            cycle += 1;
        }
        else {
            cycle += 1;
            if ((reg - 1)..(reg + 2)).contains(&(cycle % 40)) {
                output2[cycle as usize] = '#';
            }
            cycle += 1;
        }
        if cycle >= targ_cycle {
            output1 += reg * targ_cycle;
            targ_cycle += 40;
        }
        if !noop {
            reg += ln.split_whitespace().nth(1).unwrap().parse::<i32>().unwrap();
        }
    }
    let output2 = output2.chunks(40)
        .map(|chunk| chunk.iter().collect::<String>())
        .collect::<Vec<String>>()
        .join("\n");
    println!("{}\n{}", output1, output2);
}