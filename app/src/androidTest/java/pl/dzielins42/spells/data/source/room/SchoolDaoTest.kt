package pl.dzielins42.spells.data.source.room

import org.apache.commons.lang3.RandomStringUtils
import pl.dzielins42.spells.data.source.room.entity.SchoolEntity
import pl.dzielins42.spells.data.source.room.entity.SpellEntity
import java.util.*

class SchoolDaoTest : AbstractCrudDaoTest<SchoolEntity, SchoolDao>() {

    override fun getDao(): SchoolDao = database.schoolDao()

    override fun getExampleRecord(seed: Int): SchoolEntity {
        val random = Random(seed.toLong())

        return SchoolEntity(
            id = random.nextLong(),
            name = RandomStringUtils.random(8, 0, 0, true, false, null, random)
        )
    }

    override fun updateRecord(record: SchoolEntity): SchoolEntity {
        return record.copy(name = "${record.name}_updated")
    }

    override fun getRecordId(record: SchoolEntity): Long = record.id
}