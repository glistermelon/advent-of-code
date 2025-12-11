from dataclasses import dataclass
from itertools import product
import pulp

from sympy.polys.matrices import DM, DomainMatrix
from sympy.polys.domains import GF
import sympy as sp

puzzle_input = open('../inputs/10.txt').read()

@dataclass
class Machine:
    lights: list[int]
    joltages: list[int]
    components: list[list[int]]

machines: list[Machine] = None

def process_input():
    global machines
    machines = []
    for ln in puzzle_input.splitlines(keepends=False):
        m = Machine(None, None, None)
        m.lights = [(0 if c == '.' else 1) for c in ln[1 : ln.index(']')]]
        m.joltages = list(map(int, ln[ln.index('{') + 1 : ln.index('}')].split(',')))
        m.components = []
        for c in ln[ln.index('(') : ln.index('{')].split():
            c = eval(c.replace(')', ',)'))
            c = [(1 if i in c else 0) for i in range(len(m.lights))]
            m.components.append(c)
        m.components = list(zip(*m.components))
        machines.append(m)

def solve_part_1():

    total_min_presses = 0

    for m in machines:

        A = DM(m.components, GF(2))
        b = DM([[n] for n in m.joltages], GF(2))
        sys = DomainMatrix.hstack(A, b)

        rref, pivots = sys.rref()
        rref = rref.to_Matrix()

        exprs = []
        for row_i in range(rref.rows):
            row = rref.row(row_i)
            if any(row[col_i] == 1 for col_i in pivots):
                expr = row[-1]
                expr -= sum(sp.symbols(f'x_{i}') for i, n in enumerate(row[:-1]) if n != 0 and i not in pivots)
                exprs.append(expr)

        nonpivots = [sp.symbols(f'x_{i}') for i in range(rref.cols - 1) if i not in pivots]

        total_min_presses += min(
            sum(values) + sum(expr.subs({ s: v for s, v in zip(nonpivots, values) }) % 2 for expr in exprs)
            for values in product(range(2), repeat=len(nonpivots))
        )

    return total_min_presses

def solve_part_2():

    total_min_presses = 0

    for m in machines:

        prob = pulp.LpProblem()
        vars = [pulp.LpVariable(f"x_{i}", lowBound=0, cat='Integer') for i in range(len(m.components[0]))]
        prob += pulp.lpSum(vars)
        for i, joltage in enumerate(m.joltages):
            prob += sum(vars[j] for j, v in enumerate(m.components[i]) if v != 0) == joltage
        prob.solve(pulp.PULP_CBC_CMD(msg=0))
    
        if prob.status != pulp.LpStatusOptimal:
            print('couldnt solve one of them')
            exit()

        min_presses = sum(int(v.varValue) for v in vars)

        total_min_presses += min_presses
    
    return total_min_presses

process_input()
print(solve_part_1())
print(solve_part_2())