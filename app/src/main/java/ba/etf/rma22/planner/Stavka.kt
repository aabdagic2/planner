package ba.etf.rma22.planner

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(tableName = "stavke")
class Stavka(@PrimaryKey(autoGenerate = true) val id: Int=0, var naziv: String,var uradjeno:Boolean,  val danId:Int) :
    Parcelable {

}
