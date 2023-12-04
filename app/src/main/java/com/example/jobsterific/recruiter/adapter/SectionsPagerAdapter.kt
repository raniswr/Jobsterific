package com.example.jobsterific.recruiter.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.jobsterific.recruiter.ui.CampaignFragment
import com.example.jobsterific.recruiter.ui.CandidatesFragment
import com.example.jobsterific.recruiter.ui.ContenderFragment
import com.example.jobsterific.recruiter.ui.HomeRecruiterFragment

class SectionsPagerAdapter(activity: HomeRecruiterFragment) : FragmentStateAdapter(activity) {
    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = CampaignFragment()
            1 -> fragment = CandidatesFragment()
            2 -> fragment = ContenderFragment()
        }
        return fragment as Fragment
    }

    override fun getItemCount(): Int {
        return 3
    }

}