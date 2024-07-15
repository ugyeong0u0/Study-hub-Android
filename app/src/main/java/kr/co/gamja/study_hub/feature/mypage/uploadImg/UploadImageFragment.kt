package kr.co.gamja.study_hub.feature.mypage.uploadImg

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.data.repository.CallBackListener
import kr.co.gamja.study_hub.data.repository.SecondCallBackListener
import kr.co.gamja.study_hub.databinding.FragmentUploadImageBinding
import kr.co.gamja.study_hub.global.CustomSnackBar
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

// 마이페이지 사진 업로드 관련 바텀 시트
class UploadImageFragment : BottomSheetDialogFragment() {
    private val msgTag = this.javaClass.simpleName
    private lateinit var binding: FragmentUploadImageBinding
    private lateinit var cameraPermissionLauncher: ActivityResultLauncher<Array<String>> // 카메라 권한 반환값
    private lateinit var takePictureLauncher: ActivityResultLauncher<Intent>
    private lateinit var currentPhotoPath: String // 사진 절대 경로
    private lateinit var storageDir: File // 갤러리 경로
    private lateinit var timeStamp: String // 파일명 만들 때 "yyyyMMdd_HHmmss"
    private lateinit var galleryPermissionLauncher: ActivityResultLauncher<Array<String>> // 갤러리 권한 반환값
    private lateinit var getGalleryLauncher: ActivityResultLauncher<Intent> // 갤러리에서 선택한 사진 반환
    private val viewModel: UploadImgViewModel by viewModels()
//    private lateinit var snackBarListener :SecondCallBackListener
    var snackBarListener :SecondCallBackListener?=null
    /*fun setOnSnackBarListener(listener: SecondCallBackListener){
        snackBarListener =listener
    }*/
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
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        cameraPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                if (permissions.all { it.value }) {
                    openCamera()
                } else {
                    Toast.makeText(requireContext(), "카메라 권한 거부됨", Toast.LENGTH_SHORT).show()
                }
            }
        // 사진 촬영 이미지
        takePictureLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == AppCompatActivity.RESULT_OK) {
                    setPicture(currentPhotoPath)
                } else {
                    Log.e(msgTag, " 사진 촬영 결과 받기 실패")
                }
            }
        // 갤러리 권한
        galleryPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                if (permissions.all { it.value }) {
                    openGallery()
                } else {
                    Toast.makeText(requireContext(), "갤러리 권한 거부됨", Toast.LENGTH_SHORT).show()
                }
            }
        // 사진 선택 결과값
        getGalleryLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == AppCompatActivity.RESULT_OK && result.data != null) {
                    // 갤러리에서 선택한 사진
                    val imgUri = result.data?.data
                    imgUri.let { uri ->
                        try {
                            val imgPath = getPathFromUri(uri!!)
                            imgPath?.let { path ->
                                setPicture(path)
                            }

                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
            }

        // 앱전용 디렉터리 Pictures에 저장
        storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        timeStamp =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        // 닫기
        binding.btnClose.setOnClickListener {
            dismiss()
        }

        // 갤러리 클릭
        binding.bntPickPhoto.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.READ_MEDIA_IMAGES
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    openGallery()
                } else {
                    galleryPermissionLauncher.launch(
                        arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
                    )
                }
            } else {
                val permissions = arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                if (permissions.all {
                        ContextCompat.checkSelfPermission(
                            requireContext(),
                            it
                        ) == PackageManager.PERMISSION_GRANTED
                    }) {
                    openGallery()
                } else {
                    galleryPermissionLauncher.launch(permissions)
                }
            }
            /** 갤러리 클릭 시 로직 동작 후 dismiss */
            dismiss()
        }

        // 사진찍기 클릭
        binding.btnTakingPhoto.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val permissions = arrayOf(
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.CAMERA
                )
                if (permissions.all {
                        ContextCompat.checkSelfPermission(
                            requireContext(),
                            it
                        ) == PackageManager.PERMISSION_GRANTED
                    }
                ) {
                    openCamera()
                } else {
                    cameraPermissionLauncher.launch(
                        permissions
                    )
                }
            } else {
                val permissions = arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                )
                if (permissions.all {
                        ContextCompat.checkSelfPermission(
                            requireContext(),
                            it
                        ) == PackageManager.PERMISSION_GRANTED
                    }) {
                    openCamera()
                } else {
                    cameraPermissionLauncher.launch(permissions)
                }
            }
            /** 사진찍기 클릭 시 비즈니스 로직 동작 후 dismiss */
            dismiss()
        }
    }

    private fun openCamera() {
        val photoFile: File? = try {
            createImageFile()
        } catch (e: IOException) {
            Toast.makeText(context, "이미지 파일 생성 실패", Toast.LENGTH_SHORT).show()
            null
        }
        photoFile.also { // uri 생성
            val photoUri: Uri = FileProvider.getUriForFile(
                requireContext(),
                "kr.co.gamja.study_hub.fileprovider",
                it!!
            )
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri) // 데이터 저장 위치
            takePictureLauncher.launch(intent) // 사진 촬영 시작
        }
    }

    // 카메라 - 임시 파일 생성
    @Throws(java.io.IOException::class)
    private fun createImageFile(): File {
        return File.createTempFile( // 임시 파일 생성
            "s${timeStamp}",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath // 절대 경로 저장
        }
    }

    // 촬영한 사진 api연결
    private fun setPicture(path: String) {
        val file = File(path)
        // 콘텐츠 타입 지정
        val contentType = "image/jpeg".toMediaTypeOrNull()
        // RequestBody 생성
        val requestFile = file.asRequestBody(contentType)
        val requestBody = MultipartBody.Part.createFormData("image", file.name, requestFile)
        // 업로딩 api 연결
        viewModel.uploadImg(requestBody, object : CallBackListener {
            override fun isSuccess(result: Boolean) {
               snackBarListener?.isSuccess(result) // snackBar 개인정보 페이지에 띄우기위한 리스너
                dismiss() // 바텀 싯 종료
            }
        })
    }

    private fun openGallery() {
        // Media.EXTERNAL_CONTENT_URI : 외부 저장소에 있는 이미지에 접근할
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        getGalleryLauncher.launch(intent)
    }

    private fun getPathFromUri(uri: Uri): String? {
        val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val columnIdx = it.getColumnIndexOrThrow(Media.DATA)
                return it.getString(columnIdx)
            }
        }
        return null
    }

}
