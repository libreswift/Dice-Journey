package com.avian.dice.data.model

import kotlin.random.Random

object Dice {
    private const val dots = 6

    fun roll() = Random.nextInt(1, dots + 1)
}