package com.avian.dice.data.model

class Enemy() : Entity(
    attackPoints = 10,
    maxDamage = 27,
    defencePoints = 101,
    healthPoints = 100
) {
    override fun attack(target: Entity) {
        println("Attacking the Player!")
        super.attack(target)
    }
}