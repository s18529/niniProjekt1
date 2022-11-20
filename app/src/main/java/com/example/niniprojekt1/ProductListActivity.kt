package com.example.niniprojekt1

import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.niniprojekt1.databinding.ActivityProductListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductListBinding
    private lateinit var sp: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sp = getSharedPreferences("mainSP",Context.MODE_PRIVATE)
        editor = sp.edit()


        var productViewModel = ProductViewModel(application)
        var productadapter = ProductAdapter(productViewModel)

        productViewModel.allProducts.observe(this, Observer {
            productadapter.setProducts(it)
        })

        //LayoutManager do listy
        binding.rv1.layoutManager = LinearLayoutManager(this)
        //DividerItemDecorator (optional)
        binding.rv1.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        //Adapter do Listy
        binding.rv1.adapter = productadapter


        binding.addButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                productadapter.add(
                    Product(name = binding.name.text.toString(),
                    price = (binding.price.text.toString()).toDouble(),
                    quantity = (binding.quantity.text.toString()).toInt(),
                    state = binding.checkBox.isChecked)
                )
                CoroutineScope(Dispatchers.IO).launch {
                    val values = ContentValues()
                    values.put(MyContentProvider.name, binding.name.text.toString())
                    values.put(MyContentProvider.price, binding.price.text.toString().toDouble())


                    contentResolver.insert(MyContentProvider.CONTENT_URI, values)
                }

                binding.name.text.clear()
                binding.price.text.clear()
                binding.quantity.text.clear()
                binding.checkBox.isChecked = false
            }
            Toast.makeText(binding.root.context,"Dodano nowy produkt",Toast.LENGTH_SHORT).show()
        }

        binding.buttonDeleteSelected.setOnClickListener(){
            CoroutineScope(Dispatchers.IO).launch {
                productadapter.delete()
            }
            //Toast.makeText(binding.root.context,"Usunięto produkt",Toast.LENGTH_SHORT).show()
        }

        binding.buttonDelete.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                productadapter.deleteAll()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (sp.getBoolean("backgroundColor", false)) {
            binding.addButton.setBackgroundColor(getColor(R.color.green))
            binding.buttonDelete.setBackgroundColor(getColor(R.color.green))
        } else {
            binding.addButton.setBackgroundColor(getColor(R.color.purple_200))
            binding.buttonDelete.setBackgroundColor(getColor(R.color.purple_200))
        }
    }
}