package com.example.niniprojekt1

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

    class ViewHolder(val binding: ElementBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ElementBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
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
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.delete(products[position])
            }
            Toast.makeText(holder.binding.root.context, "Usunięto produkt o id: ${products[position].id}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = products.size

    suspend  fun add(product : Product){
        viewModel.insert(product)
        withContext(Dispatchers.Main){
        notifyDataSetChanged()
        }
    }

    suspend fun delete(product: Product, position: Int){
        viewModel.delete(product)
        withContext(Dispatchers.Main){
            viewModel.delete(products[position])
            notifyDataSetChanged()
        }
    }

    suspend fun deleteAll(){
        viewModel.deleteAll()
        withContext(Dispatchers.Main){
            notifyDataSetChanged()
        }
    }

    suspend fun update(product: Product){
        viewModel.update(product)
        withContext(Dispatchers.Main){
            notifyDataSetChanged()
        }
    }

    fun setProducts(dbproducts: List<Product>){
        products = dbproducts
        notifyDataSetChanged()
    }


}