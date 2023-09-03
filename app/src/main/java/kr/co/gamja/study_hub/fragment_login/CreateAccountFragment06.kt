package kr.co.gamja.study_hub.fragment_login

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.databinding.FragmentCreateAccount06Binding


/*
class CreateAccountFragment06 : Fragment() {
    private var _binding: FragmentCreateAccount06Binding? = null
    private val binding get() = _binding!!

    private lateinit var chipGroup: ChipGroup
    private lateinit var arrayInterest: Array<String>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateAccount06Binding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fca06BtnNext.setOnClickListener{
            findNavController().navigate(R.id.action_createAccountFragment06_to_login)
        }

        chipGroup = binding.chipGroup
        chipGroup.setChipSpacingVerticalResource(R.dimen.chip_interval) //...1
        arrayInterest = resources.getStringArray(R.array.array_interest)
        for (s in arrayInterest) {
//            Log.d("리소스값","$s")
            addChip(s)
        }


    }

    // chip 추가
    private fun addChip(word: String) {
        val newChip = Chip(requireContext()).apply {
            text = word
            isCheckable = true
            isCheckedIconVisible = false
            chipStrokeWidth = 2f
            setEnsureMinTouchTargetSize(false) // ...1+이거 바꿔줘야만 chip끼리 높이 변함!!!
            // 16dp 를 픽셀 단위
            setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f)
            chipStrokeColor = ColorStateList(
                arrayOf(
                    intArrayOf(-android.R.attr.state_checked),
                    intArrayOf(android.R.attr.state_checked)
                ),
                intArrayOf(R.color.O_50, R.color.G_80)
            )
            chipBackgroundColor = ColorStateList(
                arrayOf(
                    intArrayOf(-android.R.attr.state_checked),
                    intArrayOf(android.R.attr.state_checked)
                ),
                intArrayOf(Color.WHITE, Color.BLACK)
            )
            setTextColor(
                ColorStateList(
                    arrayOf(
                        intArrayOf(-android.R.attr.state_checked),
                        intArrayOf(android.R.attr.state_checked)
                    ),
                    intArrayOf(R.color.O_50, R.color.syswhite)
                )
            )
        }
        newChip.setOnClickListener{
            Toast.makeText(requireContext(),"눌림",Toast.LENGTH_LONG).show()
        }
        binding.chipGroup.addView(newChip)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}*/
