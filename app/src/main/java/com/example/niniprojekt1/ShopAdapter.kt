package com.example.niniprojekt1

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.niniprojekt1.databinding.Element2Binding
import com.example.niniprojekt1.databinding.ElementBinding
import com.google.firebase.auth.FirebaseAuth

class ShopAdapter (private val viewModel: ProductViewModel) : RecyclerView.Adapter<ShopAdapter.ViewHolder>() {


    private var shops = emptyList<Shop>()
    var positionPom: Int? = null
    //val user = FirebaseAuth.getInstance().currentUser?.uid

    class ViewHolder(val binding: Element2Binding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = Element2Binding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)


    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.shopName.text = shops[position].name
        holder.binding.tvpromien.text = shops[position].radius.toString()
        holder.binding.tvopis.text = shops[position].description
        holder.binding.cBShopList.isChecked= shops[position].best

        holder.binding.cBShopList.setOnClickListener {

            var shop = shops[position]

            shop.best = !shop.best
            viewModel.updateShop(shop)

            Toast.makeText(holder.binding.root.context,
                "Zaktualizowano sklep o id: ${shops[position].id}",
                Toast.LENGTH_LONG)
                .show()
        }

    }

    override fun getItemCount(): Int = shops.size


    fun setShops(dbshops: List<Shop>){
        shops = dbshops
        notifyDataSetChanged()
    }

}