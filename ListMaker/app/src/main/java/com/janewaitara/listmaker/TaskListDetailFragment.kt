package com.janewaitara.listmaker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class TaskListDetailFragment : Fragment() {

    lateinit var list: TaskList


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            list = it.getParcelable(ARG_LIST)!!
        }//check if there are arguments, if yes, you can access the list
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    companion object {

        private val ARG_LIST = "list"

        fun newInstance(list: TaskList): TaskListDetailFragment {

            val bundle = Bundle() //object containing key, value pairs
            //accessing your list within a bundle
            bundle.putParcelable(ARG_LIST,list)
            val fragment = TaskListDetailFragment()
            fragment.arguments = bundle //using arguments to pass data into the fragment
            return fragment

        }
    }
}
