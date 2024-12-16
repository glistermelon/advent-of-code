for starting_a in 0, 1:

    registers = {
        'a': starting_a,
        'b': 0
    }

    instructions = [ln.replace(',', '').replace('+', '').split() for ln in open('../inputs/23.txt')]

    ip = 0

    while ip >= 0 and ip < len(instructions):
        instr = instructions[ip]
        mnemonic = instr[0]
        args = instr[1:]
        if mnemonic == 'hlf':
            registers[args[0]] //= 2
        elif mnemonic == 'tpl':
            registers[args[0]] *= 3
        elif mnemonic == 'inc':
            registers[args[0]] += 1
        elif mnemonic == 'jmp':
            ip += int(args[0])
            continue
        elif mnemonic == 'jie':
            if registers[args[0]] % 2 == 0:
                ip += int(args[1])
                continue
        elif mnemonic == 'jio':
            if registers[args[0]] == 1:
                ip += int(args[1])
                continue
        ip += 1

    print(registers['b'])