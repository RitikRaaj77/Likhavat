package com.example.likhavat.activities

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.likhavat.R
import com.example.likhavat.dataBase.NoteDatabase
import com.example.likhavat.databinding.ActivityMainBinding
import com.example.likhavat.repository.NoteRepository
import com.example.likhavat.viewModel.NoteActivityViewModel
import com.example.likhavat.viewModel.NoteActivityViewModelFactory

class MainActivity : AppCompatActivity() {

    lateinit var vieActivityViewModel: NoteActivityViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        try {
            val noteRepository = NoteRepository(NoteDatabase(this))
            val noteActivityViewModelFactory = NoteActivityViewModelFactory(noteRepository)
            vieActivityViewModel = ViewModelProvider(
                this,
                noteActivityViewModelFactory)[NoteActivityViewModel::class.java]
        }catch (e : Exception){
            Log.d("TAG", "error")
        }
    }
}
