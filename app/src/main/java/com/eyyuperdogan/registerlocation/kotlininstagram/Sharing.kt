package com.eyyuperdogan.registerlocation.kotlininstagram

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.eyyuperdogan.registerlocation.kotlininstagram.databinding.ActivitySharingBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.IOException
import java.lang.ref.Reference
import java.util.UUID

private lateinit var binding: ActivitySharingBinding
private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
private lateinit var permissionLauncher: ActivityResultLauncher<String>
private lateinit var firestore: FirebaseFirestore
private lateinit var storage:FirebaseStorage
private lateinit var auth:FirebaseAuth
var selectedPicture : Uri? = null
class Sharing : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySharingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth=Firebase.auth
        firestore=Firebase.firestore//database
        storage=Firebase.storage//görsel


        registerLauncher()




        //galeriye gitmek için izin istiyoruz
        binding.imageSelect.setOnClickListener(){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Snackbar.make(it, "Permission needed for gallery", Snackbar.LENGTH_INDEFINITE).setAction("izin ver",
                        View.OnClickListener {
                            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                        }).show()
                } else {
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            } else {
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }
        }


        //rasgele urı ıd oluşrulur
        var uuıd=UUID.randomUUID()
        var imageName="$uuıd.jpg"





        //görsellerimizi images dosyasında(veri tabanı dosyasında) saklamamızı sağlar
        binding.buttonSave.setOnClickListener(){
            var referance= storage.reference
            var imageReference=referance.child("images").child(imageName)
            if (selectedPicture!=null){
                imageReference.putFile(selectedPicture!!).addOnSuccessListener {

                    //downloadurl url->firestore(url yi alıyoruz)
                    var pictureReferance = referance.child("images").child(imageName)
                    pictureReferance.downloadUrl.addOnSuccessListener {
                        val downloadurl = it.toString()

                        //veri alıyoruz  veri tabanında gösteriyoruz(downloadurl,eposta,tarih,yorum)
                        if (auth.currentUser != null) {
                            var postHasMap = hashMapOf<String, Any>()
                            postHasMap.put("downloadurl", downloadurl)
                            postHasMap.put("date", Timestamp.now())
                            postHasMap.put("comment", binding.textComment.text.toString())
                            postHasMap.put("gmail", auth.currentUser!!.email!!)

                            //veri tabanımıza kaydediyoruz (downloadurl,eposta,tarih,yorum)
                            firestore.collection("posts").add(postHasMap).addOnSuccessListener {
                                var intent=Intent(this,Shares::class.java)
                                startActivity(intent)
                                finish()
                            }.addOnFailureListener {
                                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
                            }
                        }
                    }.addOnFailureListener {
                        Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

//izin istemek
    private fun registerLauncher() {
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val intentFromResult = result.data
                if (intentFromResult != null) {
                    selectedPicture = intentFromResult.data

                selectedPicture.let {
                    binding.imageSelect.setImageURI(it)
                }
            }
        }
        }
    permissionLauncher=registerForActivityResult(ActivityResultContracts.RequestPermission()){
        result->
       if (result){
           val intenToGallery=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
           activityResultLauncher.launch(intenToGallery)
       }
        else
       {
           Toast.makeText(this, "izin verilmedi!", Toast.LENGTH_LONG).show()
       }
    }

    }
}