package com.tfltravelalerts.ui.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tfltravelalerts.databinding.MainLineStatusRowBinding
import com.tfltravelalerts.model.Line
import com.tfltravelalerts.model.LineStatus
import com.tfltravelalerts.model.NetworkStatus


class NetworkStatusAdapter
    : RecyclerView.Adapter<NetworkStatusViewHolder>(),
        NetworkStatusRowListener {
    var networkStatus: NetworkStatus? = null
        set(value) {
            if (field != value) {
                field = value
                notifyDataSetChanged()
            }
        }
    private val expandedRows = HashSet<Line>(4)
    private var layoutInflater: LayoutInflater? = null

    // to be more pure MVI, the expanded rows should come from the outside and this should expose
    // the click events that would result in a new state. Let's be a bit lazy and skip that cycle

    override fun onBindViewHolder(holder: NetworkStatusViewHolder, position: Int) {
        networkStatus?.let {
            val status = it.lineStatus[position]
            holder.showLineStatus(status)
            holder.setRowExpanded(expandedRows.contains(status.line))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NetworkStatusViewHolder {
        val localLayoutInflater = layoutInflater ?: LayoutInflater.from(parent.context)
        layoutInflater = localLayoutInflater
        return NetworkStatusViewHolder(localLayoutInflater, parent, this)
    }

    override fun getItemCount(): Int {
        return networkStatus?.lineStatus?.size ?: 0
    }

    override fun onStatusClicked(lineStatus: LineStatus, position: Int) {
        if (toggleExpandedStateIfValid(lineStatus)) {
            notifyItemChanged(position)
        }
    }

    /**
     * returns whether there was an actual change in the state
     */
    private fun toggleExpandedStateIfValid(lineStatus: LineStatus): Boolean {
        val wasExpanded = expandedRows.remove(lineStatus.line)
        if (!wasExpanded && !lineStatus.isGoodService) {
            expandedRows.add(lineStatus.line)
            return true
        }
        return wasExpanded
    }
}

interface NetworkStatusRowListener {
    fun onStatusClicked(lineStatus: LineStatus, position: Int)
}

class NetworkStatusViewHolder(inflater: LayoutInflater,
                              parent: ViewGroup,
                              val listener: NetworkStatusRowListener,
                              val binding: MainLineStatusRowBinding = MainLineStatusRowBinding.inflate(inflater, parent, false))
    : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.listener = this
    }

    fun setRowExpanded(expanded: Boolean) {
        binding.expanded = expanded
    }

    fun showLineStatus(status: LineStatus) {
        binding.status = status
    }

    fun onRowClicked(status: LineStatus) {
        listener.onStatusClicked(status, adapterPosition)
    }
}
