package com.esmapolat.kotlininstagram.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.esmapolat.kotlininstagram.databinding.ActivityUploadBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.util.UUID

class UploadActivity : AppCompatActivity() {
    lateinit var binding: ActivityUploadBinding
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    var selectedPicture : Uri? = null
    private lateinit var auth:FirebaseAuth
    private lateinit var firestore:FirebaseFirestore
    private lateinit var storge:FirebaseStorage


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth=Firebase.auth
        firestore=Firebase.firestore
        storge=Firebase.storage


        registerLauncher()

    }

    fun upload(view:View){
        val uuid = UUID.randomUUID()
        val imageName = "$uuid.jpg"
        val reference=storge.reference
        val imageReference=reference.child("images").child(imageName)

        if(selectedPicture!=null){
            imageReference.putFile(selectedPicture!!).addOnSuccessListener{
                val uploadedPictureReference = storge.reference.child("images").child(imageName)
                uploadedPictureReference.downloadUrl.addOnSuccessListener{
                    val downoaldUrl=it.toString()
                    val postMap= hashMapOf<String,Any >()
                    postMap.put("downloadUrl",downoaldUrl)
                    postMap.put("userEmail",auth.currentUser!!.email!!)

                    postMap.put("date",Timestamp.now())
                    postMap.put("comment",binding.editTextText.text.toString())

                    firestore.collection("Posts").add(postMap).addOnSuccessListener {
                        finish()

                    }.addOnFailureListener{
                        Toast.makeText(this@UploadActivity,it.localizedMessage,Toast.LENGTH_LONG).show()
                    }
                }
                //DOWNLOAD URL
            }.addOnFailureListener{
                Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
            }

        }

    }
    fun SelectImageView(view: View){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                Snackbar.make(view,"Permission needed for Gallery!",Snackbar.LENGTH_INDEFINITE).setAction("Give Permission"){
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }.show()

            }
            else{
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)


            }


        }
        else{
            val intentGallery=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intentGallery)

        }

    }
    private fun registerLauncher() {
        activityResultLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
            if(result.resultCode== RESULT_OK){
                val intentFromResult=result.data
                if(intentFromResult!=null){
                    selectedPicture= intentFromResult.data
                    selectedPicture?.let {
                        binding.imageView.setImageURI(it)
                    }

                }
            }
        }

        permissionLauncher=registerForActivityResult(ActivityResultContracts.RequestPermission()){result->
            if(result){
                val intentGallery=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentGallery)

            }else{
                Toast.makeText(this@UploadActivity,"Permission needed!",Toast.LENGTH_LONG).show()
            }

        }
    }
}