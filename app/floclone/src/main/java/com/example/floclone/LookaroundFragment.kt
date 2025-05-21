package com.example.floclone

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LookaroundFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LookaroundFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    //tab들
    private lateinit var tabs: List<TextView>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lookaround, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //각 탭들 list로
        tabs = listOf(
            view.findViewById(R.id.btn_one_lookaroundFragment),
            view.findViewById(R.id.btn_two_lookaroundFragment),
            view.findViewById(R.id.btn_three_lookaroundFragment),
            view.findViewById(R.id.btn_four_lookaroundFragment),
            view.findViewById(R.id.btn_five_lookaroundFragment),
            view.findViewById(R.id.btn_six_lookaroundFragment),
        )
        //초기 설정
        val tabOne = view.findViewById<TextView>(R.id.btn_one_lookaroundFragment)
        val tvSubtitle = view.findViewById<TextView>(R.id.tv_tabtitle_lookaroundFragment)
        tabOne.isSelected = true




        tabs.forEach { tab ->
            tab.setOnClickListener {
                //다 false
                tabs.forEach { it.isSelected = false }
                //선택한 놈만
                tab.isSelected = true
                tvSubtitle.text = tab.text
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LookaroundFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LookaroundFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}