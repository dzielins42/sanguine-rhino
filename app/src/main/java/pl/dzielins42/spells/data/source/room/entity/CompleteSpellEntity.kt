package pl.dzielins42.spells.data.source.room.entity

import androidx.room.Embedded
import androidx.room.Relation

data class CompleteSpellEntity(
    @Embedded
    val spell: SpellEntity,
    @Relation(
        parentColumn = "schoolId",
        entityColumn = "id"
    )
    val school: SchoolEntity
)