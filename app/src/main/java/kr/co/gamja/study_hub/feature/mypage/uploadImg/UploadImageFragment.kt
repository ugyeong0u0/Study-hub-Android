package kr.co.gamja.study_hub.feature.mypage.uploadImg

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.databinding.FragmentUploadImageBinding

class UploadImageFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentUploadImageBinding
    private lateinit var cameraPermissionLauncher: ActivityResultLauncher<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    onCameraPermissionGranted()
                } else {
                    onCameraPermissionDenied()
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_upload_image, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 닫기
        binding.btnClose.setOnClickListener {
            dismiss()
        }
        // todo("갤러리에서 가져오기")
        binding.bntPickPhoto.setOnClickListener {
            selectPhoto()

        }
        // todo("사진찍기")
        binding.btnTakingPhoto.setOnClickListener {
            cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA,)
            // 사진 저장

        }

    }

    private fun onCameraPermissionGranted() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivity(cameraIntent)
    }

    private fun onCameraPermissionDenied() {

    }

    private fun selectPhoto(){

    }

}