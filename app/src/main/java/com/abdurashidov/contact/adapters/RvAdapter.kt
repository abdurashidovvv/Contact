package com.abdurashidov.contact.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abdurashidov.contact.databinding.RvItemBinding
import com.abdurashidov.contact.models.MyContact

class RvAdapter(var list:List<MyContact>, val rvEvent: RvEvent): RecyclerView.Adapter<RvAdapter.Vh>() {

    inner class Vh(val rvItem: RvItemBinding): RecyclerView.ViewHolder(rvItem.root){
        fun onBind(myContact: MyContact){
            rvItem.name.text=myContact.name
            rvItem.number.text=myContact.number
            rvItem.menuImg.setOnClickListener {
                rvEvent.menuClick(myContact, rvItem.menuImg)
            }
            rvItem.callNow.setOnClickListener {
                rvEvent.callNow(myContact)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(RvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

}