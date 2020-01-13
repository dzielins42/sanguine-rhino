package pl.dzielins42.spells.data.repository

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import pl.dzielins42.spells.data.model.Spell
import pl.dzielins42.spells.data.source.room.RoomDatabase
import pl.dzielins42.spells.data.source.room.entity.SpellEntity

interface SpellRepository : BaseCrudRepository<Spell>

class RoomSpellRepository(
    private val roomDatabase: RoomDatabase
) : SpellRepository {
    override fun getAll(): Flowable<List<Spell>> {
        return roomDatabase.spellDao().getAll()
            .subscribeOn(Schedulers.io())
            .flatMapSingle { list ->
                Flowable.fromIterable(list)
                    .map { entity ->
                        Spell(
                            id = entity.id,
                            name = entity.name,
                            level = entity.level,
                            castingTime = entity.castingTime,
                            range = entity.range,
                            hasVerbalComponent = entity.hasVerbalComponent,
                            hasSomaticComponent = entity.hasSomaticComponent,
                            hasMaterialComponent = entity.hasMaterialComponent,
                            duration = entity.duration,
                            isRitual = entity.isRitual
                        )
                    }.toList()
                    .subscribeOn(Schedulers.computation())
            }
    }

    override fun insert(record: Spell): Single<Long> {
        return roomDatabase.spellDao().insert(convertSpellToSpellEntity(record))
    }

    override fun insert(vararg records: Spell): Single<List<Long>> {
        return roomDatabase.spellDao()
            .insert(*records.map { convertSpellToSpellEntity(it) }.toTypedArray())
    }

    override fun update(record: Spell): Completable {
        return roomDatabase.spellDao().update(convertSpellToSpellEntity(record))
    }

    override fun update(vararg records: Spell): Completable {
        return roomDatabase.spellDao()
            .update(*records.map { convertSpellToSpellEntity(it) }.toTypedArray())
    }

    override fun delete(record: Spell): Completable {
        return roomDatabase.spellDao().delete(convertSpellToSpellEntity(record))
    }

    override fun delete(vararg records: Spell): Completable {
        return roomDatabase.spellDao()
            .delete(*records.map { convertSpellToSpellEntity(it) }.toTypedArray())
    }

    override fun delete(id: Long): Completable {
        return roomDatabase.spellDao().delete(id)
    }

    private fun convertSpellToSpellEntity(spell: Spell): SpellEntity {
        return SpellEntity(
            id = spell.id,
            schoolId = 0,
            name = spell.name,
            level = spell.level,
            castingTime = spell.castingTime,
            range = spell.range,
            hasVerbalComponent = spell.hasVerbalComponent,
            hasSomaticComponent = spell.hasSomaticComponent,
            hasMaterialComponent = spell.hasMaterialComponent,
            duration = spell.duration,
            isRitual = spell.isRitual
        )
    }
}