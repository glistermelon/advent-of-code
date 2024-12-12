import hashlib

data = open('../inputs/4.txt').read()

digits = 5
start_i = 1
for digits in 5, 6:
    i = start_i
    while hashlib.md5((data + str(i)).encode()).hexdigest()[:digits].count('0') != digits:
        i += 1
    start_i = i
    print(i)