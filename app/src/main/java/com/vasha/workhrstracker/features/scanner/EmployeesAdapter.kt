package com.vasha.workhrstracker.features.scanner

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vasha.workhrstracker.data.Employee
import com.vasha.workhrstracker.data.employeeAvatars
import com.vasha.workhrstracker.data.employeePositions
import com.vasha.workhrstracker.databinding.EmployeeItemBinding

class EmployeesAdapter(private val onItemClickListener: OnItemClickListener) : ListAdapter<Employee, EmployeesAdapter.EmployeeViewHolder>(EmployeeComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val binding = EmployeeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EmployeeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        val currentItem = getItem(position)
        if(currentItem != null) {
            holder bindTo currentItem
        }
    }

    inner class EmployeeViewHolder(val binding: EmployeeItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if(position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if(item != null) {
                        onItemClickListener.onItemClicked(item.id, item.username)
                    }
                }
            }
        }

        infix fun bindTo(employee: Employee) {
            binding.employeeName.text = employee.username
            binding.employeePosition.text = employeePositions.random()
            binding.employeeAvatar.setImageResource(employeeAvatars.random())
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(id: String, fullname: String)
    }

    class EmployeeComparator : DiffUtil.ItemCallback<Employee>() {
        override fun areItemsTheSame(oldItem: Employee, newItem: Employee): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Employee, newItem: Employee): Boolean {
            return oldItem.id == newItem.id
        }
    }
}