use itertools::Itertools;
use utils::input_path;
use std::collections::HashMap;
use std::fs;

////////////////////////////////////////////////////////////////////////////
/// The runtime on this is really really really terrible (MINUTES in RUST bruh)
/// I know that it would be much faster if I implemented memoization or multiprocessing
/// But it's New Years Eve and I want to spend time with my family
/// so screw that I'm outta here
////////////////////////////////////////////////////////////////////////////

struct Valve {
    name : String,
    flow : i32,
    id : i32
}

impl std::fmt::Debug for Valve {
    fn fmt(&self, f: &mut std::fmt::Formatter) -> std::fmt::Result {
        write!(f, "{}", self.name)
    }
}

impl std::hash::Hash for Valve {
    fn hash<H: std::hash::Hasher>(&self, state: &mut H) {
        self.id.hash(state);
    }
}

struct DFS<'a> {
    valves: Vec<&'a Valve>,
    paths: &'a HashMap<(i32, i32), i32>
}

impl<'a> DFS<'a> {
    fn solve(&mut self, current : &'a Valve, pressure : i32, time : i32) -> i32 {
        let mut max_pressure = 0;
        for i in 0..self.valves.len() {
            let valve = self.valves[i];
            let path = self.paths[&(current.id, valve.id)];
            if time - path - 1 <= 0 {
                continue;
            }
            self.valves.remove(i);
            let new_pressure = self.solve(valve, valve.flow * (time - path - 1), time - path - 1);
            self.valves.insert(i, valve);
            if new_pressure > max_pressure {
                max_pressure = new_pressure;
            }
        }
        pressure + max_pressure
    }
}

struct DFS2<'a> {
    valves: Vec<&'a Valve>,
    paths: &'a HashMap<(i32, i32), i32>,
    start_valve: &'a Valve
}

impl<'a> DFS2<'a> {
    fn solve(&mut self, current : &'a Valve, pressure : i32, time : i32) -> i32 {
        let mut max_pressure = 0;
        for i in 0..self.valves.len() {
            let valve = self.valves[i];
            let path = self.paths[&(current.id, valve.id)];
            if time - path - 1 <= 0 {
                continue;
            }
            self.valves.remove(i);
            let new_pressure = self.solve(valve, valve.flow * (time - path - 1), time - path - 1);
            self.valves.insert(i, valve);
            if new_pressure > max_pressure {
                max_pressure = new_pressure;
            }
        }
        let elephant_pressure = (DFS {
            valves: self.valves.clone(),
            paths: &self.paths
        }).solve(self.start_valve, 0, 26);
        if elephant_pressure > max_pressure {
            max_pressure = elephant_pressure;
        }
        pressure + max_pressure
    }
}

fn shortest_path(start_valve : &Valve, end_valve : &Valve, neighbors : &HashMap<i32, Vec<&Valve>>) -> i32 {
    let mut queue = vec![start_valve];
    let mut visited : Vec<&Valve> = vec![start_valve];
    let mut step = 1;
    while !queue.is_empty() {
        let mut next : Vec<&Valve> = vec![];
        for &valve in queue.drain(..).into_iter().flat_map(|v| neighbors[&v.id].iter()) {
            if visited.iter().filter(|&&v| std::ptr::eq(v, valve)).nth(0).is_some() { continue; }
            visited.push(valve);
            if std::ptr::eq(valve, end_valve) {
                return step;
            }
            next.push(valve);
        }
        queue.append(&mut next);
        step += 1;
    }
    panic!("no shortest path found between {} and {}", start_valve.name, end_valve.name);
}

fn main() {

    let mut valves = vec![];
    let mut neighbors = HashMap::new();
    let input = fs::read_to_string(input_path(2022, 16)).unwrap()
        .replace("=", " ")
        .replace(";", "")
        .replace("\r", "")
        .replace(",", "")
        .split("\n")
        .map(|s| s.to_string())
        .collect::<Vec<String>>();
    {
        let mut paths : HashMap<String, Vec<String>> = HashMap::new();
        for ln in input.iter() {
            let ln = ln.split_whitespace().into_iter().collect::<Vec<_>>();
            let name = ln[1].to_string();
            let valve = Valve { name: name.clone(), flow: ln[5].parse().unwrap(), id : valves.len() as i32 };
            valves.push(valve);
            paths.insert(name, ln[10..].iter().map(|s| s.to_string()).collect_vec());
        }
        for (src, dsts) in paths.iter() {
            neighbors.insert(
                valves.iter().filter(|v| v.name == *src).nth(0).unwrap().id,
                dsts.iter().map(
                    |dst| valves.iter().filter(|v| v.name == *dst).nth(0).unwrap()
                ).collect_vec()
            );
        }
    }

    let mut paths = HashMap::new();
    for combo in valves.iter().combinations(2) {
        let v1 = combo[0];
        let v2 = combo[1];
        if (v1.flow == 0 && v1.name != "AA") || (v2.flow == 0 && v2.name != "AA") {
            continue;
        }
        let path = shortest_path(v1, v2, &neighbors);
        paths.insert((v1.id, v2.id), path);
        paths.insert((v2.id, v1.id), path);
    }

    let mut j = 0;
    for mut i in 0..valves.len() {
        i -= j;
        if valves[i].flow == 0 && valves[i].name != "AA" {
            valves.remove(i);
            j += 1;
        }
    }

    let start_valve = valves.iter().filter(|v| v.name == "AA").nth(0).unwrap();

    let output1 = (DFS {
        valves: valves.iter().filter(|v| v.name != "AA").collect(),
        paths: &paths
    }).solve(start_valve, 0, 30);

    println!("{}", output1);

    let output2 = (DFS2 {
        valves: valves.iter().filter(
            |&v| v.name != "AA"
        ).collect(),
        paths: &paths,
        start_valve
    }).solve(start_valve, 0, 26);

    println!("{}", output2);

}