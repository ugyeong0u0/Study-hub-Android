package kr.co.gamja.study_hub.feature.studypage.studyContent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.databinding.FragmentBottomSheetBinding


class BottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBottomSheetBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(inflater, R.layout.fragment_bottom_sheet,container,false)
        return binding.root
    }


}