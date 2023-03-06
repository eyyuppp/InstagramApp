package com.eyyuperdogan.registerlocation.kotlininstagram.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eyyuperdogan.registerlocation.kotlininstagram.databinding.RecyclerRowBinding
import com.eyyuperdogan.registerlocation.kotlininstagram.modul.Post
import com.squareup.picasso.Picasso

class HolderAdapter(var postsList:java.util.ArrayList<Post>):RecyclerView.Adapter<HolderAdapter.PostHolder>(){
    class PostHolder(val binding: RecyclerRowBinding):RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val binding=RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PostHolder(binding)
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.binding.recyclerViewGmail.text=postsList.get(position).gmail
        holder.binding.recyclerViewComment.text=postsList.get(position).comment

        //image kütüphaneleri
        //piccaso android arat
        //glide android
        Picasso.get().load(postsList.get(position).downloadurl).into(holder.binding.recyclerViewMageView)

    }

    override fun getItemCount(): Int {
        return postsList.size
    }
}