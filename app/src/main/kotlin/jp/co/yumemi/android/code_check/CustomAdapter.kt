package jp.co.yumemi.android.code_check

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter(
    private val itemClickListener: OnItemClickListener,
) : ListAdapter<RepositoryItem, ViewHolder>(diff_util) {

    interface OnItemClickListener { fun itemClick(item: RepositoryItem) }

    companion object {
        val diff_util = object : DiffUtil.ItemCallback<RepositoryItem>() {
            override fun areItemsTheSame(oldItem: RepositoryItem, newItem: RepositoryItem): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: RepositoryItem, newItem: RepositoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_repository_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        (holder.itemView.findViewById<View>(R.id.repositoryNameView) as TextView).text =
            item.name

        holder.itemView.setOnClickListener {
            itemClickListener.itemClick(item)
        }
    }
}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view)