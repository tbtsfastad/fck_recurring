package fck.and.recurring

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Index
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Entity(
    tableName = "work",
    indices = [Index(value = ["name", "round"], unique = true)],
)
internal data class Work(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val worker: String,
    val interval: Int,
    val round: Int,
    val state: Int,
) {
    companion object {
        const val PENDING = 0
        const val DID = 1
    }
}

@Dao
internal interface WorkDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(work: Work)
    @Query("UPDATE work SET interval = :interval WHERE id = :id")
    fun updateInterval(id: Int, interval: Int)
    @Query("UPDATE work SET state = :state WHERE id = :id")
    fun updateState(id: Int, state: Int)
    @Query("SELECT * FROM work WHERE name = :name AND round = :round")
    fun query(name: String, round: Int): Work?
    @Query("SELECT * FROM work WHERE name = :name AND state = 0 ORDER BY round DESC LIMIT 1")
    fun query(name: String): Work?
}

@Database(entities = [Work::class], version = 1, exportSchema = false)
internal abstract class RecurringDatabase : RoomDatabase() {
    abstract fun workDao(): WorkDao
}

internal class Repo private constructor() {
    private val dao = Room.databaseBuilder(
        FckRecurring.app,
        RecurringDatabase::class.java,
        "fck_recurring.db",
    ).build().workDao()
    suspend fun insert(work: Work) =
        withContext(Dispatchers.IO) { runCatching { dao.insert(work) } }
    suspend fun updateInterval(id: Int, interval: Int) =
        withContext(Dispatchers.IO) { runCatching { dao.updateInterval(id, interval) } }
    suspend fun updateState(id: Int, state: Int) =
        withContext(Dispatchers.IO) { runCatching { dao.updateState(id, state) } }
    suspend fun query(name: String, round: Int): Work? =
        withContext(Dispatchers.IO) { runCatching { dao.query(name, round) }.getOrNull() }
    suspend fun query(name: String): Work? =
        withContext(Dispatchers.IO) { runCatching { dao.query(name) }.getOrNull() }
    companion object {
        val instance by lazy { Repo() }
    }
}
