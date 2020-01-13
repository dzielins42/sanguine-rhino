package pl.dzielins42.spells.data.source

import org.junit.Rule
import org.junit.Test

abstract class AbstractCrudDaoTest<M, T : BaseCrudDao<M>> : AbstractRoomDatabaseTest() {

    @Test
    fun getFromEmpty() {
        getDao().getAll()
            .test()
            .assertNoErrors()
            .assertNotComplete()
            .assertValueCount(1)
            .assertValue { value -> value.isEmpty() }
    }

    @Test
    fun insertAndGet() {
        val testSubscriber = getDao().getAll().test()

        val exampleRecord = getExampleRecord()

        getDao().insert(exampleRecord).blockingGet()

        testSubscriber.assertNoErrors()
            .assertNotComplete()
            .assertValueCount(2)
            .assertValueAt(0) { value ->
                value.isEmpty()
            }
            .assertValueAt(1) { value ->
                value.isNotEmpty() && value.size == 1 && value[0] == exampleRecord
            }
    }

    @Test
    fun insertUpdateAndGet() {
        val testSubscriber = getDao().getAll().test()

        val exampleRecord = getExampleRecord()
        val updatedRecord = updateRecord(exampleRecord)

        getDao().insert(exampleRecord).blockingGet()
        getDao().update(updatedRecord).blockingAwait()

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
                value.isNotEmpty() && value.size == 1 && value[0] == updatedRecord && getRecordId(exampleRecord) == getRecordId(updatedRecord)
            }
    }

    @Test
    fun insertDeleteAndGet() {
        val testSubscriber = getDao().getAll().test()

        val exampleRecord = getExampleRecord()

        getDao().insert(exampleRecord).blockingGet()
        getDao().delete(exampleRecord).blockingAwait()

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
    fun insertDeleteByIdAndGet() {
        val testSubscriber = getDao().getAll().test()

        val exampleRecord = getExampleRecord()

        getDao().insert(exampleRecord).blockingGet()
        getDao().delete(getRecordId(exampleRecord)).blockingAwait()

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

    protected abstract fun getDao(): T

    protected abstract fun getExampleRecord(seed: Int = 42): M

    protected abstract fun updateRecord(record: M): M

    protected abstract fun getRecordId(record: M): Long
}