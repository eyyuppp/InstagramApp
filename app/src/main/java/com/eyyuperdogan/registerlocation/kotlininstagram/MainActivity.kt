package com.eyyuperdogan.registerlocation.kotlininstagram

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.eyyuperdogan.registerlocation.kotlininstagram.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private lateinit var binding: ActivityMainBinding
private lateinit var auth: FirebaseAuth
class MainActivity : AppCompatActivity() {
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth=Firebase.auth


        //kullanıcı her defasında gmail ve eposta girmek zorunda kalmaması için
        val currentUser= auth.currentUser
        if (currentUser!=null){
            intent=Intent(this@MainActivity,Shares::class.java)
            startActivity(intent)
        }


        //kullanıcı eposta ve şifre doğru olup olmadığı kontrol edilir
        binding.buttonLogin.setOnClickListener(){
          val gmail= binding.textEposta.text.toString()
          var password= binding.textPassword.text.toString()

            if (gmail.isNotEmpty() && password.isNotEmpty()){
                auth.signInWithEmailAndPassword(gmail,password).addOnSuccessListener {
                    intent=Intent(this@MainActivity,Shares::class.java)
                    startActivity(intent)
                    finish()
                }.addOnFailureListener{
                    Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
                }
            }
            else{
                Toast.makeText(this,"eposta ve şifre boş olamaz!",Toast.LENGTH_LONG).show()
            }
        }
        binding.buttonRegister.setOnClickListener() {
            var intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
    }
}