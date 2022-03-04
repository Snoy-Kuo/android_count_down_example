package com.snoy.count_down_example.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.snoy.count_down_example.databinding.MainFragmentBinding
import com.snoy.count_down_example.model.repo.FakeAuthRepo
import com.snoy.count_down_example.ui.ViewModelFactory
import io.reactivex.rxjava3.disposables.Disposable

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
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
    }

    @SuppressLint("SetTextI18n")
    private fun onClickGetOTP() {
        binding.progress.visibility = View.VISIBLE
        binding.btnGetOTP.isEnabled = false

        val countdownSec = 10L
        disposable = viewModel.getSmsOTP(countdownSec)
            .subscribe {
                binding.progress.visibility = View.INVISIBLE
                val secs = countdownSec - it
                when {
                    secs == 0L -> {
                        binding.btnGetOTP.isEnabled = true
                        binding.btnGetOTP.text = "get SMS OTP"
                    }
                    secs < 0L -> {
                        binding.btnGetOTP.isEnabled = true
                        binding.btnGetOTP.text = "get SMS OTP"
                        Toast.makeText(requireContext(), "Get OTP fail!", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        binding.btnGetOTP.text = "get SMS OTP (Wait $secs secs)"
                        if (secs == countdownSec) {
                            Toast.makeText(requireContext(), "Get OTP success!", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
    }
}