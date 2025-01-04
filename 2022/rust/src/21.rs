use utils::input_path;
use std::{fs, collections::HashMap, hash::{DefaultHasher, Hash, Hasher}};

fn hash_str(s: &str) -> u64 {
    let mut h = DefaultHasher::new();
    s.hash(&mut h);
    h.finish()
}

#[derive(Clone, Debug)]
struct Monkey {
    number : Option<i64>,
    arithmetic : char,
    arg1 : u64,
    arg2 : u64
}

fn solve_monkey(monkey : u64, monkeys : &HashMap<u64, Monkey>, mem : &HashMap<u64, i64>) -> Result<i64, ()> {
    if mem.contains_key(&monkey) {
        return Ok(mem[&monkey]);
    }
    if !monkeys.contains_key(&monkey) {
        return Err(())
    }
    let expr = &monkeys[&monkey];
    if let Some(number) = expr.number {
        Ok(number)
    }
    else {
        if [expr.arg1, expr.arg2].iter().any(|k| !monkeys.contains_key(k)) {
            Err(())
        }
        else {
            let a = solve_monkey(expr.arg1, monkeys, mem);
            let b = solve_monkey(expr.arg2, monkeys, mem);
            if a.is_err() {
                a
            }
            else if b.is_err() {
                b
            }
            else {
                let a = a.unwrap();
                let b = b.unwrap();
                match expr.arithmetic {
                    '+' => Ok(a + b),
                    '-' => Ok(a - b),
                    '*' => Ok(a * b),
                    '/' => Ok(a / b),
                    _ => { panic!("unrecognized arithmetic character") }
                }
            }
        }
    }
}

fn reverse_solve(monkey : Monkey, solve : &u64, monkeys : &mut HashMap<u64, Monkey>, mem : &HashMap<u64, i64>) -> Option<i64> {
    let mut sol1 = solve_monkey(monkey.arg1, monkeys, mem);
    let mut sol2 = solve_monkey(monkey.arg2, monkeys, mem);
    let n = monkey.number.unwrap();
    if sol1.is_err() && sol2.is_err() {
        return None;
    }
    if sol1.is_ok() && sol2.is_err() {
        let sol1 = sol1.unwrap();
        sol2 = Ok(match monkey.arithmetic {
            '+' => n - sol1,
            '-' => sol1 - n,
            '*' => n / sol1,
            '/' => sol1 / n,
            _ => { panic!("unrecognized arithmetic character") }
        });
        if monkey.arg2 == *solve {
            return Some(sol2.unwrap());
        }
    }
    else if sol1.is_err() && sol2.is_ok() {
        let sol2 = sol2.unwrap();
        sol1 = Ok(match monkey.arithmetic {
            '+' => n - sol2,
            '-' => n + sol2,
            '*' => n / sol2,
            '/' => n * sol2,
            _ => { panic!("unrecognized arithmetic character") }
        });
        if monkey.arg1 == *solve {
            return Some(sol1.unwrap())
        }

    }
    monkeys.get_mut(&monkey.arg1).unwrap().number = Some(sol1.unwrap());
    monkeys.get_mut(&monkey.arg2).unwrap().number = Some(sol2.unwrap());
    if let Some(s) = reverse_solve(monkeys[&monkey.arg1].clone(), solve, monkeys, mem) {
        Some(s)
    }
    else if let Some(s) = reverse_solve(monkeys[&monkey.arg2].clone(), solve, monkeys, mem) {
        Some(s)
    }
    else {
        None
    }
}

fn main() {

    let mut monkeys = HashMap::new();
    for ln in fs::read_to_string(input_path(2022, 21)).unwrap()
            .replace("\r", "")
            .split("\n") {
        {
            let expr = &ln[6..];
            let expr = if expr.contains(' ') {
                let mut expr: std::str::SplitWhitespace<'_> = expr.split_whitespace();
                Monkey {
                    number: None,
                    arg1: hash_str(expr.next().unwrap()),
                    arithmetic: expr.next().unwrap().chars().nth(0).unwrap(),
                    arg2: hash_str(expr.next().unwrap())
                }
            }
            else {
                Monkey {
                    number: Some(expr.parse().unwrap()),
                    arithmetic: '\0',
                    arg1: 0,
                    arg2: 0
                }
            };
            monkeys.insert(hash_str(&ln[..4]), expr);
        }
    }

    let root_hash = hash_str("root");
    let output1 = solve_monkey(root_hash, &monkeys, &HashMap::new()).unwrap();

    let human_hash = hash_str("humn");
    monkeys.get_mut(&root_hash).unwrap().arithmetic = '\0';
    monkeys.get_mut(&human_hash).unwrap().arithmetic = '\0';
    monkeys.get_mut(&human_hash).unwrap().number = None;
    let root_monkey = &monkeys[&root_hash];
    let mut solve_hash : Option<u64> = None;
    let equal_to = solve_monkey(root_monkey.arg1, &monkeys, &HashMap::new()).unwrap_or_else(|_| {
        solve_hash = Some(root_monkey.arg1);
        solve_monkey(root_monkey.arg2, &monkeys, &HashMap::new()).unwrap()
    });
    let solve_hash = if solve_hash.is_none() {
        root_monkey.arg2
    }
    else {
        solve_hash.unwrap()
    };
    monkeys.get_mut(&solve_hash).unwrap().number = Some(equal_to);
    let output2 = reverse_solve(monkeys[&solve_hash].clone(), &human_hash, &mut monkeys, &HashMap::new()).unwrap();

    println!("{}\n{}", output1, output2);
}