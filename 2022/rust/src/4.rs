use utils::{input_path, read_lines};

fn main() {
    let mut output1 = 0;
    let mut output2 = 0;
    for ln in read_lines(input_path(2022, 4)).unwrap() {
        let nums = ln.split(|c| c == ',' || c == '-').map(|s| s.parse::<u8>()).flatten().collect::<Vec<u8>>();
        if (nums[0] <= nums[2] && nums[1] >= nums[3]) || (nums[2] <= nums[0] && nums[3] >= nums[1]) {
            output1 += 1;
        }
        if (nums[1] >= nums[2] && nums[0] <= nums[2]) || (nums[3] >= nums[0] && nums[2] <= nums[0]) {
            output2 += 1;
        }
    }
    println!("{}\n{}", output1, output2);
}