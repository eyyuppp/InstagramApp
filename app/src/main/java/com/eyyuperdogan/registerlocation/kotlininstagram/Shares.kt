package com.eyyuperdogan.registerlocation.kotlininstagram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.eyyuperdogan.registerlocation.kotlininstagram.adapter.HolderAdapter
import com.eyyuperdogan.registerlocation.kotlininstagram.databinding.ActivitySharesBinding
import com.eyyuperdogan.registerlocation.kotlininstagram.modul.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.ArrayList

private lateinit var binding:ActivitySharesBinding
private lateinit var auth: FirebaseAuth
private lateinit var db:FirebaseFirestore
private lateinit var postAdapter:HolderAdapter
private lateinit var postList:ArrayList<Post>
class Shares : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySharesBinding.inflate(layoutInflater)

        setContentView(binding.root)
        db=Firebase.firestore
        postList= ArrayList()
        getAll()
        //recyclerrowu recylerviewe bağlama işlemi yapilır
        binding.recyclerView.layoutManager=LinearLayoutManager(this)
        postAdapter= HolderAdapter(postList)
        binding.recyclerView.adapter= postAdapter


        //Firabase etkin hale getiriyoruz
        auth= Firebase.auth
        binding.imageViewAdd.setOnClickListener(){
            intent= Intent(this,Sharing::class.java)
            startActivity(intent)
        }
       binding.imageViewOut.setOnClickListener(){
           auth.signOut()
           intent= Intent(this,MainActivity::class.java)
           startActivity(intent)
           finish()
       }
    }

fun getAll(){
    //order by dizme
    //where ilede kısılamalar getirebilirsin
    //DESCENDING tarihi yukarıdan sırala(yani en son kayıtı ön sırada dizer)
   db.collection("posts").orderBy("date",Query.Direction.DESCENDING).addSnapshotListener { value, error ->
       if (error!=null) {
           Toast.makeText(this, error.localizedMessage, Toast.LENGTH_LONG).show()
       }
         else{
            if (value!=null){
                if (!value.isEmpty){
                    val documants=value.documents
                   //casting
                  for (document in documants){
                      val comment=document.get("comment") as String
                      val gmail=document.get("gmail") as String
                      val downloadurl=document.get("downloadurl") as String
                      val post=Post(gmail,comment,downloadurl)
                      postList.add(post)
                  }
                    postAdapter.notifyDataSetChanged()//veriler güncelensin
                }
            }
           }
   }

}

}