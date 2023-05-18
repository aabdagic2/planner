package ba.etf.rma22.planner

import androidx.room.*


@Dao
    interface StavkaDAO {
        @Query("SELECT * FROM stavke")
        fun getAll(): List<Stavka>

        @Query("SELECT * FROM stavke WHERE id = :userId")
        fun getById(userId: Long): Stavka?

    @Query("SELECT * FROM stavke WHERE danId= :dayId")
    fun getByDate(dayId: Int): List<Stavka>?

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insert(user: Stavka)

        @Delete
        fun delete(user: Stavka)

        @Update
        fun update(user: Stavka)
    @Query("UPDATE stavke SET naziv = :newAge WHERE id = :id")
    fun updateNazivById(id: Long, newAge: String)
    @Query("UPDATE stavke SET uradjeno = :uradjeno WHERE id = :id")
    fun updateUradjenoById(id: Long, uradjeno:Boolean)
    }
