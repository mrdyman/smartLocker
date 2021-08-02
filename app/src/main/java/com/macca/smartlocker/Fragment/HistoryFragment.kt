package com.macca.smartlocker.Fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.tabs.TabLayoutMediator
import com.macca.smartlocker.Adapter.HistoryLayoutAdapter
import com.macca.smartlocker.R
import kotlinx.android.synthetic.main.fragment_history.*

class HistoryFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vp_history.adapter = HistoryLayoutAdapter(requireActivity())

        val tabl = TabLayoutMediator(tl_history, vp_history, TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            when (position){
                0 -> {
                    tab.text = "Pending"
                    tab.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_pending, null)
                }1 -> {
                    tab.text = "Running"
                    tab.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_transaction_running, null)
                }2 -> {
                    tab.text = "Completed"
                    tab.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_done, null)
                }
            }
        })

        tabl.attach()
    }
}