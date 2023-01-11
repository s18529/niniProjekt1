package com.example.niniprojekt1

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.niniprojekt1.databinding.Element2Binding
import com.example.niniprojekt1.databinding.ElementBinding
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.round
import kotlin.math.roundToInt
import kotlin.math.roundToLong

class ShopAdapter (private val viewModel: ProductViewModel) : RecyclerView.Adapter<ShopAdapter.ViewHolder>() {

    private var shops = emptyList<Shop>()

    class ViewHolder(val binding: Element2Binding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = Element2Binding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.shopName.text = shops[position].name
        holder.binding.tvpromien.text = shops[position].radius.toString()
        holder.binding.tvopis.text = shops[position].description
        holder.binding.tvgeo.text = "${(shops[position].longitude* 1000.0).roundToInt() / 1000.0}" +
                "x${(shops[position].latitude* 1000.0).roundToInt() / 1000.0}"
    }

    override fun getItemCount(): Int = shops.size

    fun setShops(dbshops: List<Shop>){
        shops = dbshops
        notifyDataSetChanged()
    }
}