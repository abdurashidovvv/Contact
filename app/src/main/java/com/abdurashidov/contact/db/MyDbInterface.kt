package com.abdurashidov.contact.db

import com.abdurashidov.contact.models.MyContact

interface MyDbInterface {

    fun addContact(myContact: MyContact)
    fun getAllContact():ArrayList<MyContact>
    fun deleteContact(myContact: MyContact)
    fun editContact(myContact: MyContact)

}