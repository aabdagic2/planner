package ba.etf.rma22.planner

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "dani")
class Progres(@PrimaryKey val id:Int,var progres: Float) {
}