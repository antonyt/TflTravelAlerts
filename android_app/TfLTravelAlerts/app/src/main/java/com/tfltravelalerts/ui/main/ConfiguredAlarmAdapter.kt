package com.tfltravelalerts.ui.main

import android.content.Context
import android.databinding.ViewDataBinding
import android.graphics.Paint
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.tfltravelalerts.R
import com.tfltravelalerts.common.getColorCompat
import com.tfltravelalerts.common.setTextColorRes
import com.tfltravelalerts.databinding.MainAddAlarmRowBinding
import com.tfltravelalerts.databinding.MainConfiguredAlarmRowBinding
import com.tfltravelalerts.model.AndroidTimePrinter
import com.tfltravelalerts.model.ConfiguredAlarm
import com.tfltravelalerts.model.Day


private val VIEW_TYPE_ALARM = 0
private val VIEW_TYPE_CREATE_NEW = 1

class ConfiguredAlarmAdapter(private val context: Context)
    : RecyclerView.Adapter<ConfiguredAlarmBaseViewHolder>(),
        AddAlarmViewHolder.ViewActions,
        ConfiguredAlarmViewHolder.ViewActions {
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
    private val timePrinter = AndroidTimePrinter(context)
    var alarms : MutableList<ConfiguredAlarm> = mutableListOf<ConfiguredAlarm>()

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1) VIEW_TYPE_CREATE_NEW else VIEW_TYPE_ALARM
    }

    override fun onBindViewHolder(holder: ConfiguredAlarmBaseViewHolder, position: Int) {
        when (holder) {
            is AddAlarmViewHolder -> {
                // nothing has to be done
            }
            is ConfiguredAlarmViewHolder -> {
                holder.showAlarm(alarms[position])
            }
        }
        holder.executePendingBindings()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConfiguredAlarmBaseViewHolder {
        when (viewType) {
            VIEW_TYPE_ALARM -> {
                val binding = MainConfiguredAlarmRowBinding.inflate(layoutInflater, parent, false)
                binding.timePrinter = timePrinter
                return ConfiguredAlarmViewHolder(binding, this)
            }
            VIEW_TYPE_CREATE_NEW -> {
                val binding = MainAddAlarmRowBinding.inflate(layoutInflater, parent, false)
                return AddAlarmViewHolder(binding, this)
            }
            else -> {
                throw IllegalStateException("Failed to map viewType $viewType")
            }
        }
    }

    override fun getItemCount() = alarms.size + 1

    override fun onAddAlarmClicked() {
        Toast.makeText(context, "Create new", Toast.LENGTH_LONG).show()
    }

    override fun onAlarmClicked(alarm: ConfiguredAlarm) {
        Toast.makeText(context, "Edit alarm", Toast.LENGTH_LONG).show()
    }

    override fun onEnabledChanged(alarm: ConfiguredAlarm, isEnabled: Boolean, position: Int) {
        alarms[position] = alarms[position].withEnabledValue(isEnabled)
        notifyItemChanged(position)
    }
}

sealed class ConfiguredAlarmBaseViewHolder(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
    abstract fun executePendingBindings()
}

class ConfiguredAlarmViewHolder(
        val binding: MainConfiguredAlarmRowBinding,
        val listener: ViewActions)
    : ConfiguredAlarmBaseViewHolder(binding) {

    init {
        binding.listener = this
    }

    override fun executePendingBindings() {
        binding.executePendingBindings()
    }

    fun showAlarm(alarm: ConfiguredAlarm) {
        binding.listener = null
        binding.alarm = alarm
        val viewGroup = binding.weekDaysContainer?.root as ViewGroup
        binding.executePendingBindings()
        // need to execute bindings before setting up the font
        setupFont(viewGroup, alarm)
        binding.listener = this
    }

    private fun setupFont(viewGroup: ViewGroup, alarm: ConfiguredAlarm) {
        for (i in 0 until viewGroup.childCount) {
            val view = viewGroup.getChildAt(i) as TextView
            val day = view.tag as Day
            var flags = view.paintFlags
            if (alarm.includesDay(day)) {
                flags = flags or Paint.UNDERLINE_TEXT_FLAG
                view.typeface = Typeface.DEFAULT_BOLD
                view.context.getColorCompat(R.color.main_configured_alarm_week_text_enabled)
                view.setTextColorRes(R.color.main_configured_alarm_week_text_enabled)
            } else {
                flags = flags and Paint.UNDERLINE_TEXT_FLAG.inv()
                view.typeface = Typeface.DEFAULT
                view.setTextColorRes(R.color.main_configured_alarm_week_text_disabled)
            }
            view.paintFlags = flags
        }
    }

    fun onAlarmClicked(alarm: ConfiguredAlarm) {
        listener.onAlarmClicked(alarm)
    }

    fun onEnabledChanged(alarm: ConfiguredAlarm, isEnabled: Boolean) {
        listener.onEnabledChanged(alarm, isEnabled, adapterPosition)
    }

    interface ViewActions {
        fun onAlarmClicked(alarm: ConfiguredAlarm)
        fun onEnabledChanged(alarm: ConfiguredAlarm, isEnabled: Boolean, position: Int)
    }
}

class AddAlarmViewHolder(val binding: MainAddAlarmRowBinding,
                         val listener: ViewActions)
    : ConfiguredAlarmBaseViewHolder(binding) {
    init {
        binding.listener = this
    }

    override fun executePendingBindings() {
        binding.executePendingBindings()
    }

    fun onClick() {
        listener.onAddAlarmClicked()
    }

    interface ViewActions {
        fun onAddAlarmClicked()
    }
}
