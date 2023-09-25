package com.avian.dice.data.model

import kotlin.math.roundToInt

class Player(): Entity(
    attackPoints = 20,
    maxDamage = 20,
    defencePoints = 10,
    healthPoints = 50,
) {
    private var aidKit = 4
    private var killsCounter = 0
    private var level = 1
    private var maxHealth: Int = 50

    override fun heal() {
        if (aidKit == 0) {
            println("Aid kits are out of your inventory.")
            return
        }
        aidKit--
        super.heal()
        if (getHealth() > maxHealth) setHealth(maxHealth)
        println("Your HP has been restored!")

    }

    override fun attack(target: Entity) {
        println("Attacking the Enemy!")
        super.attack(target)
        killsCounter++
        updateProgress()
    }

    private fun updateProgress() {
        val levelProgression = sequence {
            val start = 0
            yield(start)
            yieldAll(1..2 step 2)
            yieldAll(generateSequence(3) { it * 2 })
        }

        if (killsCounter in levelProgression.take(level + 1)) levelUp()
    }

    private fun levelUp() {
        level++

        val healthRatio = getHealth() * 1.05
        val bumpRatio = getAttackPoints() * 1.25

        maxHealth = healthRatio.roundToInt()
        setAttackPoints(bumpRatio.roundToInt())
        println("Your level has been increased to $level")
    } //todo make to increase level only by killing enemies


}
