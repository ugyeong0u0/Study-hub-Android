package kr.co.gamja.study_hub.feature.signup.major

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.databinding.FragmentMajorBinding
import kr.co.gamja.study_hub.global.ExtensionFragment.Companion.hideKeyboard

class MajorFragment : Fragment() {
    private lateinit var binding: FragmentMajorBinding
    private val viewModel: MajorViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_major, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel=viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        // 에딧텍스트 자판 내리기
        binding.root.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    true
                }
                MotionEvent.ACTION_UP -> {
                    this.hideKeyboard()
                    v.performClick()
                    true
                }
                else -> false
            }
        }

        // 툴바 설정
        val toolbar = binding.createMajorToolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ""

        binding.iconBack.setOnClickListener {
            val navcontroller = findNavController()
            navcontroller.navigateUp() // 뒤로 가기
        }

        binding.btnNext.setOnClickListener {
            // 회원가입 api보냄
            Log.d("회원가입 버튼 누름", "")
            viewModel.requestSignup(object : RegisterCallback {
                override fun onSucess(isValid: Boolean) {
                    if (isValid) {
                        Log.d("회원가입 최종 성공", "")
                        findNavController().navigate(R.id.action_createAccountFragment04_to_createAccountFragmentEnd05)
                    } else {
                        Log.e("회원가입 fail로 넘어감?", "")
                    }
                }

                override fun onFail(eIsValid: Boolean, eStatus: String, eMessage: String) {
                    if (eIsValid) {
                        Toast.makeText(requireContext(), eStatus, Toast.LENGTH_LONG).show()
                        Toast.makeText(requireContext(), eMessage, Toast.LENGTH_LONG).show()
                        Log.e("회원가입 중복 혹은 삭제", "")
                    } else {
                        Log.e("회원가입 fail로 넘어감?", "")
                    }
                }
            })
        }
        binding.txtPageNumber.text = getString(R.string.txt_pagenumber, 4)
        // 학과 선택박스(AutoCompleteTextView)
        selectMajor()
    }

    // 학과 선택박스(AutoCompleteTextView)
    fun selectMajor() {
        val editTxt_major = binding.autoMajor
        val array_major: Array<String> = resources.getStringArray(R.array.array_majors)
        val adapter_array =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, array_major)
        editTxt_major.setAdapter(adapter_array)

        binding.viewDelete.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                binding.autoMajor.text.clear()
            }
        })

        binding.autoMajor.setOnItemClickListener { parent, _, position, _ ->
            binding.autoMajor.isEnabled = true
            var selectedItem=parent.adapter.getItem(position) as String
            viewModel.setUserMajor(selectedItem)

        }
        // 드랍다운 배경셋팅
        binding.autoMajor.let {
            it.setDropDownBackgroundResource(R.drawable.back_select_major)
            it.dropDownVerticalOffset = 10 // TODO("위치 물어봐야 함")
        }

    }

/*    // EditText자동완성 중간글자를 먼저 쳐도 검색가능하게 하는 customAdapter
    class AutoCompleteAdapter(
        context: Context,
        @LayoutRes resource: Int,
        @IdRes textViewResourceId: Int = 0,
        internal var items: Array<String> = arrayOf()
    ) : ArrayAdapter<Any>(context, resource, textViewResourceId, items) {
        internal var tempItems: MutableList<Any> = mutableListOf() // 단어 모음들
        internal var suggestions: MutableList<Any> = mutableListOf() // 드롭다운에 나타날 단어들

        private var filter: Filter = object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                return if (constraint != null) {
                    suggestions.clear()
                    tempItems.forEach {
                        if (it.toString().lowercase(Locale.getDefault())
                                .contains(constraint.toString().lowercase(Locale.getDefault()))
                        ) {
                            suggestions.add(it)
                        }
                    }
                    val filterResults = FilterResults()
                    filterResults.values = suggestions
                    filterResults.count = suggestions.size
                    filterResults
                } else {
                    FilterResults()
                }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                val filterList = results?.values as? List<*>
                if (results?.count?.compareTo(0) == 1) {
                    clear()
                    filterList?.forEach {
                        add(it)
                    }.also { notifyDataSetChanged() }
                }
            }
        }

        init {
            tempItems = items.toMutableList()
            suggestions = ArrayList()
        }

        override fun getFilter(): Filter {
            return filter
        }
    }*/
}
