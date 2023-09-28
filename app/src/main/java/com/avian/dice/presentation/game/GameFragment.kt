package com.avian.dice.presentation.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.avian.dice.R
import com.avian.dice.databinding.GameFragmentBinding
import com.google.android.material.snackbar.Snackbar


class GameFragment : Fragment() {
    private var _binding: GameFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GameViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = GameFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bind() {
        binding.fightButton.setOnClickListener {
            viewModel.onFightButtonPressed()
        }

        binding.healButton.setOnClickListener {
            viewModel.onHealButtonPressed()
        }

        viewModel.turnString.observe(viewLifecycleOwner) {
            binding.turn.text = it.orEmpty()
        }

        viewModel.diceString.observe(viewLifecycleOwner) {
            binding.dice.text = it.orEmpty()
        }

        viewModel.playerTurn.observe(viewLifecycleOwner) {
            if (it == false) hideButtons()
            if (it == true) showButtons()
        }

        viewModel.pushEntity.observe(viewLifecycleOwner) {
            if (it == Type.Player) shakeView(binding.player)
            if (it == Type.Monster) shakeView(binding.enemy)
        }

        viewModel.playerHP.observe(viewLifecycleOwner) {
            binding.healthPlayer.text = it
        }

        viewModel.monsterHP.observe(viewLifecycleOwner) {
            binding.healthEnemyPoints.text = it
        }

        viewModel.diceDots.observe(viewLifecycleOwner) {
            rollDice(it)
        }

        viewModel.liveStatus.observe(viewLifecycleOwner) {status ->
            if (status == Type.BOTH) popUp("Game over\nEverybody win!")
            if (status == Type.Player) popUp("Game over\nEnemy wins!")
            if (status == Type.Monster) popUp("Game over\nPlayer wins!")
        }

        binding.attackPlayerPoints.text = viewModel.playerAttackPoints
        binding.attackEnemyPoints.text = viewModel.monsterAttackPoints

        binding.defenceEnemyPoints.text = viewModel.monsterDefencePoints
        binding.defencePlayerPoints.text = viewModel.playerDefencePoints
    }

    override fun onResume() {
        super.onResume()
        bind()
    }

    private fun rollDice(dice: Int) {
        val drawableResource = when (dice) {
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            else -> R.drawable.dice_6
        }

        binding.diceImage.setImageResource(drawableResource)
    }

    private fun hideButtons() {
        hideButton(binding.fightButton)
        hideButton(binding.healButton)
    }

    private fun showButtons() {
        showButton(binding.fightButton)
        showButton(binding.healButton)
    }

    private fun hideButton(button: Button) {
        button.alpha = .5f;
        button.isClickable = false;
    }

    private fun showButton(button: Button) {
        button.alpha = 1.0f;
        button.isClickable = true;
    }

    private fun popUp(str: String) {
        GameOverPopUp(str).show((activity as AppCompatActivity).supportFragmentManager, "show")
    }




    private fun shakeView(view: View) {
        val animShake: Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.shake)
        view.startAnimation(animShake)
    }




}