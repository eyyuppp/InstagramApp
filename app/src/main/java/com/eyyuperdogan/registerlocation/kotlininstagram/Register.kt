package com.eyyuperdogan.registerlocation.kotlininstagram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.eyyuperdogan.registerlocation.kotlininstagram.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private lateinit var binding: ActivityRegisterBinding
private lateinit var auth:FirebaseAuth
class Register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth=Firebase.auth

       //kullanıcı eposta ve şifresini veri tabanına kayıt eder
        binding.buttonNewRegister.setOnClickListener(){
            var gmail= binding.textEpostaRegister.text.toString()
            var password= binding.textPassword.text.toString()
            if (gmail.isNotEmpty() && password.isNotEmpty()){
               auth.createUserWithEmailAndPassword(gmail,password).addOnSuccessListener {
                   //success (başarılı)
                   intent= Intent(this,MainActivity::class.java)
                   startActivity(intent)
                   finish()
               }.addOnFailureListener {
                   //başarısız
            Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
               }
            }
            else{
                Toast.makeText(this,"eposta ve şifre boş olamaz!",Toast.LENGTH_LONG).show()
            }
        }
    }
}