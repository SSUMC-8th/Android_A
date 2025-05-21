package com.example.umc_8th.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.umc_8th.R

class CategoryAdapter(
    private val items: List<String>,
    private val itemClickListener: (String) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(val button: Button) : RecyclerView.ViewHolder(button) {
        fun bind(text: String) {
            button.text = text
            button.setOnClickListener {
                itemClickListener(text)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val button = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false) as Button
        return CategoryViewHolder(button)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
