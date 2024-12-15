from itertools import combinations
from math import ceil

boss_health = None
boss_damage = None

for ln in open('../inputs/22.txt'):
    ln = ln.replace(':', ' ').split()
    if not ln: continue
    label = ln[0]
    if boss_health is None: boss_health = int(ln[2])
    elif boss_damage is None: boss_damage = int(ln[1])

class Spell:

    def __init__(self, name : str, mana : int, fn):
        self.name = name
        self.mana = mana
        self.fn = fn
    
    def activate(self, round):
        round.mana -= self.mana
        self.fn(round)
    
    def copy(self):
        return Spell(self.name, self.mana, self.fn)

class Effect(Spell):

    def __init__(self, name : str, mana : int, turns : int, fn):
        super().__init__(name, mana, None)
        self.turns = turns
        self.timer = 0
        self.fn = fn
    
    def activate(self, round):
        round.mana -= self.mana
        self.timer = self.turns
    
    def copy(self):
        cp = Effect(self.name, self.mana, self.turns, self.fn)
        cp.timer = self.timer
        return cp

class Round:

    def __init__(self, player_health, boss_health, mana):
        self.player_health = player_health
        self.boss_health = boss_health
        self.mana = mana
        self.armor = 0
        self.spells = []

    def apply_effects(self):
        for spell in self.spells:
            if isinstance(spell, Effect) and spell.timer > 0:
                spell.fn(self)
                spell.timer -= 1
    
    def copy(self):
        copy = Round(self.player_health, self.boss_health, self.mana)
        copy.spells = [spell.copy() for spell in self.spells]
        return copy

def magic_missile(r : Round):
    r.boss_health -= 4

def drain(r : Round):
    r.boss_health -= 2
    r.player_health += 2

def shield(r : Round):
    r.armor += 7

def poison(r : Round):
    r.boss_health -= 3

def recharge(r : Round):
    r.mana += 101

spells = [
    Spell('magic missile', 53, magic_missile),
    Spell('drain', 73, drain),
    Effect('shield', 113, 6, shield),
    Effect('poison', 173, 6, poison),
    Effect('recharge', 229, 5, recharge)
]

for part1 in True, False:

    least_mana = 9999999999

    def play(round : Round, mana_spent = 0, player_turn : bool = True):

        global least_mana

        if mana_spent >= least_mana:
            return
        
        if round.boss_health <= 0:
            least_mana  = mana_spent
            return
        
        round.armor = 0
        
        if (not part1) and player_turn:
            round.player_health -= 1
            if round.player_health <= 0:
                return

        round.apply_effects()

        if round.boss_health <= 0:
            least_mana = mana_spent
            return
        
        if player_turn:

            for i, spell in enumerate(round.spells):
                if round.mana >= spell.mana and not (isinstance(spell, Effect) and spell.timer != 0):
                    new_round = round.copy()
                    new_round.spells[i].activate(new_round)
                    play(new_round, mana_spent + spell.mana, not player_turn)

        else:

            round.player_health -= max(boss_damage - round.armor, 1)
            if round.player_health <= 0:
                return

            play(round, mana_spent, not player_turn)

    round = Round(50, boss_health, 500)
    round.spells = list(spells)
    play(round)
    print(least_mana)