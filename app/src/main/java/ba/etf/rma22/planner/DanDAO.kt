package ba.etf.rma22.planner

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

@Dao
interface DanDAO {

    @Query("SELECT * FROM dani")
    fun getAll(): List<Dan>

    @Query("SELECT * FROM dani WHERE id = :userId")
    fun getById(userId: Long): Dan?

    @Query("SELECT * FROM dani WHERE dan= :day AND mjesec= :month AND godina=:year")
    fun getByDate(day: String, month: String, year: String): List<Dan>?
    @Query("SELECT * FROM dani WHERE dan=:dan AND mjesec=:mjesec AND godina=:godina")
    fun getByDateAsync(dan:String, mjesec:String, godina:String): LiveData<List<Dan>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: Dan)

    @Delete
    fun delete(user: Dan)

    @Update
    fun update(user: Dan)
}