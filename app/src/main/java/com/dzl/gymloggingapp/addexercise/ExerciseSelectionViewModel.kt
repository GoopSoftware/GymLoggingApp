package com.dzl.gymloggingapp.addexercise

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ExerciseSelectionViewModel : ViewModel() {

    // Internal mutable (The _ signals it shouldnt be exposed outside of the class) Which confuses me because shouldnt private do that too?
    private val _selectedExercise = MutableLiveData<String?>()

    // External Read only
    val selectedExercise: LiveData<String?> = _selectedExercise

    fun selectedExercise(name: String) {
        _selectedExercise.value = name
    }

    fun clearSelection() {
        _selectedExercise.value = null
    }


}