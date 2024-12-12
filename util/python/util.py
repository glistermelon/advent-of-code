import functools

# dunno if i will ever need this
def dim(arr, iterable_types = (list, tuple)):
    d = 0
    a = arr
    while type(a) in iterable_types:
        d += 1
        a = a[0]
    return d

def is_iterable(x):
    try:
        iter(x)
    except TypeError:
        return False
    return True

counter = 0
# n dimensional find even though it works regardless of whether arr actually has a dimension
def find_nd(arr, v, stop_recursion = False):
    if len(arr) == 0:
        return []
    out = []
    for i in range(len(arr)):
        if arr[i] == v:
            out.append((i,))
        elif (not stop_recursion) and is_iterable(arr[i]):
            for j in find_nd(arr[i], v, type(arr[i]) is str):
                out.append((i,) + j)
    return out

def piecewise(a, b, opp, out_type = None):
    if out_type is None:
        out_type = type(a)
    if len(a) != len(b):
        raise RuntimeError('piecewise(a, b) requires len(a) == len(b)')
    return out_type(opp(a[i], b[i]) for i in range(len(a)))

def find_with_key(indexable, key):
    for i, v in enumerate(indexable):
        if key(v):
            return i
    return -1

def prod(iterable):
    return functools.reduce(lambda a, b : a * b, iterable)

def windows(iterable, size : int):
    iterator = iter(iterable)
    window = []
    while len(window) != size:
        window.append(next(iterator))
    yield tuple(window) if type(iterable) is not str else ''.join(window)
    for item in iterator:
        window.pop(0)
        window.append(item)
        yield tuple(window) if type(iterable) is not str else ''.join(window)