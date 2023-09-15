package kr.co.gamja.study_hub.feature.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.data.datastore.App
import kr.co.gamja.study_hub.databinding.FragmentMypageMainBinding


class MypageMainFragment : Fragment() {
    private lateinit var binding: FragmentMypageMainBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mypage_main, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 툴바 설정
        val toolbar = binding.myPageMainToolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ""

        binding.iconAlarm.setOnClickListener {
            findNavController().navigate(
                R.id.action_global_mainAlarmFragment,
                null
            )
        }
        // 회원 조회 누를시
        binding.layoutUserInfo.setOnClickListener {
            runBlocking {
                val accessTokenDeferred = async(Dispatchers.IO) {
                    App.getInstance().getDataStore().accessToken.first()
                }
                val accessToken = accessTokenDeferred.await()

                if (accessToken == null) {
                    findNavController().navigate(
                        R.id.action_global_loginFragment,
                        null
                    )
                } else {
                    findNavController().navigate(
                        R.id.action_mypageMainFragment_to_myInfoFragment,
                        null
                    )

                }
            }

        }

    }

}