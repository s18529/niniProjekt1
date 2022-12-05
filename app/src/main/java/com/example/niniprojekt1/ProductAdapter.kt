package com.example.niniprojekt1

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.niniprojekt1.databinding.ElementBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*

class ProductAdapter (private val viewModel: ProductViewModel) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    private var products = emptyList<Product>()
    private lateinit var sp: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    var positionPom: Int? = null



    val user = FirebaseAuth.getInstance().currentUser?.uid


    class ViewHolder(val binding: ElementBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ElementBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)


    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.textView2.text = products[position].name
        holder.binding.textView3.text = products[position].price.toString()
        holder.binding.textView.text = products[position].quantity.toString()
        holder.binding.checkBox.isChecked = products[position].state



        //sp = getSharedPreferences("mainSP", Context.MODE_PRIVATE)

        holder.binding.checkBox.setOnClickListener {
            //val private =sp.getBoolean("private",false)
            val product = products[position]


            product.state = !product.state

            //if (private){
                viewModel.update(product)
            //}else{
                //viewModel.updateUserProduct(user,product)
            //}



            Toast.makeText(holder.binding.root.context,
                "Zaktualizowano produkt o id: ${products[position].id}",
                Toast.LENGTH_LONG)
                .show()
        }

        holder.binding.root.setOnClickListener{
            positionPom = holder.adapterPosition

            Toast.makeText(holder.binding.root.context,
                "Zaznaczono element o id: ${products[position].id}",
                Toast.LENGTH_SHORT)
                .show()
        }

    }

     fun update(product: Product){

        viewModel.update(product)

            notifyDataSetChanged()

    }

    override fun getItemCount(): Int = products.size

    fun add(product : Product){
        viewModel.insert(product)

        notifyDataSetChanged()
    }


    fun addUserProducts(users: String?, product: Product){
        viewModel.insterUserProduct(users,product)
            notifyDataSetChanged()

    }

     fun delete(){

        if (positionPom != null) {

                viewModel.delete(products[positionPom!!])

            //positionPom = null
            //Toast.makeText(ViewHolder., positionPom.toString(), Toast.LENGTH_SHORT).show()
        }else{
        }

            notifyDataSetChanged()
        positionPom = null
    }
 fun deleteUserProduct(){

        if (positionPom != null) {

                viewModel.deleteUserProduct(user,products[positionPom!!])

            //positionPom = null
            //Toast.makeText(ViewHolder., positionPom.toString(), Toast.LENGTH_SHORT).show()
        }else{

        }

            notifyDataSetChanged()

        positionPom = null
    }

     fun deleteAll(){
        viewModel.deleteAll()
            notifyDataSetChanged()
    }
     fun deleteAllUserProducts(){
        viewModel.deleteAllUserProducts(user)
            notifyDataSetChanged()

    }
//    suspend fun maxId():Long{
//        return viewModel.maxId()
//    }


    fun setProducts(dbproducts: List<Product>){
        products = dbproducts
        notifyDataSetChanged()
    }
}