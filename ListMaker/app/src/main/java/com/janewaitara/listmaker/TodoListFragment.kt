package com.janewaitara.listmaker

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup



/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [TodoListFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [TodoListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TodoListFragment : Fragment() {

    private var listener: OnFragmentInteractionListener? = null //interface created

    //used when the fragment is in the process of being created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    //when fragment requires a layout it must have in order to be presented within the activity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_todo_list, container, false)
    }

    //first lifecycle method - run when fragment is first associated with an activity
    // set up anything required before the fragment is created
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context //assigning context to a listener
        }
    }

    //called when a fragment is no longer attached to an activity(destroyed or removed)
    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
         //informs objects when somethings happens
        fun onTodoListClicked(list: TaskList)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment
         *
         * @return A new instance of fragment TodoListFragment.
         */

        fun newInstance(): TodoListFragment{
            return TodoListFragment()
        }
    }
}
