package ba.etf.rma22.planner

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "dani")
class Dan(@PrimaryKey(autoGenerate = true)
          val id: Int = 0,var dan:String, var mjesec:String,var godina:String) {
}