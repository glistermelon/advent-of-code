from utils import *
from board_util import *
import itertools
import functools

numeric_keypad = {
    '7': (0, 0),
    '8': (0, 1),
    '9': (0, 2),
    '4': (1, 0),
    '5': (1, 1),
    '6': (1, 2),
    '1': (2, 0),
    '2': (2, 1),
    '3': (2, 2),
    '0': (3, 1),
    'A': (3, 2)
}

direc_keypad = {
    '^': (0, 1),
    'A': (0, 2),
    '<': (1, 0),
    'v': (1, 1),
    '>': (1, 2)
}

def keypad_encode(input_seq : str, keypad : bool):
    keypad = numeric_keypad if keypad else direc_keypad
    output = ''
    for w in windows('A' + input_seq, 2):
        p0 = keypad[w[0]]
        p1 = keypad[w[1]]
        dy, dx = delta_taxi(p0, p1)
        if dy or dx:
            moves = []
            if dy > 0: moves.append('v' * dy)
            elif dy < 0: moves.append('^' * -dy)
            if dx > 0: moves.append('>' * dx)
            elif dx < 0: moves.append('<' * -dx)
            if len(moves) == 2:
                check1 = Dir.down.advance(p0, dy) in keypad.values()
                check2 = Dir.right.advance(p0, dx) in keypad.values()
                if check1 and check2:
                    if output:
                        yield output
                        output = ''
                    yield tuple((moves[1], moves[0]))
                    continue
                elif not check1:
                    output += moves[1] + moves[0]
                else:
                    output += moves[0] + moves[1]
            else:
                output += moves[0]
        output += 'A'
    if output:
        yield output

@functools.cache
def best_encoding(input : str, keypad : bool, iteration : int):
    if not iteration:
        return len(input)
    output = 0
    for chunk in keypad_encode(input, keypad):
        if type(chunk) is str:
            output += best_encoding(chunk, False, iteration - 1)
        else:
            output += best_permutation(chunk, iteration - 1)
    return output

@functools.cache
def best_permutation(pair : tuple[str, str], iteration : int):
    if not iteration:
        return len(pair[0]) + len(pair[1]) + 1
    outputs = [0, 0]
    for i, prm in enumerate(itertools.permutations(pair)):
        outputs[i] = best_encoding(prm[0] + prm[1] + 'A', False, iteration)
    return min(outputs)

codes = open('../inputs/21.txt').read().splitlines(keepends=False)

for keypads in 3, 26:
    print(sum(int(code[:-1]) * best_encoding(code, True, keypads) for code in codes))