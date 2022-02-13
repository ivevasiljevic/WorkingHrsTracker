package com.vasha.workhrstracker.features.scanner

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vasha.workhrstracker.data.Log
import com.vasha.workhrstracker.databinding.LogItemBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class LogsAdapter : ListAdapter<Log, LogsAdapter.LogViewHolder>(LogComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        val binding = LogItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        val currentItem = getItem(position)
        if(currentItem != null) {
            holder bindTo currentItem
        }
    }

    inner class LogViewHolder(val binding: LogItemBinding) : RecyclerView.ViewHolder(binding.root) {
        infix fun bindTo(log: Log) {
            binding.activityType.text = log.type
            binding.activityDate.text = formatTimestamp(log.timestamp)
        }

        private fun formatTimestamp(timestamp: String): String {
            val dateTime = timestamp.split("T")
            val dateParts = dateTime[0].split("-")
            val date = dateParts[2] + "." + dateParts[1] + "." + dateParts[0]
            val time = dateTime[1].replace("Z", "")
            return "$date@$time"
        }
    }

    class LogComparator : DiffUtil.ItemCallback<Log>() {
        override fun areItemsTheSame(oldItem: Log, newItem: Log): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Log, newItem: Log): Boolean {
            return oldItem.id == newItem.id
        }
    }
}