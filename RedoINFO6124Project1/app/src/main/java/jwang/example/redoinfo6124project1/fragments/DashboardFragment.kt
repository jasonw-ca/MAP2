package jwang.example.redoinfo6124project1.fragments

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import jwang.example.redoinfo6124project1.*
import jwang.example.redoinfo6124project1.databinding.FragmentDashboardBinding
//import jwang.example.redoinfo6124project1.gradeList
import jwang.example.redoinfo6124project1.models.Course
import jwang.example.redoinfo6124project1.models.Grade


const val ARG_PARAM1 = "param1"
const val ARG_PARAM2 = "param2"



class DashboardFragment : Fragment() {

    private var param1: String? = null
    private val toolbar: Toolbar by lazy {
        requireActivity().findViewById<Toolbar>(R.id.toolbar)
    }
    private lateinit var binding: FragmentDashboardBinding

    val courses: List<Course> = listOf(
        Course("INFO6124", "Android App Development", 0, 0),
        Course("INFO6125", "iOS App Development", 0, 0),
        Course("INFO6126", "UI/UX Design" , 0, 0),
        Course("INFO6127", "Enterprise Platform" , 0, 0),
        Course("INFO6128", "Progressive Web Application" , 0, 0),
        Course("INFO6129", "Cross-platform App Development" , 0, 0)
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)
        binding.listViewCourses.adapter = CourseAdapter(requireContext(), courses)
        binding.listViewCourses.onItemClickListener =
            OnItemClickListener { parent, view, position, id ->
                val courseCode = courses.get(position).courseCode
                val courseName = courses.get(position).courseName
                val transaction = fragmentManager?.beginTransaction()
                transaction?.replace(
                    R.id.fragment_placeholder,
                    GradesListFragment.newInstance(courseCode, courseName)
                )?.addToBackStack(null)?.commit()
            }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (context as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (context as MainActivity).supportActionBar?.setDisplayShowHomeEnabled(false)

        //(context as MainActivity).scrollable_toolbar.isTitleEnabled = false
        (context as MainActivity).toolbar.title = getString(R.string.app_name)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.toolbar_main, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onResume() {
        super.onResume()
        for (course in courses) {
            when(course.courseCode){
                "INFO6124" -> getSummary(course, gradeList6124)
                "INFO6125" -> getSummary(course, gradeList6125)
                "INFO6126" -> getSummary(course, gradeList6126)
                "INFO6127" -> getSummary(course, gradeList6127)
                "INFO6128" -> getSummary(course, gradeList6128)
                "INFO6129" -> getSummary(course, gradeList6129)
            }
        }
    }

    private fun getSummary(course: Course, gradeList: ArrayList<Grade>) {
        var currentGrade: Double = 0.0
        var currentMark: Double = 0.0
        var currentFullMark: Double = 0.0
        gradeList.forEach {
                currentFullMark += it.fullMark
                currentMark += it.myMark
                if (currentMark != 0.0) {
                    currentGrade = Math.ceil(currentMark / currentFullMark * 100)
                }
        }
        course.currentMarks = currentMark.toInt()
        course.currentGrade = currentGrade.toInt()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.item_about -> {
                Log.d("MyTag", "about clicked")
                val ft = requireFragmentManager().beginTransaction()
                ft.replace(
                    R.id.fragment_placeholder,
                    AboutFragment.newInstance()
                )
                ft.addToBackStack(null)
                ft.commit()
                true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            DashboardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)

                }
            }
    }
}