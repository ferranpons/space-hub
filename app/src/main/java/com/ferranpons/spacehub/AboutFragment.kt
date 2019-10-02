package com.ferranpons.spacehub

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class AboutFragment : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, 0)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_about, container)
        /*val projectUrl = view.findViewById(R.id.projectUrl) as TextView
        projectUrl.setOnClickListener { view1 ->
            if (activity != null) {
                val intent = Intent(activity.applicationContext, WebViewActivity::class.java)
                startActivity(intent)
            }
        }*/
        return view
    }
}
