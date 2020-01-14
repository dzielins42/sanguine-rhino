package pl.dzielins42.spells.data.source.room

import org.apache.commons.lang3.RandomStringUtils
import pl.dzielins42.spells.data.source.room.entity.CharacterClassEntity
import pl.dzielins42.spells.data.source.room.entity.SchoolEntity
import pl.dzielins42.spells.data.source.room.entity.SpellEntity
import java.util.*

class CharacterClassDaoTest : AbstractCrudDaoTest<CharacterClassEntity, CharacterClassDao>() {

    override fun getDao(): CharacterClassDao = database.characterClassDao()

    override fun getExampleRecord(seed: Int): CharacterClassEntity {
        val random = Random(seed.toLong())

        return CharacterClassEntity(
            id = random.nextLong(),
            name = RandomStringUtils.random(8, 0, 0, true, false, null, random)
        )
    }

    override fun updateRecord(record: CharacterClassEntity): CharacterClassEntity {
        return record.copy(name = "${record.name}_updated")
    }

    override fun getRecordId(record: CharacterClassEntity): Long = record.id
}