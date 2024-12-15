from itertools import combinations
from math import ceil

boss_health = None
boss_damage = None
boss_armor = None

class ShopItem:
    def __init__(self, name : str, cost : int, damage : int, armor : int):
        self.name = name
        self.cst = cost
        self.dmg = damage
        self.arm = armor

weapon_shop = []
armor_shop = []
ring_shop = []

current_shop = None
for ln in open('../inputs/21.txt'):
    ln = ln.replace(':', ' ').split()
    if not ln: continue
    label = ln[0]
    if boss_health is None: boss_health = int(ln[2])
    elif boss_damage is None: boss_damage = int(ln[1])
    elif boss_armor is None: boss_armor = int(ln[1])
    elif label == 'Weapons': current_shop = weapon_shop
    elif label == 'Armor': current_shop = armor_shop
    elif label == 'Rings': current_shop = ring_shop
    else:
        if current_shop is ring_shop:
            label += ' ' + ln.pop(1)
        current_shop.append(ShopItem(label, *map(int, ln[1:])))

player_health = 100

lowest_gold = 10000000000000000
ideal_combo = None
most_gold = 0
worst_combo = None
for weapons in [(w,) for w in weapon_shop]:
    for armors in [()] + [(a,) for a in armor_shop]:
        for num_rings in range(3):
            for rings in combinations(ring_shop, num_rings):
                items = weapons + armors + rings
                player_damage = sum(item.dmg for item in items)
                player_armor = sum(item.arm for item in items)
                player_loss_time = ceil(player_health / max(boss_damage - player_armor, 1))
                boss_loss_time = ceil(boss_health / max(player_damage - boss_armor, 1))
                gold = sum(item.cst for item in items)
                if player_loss_time >= boss_loss_time:
                    if gold < lowest_gold:
                        lowest_gold = gold
                        ideal_combo = items
                else:
                    if gold > most_gold:
                        most_gold = gold
                        worst_combo = items

print('----------- Part 1 -----------')
print('Gold:', lowest_gold)
for item in ideal_combo:
    print(item.name)

print('----------- Part 2 -----------')
print('Gold:', most_gold)
for item in worst_combo:
    print(item.name)