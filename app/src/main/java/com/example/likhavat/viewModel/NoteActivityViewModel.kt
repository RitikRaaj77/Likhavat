package com.example.likhavat.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.likhavat.model.Note
import com.example.likhavat.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteActivityViewModel(private val repository: NoteRepository) : ViewModel(){

    fun saveNote(newNote : Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.addNote(newNote)
    }
    fun updateNote(existingNote : Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateNote(existingNote)
    }
    fun deleteNote(existingNote : Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteNote(existingNote )
    }
    fun searchNote(query :String) : LiveData<List<Note>> {
        return repository.searchNote(query)
    }
    fun getAllNote() : LiveData<List<Note>> = repository.getNote()

}