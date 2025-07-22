package com.dzl.gymloggingapp.lifting

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.dzl.gymloggingapp.databinding.ItemEditSetBinding

class EditSetsAdapter(
    private val sets: MutableList<SetEntry>,
) : RecyclerView.Adapter<EditSetsAdapter.SetViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EditSetsAdapter.SetViewHolder {
        val binding = ItemEditSetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EditSetsAdapter.SetViewHolder, position: Int) {
        holder.bind(sets[position])
    }

    override fun getItemCount(): Int = sets.size

    fun addSet(set: SetEntry) {
        sets.add(set)
        notifyItemInserted(sets.size - 1)
    }

    fun getCurrentSets(): List<SetEntry> = sets

    inner class SetViewHolder(private val binding: ItemEditSetBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var currentWeightWatcher: TextWatcher? = null
        private var currentRepsWatcher: TextWatcher? = null

        fun bind(setEntry: SetEntry) {

            currentWeightWatcher?.let { binding.editTextWeight.removeTextChangedListener(it) }
            currentRepsWatcher?.let { binding.editTextReps.removeTextChangedListener(it) }

            binding.editTextWeight.setText(setEntry.weight.toString())
            binding.editTextReps.setText(setEntry.reps.toString())

            currentWeightWatcher = createTextWatcher(binding.editTextWeight) {
                setEntry.weight = it.toIntOrNull() ?: 0
            }

            currentRepsWatcher = createTextWatcher(binding.editTextReps) {
                setEntry.weight = it.toIntOrNull() ?: 0
            }

            binding.editTextWeight.addTextChangedListener(currentWeightWatcher)
            binding.editTextReps.addTextChangedListener(currentRepsWatcher)

            binding.buttonDeleteSet.setOnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    sets.removeAt(pos)
                    notifyItemRemoved(pos)
                }

            }

        }

        private fun createTextWatcher(
            editText: EditText,
            onChange: (String) -> Unit
        ): TextWatcher? {
            return object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable?) {
                    onChange(s?.toString() ?: "")
                }
            }
        }
    }


}