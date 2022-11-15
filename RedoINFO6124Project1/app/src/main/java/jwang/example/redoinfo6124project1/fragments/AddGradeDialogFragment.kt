package jwang.example.redoinfo6124project1.fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import jwang.example.redoinfo6124project1.R
import jwang.example.redoinfo6124project1.databinding.FragmentAddGradeDialogBinding
import jwang.example.redoinfo6124project1.databinding.FragmentDashboardBinding
import jwang.example.redoinfo6124project1.models.GradeType

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

class AddGradeDialogFragment : DialogFragment() {

    private var param1: String? = null
    private var param2: GradeType? = null

    private lateinit var binding: FragmentAddGradeDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getSerializable(ARG_PARAM2) as GradeType?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddGradeDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setTitle("Title")
        binding.editTextFullMark.requestFocus()
        dialog?.window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
 //       return super.onCreateDialog(savedInstanceState)
        return AlertDialog.Builder(requireContext())
            .setMessage("Message")
            .setCancelable(false)
//            .setPositiveButton("Save", {
//                Log.d("Tag", "dialog OK")
//            })
//            .setNegativeButton("Cancel"){
//                ()->
//                Log.d("Tag", "dialog Cancel")
//            }
            .create()


    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: GradeType) =
            AddGradeDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putSerializable(ARG_PARAM2, param2)
                }
            }
    }
}