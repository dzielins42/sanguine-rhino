package pl.dzielins42.spells.data.source.room

import org.apache.commons.lang3.RandomStringUtils
import org.junit.Before
import org.junit.Test
import pl.dzielins42.spells.data.source.room.entity.SchoolEntity
import pl.dzielins42.spells.data.source.room.entity.SpellEntity
import java.util.*
import kotlin.collections.ArrayList

class SpellDaoTest : AbstractCrudDaoTest<SpellEntity, SpellDao>() {

    private var schools: List<SchoolEntity> = emptyList()

    @Before
    override fun setUp() {
        super.setUp()

        val random = Random()

        schools = ArrayList<SchoolEntity>().apply {
            for (i in 0..5) {
                add(
                    SchoolEntity(
                        id = random.nextLong(),
                        name = RandomStringUtils.random(8, 0, 0, true, false, null, random)
                    )
                )
            }
        }
        schools.forEach {
            database.schoolDao().insert(it).blockingGet()
        }
    }

    override fun getDao(): SpellDao = database.spellDao()

    override fun getExampleRecord(seed: Int): SpellEntity {
        val random = Random(seed.toLong())

        return SpellEntity(
            id = random.nextLong(),
            schoolId = schools[random.nextInt(schools.size)].id,
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

    @Test
    fun insertDeleteSchoolAndGet() {
        val testSubscriber = getDao().getAll().test()

        val exampleRecord = getExampleRecord()

        getDao().insert(exampleRecord).blockingGet()
        database.schoolDao().delete(exampleRecord.schoolId).blockingGet()

        testSubscriber.assertNoErrors()
            .assertNotComplete()
            .assertValueCount(3)
            .assertValueAt(0) { value ->
                value.isEmpty()
            }
            .assertValueAt(1) { value ->
                value.isNotEmpty() && value.size == 1 && value[0] == exampleRecord
            }
            .assertValueAt(2) { value ->
                value.isEmpty()
            }
    }
}