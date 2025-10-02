package com.example.calcapp.menu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.calcapp.R
import android.widget.Button


class MenuAdapter(
    private val menuList: List<MenuItem>,
    private val onTotalChanged: (() -> Unit)? = null // 총액 갱신 콜백
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_MENU = 0
        private const val TYPE_SEPARATOR = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (menuList[position].isSeparator) TYPE_SEPARATOR else TYPE_MENU
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_MENU) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_menu, parent, false)
            MenuViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_separator, parent, false)
            SeparatorViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MenuViewHolder) {
            holder.bind(menuList[position])
        }
    }

    override fun getItemCount() = menuList.size

    inner class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtName: TextView = itemView.findViewById(R.id.txtMenuName)
        private val txtPrice: TextView = itemView.findViewById(R.id.txtMenuPrice)
        private val txtCount: TextView = itemView.findViewById(R.id.txtCount)
        private val btnIncrease: Button = itemView.findViewById(R.id.btnIncrease) // ▲ 버튼
        private val btnDecrease: Button = itemView.findViewById(R.id.btnDecrease) // ▼ 버튼

        fun bind(item: MenuItem) {
            txtName.text = item.name
            txtPrice.text = "${item.price}원"
            txtCount.text = item.count.toString()

            btnIncrease.setOnClickListener {
                item.count++
                txtCount.text = item.count.toString()
                onTotalChanged?.invoke()
            }

            btnDecrease.setOnClickListener {
                if (item.count > 0) {
                    item.count--
                    txtCount.text = item.count.toString()
                    onTotalChanged?.invoke()
                }
            }
        }
    }

    class SeparatorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}