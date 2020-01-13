package pl.dzielins42.spells.data.source.room

import org.apache.commons.lang3.RandomStringUtils
import pl.dzielins42.spells.data.source.room.entity.SpellEntity
import java.util.*

class SpellDaoTest : AbstractCrudDaoTest<SpellEntity, SpellDao>() {

    override fun getDao(): SpellDao = database.spellDao()

    override fun getExampleRecord(seed: Int): SpellEntity {
        val random = Random(seed.toLong())

        return SpellEntity(
            id = random.nextLong(),
            schoolId = random.nextLong(),
            name = RandomStringUtils.random(8, 0, 0, true, false, null, random),
            level = random.nextInt(10),
            castingTime = RandomStringUtils.random(8, 0, 0, true, false, null, random),
            range = RandomStringUtils.random(8, 0, 0, true, false, null, random),
            hasVerbalComponent = random.nextBoolean(),
            hasSomaticComponent = random.nextBoolean(),
            hasMaterialComponent = random.nextBoolean(),
            duration = RandomStringUtils.random(8, 0, 0, true, false, null, random),
            isRitual = random.nextBoolean()
        )
    }

    override fun updateRecord(record: SpellEntity): SpellEntity {
        return record.copy(name = "${record.name}_updated")
    }

    override fun getRecordId(record: SpellEntity): Long = record.id
}