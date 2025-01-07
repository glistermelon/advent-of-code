use utils::{input_path, read_lines};

fn to_snafu(mut n : i64) -> String {
    if n == 0 {
        return "0".to_string();
    }
    let mut snafu = String::new();
    let mut p = 1;
    while n != 0 {
        let mut m = (n / p) % 5;
        if m > 2 {
            m -= 5;
        }
        n -= m * p;
        p *= 5;
        snafu.insert(0, match m {
            -2 => '=',
            -1 => '-',
            x => x.to_string().chars().nth(0).unwrap()
        });
    }
    snafu
}

fn from_snafu(snafu : &str) -> i64 {
    let mut n = 0;
    let mut p = 1;
    for c in snafu.chars().rev() {
        let m = match c {
            '=' => -2,
            '-' => -1,
            x => x.to_digit(10).unwrap() as i64
        };
        n += m * p;
        p *= 5;
    }
    n
}

fn main() {
    let output = read_lines(input_path(2022, 25)).unwrap()
        .map(|snafu| from_snafu(&snafu))
        .sum::<i64>();
    let output = to_snafu(output);
    println!("{}", output);
}