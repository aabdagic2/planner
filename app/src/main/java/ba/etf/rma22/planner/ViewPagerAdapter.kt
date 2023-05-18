package ba.etf.rma22.planner

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import kotlinx.coroutines.*
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

class ViewPagerAdapter(activity: FragmentActivity, stavke: StavkaDAO, dani: DanDAO) : FragmentStateAdapter(activity) {

    private val fragments = mutableListOf<Fragment>()
    private val stavke = stavke
    private val dani = dani


    init {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1 // Note: Calendar.MONTH is zero-based
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        // Use a coroutine to fetch and process data on a background thread
        CoroutineScope(Job() + Dispatchers.Main).launch {
            val fragmentsList = mutableListOf<Fragment>()
            for (i in -6..6) {
                withContext(Dispatchers.IO){
                val danas = dani.getByDate((dayOfMonth + i).toString(), month.toString(), year.toString())?.get(0)
                if (danas != null) {
                    println(danas.dan)
                }
                if (danas == null) {
                    val newDan = Dan(dan = (dayOfMonth + i).toString(), mjesec = month.toString(), godina = year.toString())
                    withContext(Dispatchers.IO) {
                        dani.insert(newDan)
                    }
                    createDailyFragment(newDan) { fragment ->
                        fragmentsList.add(fragment)
                    }
                } else {
                    createDailyFragment(danas) { fragment ->
                        fragmentsList.add(fragment)
                    }
                }
            }}
            // Update the UI on the main thread
            fragments.clear()
            fragmentsList.sortBy { fragment ->
                val args = fragment.arguments
                val year = args?.getString("godina")?.toInt() ?: 0
                val month = args?.getString("mjesec")?.toInt() ?: 0
                val day = args?.getString("dan")?.toInt() ?: 0
                LocalDate.of(year, month, day)
            }
            fragments.addAll(fragmentsList)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    private fun createDailyFragment(danas: Dan, callback: (DailyFragment) -> Unit) {
        val bundle = Bundle().apply {
            putString("dan", danas.dan)
            putString("mjesec", danas.mjesec)
            putString("godina", danas.godina)
            putInt("danId", danas.id)
        }
        //println(danas.id)
        CoroutineScope(Dispatchers.IO).launch {
            val dns=dani.getByDate(danas.dan,danas.mjesec,danas.godina)
            val dnevneStavke = dns?.get(0)?.let { stavke.getByDate(it.id) }
            val uradjene = dnevneStavke?.filter { it.uradjeno }?.size ?: 0
            val progres = if (dnevneStavke != null && dnevneStavke.isNotEmpty()) (uradjene.toDouble() / dnevneStavke.size * 100).roundToInt() else 0
            withContext(Dispatchers.Main) {
                bundle.putParcelableArrayList("stavke", ArrayList(dnevneStavke))
                bundle.putInt("progres", progres)
                val dailyFragment = DailyFragment(dani,stavke)
                dailyFragment.arguments = bundle
                callback(dailyFragment)
            }
        }
    }

    fun addFragment(fragment: Fragment, position: Int) {
        fragments.add(position, fragment)
    }

    fun addFragmentLast(newFragment: DailyFragment) {
        fragments.add(newFragment)
        notifyDataSetChanged()
    }
}

