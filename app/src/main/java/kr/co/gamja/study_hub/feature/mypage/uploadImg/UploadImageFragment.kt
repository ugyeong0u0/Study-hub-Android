package kr.co.gamja.study_hub.feature.mypage.uploadImg

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.databinding.FragmentUploadImageBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class UploadImageFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentUploadImageBinding
    private lateinit var cameraPermissionLauncher: ActivityResultLauncher<String> // 카메라 권한 반환값
    private lateinit var galleryPermissionLauncher: ActivityResultLauncher<Array<String>> // 갤러리 권한 반환값
    private lateinit var getImgResult: ActivityResultLauncher<Intent> // 갤러리에서 선택한 사진 반환

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    onCameraPermissionGranted()
                } else {
                    Toast.makeText(requireContext(), "카메라 권한 거부됨", Toast.LENGTH_SHORT).show()
                }
            }
        // todo("SDK낮은거 확인해야함 ")
        galleryPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                var isGalleryPermissionGranted = false
                var isWritePermissionGranted = false
                var isReadPermissionGranted = false

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    isGalleryPermissionGranted =
                        it[Manifest.permission.READ_MEDIA_IMAGES] ?: false
                } else {
                    isWritePermissionGranted =
                        it[Manifest.permission.WRITE_EXTERNAL_STORAGE] ?: false
                    isReadPermissionGranted =
                        it[Manifest.permission.READ_EXTERNAL_STORAGE] ?: false
                }
                if ((isWritePermissionGranted && isReadPermissionGranted) || isGalleryPermissionGranted) {
                    onGalleryPermissionGranted()
                } else {
                    Toast.makeText(requireContext(), "갤러리 권한 거부됨", Toast.LENGTH_SHORT).show()
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

        // 갤러리 권한 허가
        binding.bntPickPhoto.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                galleryPermissionLauncher.launch(
                    arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
                )
            } else {
                galleryPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                )
            }


        }
        // 사진 가져오기 및 JPEG로 변환("TODO: api연결 후 확인해보기")
        getImgResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                it.data?.data?.let { uri ->
                    val bitmapImg = getRealBitmap(uri)
                    val img = bitmapToFile(bitmapImg!!)
                    // todo("api연결")
                }
            }
        }


        // 사진 권한 허가
        binding.btnTakingPhoto.setOnClickListener {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            // todo("찍은 사진 변환")
        }

    }

    private fun onCameraPermissionGranted() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivity(cameraIntent)
    }

    private fun onGalleryPermissionGranted() {
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"
        getImgResult.launch(galleryIntent) // 인텐트 결과 가져옴
    }

    // 사진 가져오기
    private fun getRealBitmap(uri: Uri): Bitmap? {
        val contentResolver = requireContext().contentResolver
        return try {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }
        } catch (e: Exception) {
            null
        }
    }

    // JPEG로 변환
    private fun bitmapToFile(bitmap: Bitmap): File? {
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "$timeStamp.jpg"
        val imgFile = File(storageDir, fileName)
        return try {
            FileOutputStream(imgFile).use {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            }
            imgFile
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

}