import requests
import dotenv
import os

year = input('year: ')

dotenv.load_dotenv()

for day in range(1, 26):
    r = requests.get(
        f'https://adventofcode.com/{year}/day/{day}/input',
        headers={'Cookie': f'session={os.getenv('AOC_SESSION')}'}
    )
    if r.status_code != 200:
        print('Failed: day', day)
        continue
    if not os.path.exists(f'{year}/inputs'):
        os.mkdir(f'{year}/inputs')
    with open(f'{year}/inputs/{day}.txt', 'w') as f:
        f.write(r.text)