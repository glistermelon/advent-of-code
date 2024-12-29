use utils::{input_path};
use std::{cell::RefCell, fs};

struct Monkey {
    items : Vec<u128>,
    op : fn(u128, u128) -> u128,
    op_n : u128,
    test : u128,
    success_throw : usize,
    fail_throw : usize,
    inspected : usize
}

fn main() {
    for part1 in [true, false] {
        let monkeys = fs::read_to_string(input_path(2022, 11)).unwrap()
        .split("\r\n\r\n")
        .map(|section| {
            let lines = section.split("\r\n").map(|ln| ln.to_string()).collect::<Vec<String>>();
            let items = lines[1][18..]
                .split(", ")
                .map(|s| s.parse::<u128>())
                .flatten()
                .collect::<Vec<u128>>();
            let op_ln = &lines[2][19..];
            let op_fn : fn(u128, u128) -> u128 = if op_ln == "old * old" {
                |n, _| n * n
            }
            else if op_ln.chars().nth(4).unwrap() == '*' {
                |n, m| n * m
            }
            else {
                |n, m| n + m
            };
            let op_n = lines[2][25..].parse::<u128>().unwrap_or(0);
            RefCell::new(
                Monkey {
                    items,
                    op: op_fn,
                    op_n,
                    test: lines[3][21..].parse::<u128>().unwrap(),
                    success_throw: lines[4][29..].parse::<usize>().unwrap(),
                    fail_throw: lines[5][30..].parse::<usize>().unwrap(),
                    inspected: 0
                }
            )
        })
        .collect::<Vec<RefCell<Monkey>>>();
        let modulo : u128 = monkeys.iter().map(|m| m.borrow().test).product();
        for _ in 0..(if part1 { 20 } else { 10000 }) {
            for i in 0..monkeys.len() {
                let monkey = unsafe { &mut *monkeys.get(i).unwrap().as_ptr() };
                let success_monkey = unsafe { &mut *monkeys.get(monkey.success_throw).unwrap().as_ptr() };
                let fail_monkey = unsafe { &mut *monkeys.get(monkey.fail_throw).unwrap().as_ptr() };
                monkey.inspected += monkey.items.len();
                for item in monkey.items.drain(..) {
                    let mut item = (monkey.op)(item, monkey.op_n) % modulo;
                    if part1 {
                        item /= 3;
                    }
                    if item % monkey.test == 0 {
                        success_monkey.items.push(item);
                    }
                    else {
                        fail_monkey.items.push(item);
                    }
                }
            }
        }
        let mut output = monkeys.iter()
            .map(|m| unsafe { (*m.as_ptr()).inspected })
            .collect::<Vec<_>>();
        output.sort();
        output.reverse();
        let output = output[0] * output[1];
        println!("{}", output);
    }
}