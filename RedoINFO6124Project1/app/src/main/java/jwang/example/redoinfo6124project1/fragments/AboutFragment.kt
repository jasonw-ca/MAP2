package jwang.example.redoinfo6124project1.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import jwang.example.redoinfo6124project1.MainActivity
import jwang.example.redoinfo6124project1.R


class AboutFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        (context as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (context as MainActivity).toolbar.title = "About"


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                (context as MainActivity).supportFragmentManager.popBackStackImmediate()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    companion object {

        @JvmStatic
        fun newInstance() = AboutFragment()
    }
}