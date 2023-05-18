package ba.etf.rma22.planner

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Stavka::class,Dan::class], version = 1)
abstract class DBPlanner : RoomDatabase() {
    abstract fun stavkaDao(): StavkaDAO
    abstract fun danDao(): DanDAO
}