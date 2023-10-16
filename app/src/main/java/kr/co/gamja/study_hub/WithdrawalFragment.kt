package kr.co.gamja.study_hub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import kr.co.gamja.study_hub.databinding.FragmentWithdrawalBinding


class WithdrawalFragment : Fragment() {
    private val tag = this.javaClass.simpleName
    private lateinit var binding: FragmentWithdrawalBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_withdrawal, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)// 툴바 설정
        val toolbar = binding.withdrawalToolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ""

        binding.btnNext.setOnClickListener {

        }
        binding.btnCancel.setOnClickListener {

        }
    }

}