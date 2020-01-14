package pl.dzielins42.spells.data.source.room

import androidx.room.*
import androidx.room.RoomDatabase
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import pl.dzielins42.spells.data.source.room.entity.SchoolEntity
import pl.dzielins42.spells.data.source.room.entity.SpellEntity
import pl.dzielins42.spells.data.repository.BaseCrudRepository

@Database(
    entities = [
        SpellEntity::class,
        SchoolEntity::class
    ],
    version = 1
)
abstract class RoomDatabase : RoomDatabase() {
    abstract fun spellDao(): SpellDao
    abstract fun schoolDao(): SchoolDao
}

@Dao
interface SpellDao : BaseCrudDao<SpellEntity> {
    @Query("SELECT * FROM spells")
    override fun getAll(): Flowable<List<SpellEntity>>

    @Query("DELETE FROM spells WHERE id = :id")
    override fun delete(id: Long): Completable
}

@Dao
interface SchoolDao : BaseCrudDao<SchoolEntity> {
    @Query("SELECT * FROM schools")
    override fun getAll(): Flowable<List<SchoolEntity>>

    @Query("DELETE FROM schools WHERE id = :id")
    override fun delete(id: Long): Completable
}

interface BaseCrudDao<M> : BaseCrudRepository<M> {
    @Insert
    override fun insert(record: M): Single<Long>

    @Insert
    override fun insert(vararg records: M): Single<List<Long>>

    @Update
    override fun update(record: M): Completable

    @Update
    override fun update(vararg records: M): Completable

    @Delete
    override fun delete(record: M): Completable

    @Delete
    override fun delete(vararg records: M): Completable
}


