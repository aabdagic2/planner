package ba.etf.rma22.planner

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var stavkaDao: StavkaDAO
    private lateinit var danDao: DanDAO
    private lateinit var db: DBPlanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize database
        db = Room.databaseBuilder(
            applicationContext,
            DBPlanner::class.java, "my-db"
        ).build()

        stavkaDao = db.stavkaDao()
        danDao = db.danDao()
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        scope.launch {
           /* withContext(Dispatchers.IO) {
                // Insert data
                danDao.insert(Dan(dan = "12", mjesec = "3", godina = "2023"))
                danDao.insert(Dan(dan = "11", mjesec = "3", godina = "2023"))
                danDao.insert(Dan(dan = "9", mjesec = "3", godina = "2023"))
                stavkaDao.insert(Stavka(naziv = "otići u teretanu", uradjeno = false, danId = 1))
                stavkaDao.insert(Stavka(naziv = "uraditi zadacu", uradjeno = true, danId = 1))
                stavkaDao.insert(Stavka(naziv = "otići u teretanu2", uradjeno = false, danId = 2))
                stavkaDao.insert(Stavka(naziv = "uraditi zadacu2", uradjeno = true, danId = 2))
                stavkaDao.insert(Stavka(naziv = "otići u teretanu3", uradjeno = false, danId = 3))
                stavkaDao.insert(Stavka(naziv = "uraditi zadacu3", uradjeno = true, danId = 3))
            }*/

            // Set up UI
            val viewPager2: ViewPager2 = findViewById(R.id.viewPager)
            val adapter = ViewPagerAdapter(this@MainActivity, stavkaDao, danDao)
            viewPager2.adapter = adapter

            val callback = SwipeLeftToAddFragmentCallback(viewPager2, adapter)
            viewPager2.registerOnPageChangeCallback(callback)

            var imageView = findViewById<ImageView>(R.id.imageView)
             Glide.with(this@MainActivity).load(R.drawable.ated).into(imageView)

            val calendar = Calendar.getInstance()
            val currentDate = calendar.time
            val dateFormat = SimpleDateFormat("EEE, MMM dd")
            val formattedDate = dateFormat.format(currentDate)
        }
    }
}
