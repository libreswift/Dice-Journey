package com.avian.dice.data.model

import kotlin.math.roundToInt
import kotlin.random.Random

abstract class Entity(
    var name: String,
    private val attackPoints: Int,
    private val maxHealth: Int,
    private val defencePoints: Int,
    private var healthPoints: Int,
    private var status: Status = Status.Alive
) {
    private var isAttacking = false
    private var isSuccessAttack = false
    init {
        if (
            (attackPoints <= 0) ||
            (defencePoints <= 0) ||
            (healthPoints <= 0) ||
            (status == Status.Dead)
        ) throw IllegalArgumentException()
    }
    open fun attack(target: Entity, dices: List<Int>) {
        if (target == this) {
            println("Dont touch yourself!")
            return
        }

        if (target.status == Status.Dead) {
            println("Your opponent is dead!")
            return
        }

        isAttacking = true
        println("attacking! target HP is ${target.healthPoints}")

        if (countDamage(dices) == 0) {
            isSuccessAttack = false
            println("Miss!")
        } else {
            isSuccessAttack = true
            target.takeDamage(countDamage(dices))
            println("Attack is successful! Target HP is ${target.healthPoints}")
        }

        if (target.isDead()) {
            target.status = Status.Dead
            println("Your opponent is dead!")
        }
    }


    open fun heal() {
        isAttacking = false
        println("before heal: $healthPoints")

        val meds = healthPoints * 1.3
        healthPoints = meds.roundToInt()

        println("after heal: $healthPoints")
    }

    private fun takeDamage(value: Int) {
        isAttacking = false
        val damage = healthPoints - value
        if (damage < 1) {
            healthPoints = 0
        } else healthPoints -= damage
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
    fun getMaxHealth() = maxHealth

    fun getDefencePoints() = defencePoints

    fun setHealth(maxhealth: Int){
        healthPoints = maxhealth
    }

    private fun getRandomDamage(): Int = Random.nextInt(1, attackPoints + 1)
    fun isDead() = (healthPoints <= 0)
    fun isAttacking() = isAttacking
    fun isSuccessAttack() = isSuccessAttack
}

enum class Status {
    Alive,
    Dead
}
