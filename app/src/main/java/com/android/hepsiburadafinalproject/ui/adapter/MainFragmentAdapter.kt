package com.android.hepsiburadafinalproject.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.hepsiburadafinalproject.R
import com.android.hepsiburadafinalproject.databinding.CollectionItemBinding
import com.android.hepsiburadafinalproject.model.Result
import com.android.hepsiburadafinalproject.ui.fragment.MainFragmentDirections

class MainFragmentAdapter:PagingDataAdapter<Result, MainFragmentAdapter.ViewHolder>(DiffUtilCallBack()) {
    class ViewHolder(val binding:CollectionItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(result: Result){
            binding.apply {
                content=result
                executePendingBindings()
                itemClickListener(result)
            }
        }

        fun itemClickListener(result: Result){
            binding.itemLayout.apply {
                setOnClickListener {
                    val action= MainFragmentDirections.actionMainFragmentToDetailFragment(result)
                    findNavController().navigate(action)
                }
            }
        }



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater=LayoutInflater.from(parent.context)
        val view=DataBindingUtil.inflate<CollectionItemBinding>(inflater, R.layout.collection_item,parent,false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    class DiffUtilCallBack:DiffUtil.ItemCallback<Result>(){
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {//??
            return oldItem.trackId.equals(newItem.trackId)

        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.trackId.equals(newItem.trackId)
        }

    }

}