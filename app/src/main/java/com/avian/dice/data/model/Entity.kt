package com.avian.dice.data.model

import kotlin.math.roundToInt
import kotlin.random.Random

abstract class Entity(
    private var attackPoints: Int,
    private var maxDamage: Int,
    private val defencePoints: Int,
    private var healthPoints: Int,
    private var status: Status = Status.Alive
) {
    init {
        if (
            (attackPoints <= 0) ||
            (maxDamage <= 0) ||
            (defencePoints <= 0) ||
            (healthPoints <= 0) ||
            (status == Status.Dead)
        ) throw IllegalArgumentException()
    }

    open fun attack(target: Entity) {
        if (target == this) {
            println("Dont touch yourself!")
            return
        }

        if (target.status == Status.Dead) {
            println("Your opponent is dead!")
        } else {
            attackOther(target)
        }
    }

    private fun attackOther(target: Entity) {
        println("attacking! target HP is ${target.healthPoints}")

        var modifier = getModifier(target.defencePoints)
        if (modifier < 1) modifier = 1

        val dices = mutableListOf<Int>()
        repeat(modifier) {
            println("modifier: $modifier")
            dices.add(Dice.roll())
        }

        println(dices.toString())

        if (countDamage(dices) == 0) {
            println("Miss!")
        } else {
            target.takeDamage(countDamage(dices))
            println("Attack is successful! Target HP is ${target.healthPoints}")
        }

        if (target.isDead()) {
            target.status = Status.Dead
            println("Your opponent is dead!")
        }
    }


    open fun heal() {
        println("before heal: $healthPoints")
        val meds = healthPoints * 1.3
        healthPoints = meds.roundToInt()
        println("after heal: $healthPoints")
    }

    private fun takeDamage(damage: Int) {
        healthPoints -= damage
    }

    private fun countDamage(dices: List<Int>): Int {
        val successValues = listOf(5, 6)
        var damage = 0

        dices.forEach { dice ->
            if (dice in successValues) {
                damage += getRandomDamage()
            }
        }
        return damage
    }

    fun getHealth() = healthPoints
    fun getAttackPoints() = attackPoints

    fun setHealth(maxhealth: Int){
        healthPoints = maxhealth
    }
    fun setAttackPoints(attack: Int){
        attackPoints = attack
    }

    private fun getModifier(defenceOther: Int) = (attackPoints - defenceOther) + 1
    private fun getRandomDamage(): Int = Random.nextInt(1, maxDamage + 1)
    private fun isDead() = (healthPoints <= 0)
}

enum class Status {
    Alive,
    Dead
}
