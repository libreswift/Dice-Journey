package com.avian.dice.presentation.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avian.dice.data.model.Dice
import com.avian.dice.data.model.Monster
import com.avian.dice.data.model.Entity
import com.avian.dice.data.model.Player
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameViewModel() : ViewModel() {
    private var player = initPlayer()
    private var monster = initMonster()
    private val diceCube = Dice

    private val mutableTurnString: MutableLiveData<String?> = MutableLiveData<String?>()
    val turnString: LiveData<String?>
        get() = mutableTurnString

    private val mutableDiceString: MutableLiveData<String?> = MutableLiveData<String?>()
    val diceString: LiveData<String?>
        get() = mutableDiceString

    private val mutableDiceDots: MutableLiveData<Int> = MutableLiveData(1)
    val diceDots: LiveData<Int>
        get() = mutableDiceDots

    private val mutablePlayerTurn: MutableLiveData<Boolean> = MutableLiveData(true)
    val playerTurn: LiveData<Boolean>
        get() = mutablePlayerTurn

    private val mutableMonsterHP: MutableLiveData<String> = MutableLiveData(
        monster.getHealth().toString() + '/' + monster.getMaxHealth().toString()
    )
    val monsterHP: LiveData<String>
        get() = mutableMonsterHP

    private val mutablePlayerHP: MutableLiveData<String> = MutableLiveData(
        player.getHealth().toString() + '/' + player.getMaxHealth().toString()
    )
    val playerHP: LiveData<String>
        get() = mutablePlayerHP

    val pushEntity: MutableLiveData<Type?> = MutableLiveData(null)
    val liveStatus: MutableLiveData<Type?> = MutableLiveData(null)


    val playerAttackPoints: String
        get() = player.getAttackPoints().toString()
    val monsterAttackPoints: String
        get() = monster.getAttackPoints().toString()
    val playerDefencePoints: String
        get() = player.getDefencePoints().toString()
    val monsterDefencePoints: String
        get() = monster.getDefencePoints().toString()


    fun onFightButtonPressed() {
        mutablePlayerTurn.value = false
        val modifier = getModifier(player, monster)
        val dices = roll(modifier)

        player.attack(monster, dices)
        viewModelScope.launch {
            turnStringBuilder(player)
            if (liveStatus.value != Type.Monster) enemyTurn()
            if (monster.isDead()) resetHP()
        }
        viewModelScope.launch {
            rollsStringBuilder(modifier, dices)
            updateHP(monster)
        }
    }

    fun restartButtonPressed() {
        setNewGameStatus()
        mutablePlayerTurn.value = true
    }

    fun onHealButtonPressed() {
        player.heal()
        mutablePlayerTurn.value = false
        viewModelScope.launch {
            turnStringBuilder(player)
            updateHP(player)
            enemyTurn()
        }
        mutableDiceString.value = ""
    }

    private fun enemyTurn() {
        mutablePlayerTurn.value = false
        val modifier = getModifier(monster, player)
        val dices = roll(modifier)
        monster.attack(player, dices)
        viewModelScope.launch {
            turnStringBuilder(monster)
            mutablePlayerTurn.value = true
            if (player.isDead()) resetHP()
        }

        viewModelScope.launch {
            rollsStringBuilder(modifier, dices)
            updateHP(player)

        }

    }


    private suspend fun turnStringBuilder(obj: Entity) {
        val n = '\n'
        val turn = "Now is ${obj.name}'s Turn!"
        val attack = "${obj.name} is attacking"
        val heal = "${obj.name} is healing"

        val unSuccessAttack = "Attack is unsuccessful!"
        val successAttack = "Attack is successful!"
        val successHeal = "${obj.name}'s HP has been increased!"

        var output = turn + n
        setTurn(output)
        delay(500L)
        if (obj.isAttacking()) {
            output += attack + n
            setTurn(output)
            delay(2000L)
        } else {
            output += heal + n
            setTurn(output)
            delay(2000L)
            output += successHeal + n
            setTurn(output)
        }

        if ((obj.isSuccessAttack()) && (obj.isAttacking())) {
            output += successAttack + n

            when (obj) {
                is Monster -> {
                    pushPlayer()
                }

                is Player -> {
                    pushMonster()
                }

                else -> {
                    return
                }
            }

            setTurn(output)
            delay(2000L)
        } else if (obj.isAttacking()){
            output += unSuccessAttack + n
            setTurn(output)
            delay(1000L)
        }

        setStatus()
    }

    private suspend fun rollsStringBuilder(
        modifier: Int,
        dices: List<Int>
    ) {
        val n = '\n'
        val comma = ", "
        val mod = if (modifier < 1) 1 else modifier
        val rolls = "Rolls: $mod"
        val result = "Result: "
        var output = rolls + n + result

        mutableDiceString.value = output
        dices.forEachIndexed { index, dice ->
            delay(200L)

            output += if ((index >= 0)&&(dices.size - 1 != index)) dice.toString() + comma
            else dice.toString()

            mutableDiceDots.value = dice
            mutableDiceString.value = output
        }

    }

    private fun roll(mod: Int): List<Int> {
        val modifier = if (mod < 1) 1 else mod

        val dices = mutableListOf<Int>()
        repeat(modifier) {
            dices.add(diceCube.roll())
        }
        return dices
    }

    private fun getModifier(attack: Entity, defence: Entity): Int =
        attack.getAttackPoints() - defence.getDefencePoints() + 1

    private fun updateHP(obj: Player) {
        mutablePlayerHP.value = obj.getHealth().toString() + '/' + obj.getMaxHealth().toString()
    }

    private fun updateHP(obj: Monster) {
        mutableMonsterHP.value = obj.getHealth().toString() + '/' + obj.getMaxHealth().toString()
    }

    private fun setTurn(output: String) {
        mutableTurnString.value = output
    }

    private fun pushPlayer() {
        pushEntity.value = Type.Player
    }

    private fun pushMonster() {
        pushEntity.value = Type.Monster
    }

    private fun setStatus() {
        if ((monster.isDead())&&(player.isDead())) liveStatus.value = Type.BOTH
        if (player.isDead()) liveStatus.value = Type.Player
        if (monster.isDead()) {
            liveStatus.value = Type.Monster
            mutablePlayerTurn.value = true
        }
    }

    private fun setNewGameStatus() {
        liveStatus.value = null
    }
    private fun resetHP() {
        player = initPlayer()
        monster = initMonster()
        updateHP(monster)
        updateHP(player)
    }
    private fun initPlayer(): Player = Player(playerName = "Player")
    private fun initMonster(): Monster = Monster(monsterName = "Monster")
}

enum class Type {
    Player,
    Monster,
    BOTH
}
