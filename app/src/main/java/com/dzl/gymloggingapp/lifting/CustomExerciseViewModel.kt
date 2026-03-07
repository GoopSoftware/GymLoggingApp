package com.dzl.gymloggingapp.lifting

import DefaultExercises
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

class CustomExerciseViewModel(application: Application) : AndroidViewModel(application) {
    /*
    This View Models job is to load and save the exercise list for the app. The user
    can add custom exercises which will be added to the list.
     */

    private val file = File(application.filesDir, "exercises.json")
    private val gson = Gson()

    private val _exerciseList = MutableLiveData<List<String>>(mutableListOf())
    val exerciseList: LiveData<List<String>> get() = _exerciseList


    // Says its never used?? However it's being used in AddExerciseDialog??
    fun loadExercises() {
        if (file.exists()) {
            val json = file.readText()
            val type = object : TypeToken<MutableList<String>>() {}.type
            val savedList: MutableList<String> = gson.fromJson(json, type)
            _exerciseList.value = savedList
        } else {
            val default = DefaultExercises.list.map { it.name.lowercase()}
            _exerciseList.value = default
            saveExercises(default)
        }
    }

    fun addExercise(name: String): Boolean {

        val trimmed = name.trim()
        val lower = trimmed.lowercase()


        val existing = _exerciseList.value.orEmpty().map { it.lowercase() }
        val defaultExercises = DefaultExercises.list.map { it.name.lowercase()}

        return if (trimmed.isNotEmpty() && lower !in existing && lower !in defaultExercises) {
            val updatedList = _exerciseList.value?.toMutableList() ?: mutableListOf()
            updatedList.add(trimmed)
            _exerciseList.value = updatedList.sortedBy { it.lowercase() }
            saveExercises(updatedList)
            true
        } else {
            false
        }
    }

    fun renameExercise(oldName: String, newName: String): Boolean {
        val current = _exerciseList.value?.toMutableList() ?: return false
        if (newName.isBlank() || newName in current) return false

        val index = current.indexOf(oldName)
        if (index != -1) {
            current[index] = newName
            _exerciseList.value = current.sortedBy { it.lowercase() }
            saveExercises(current)
            return true
        }
        return false
    }

    fun deleteExercise(name: String) {
        val current = _exerciseList.value?.toMutableList() ?: return
        if (current.remove(name)) {
            _exerciseList.value = current.sortedBy { it.lowercase() }
            saveExercises(current)
        }
    }

    private fun saveExercises(list: List<String>) {
        file.writeText(gson.toJson(list))
    }


}