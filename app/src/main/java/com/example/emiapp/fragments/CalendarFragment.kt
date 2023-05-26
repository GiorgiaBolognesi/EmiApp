package com.example.emiapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.emiapp.R
import com.example.emiapp.models.AppAdapter
import com.example.emiapp.models.Startdate
import com.example.emiapp.models.ViewModel

private lateinit var viewModel : ViewModel
private lateinit var dataRecyclerView : RecyclerView
lateinit var adapter: AppAdapter



class CalendarFragment : Fragment() {

    private var tvstartdate: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tvstartdate = it.getString("tvstartDate")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view =  inflater.inflate(R.layout.fragment_calendar, container, false)
        val data = arguments
        if (data != null) {
            //    Toast.makeText(context, data.getString("string").toString(), Toast.LENGTH_SHORT).show()
            val dat = Startdate()
            dat.start = data.getString("string").toString()
            viewModel = ViewModelProvider(this).get(ViewModel::class.java)
            // viewModel.add(data.getString("string").toString())
            tvstartdate?.let { viewModel.add(it) }
        }
        return view
    }

    //QUESTO MOSTRA A SCHERMO
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataRecyclerView = view.findViewById(R.id.recyclerView)
        dataRecyclerView.layoutManager = LinearLayoutManager(context)
        dataRecyclerView.setHasFixedSize(true)
        adapter = AppAdapter()
        dataRecyclerView.adapter = adapter

        viewModel = ViewModelProvider(this).get(ViewModel::class.java)
        viewModel.allData.observe(viewLifecycleOwner, Observer {
            adapter.updateDataList(it)
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(/*events : List<MyEvent>*/tvstartDate:String) =
            CalendarFragment().apply {
                arguments = Bundle().apply {
                    putString("tvstartDate", tvstartDate)

                }
            }
    }


}