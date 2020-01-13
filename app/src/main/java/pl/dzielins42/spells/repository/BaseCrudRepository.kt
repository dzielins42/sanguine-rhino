package pl.dzielins42.spells.repository

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface BaseCrudRepository<M> {
    fun getAll(): Flowable<List<M>>

    fun insert(record: M): Single<Long>

    fun insert(vararg records: M): Single<List<Long>>

    fun update(record: M): Completable

    fun update(vararg records: M): Completable

    fun delete(record: M): Completable

    fun delete(vararg records: M): Completable

    fun delete(id: Long): Completable
}