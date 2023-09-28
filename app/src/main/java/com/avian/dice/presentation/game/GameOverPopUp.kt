package com.avian.dice.presentation.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.avian.dice.databinding.GameOverDialogBinding

class GameOverPopUp(val str: String): DialogFragment() {
    private var _binding: GameOverDialogBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GameViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = GameOverDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.titleText.text = str
        binding.restartButton.setOnClickListener {
            viewModel.restartButtonPressed()
            onDestroyView()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}