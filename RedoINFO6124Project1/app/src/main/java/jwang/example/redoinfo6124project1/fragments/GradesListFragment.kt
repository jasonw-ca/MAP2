package jwang.example.redoinfo6124project1.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import jwang.example.redoinfo6124project1.*
import jwang.example.redoinfo6124project1.databinding.FragmentGradesListBinding
import jwang.example.redoinfo6124project1.models.Grade
import jwang.example.redoinfo6124project1.models.GradeType


class GradesListFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentGradesListBinding

    private lateinit var viewAdapter: GradeAdapter





    private var thisGradeList = arrayListOf<Grade>()

    private var isFabShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { it ->
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)


        }

        /*
        Take this course's grades from main list and delete them in the main list, and then do
        various operation, adding, deleting and so on. Add the list back the main list when this
        fragment destroyed.
         */
        when(param1){
            "INFO6124" -> thisGradeList = gradeList6124
            "INFO6125" -> thisGradeList = gradeList6125
            "INFO6126" -> thisGradeList = gradeList6126
            "INFO6127" -> thisGradeList = gradeList6127
            "INFO6128" -> thisGradeList = gradeList6128
            "INFO6129" -> thisGradeList = gradeList6129
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentGradesListBinding.inflate(inflater, container, false)
        binding.fabShowMenu.setOnClickListener{
            if (isFabShown) {
                hideFabMenu()
            } else {
                showFabMenu()
            }
        }
        binding.fabLayoutAddLab.visibility = ConstraintLayout.INVISIBLE
        binding.fabLayoutAddExam.visibility = ConstraintLayout.INVISIBLE
        binding.fabLayoutAddProject.visibility = ConstraintLayout.INVISIBLE


        binding.tvCourseName.text = param2
        binding.fabLab.setOnClickListener {
            showDialogFragment(GradeType.LAB)
            hideFabMenu()
        }

        binding.fabExam.setOnClickListener {
            showDialogFragment(GradeType.EXAM)
            hideFabMenu()
        }

        binding.fabProject.setOnClickListener {
            showDialogFragment(GradeType.PROJECT)
            hideFabMenu()
        }


        viewAdapter = GradeAdapter(thisGradeList)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        binding.recyclerView.adapter = viewAdapter
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                // this method is called
                // when the item is moved.
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val deletedCourse: Grade =
                    thisGradeList[viewHolder.adapterPosition]


                val position = viewHolder.adapterPosition


                thisGradeList.removeAt(viewHolder.adapterPosition)


                viewAdapter.notifyItemRemoved(viewHolder.adapterPosition)


                Snackbar.make(binding.recyclerView, deletedCourse.type.string, Snackbar.LENGTH_LONG)
                    .setAction("Undo") {


                            thisGradeList.add(position, deletedCourse)


                            viewAdapter.notifyItemInserted(position)
                    }.show()
            }
        }).attachToRecyclerView(binding.recyclerView)

        setHasOptionsMenu(true)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (context as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)


        (context as MainActivity).toolbar.title = param1
        if (thisGradeList.size > 0) {
            binding.tvNoEntry.visibility = TextView.GONE
        }
    }

    private fun showFabMenu() {
        isFabShown = true
        //binding.fabShowMenu.isClickable = false
        binding.fabLayoutAddLab.visibility = ConstraintLayout.VISIBLE
        binding.fabLayoutAddExam.visibility = ConstraintLayout.VISIBLE
        binding.fabLayoutAddProject.visibility = ConstraintLayout.VISIBLE

        binding.recyclerView.animate().alpha(0.15f).apply {
            duration = 175
        }

        binding.fabLayoutAddLab.animate().translationYBy(
            -resources.getDimension(R.dimen.standard_155) - 60).apply {
                duration = 100
        }.withEndAction{
            binding.fabLayoutAddLab.animate().translationYBy(60.toFloat()).apply {
                duration = 75
            }
        }

        binding.fabLayoutAddExam.animate().translationYBy(
            -resources.getDimension(R.dimen.standard_105) - 40).apply {
            duration = 100
        }.withEndAction{
            binding.fabLayoutAddExam.animate().translationYBy(40.toFloat()).apply {
                duration = 75
            }
        }

        binding.fabLayoutAddProject.animate().translationYBy(
            -resources.getDimension(R.dimen.standard_55) - 20).apply {
            duration = 100
        }.withEndAction{
            binding.fabLayoutAddProject.animate().translationYBy(20.toFloat()).apply {
                duration = 75
            }
        }

        binding.fabShowMenu.animate().duration = 100
        binding.fabShowMenu.animate().rotationBy(45.0f)

    }

    private fun hideFabMenu() {
        isFabShown = false
        binding.recyclerView.animate().alpha(1.0f)
        binding.fabLayoutAddLab.animate()
            .translationYBy(resources.getDimension(R.dimen.standard_155))
            .withEndAction { binding.fabLayoutAddLab.visibility = ConstraintLayout.INVISIBLE }
        binding.fabLayoutAddExam.animate()
            .translationYBy(resources.getDimension(R.dimen.standard_105))
            .withEndAction { binding.fabLayoutAddExam.visibility = ConstraintLayout.INVISIBLE }
        binding.fabLayoutAddProject.animate()
            .translationYBy(resources.getDimension(R.dimen.standard_55))
            .withEndAction { binding.fabLayoutAddProject.visibility = ConstraintLayout.INVISIBLE }
        binding.fabShowMenu.animate().rotationBy(-45.0f)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.toolbar_list, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                (context as MainActivity).supportFragmentManager.popBackStackImmediate()
                Log.d("MyTag", "back clicked")
            }
            R.id.item_clear_all -> {
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Warning")
                    .setMessage("Are you sure to delete all the records?")
                    .setCancelable(true)
                    .setPositiveButton("OK"){
                    d,w ->
                    Log.d("MyTag", "clear all clicked")

                    thisGradeList.clear()
                    viewAdapter.notifyDataSetChanged()
                    binding.tvNoEntry.visibility = TextView.VISIBLE
                    d.dismiss()
                    }
                    .setNegativeButton("Cancel"){
                        dialog, which -> dialog.cancel()
                    }
                    .show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDialogFragment(type: GradeType){

        // Dialog can have only one view, so wrap all edittext views in a layout and add it to the dialog

        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL
        val etDialogFullMark = EditText(context).apply {
            this.hint = "Full Mark"
            inputType = InputType.TYPE_CLASS_NUMBER
        }
        val etDialogMyMark = EditText(context).apply {
            this.hint = "My Mark"
            inputType = InputType.TYPE_CLASS_NUMBER
        }
        val etDialogWeight = EditText(context).apply {
            this.hint = "Weight(%)"
            inputType = InputType.TYPE_CLASS_NUMBER
        }
        layout.addView(etDialogFullMark)
        layout.addView(etDialogMyMark)
        layout.addView(etDialogWeight)
        layout.setPadding(50, 20, 50, 20)

        val builder = AlertDialog.Builder(context)
        builder.setTitle(param1 + " " + type.string)
        builder.setView(layout)

        builder.setCancelable(false)
// Though this listener does nothing, it has to be implemented to show the positive button.
        builder.setPositiveButton("Save"){
                dialog, which -> Log.d("MyTag", "positive button")

        }
        builder.setNegativeButton("Cancel"){
                dialog, which -> dialog.cancel()
        }
/*
Disable dialog positive button dismiss the dialog if some check is not passed.
Don't create(). Only show(). Otherwise it won't work to overwrite the listner.
 */
        val dialog = builder.show()


        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val fullMark = etDialogFullMark.text.toString()
            val myMark = etDialogMyMark.text.toString()
            val weight = etDialogWeight.text.toString()
            if (fullMark == ""|| myMark == "" || weight == ""){
                Toast.makeText(context, getString(R.string.empty_field), Toast.LENGTH_LONG).show()
            }else if (fullMark.toInt() > 100){
                Toast.makeText(context, getString(R.string.full_over_100),Toast.LENGTH_SHORT).show()
            }else if (myMark.toInt() > fullMark.toInt()) {
                Toast.makeText(context, getString(R.string.rcv_gt_full),Toast.LENGTH_SHORT).show()
            } else if (weight.toInt() > 100) {
                Toast.makeText(context, getString(R.string.perc_gt_100),Toast.LENGTH_SHORT).show()
            }else {

                var grade = Grade(param1!!, type, fullMark.toInt(), myMark.toInt(), weight.toInt()
                )


                thisGradeList.add(grade)
/*
At first, the recyclerview list could be updated without notifyItem after adding, because after the dialog being
dismissed, the fragment creates view again and assign adapter again. However, if the keyboard is dismissed first, onViewCreated
occurs before saving button being clicked, and then UI will not updated even though data has been added.
 */
                viewAdapter.notifyItemInserted(thisGradeList.size - 1)
                binding.tvNoEntry.visibility = TextView.GONE
                dialog.dismiss()
                Log.d("MyTag", "array size: ${gradeList6124.size}")
            }
        }
    }



    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GradesListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}


