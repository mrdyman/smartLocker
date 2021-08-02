package com.macca.smartlocker.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.macca.smartlocker.Fragment.TransactionCompletedFragment
import com.macca.smartlocker.Fragment.TransactionPendingFragment
import com.macca.smartlocker.Fragment.TransactionRunningFragment

class HistoryLayoutAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TransactionPendingFragment()
            1 -> TransactionRunningFragment()
            else -> TransactionCompletedFragment()
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}