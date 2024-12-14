from util import *
from board_util import *

W = 101
H = 103

class Robot:
    def __init__(self, pos : list[int], delta : tuple[int, int]):
        self.pos = pos
        self.delta = delta

robots = []
for ln in open('../inputs/14.txt'):
    ln = ln.strip().replace('=', '').replace('p', '').replace('v', '').replace(',', ' ').split()
    robots.append(Robot([int(ln[1]), int(ln[0])], (int(ln[3]), int(ln[2]))))    

part2 = input("Solve part 1 or 2? (1/2): ").strip() == '2'

step = 0

while True:

    board = Board([[' ' for x in range(W)] for y in range(H)])
    for i, r in enumerate(robots):
        r.pos[0] = (r.pos[0] + r.delta[0]) % H
        r.pos[1] = (r.pos[1] + r.delta[1]) % W
        board[r.pos] = '#'
    
    step += 1
    
    if part2:

        for r in robots:
            if len(board.expand_region(tuple(r.pos))) > 30:
                print(board)
                print('Maybe:', step)
                exit()
    
    elif step == 100:
        
        output = 1
        comps = (lambda a, b : a > b, lambda a, b : a < b)
        for i in range(len(comps)):
            for j in range(len(comps)):
                comp0 = comps[i]
                comp1 = comps[j]
                quad = 0
                for r in robots:
                    if comp0(r.pos[0], H // 2) and comp1(r.pos[1], W // 2):
                        quad += 1
                output *= quad
        print(output)
        break