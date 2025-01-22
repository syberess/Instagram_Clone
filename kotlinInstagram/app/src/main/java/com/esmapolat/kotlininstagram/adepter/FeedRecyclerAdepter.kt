package com.esmapolat.kotlininstagram.adepter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.esmapolat.kotlininstagram.databinding.ReceyclerRowBinding
import com.esmapolat.kotlininstagram.model.Post
import com.squareup.picasso.Picasso

class FeedRecyclerAdepter(private var postList:ArrayList<Post>):RecyclerView.Adapter<FeedRecyclerAdepter.PostHolder> (){
    class PostHolder(val binding:ReceyclerRowBinding):RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val binding=ReceyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PostHolder(binding)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.binding.receylerEmailText.text=postList.get(position).email
        holder.binding.commentTextView.text=postList.get(position).comment
        Picasso.get().load(postList.get(position).downloadUrl).into(holder.binding.reyclerMageView)

    }
}