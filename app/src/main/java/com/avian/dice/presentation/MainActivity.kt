package com.avian.dice.presentation

import androidx.appcompat.app.AppCompatActivity
import com.avian.dice.data.model.Enemy
import com.avian.dice.data.model.Player

class MainActivity: AppCompatActivity() {

}

fun main() {
    val player = Player()
    val enemy = Enemy()

    enemy.attack(enemy)
}

private fun turn() {

}