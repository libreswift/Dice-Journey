package com.avian.dice.data.model

class Monster(
    monsterName: String
) : Entity(
    attackPoints = 56,
    defencePoints = 15,
    healthPoints = 100,
    maxHealth = 100,
    name = monsterName
) {
    override fun attack(target: Entity, dices: List<Int>) {
        println("Attacking the Player!")
        super.attack(target, dices)
    }
}