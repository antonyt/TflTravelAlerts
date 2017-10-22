package com.tfltravelalerts.ui.main

import android.content.Context
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.tfltravelalerts.databinding.MainAddAlarmRowBinding
import com.tfltravelalerts.model.ConfiguredAlarm

class ConfiguredAlarmAdapter(val context: Context)
    : RecyclerView.Adapter<ConfiguredAlarmBaseViewHolder>(),
        AddAlarmViewHolder.ViewActions {
    val VIEW_TYPE_ALARM = 0
    val VIEW_TYPE_CREATE_NEW = 1
    val layoutInflator = LayoutInflater.from(context)
    var alarms = emptyList<ConfiguredAlarm>()

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1) VIEW_TYPE_CREATE_NEW else VIEW_TYPE_ALARM
    }

    override fun onBindViewHolder(holder: ConfiguredAlarmBaseViewHolder, position: Int) {
        when (holder) {
            is AddAlarmViewHolder -> {
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConfiguredAlarmBaseViewHolder {
        when (viewType) {
            VIEW_TYPE_ALARM -> {

            }
            VIEW_TYPE_CREATE_NEW -> {

            }
            else -> {
            }
        }

        val binding = MainAddAlarmRowBinding.inflate(layoutInflator, parent, false)
        return AddAlarmViewHolder(binding, this)
    }

    override fun getItemCount() = alarms.size + 1

    override fun onAddAlarmClicked() {
        Toast.makeText(context, "Create new", Toast.LENGTH_LONG).show()
    }


}

sealed class ConfiguredAlarmBaseViewHolder(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)

class ConfiguredAlarmViewHolder : ConfiguredAlarmBaseViewHolder(null!!)

class AddAlarmViewHolder(binding: MainAddAlarmRowBinding,
                         val listener: ViewActions)
    : ConfiguredAlarmBaseViewHolder(binding) {
    init {
        binding.listener = this
    }

    fun onClick() {
        listener.onAddAlarmClicked()
    }

    interface ViewActions {
        fun onAddAlarmClicked()
    }
}
