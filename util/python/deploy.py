import shutil
import os

for item_name in os.listdir('../..'):
    if len(item_name) != 4:
        continue
    try:
        int(item_name)
    except:
        continue
    cp_to = f'../../{item_name}/python'
    if os.path.exists(cp_to):
        shutil.copy('util.py', cp_to)
        shutil.copy('board_util.py', cp_to)