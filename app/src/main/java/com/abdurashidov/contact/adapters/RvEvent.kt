package com.abdurashidov.contact.adapters

import android.widget.ImageView
import com.abdurashidov.contact.models.MyContact

interface RvEvent {
    fun menuClick(myContact: MyContact, view:ImageView)
    fun callNow(myContact: MyContact)
}