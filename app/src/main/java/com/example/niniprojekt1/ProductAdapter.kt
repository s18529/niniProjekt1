package com.example.niniprojekt1

import android.annotation.SuppressLint
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.niniprojekt1.databinding.ActivityMainBinding
import com.example.niniprojekt1.databinding.ElementBinding
import kotlinx.coroutines.*
import java.lang.annotation.ElementType

class ProductAdapter (private val viewModel: ProductViewModel) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    private var products = emptyList<Product>()

    var positionPom: Int? = null



    class ViewHolder(val binding: ElementBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ElementBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
         //var positionPom = 0
        holder.binding.textView2.text = products[position].name
        holder.binding.textView3.text = products[position].price.toString()
        holder.binding.textView.text = products[position].quantity.toString()
        holder.binding.checkBox.isChecked = products[position].state

        //holder.binding.checkBox.isChecked = products[position].state
        holder.binding.checkBox.setOnClickListener {
            val product = products[position]

            product.state = !product.state
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.update(product)
            }

            Toast.makeText(holder.binding.root.context, "Zaktualizowano produkt o id: ${products[position].id}", Toast.LENGTH_LONG).show()
        }

        holder.binding.root.setOnClickListener{

            positionPom = holder.adapterPosition
//            CoroutineScope(Dispatchers.IO).launch {
//                viewModel.delete(products[position])
//            }
                Toast.makeText(holder.binding.root.context, "Zaznaczono element o id: ${products[position].id}", Toast.LENGTH_SHORT).show()

            //Toast.makeText(holder.binding.root.context, "Usunięto produkt o id: ${products[position].id}", Toast.LENGTH_SHORT).show()
        }
//        holder.binding.buttonList2.setOnClickListener {
//            Toast.makeText(holder.binding.root.context, positionPom.toString(), Toast.LENGTH_SHORT).show()
//        }
    }

    suspend fun update(product: Product){

        viewModel.update(product)
        withContext(Dispatchers.Main){
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = products.size

    suspend  fun add(product : Product){
        viewModel.insert(product)
        withContext(Dispatchers.Main){
        notifyDataSetChanged()
        }
    }

    suspend fun delete(){

        if (positionPom != null) {
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.delete(products[positionPom!!])
            }
            //positionPom = null
            //Toast.makeText(ViewHolder., positionPom.toString(), Toast.LENGTH_SHORT).show()
        }else{

        }
        withContext(Dispatchers.Main){
            notifyDataSetChanged()
        }
        positionPom = null
    }

    suspend fun deleteAll(){
        viewModel.deleteAll()
        withContext(Dispatchers.Main){
            notifyDataSetChanged()
        }
    }


    fun setProducts(dbproducts: List<Product>){
        products = dbproducts
        notifyDataSetChanged()
    }
}