package com.example.likhavat.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.transition.Visibility
import com.example.likhavat.R
import com.example.likhavat.activities.MainActivity
import com.example.likhavat.databinding.FragmentNoteBinding
import com.example.likhavat.utils.hideKeyboard
import com.example.likhavat.viewModel.NoteActivityViewModel
import com.google.android.material.transition.MaterialElevationScale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class NoteFragment : Fragment(R.layout.fragment_note) {

    private lateinit var noteBinding: FragmentNoteBinding
    private val noteActivityViewModel: NoteActivityViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        exitTransition = MaterialElevationScale(false).apply {
            duration = 350
        }
        enterTransition = MaterialElevationScale(true).apply {
            duration = 350
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noteBinding = FragmentNoteBinding.bind(view)
        val activity = activity as MainActivity
        val navController = Navigation.findNavController(view)
        requireView().hideKeyboard()
        CoroutineScope(Dispatchers.Main).launch {
            delay(10)
            //activity.window.statusBarColor = Color.WHITE
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            activity.window.statusBarColor = Color.parseColor("#9E9D9D")
        }

        noteBinding.addNoteFab.setOnClickListener {
            noteBinding.appBarLayout.visibility = View.INVISIBLE
            navController.navigate(NoteFragmentDirections.actionNoteFragmentToSaveYaUpdateFragment())

        }
        noteBinding.innerFab.setOnClickListener {
            noteBinding.appBarLayout.visibility = View.INVISIBLE
            navController.navigate(NoteFragmentDirections.actionNoteFragmentToSaveYaUpdateFragment())

        }
    }
}