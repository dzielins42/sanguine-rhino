package pl.dzielins42.spells.data.source.room

import org.apache.commons.lang3.RandomStringUtils
import org.junit.Before
import org.junit.Test
import pl.dzielins42.spells.data.source.room.entity.CharacterClassEntity
import pl.dzielins42.spells.data.source.room.entity.CharacterClassSpellCrossRef
import pl.dzielins42.spells.data.source.room.entity.SchoolEntity
import pl.dzielins42.spells.data.source.room.entity.SpellEntity
import java.util.*
import kotlin.collections.ArrayList

class SpellDaoTest : AbstractCrudDaoTest<SpellEntity, SpellDao>() {

    private var schools: List<SchoolEntity> = emptyList()
    private var characterClasses: List<CharacterClassEntity> = emptyList()

    @Before
    override fun setUp() {
        super.setUp()

        val random = Random()

        schools = ArrayList<SchoolEntity>().apply {
            for (i in 0..4) {
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

        characterClasses = ArrayList<CharacterClassEntity>().apply {
            for (i in 0..4) {
                add(
                    CharacterClassEntity(
                        id = random.nextLong(),
                        name = RandomStringUtils.random(8, 0, 0, true, false, null, random)
                    )
                )
            }
        }
        characterClasses.forEach {
            database.characterClassDao().insert(it).blockingGet()
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

    @Test
    fun insertAndGetJoined() {
        val testSubscriber = getDao().getAllJoined().test()

        val exampleRecord = getExampleRecord()

        getDao().insert(exampleRecord).blockingGet()

        database.characterClassSpellCrossRefDao()
            .insert(
                CharacterClassSpellCrossRef(
                    spellId = exampleRecord.id,
                    characterClassId = characterClasses[0].id
                )
            ).blockingGet()
        database.characterClassSpellCrossRefDao()
            .insert(
                CharacterClassSpellCrossRef(
                    spellId = exampleRecord.id,
                    characterClassId = characterClasses[1].id
                )
            ).blockingGet()

        testSubscriber.assertNoErrors()
            .assertNotComplete()
            .assertValueCount(4)
            .assertValueAt(0) { value ->
                value.isEmpty()
            }
            .assertValueAt(1) { value ->
                value.isNotEmpty()
                        && value.size == 1
                        && value[0].spell == exampleRecord
                        && value[0].school == schools.find { it.id == value[0].spell.schoolId }
                        && value[0].characterClasses.isEmpty()
            }
            .assertValueAt(2) { value ->
                value.isNotEmpty()
                        && value.size == 1
                        && value[0].spell == exampleRecord
                        && value[0].school == schools.find { it.id == value[0].spell.schoolId }
                        && value[0].characterClasses.size == 1
                        && value[0].characterClasses.any { it.id == characterClasses[0].id }
            }
            .assertValueAt(3) { value ->
                value.isNotEmpty()
                        && value.size == 1
                        && value[0].spell == exampleRecord
                        && value[0].school == schools.find { it.id == value[0].spell.schoolId }
                        && value[0].characterClasses.size == 2
                        && value[0].characterClasses.any { it.id == characterClasses[0].id }
                        && value[0].characterClasses.any { it.id == characterClasses[1].id }
            }
    }

    @Test
    fun insertAndGetJoinedSingleTransaction() {
        val testSubscriber = getDao().getAllJoined().test()

        val exampleRecord = getExampleRecord()

        database.runInTransaction {

            getDao().insert(exampleRecord).blockingGet()

            database.characterClassSpellCrossRefDao()
                .insert(
                    CharacterClassSpellCrossRef(
                        spellId = exampleRecord.id,
                        characterClassId = characterClasses[0].id
                    )
                ).blockingGet()
            database.characterClassSpellCrossRefDao()
                .insert(
                    CharacterClassSpellCrossRef(
                        spellId = exampleRecord.id,
                        characterClassId = characterClasses[1].id
                    )
                ).blockingGet()

        }

        testSubscriber.assertNoErrors()
            .assertNotComplete()
            .assertValueCount(2)
            .assertValueAt(0) { value ->
                value.isEmpty()
            }
            .assertValueAt(1) { value ->
                value.isNotEmpty()
                        && value.size == 1
                        && value[0].spell == exampleRecord
                        && value[0].school == schools.find { it.id == value[0].spell.schoolId }
                        && value[0].characterClasses.size == 2
                        && value[0].characterClasses.any { it.id == characterClasses[0].id }
                        && value[0].characterClasses.any { it.id == characterClasses[1].id }
            }
    }
}