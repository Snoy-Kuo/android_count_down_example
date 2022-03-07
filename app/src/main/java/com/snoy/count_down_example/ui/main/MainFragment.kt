package com.snoy.count_down_example.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.snoy.count_down_example.R
import com.snoy.count_down_example.databinding.MainFragmentBinding
import com.snoy.count_down_example.model.repo.FakeAuthRepo
import com.snoy.count_down_example.ui.ViewModelFactory
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
        private const val COUNTDOWN_SECS = 10L
    }

    private var _binding: MainFragmentBinding? = null

    private val binding get() = _binding!!

    private var disposable: Disposable? = null

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        disposable?.dispose()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = ViewModelFactory(FakeAuthRepo())
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
        binding.btnGetOTP.setOnClickListener { onClickGetOTP() }

        binding.btnGetOTP2.setOnClickListener { onClickGetOTP2() }

        viewModel.getSmsOtp3State.observe(viewLifecycleOwner) { state ->
            updateButton(binding.btnGetOTP3, getString(R.string.get_sms_otp3), state)
        }
        binding.btnGetOTP3.setOnClickListener { viewModel.getSmsOTP3(COUNTDOWN_SECS) }

        viewModel.getSmsOtp4State.observe(viewLifecycleOwner) { state ->
            updateButton(binding.btnGetOTP4, getString(R.string.get_sms_otp4), state)
        }
        binding.btnGetOTP4.setOnClickListener { viewModel.getSmsOTP4(COUNTDOWN_SECS) }
    }

    @Suppress("SameParameterValue")
    @SuppressLint("SetTextI18n")
    private fun updateButton(button: Button, funName: String, states: GetSmsOtpState) {
        when (states) {
            is GetSmsOtpState.GetSmsOtpInitial -> {
                binding.progress.visibility = View.INVISIBLE
                button.isEnabled = true
                button.text = funName
            }
            is GetSmsOtpState.GetSmsOtpFail -> {
                binding.progress.visibility = View.INVISIBLE
                button.isEnabled = true
                button.text = funName
                Toast.makeText(requireContext(), "$funName ${states.msg}!", Toast.LENGTH_SHORT)
                    .show()
            }
            is GetSmsOtpState.GetSmsOtpLoading -> {
                binding.progress.visibility = View.VISIBLE
                button.isEnabled = false
            }
            is GetSmsOtpState.GetSmsOtpWaiting -> {
                binding.progress.visibility = View.INVISIBLE
                button.isEnabled = false
                button.text = "$funName (Wait ${states.secs} secs)"
            }
            is GetSmsOtpState.GetSmsOtpSuccess -> {
                binding.progress.visibility = View.INVISIBLE
                button.isEnabled = false
                Toast.makeText(requireContext(), "$funName success!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    @Suppress("SameParameterValue")
    @SuppressLint("SetTextI18n")
    private fun updateButton(button: Button, funName: String, secs: Long, countdownSec: Long) {
        when {
            secs == 0L -> {
                button.isEnabled = true
                button.text = funName
            }
            secs > countdownSec -> {
                button.isEnabled = true
                button.text = funName
                Toast.makeText(requireContext(), "$funName fail!", Toast.LENGTH_SHORT).show()
            }
            else -> {
                button.text = "$funName (Wait $secs secs)"
                if (secs == countdownSec) {
                    Toast.makeText(requireContext(), "$funName success!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun onClickGetOTP() {
        binding.progress.visibility = View.VISIBLE
        binding.btnGetOTP.isEnabled = false

        val countdownSec = COUNTDOWN_SECS
        disposable = viewModel.getSmsOTP(countdownSec)
            .subscribe {
                binding.progress.visibility = View.INVISIBLE
                val secs = countdownSec - it
                updateButton(
                    binding.btnGetOTP,
                    getString(R.string.get_sms_otp1),
                    secs,
                    countdownSec
                )
            }
    }

    private fun onClickGetOTP2() {
        binding.progress.visibility = View.VISIBLE
        binding.btnGetOTP2.isEnabled = false

        val countdownSec = COUNTDOWN_SECS
        lifecycleScope.launch {
            viewModel.getSmsOTP2(countdownSec)
                .collect {
                    binding.progress.visibility = View.INVISIBLE
                    val secs = it
                    updateButton(
                        binding.btnGetOTP2,
                        getString(R.string.get_sms_otp2),
                        secs,
                        countdownSec
                    )
                }
        }
    }
}