package com.dzl.gymloggingapp.lifting

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.dzl.gymloggingapp.databinding.ItemSetEditableBinding

class EditSetsAdapter(
        private val sets: MutableList<SetEntry>
) : RecyclerView.Adapter<EditSetsAdapter.SetViewHolder>() {

    inner class SetViewHolder(val binding: ItemSetEditableBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetViewHolder {
        val binding = ItemSetEditableBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SetViewHolder(binding)
    }

    override fun getItemCount(): Int = sets.size

    override fun onBindViewHolder(holder: SetViewHolder, position: Int) {
        val set = sets[position]
        val binding = holder.binding

        binding.editTextWeight.setText(set.weight.takeIf { it != 0 }?.toString() ?: "")
        binding.editTextReps.setText(set.reps.takeIf { it != 0 }?.toString() ?: "")

        binding.editTextWeight.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val newWeight = s?.toString()?.toIntOrNull() ?: 0
                sets[holder.adapterPosition].weight = newWeight
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })


        binding.editTextReps.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val newReps = s?.toString()?.toIntOrNull() ?: 0
                sets[holder.adapterPosition].reps = newReps
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.buttonDeleteSet.setOnClickListener {
            val pos = holder.adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                sets.removeAt(pos)
                notifyItemRemoved(pos)
                notifyItemRangeChanged(pos, sets.size)
            }
        }
    }

    fun addSet(set: SetEntry) {
        sets.add(set)
        notifyItemInserted(sets.size - 1)
    }

    fun getCurrentSets(): List<SetEntry> = sets
}