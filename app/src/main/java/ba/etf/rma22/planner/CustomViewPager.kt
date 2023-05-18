package ba.etf.rma22.planner

import androidx.viewpager2.widget.ViewPager2

class SwipeLeftToAddFragmentCallback(private val viewPager: ViewPager2, private val adapter: ViewPagerAdapter) : ViewPager2.OnPageChangeCallback() {

    override fun onPageSelected(position: Int) {
        super.onPageSelected(position)
        if (position == 0) {
           // val newFragment = DailyFragment()
          //  adapter.addFragment(newFragment, 0) // Add the new fragment at the beginning of the list
            adapter.notifyDataSetChanged()
            viewPager.setCurrentItem(1, false) // Move to the new fragment
        }
        if(position==adapter.itemCount-1){
           // val newFragment = DailyFragment()
           // adapter.addFragmentLast(newFragment)
            adapter.notifyDataSetChanged()
        }
    }

}