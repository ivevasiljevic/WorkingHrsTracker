package com.vasha.workhrstracker.features.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.vasha.workhrstracker.data.Employee
import com.vasha.workhrstracker.databinding.EmployeeItemBinding

/**
 * Created by ivasil on 1/27/2022
 */

class Adapter : ListAdapter<Employee, Adapter.AdapterViewHolder>(Comparator()) {

     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
      val binding = EmployeeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      return AdapterViewHolder(binding)
     }

     override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
      val currentItem = getItem(position)
       if(currentItem != null) {
       holder bindTo currentItem
      }
     }

     inner class AdapterViewHolder(private val binding: EmployeeItemBinding) : RecyclerView.ViewHolder(binding.root) {
       infix fun bindTo(employee: Employee) {
            binding.apply {
                username.text = employee.user
                activity.text = employee.type
                timestamp.text = employee.timestamp.split(".")[0].replace("T", " ")
            }
       }
     }

   class Comparator : DiffUtil.ItemCallback<Employee>() {
    override fun areItemsTheSame(oldItem: Employee, newItem: Employee): Boolean {
     return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Employee, newItem: Employee): Boolean {
     return oldItem.user == newItem.user
    }

   }
}