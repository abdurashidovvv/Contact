package com.abdurashidov.contact

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.abdurashidov.contact.adapters.RvAdapter
import com.abdurashidov.contact.adapters.RvEvent
import com.abdurashidov.contact.databinding.ActivityMainBinding
import com.abdurashidov.contact.databinding.ItemDialogBinding
import com.abdurashidov.contact.db.MyDbHelper
import com.abdurashidov.contact.models.MyContact
import java.util.jar.Manifest
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), RvEvent {

    private lateinit var binding: ActivityMainBinding
    private lateinit var myDbHelper: MyDbHelper
    private lateinit var rvAdapter: RvAdapter
    private lateinit var list:ArrayList<MyContact>
    private lateinit var filteredList:ArrayList<MyContact>

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {

            myDbHelper= MyDbHelper(this@MainActivity)
            list= ArrayList()
            for (c in 'A'..'Z') {
                for (i in myDbHelper.getAllContact()) {
                    if (i.name.subSequence(0,1).toString().toUpperCase() == c.toString()){
                        list.add(i)
                    }
                }
            }
            rvAdapter= RvAdapter(list, this@MainActivity)
            myRv.adapter=rvAdapter


            search.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    filterList(newText)
                    return true
                }

            })


            addBtn.setOnClickListener {
                val dialog=AlertDialog.Builder(this@MainActivity).create()
                val itemDialog=ItemDialogBinding.inflate(layoutInflater)
                dialog.setView(itemDialog.root)

                itemDialog.apply {
                    btnSave.setOnClickListener {
                        if (itemDialog.name.text.toString().isNotEmpty() && itemDialog.number.text!!.isNotEmpty()){
                            val myContact=MyContact(
                                name = itemDialog.name.text.toString(),
                                number = itemDialog.number.text.toString()
                            )
                            myDbHelper.addContact(myContact)
                            Toast.makeText(this@MainActivity, "Saqlandi", Toast.LENGTH_SHORT).show()
                            dialog.cancel()
                            list.clear()
                            for (c in 'A'..'Z') {
                                for (i in myDbHelper.getAllContact()) {
                                    if (i.name.subSequence(0,1).toString().toUpperCase() == c.toString()){
                                        list.add(i)
                                    }
                                }
                            }
                            rvAdapter.list=list
                            rvAdapter.notifyDataSetChanged()
                        }else{
                            Toast.makeText(this@MainActivity, "Hamma maydonlarni to'ldiring!!!", Toast.LENGTH_SHORT).show()
                        }
                        dialog.cancel()
                    }
                }
                 dialog.show()
            }

        }

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun menuClick(myContact: MyContact, view: ImageView) {
        val popupMenu=PopupMenu(this, view)
        popupMenu.inflate(R.menu.my_popup_menu)

        popupMenu.setOnMenuItemClickListener {

            when(it.itemId){
                R.id.menu_delete->{
                    myDbHelper.deleteContact(myContact)
                    rvAdapter.list=myDbHelper.getAllContact()
                    rvAdapter.notifyDataSetChanged()
                    Toast.makeText(this, "Delete", Toast.LENGTH_SHORT).show()
                }
                R.id.menu_edit->{

                    val dialog=AlertDialog.Builder(this).create()
                    val itemDialogBinding=ItemDialogBinding.inflate(layoutInflater)
                    dialog.setView(itemDialogBinding.root)

                    itemDialogBinding.name.setText(myContact.name)
                    itemDialogBinding.number.setText(myContact.number)

                    itemDialogBinding.btnSave.setOnClickListener {

                        myContact.name=itemDialogBinding.name.text.toString()
                        myContact.number=itemDialogBinding.number.text.toString()

                        myDbHelper.editContact(myContact)
                        rvAdapter.list=myDbHelper.getAllContact()
                        rvAdapter.notifyDataSetChanged()
                        Toast.makeText(this, "Edit", Toast.LENGTH_SHORT).show()
                        dialog.cancel()
                    }
                    dialog.show()
                }
            }

            true
        }

        popupMenu.show()
    }

    override fun callNow(myContact: MyContact) {
        val number = myContact.number.toString().trim()
        val REQUEST_PHONE_CALL = 101
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.CALL_PHONE),
                REQUEST_PHONE_CALL)
            Toast.makeText(this, "call", Toast.LENGTH_SHORT).show()
        } else {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.setData(Uri.parse("tel:" + number))
            startActivity(callIntent)
        }
    }


    private fun filterList(newText: String?) {
        filteredList= ArrayList()
        for (i in list) {
            if (i.name.toLowerCase().contains(newText!!.toLowerCase())){
                filteredList.add(i)
            }
        }
        if (filteredList.isEmpty()){
            rvAdapter.list=filteredList
            rvAdapter.notifyDataSetChanged()
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show()
        }else{
            rvAdapter.list=filteredList
            rvAdapter.notifyDataSetChanged()
        }
    }
}