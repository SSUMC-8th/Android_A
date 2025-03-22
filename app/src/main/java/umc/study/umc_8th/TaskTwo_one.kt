package umc.study.umc_8th

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.example.umc_8th.TaskOneTempActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//fragment에 전달될 인자들: bundle의 key로 사용
//Activity = intent, Fragment = bundle
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TaskTwo_one.newInstance] factory method to
 * create an instance of this fragment.
 */
class TaskTwo_one : Fragment() {
    // TODO: Rename and change types of parameters
    //bundle에 있는 값을 꺼낼 때 사용
    private var param1: String? = null
    private var param2: String? = null

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
        return inflater.inflate(R.layout.fragment_task_two_one, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnVeryHappy = view.findViewById<ImageButton>(R.id.btn_veryhappy_frag)
        val btnHappy = view.findViewById<ImageButton>(R.id.btn_happy_frag)
        val btnBoard = view.findViewById<ImageButton>(R.id.btn_board_frag)
        val btnWuul = view.findViewById<ImageButton>(R.id.btn_wuul_frag)
        val btnAngry = view.findViewById<ImageButton>(R.id.btn_angry_frag)

        btnVeryHappy.setOnClickListener { sendIntent("Happy!") }
        btnHappy.setOnClickListener { sendLink("https://www.youtube.com/shorts/7fTHD07Q9Pw?feature=share") }
        btnBoard.setOnClickListener { sendIntent("Board...") }
        btnWuul.setOnClickListener { sendIntent("Sad...") }
        btnAngry.setOnClickListener { sendIntent("Angry!") }

    }

    private fun sendLink(text: String) {
        // Fragment에서는 this가 Fragment 자신을 의미하므로, requireContext()로 Context를 얻어와야 합니다.
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(text))
        startActivity(intent)
    }

    private fun sendIntent(text: String) {
        // 두 번째 파라미터에 Activity 클래스를 넣을 때도 requireContext()를 사용
        val intent = Intent(requireContext(), TaskOneTempActivity::class.java)
        intent.putExtra("sendTemp", text)
        startActivity(intent)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TaskTwo_one.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TaskTwo_one().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}