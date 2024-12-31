use utils::{board::d2::dist_taxi_abs, input_path};
use std::{collections::HashSet, fs, ops::RangeInclusive};
use range_set::RangeSet;

fn transform(mut p : (i64, i64), bound : &i64, flip : bool) -> i64 {
    if !flip {
        if p.0 < 0 || p.1 < 0 {
            let m = -[p.0, p.1].iter().min().unwrap();
            p = (p.0 + m, p.1 + m);

        }
        if p.0 >= *bound || p.1 >= *bound {
            let m = *[p.0, p.1].iter().max().unwrap() - bound + 1;
            p = (p.0 - m, p.1 - m);
        }
    }
    else {
        if p.0 < 0 {
            p = (0, p.1 + p.0);
        }
        else if p.1 < 0 {
            p = (p.0 + p.1, 0);
        }
        else if p.0 >= *bound {
            p = (bound - 1, p.1 - p.0 + bound - 1);
        }
        else if p.1 >= *bound {
            p = (p.0 - p.1 + bound - 1, bound - 1);
        }
    }
    let mut x = if !flip { p.0 } else { bound - 1 - p.0 };
    let mut y = p.1;
    let neg_out = y > x;
    if neg_out {
        x = -x + bound - 1;
        y = -y + bound - 1;
    }
    let out = y + x*y + x*bound - y*bound + (x - x.pow(2) - y.pow(2) - y) / 2;
    let out = if neg_out {
        bound - 1 - out
    }
    else {
        out
    };
    out
}

fn inv_transform(mut z : i64, bound : &i64, flip : bool) -> (i64, i64) {
    let (x, y) = if z < 0 {
        let (x, y) = inv_transform(bound - z - 1, bound, false);
        (-x + bound - 1, -y + bound - 1)
    }
    else {
        let mut s = *bound;
        let mut d = 0;
        while z >= s {
            d += 1;
            s += bound - d;
        }
        s -= bound - d;
        z -= s;
        (d + z, z)
    };
    if flip {
        (bound - 1 - x, y)
    }
    else {
        (x, y)
    }
}

fn get_diagonals(s : &(i64, i64), r : i64, bound : &i64) -> [(((i64, i64), (i64, i64)), RangeSet::<[RangeInclusive<i64>; 1]>); 4] {
    let diagonals = [
        ((s.0, s.1 - r), (s.0 + r, s.1)),
        ((s.0 - r, s.1), (s.0, s.1 + r)),
        ((s.0, s.1 - r), (s.0 - r, s.1)),
        ((s.0 + r, s.1), (s.0, s.1 + r))
    ];
    diagonals.iter().enumerate()
        .map(
            |(i, (p1, p2))|
            (
                (*p1, *p2),
                RangeSet::<[RangeInclusive<i64>; 1]>::from_ranges([
                    transform(*p1, &bound, i >= 2)..=transform(*p2, &bound, i >= 2),
                ])
            )
        )
        .collect::<Vec<_>>()
        .try_into()
        .unwrap()
}

fn get_aligned_diagonal(sensor : &(i64, i64), range : i64, align_with : ((i64, i64), (i64, i64)), bound : &i64) -> Option<RangeInclusive<i64>> {
    let (align1, align2) = align_with;
    let flip = align2.0 < align1.0;
    let align1 = if !flip { align1 } else { (bound - 1 - align1.0, align1.1) };
    let align_diag_idx = align1.0 - align1.1;
    let origin_diag_idx = if !flip { sensor.0 } else { bound - 1 - sensor.0 } - sensor.1;
    let origin_diff = align_diag_idx - origin_diag_idx;
    if origin_diff.abs() > range {
        None
    }
    else {
        let d2 = (origin_diff - range).abs() / 2;
        let d1 = (origin_diff + range).abs() / 2;
        let sensor = if flip { (bound - 1 - sensor.0, sensor.1) } else { *sensor };
        Some(RangeInclusive::<i64>::new(
            transform((origin_diff + sensor.0 - d1, sensor.1 - d1), bound, false),
            transform((origin_diff + sensor.0 + d2, sensor.1 + d2), bound, false)
        ))
    }
}

fn border_intersection(sensors : &Vec<((i64, i64), i64)>, sensor : &(i64, i64), range : i64, bound : &i64) -> [RangeSet::<[RangeInclusive<i64>; 1]>; 4] {
    get_diagonals(sensor, range + 1, bound)
        .map(|(points, mut point_range)| {
            for (other_sensor, other_range) in sensors.iter().filter(|&&s| s.0 != *sensor) {
                if let Some(aligned) = get_aligned_diagonal(other_sensor, *other_range, points, bound) {
                    point_range.remove_range(aligned);
                }
            }
            point_range
        })
}

fn main() {

    let mut sensors = vec![];
    let mut beacons = HashSet::new();
    for ln in fs::read_to_string(input_path(2022, 15)).unwrap()
        .replace("\r", "")
        .replace("=", " ")
        .replace(":", "")
        .replace(",", "")
        .split("\n") {
        let ln = ln.split_whitespace().collect::<Vec<_>>();
        let ln = [3, 5, 11, 13].iter().map(|&i| ln[i].parse::<i64>()).flatten().collect::<Vec<_>>();
        let sensor = (ln[0], ln[1]);
        let beacon = (ln[2], ln[3]);
        let dist = dist_taxi_abs(sensor, beacon);
        sensors.push((sensor, dist));
        beacons.insert(beacon);
    }

    let targ_row = 2000000;
    let mut blocked = RangeSet::<[RangeInclusive<i64>; 1]>::new();
    for &(sensor, range) in sensors.iter() {
        let diff = sensor.1.abs_diff(targ_row) as i64;
        if diff > range { continue; }
        let diff = diff.abs_diff(range) as i64;
        blocked.insert_range((sensor.0 - diff)..=(sensor.0 + diff));
    }
    let output1 = blocked.as_ref().iter().map(|r| {
        r.end() - r.start() + 1 -
            (beacons.iter().filter(|b| b.1 == targ_row && r.contains(&b.0)).count() as i64)
    }).sum::<i64>();

    let mut output2 : Option<(i64, i64)> = None;
    let bound : i64 = 4000001;
    for (sensor, range) in sensors.iter() {
        let range = *range;
        let inner = border_intersection(&sensors, sensor, range, &bound);
        let outer = border_intersection(&sensors, sensor, range + 1, &bound);
        for (i, (inner_ranges, outer_ranges)) in inner.iter().zip(outer.iter()).enumerate() {
            let inner_ranges = inner_ranges.clone().into_smallvec();
            let outer_ranges = outer_ranges.clone().into_smallvec();
            if inner_ranges.len() != 1 || outer_ranges.len() != 0 { continue; }
            let inner_range = &inner_ranges[0];
            if inner_range.start() != inner_range.end() { continue; }
            output2 = Some(inv_transform(*inner_range.start(), &bound, i >= 2));
            break;
        }
        if output2.is_some() {
            break;
        }
    }
    let output2 = output2.unwrap();
    let output2 = 4000000 * output2.0 + output2.1;

    println!("{}\n{:?}", output1, output2);

}