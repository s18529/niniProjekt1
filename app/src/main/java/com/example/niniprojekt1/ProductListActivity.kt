package com.example.niniprojekt1

import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.niniprojekt1.databinding.ActivityProductListBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*

class ProductListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductListBinding
    private lateinit var sp: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private var maxId :Long=0

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //var user = auth.currentUser

        //if (user != null) {
            binding = ActivityProductListBinding.inflate(layoutInflater)
            setContentView(binding.root)


            sp = getSharedPreferences("mainSP", Context.MODE_PRIVATE)
            editor = sp.edit()




            var productViewModel = ProductViewModel(application)
            var productadapter = ProductAdapter(productViewModel)

//        productViewModel.allProducts.observe(this, Observer {
//            productadapter.setProducts(it)
//
//        })

        if (!binding.switchToPrivate.isChecked) {
            productViewModel.allProducts.observe(this, Observer { productsList ->
                productsList.let {
                    productadapter.setProducts(productsList.values.toList())
                }
            })
            editor.putBoolean("private",false)
        }else{
            productViewModel.allUserProduct.observe(this, Observer { productsList ->
                productsList.let {
                    productadapter.setProducts(productsList.values.toList())
                }
            })
            editor.putBoolean("private",true)
        }

        binding.switchToPrivate.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!isChecked){
                productViewModel.allProducts.observe(this, Observer { productsList ->
                    productsList.let {
                        productadapter.setProducts(productsList.values.toList())
                    }
                })
                editor.putBoolean("private",false)
            }else{
                productViewModel.allUserProduct.observe(this, Observer { productsList ->
                    productsList.let {
                        productadapter.setProducts(productsList.values.toList())
                    }
                })
                editor.putBoolean("private",true)
            }
        }

            //LayoutManager do listy
            binding.rv1.layoutManager = LinearLayoutManager(this)
            //DividerItemDecorator (optional)
            binding.rv1.addItemDecoration(
                DividerItemDecoration(
                    this,
                    DividerItemDecoration.VERTICAL
                )
            )
            //Adapter do Listy
            binding.rv1.adapter = productadapter
            binding.addButton.setOnClickListener {

                val products = Product(
                    id = "1",
                    name = binding.name.text.toString(),
                    price = (binding.price.text.toString()).toDouble(),
                    quantity = (binding.quantity.text.toString()).toLong(),
                    state = binding.checkBox.isChecked,
                    isPrivate = false
                )


//
                    val user = FirebaseAuth.getInstance().currentUser?.uid

                if (!binding.switchToPrivate.isChecked){
                    productadapter.add(products)
                }else{
                    products.isPrivate = true
                    productadapter.addUserProducts(user,products)
                }



                    val values = ContentValues()
                    values.put(MyContentProvider.name, binding.name.text.toString())
                    values.put(MyContentProvider.price, binding.price.text.toString().toDouble())


                    contentResolver.insert(MyContentProvider.CONTENT_URI, values)


                    binding.name.text.clear()
                    binding.price.text.clear()
                    binding.quantity.text.clear()
                    binding.checkBox.isChecked = false


                sendBroadcast(Intent().also {
                    it.component = ComponentName(
                        "com.example.appprivider",
                        "com.example.appprivider.ProductsReceiver"
                    )
                    it.putExtra("idProduct", maxId)
                    it.putExtra("nameProducts", products.name)
                    it.putExtra("priceProducts", products.price)
                })

                Log.w(
                    "index",
                    "----------------------------------------------------------------------------------------------------${maxId}"
                )


                Toast.makeText(binding.root.context, "Dodano nowy produkt", Toast.LENGTH_SHORT)
                    .show()
            }

            binding.buttonDeleteSelected.setOnClickListener() {


                if (!binding.switchToPrivate.isChecked){
                    productadapter.delete()
                }else{
                    productadapter.deleteUserProduct()
                }


            }

            binding.buttonDelete.setOnClickListener {


                if (!binding.switchToPrivate.isChecked){
                    productadapter.deleteAll()
                }else{
                    productadapter.deleteAllUserProducts()
                }


                CoroutineScope(Dispatchers.IO).launch {
                    contentResolver.delete(
                        MyContentProvider.CONTENT_URI,
                        null,
                        null
                    )
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