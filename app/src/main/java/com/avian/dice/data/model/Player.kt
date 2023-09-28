package com.avian.dice.data.model



class Player(
    playerName: String
): Entity(
    attackPoints = 18,
    defencePoints = 55,
    healthPoints = 50,
    maxHealth = 50,
    name = playerName
) {
    private var aidKit = 4

    override fun heal() {
        if (aidKit == 0) {
            println("Aid kits are out of your inventory.")
            return
        }
        aidKit--
        super.heal()
        if (getHealth() > getMaxHealth()) setHealth(getMaxHealth())
        println("Your HP has been restored!")
    }

    override fun attack(target: Entity, dices: List<Int>) {
        println("Attacking the Monster!")
        super.attack(target, dices)
    }
}
