package com.example.likhavat.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.likhavat.R
import com.example.likhavat.activities.MainActivity
import com.example.likhavat.databinding.BottomSheetLayoutBinding
import com.example.likhavat.databinding.FragmentSaveYaUpdateBinding
import com.example.likhavat.model.Note
import com.example.likhavat.utils.hideKeyboard
import com.example.likhavat.viewModel.NoteActivityViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.transition.MaterialContainerTransform
import com.madrapps.pikolo.listeners.OnColorSelectionListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.text.SimpleDateFormat
import java.util.Date


class SaveYaUpdateFragment : Fragment(R.layout.fragment_save_ya_update) {

    private lateinit var navController: NavController
    private lateinit var contentBinding: FragmentSaveYaUpdateBinding
    private lateinit var result : String
    private var note: Note? = null
    private var color = -1
    private val noteActivityViewModel: NoteActivityViewModel by activityViewModels()
    private val currentDate = SimpleDateFormat.getInstance().format(Date())
    private val job = CoroutineScope(Dispatchers.Main)
    private val args: SaveYaUpdateFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val animation = MaterialContainerTransform().apply {
            drawingViewId = R.id.fragmentContainerView
            scrimColor = Color.TRANSPARENT
            duration = 300L
        }
        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contentBinding = FragmentSaveYaUpdateBinding.bind(view)

        navController = Navigation.findNavController(view)
        val activity = activity as MainActivity

        contentBinding.backBtn.setOnClickListener {
            requireView().hideKeyboard()
            navController.popBackStack()
        }

        contentBinding.saveNote.setOnClickListener {
            saveNote()
        }

        try {
            contentBinding.etNoteContent.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    contentBinding.bottomBar.visibility = View.VISIBLE
                    contentBinding.etNoteContent.setStylesBar(contentBinding.styleBar)
                } else {
                    contentBinding.bottomBar.visibility = View.GONE
                }
            }
        } catch (e: Throwable) {
            Log.d("TAg", e.stackTrace.toString())
        }

        contentBinding.fabColorPick.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(
                requireContext(),
                R.style.BottomSheetDialogTheme
            )
            val bottomSheetView: View = layoutInflater.inflate(
                R.layout.bottom_sheet_layout,
                null,
            )

            with(bottomSheetDialog) {
                setContentView(bottomSheetView)
                show()
            }

            val bottomSheetBinding = BottomSheetLayoutBinding.bind(bottomSheetView)
            bottomSheetBinding.apply {
                colorPicker.apply {
                    setColor(-1)
                    setColorSelectionListener(object : OnColorSelectionListener {
                        override fun onColorSelected(color: Int) {
                            this@SaveYaUpdateFragment.color = color
                            contentBinding.noteContentFragmentParent.setBackgroundColor(color)
                            bottomSheetDialog.dismiss()
                        }

                        override fun onColorSelectionEnd(color: Int) {
                            this@SaveYaUpdateFragment.color = color
                            contentBinding.noteContentFragmentParent.setBackgroundColor(color)
                            contentBinding.toolbarFragmentNoteContent.setBackgroundColor(color)
                            contentBinding.bottomBar.setBackgroundColor(color)
                            activity.window.statusBarColor = color
                            bottomSheetBinding.bottomSheetParent.setCardBackgroundColor(color)
                        }

                        override fun onColorSelectionStart(color: Int) {
                            this@SaveYaUpdateFragment.color = color
                            contentBinding.noteContentFragmentParent.setBackgroundColor(color)
                        }
                    })
                }
                bottomSheetParent.setCardBackgroundColor(color)
            }
            bottomSheetView.post {
                bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }

    private fun saveNote() {

        if (contentBinding.etNoteContent.text.toString().isEmpty()
            || contentBinding.etTitle.text.toString().isEmpty()
        ) {
            Toast.makeText(activity, "FIll it all", Toast.LENGTH_SHORT).show()
        } else {
            note = args.note
            when (note) {
                null -> {
                    noteActivityViewModel.saveNote(
                        Note(
                            0,
                            contentBinding.etTitle.text.toString(),
                            contentBinding.etNoteContent.text.toString(),
                            currentDate,
                            color
                        )
                    )
                    result = "Note Saved"
                    setFragmentResult(
                        "Key",
                        bundleOf("bundle key" to result)
                    )
                    navController.navigate(SaveYaUpdateFragmentDirections.actionSaveYaUpdateFragmentToNoteFragment())
                }

                else -> {
                    noteActivityViewModel.updateNote(
                        note!!.copy(
                            title = contentBinding.etTitle.text.toString(),
                            content = contentBinding.etNoteContent.text.toString(),
                            date = currentDate,
                            color = color
                        )
                    )
                    navController.navigate(SaveYaUpdateFragmentDirections.actionSaveYaUpdateFragmentToNoteFragment())
                }
            }
        }
    }
}

