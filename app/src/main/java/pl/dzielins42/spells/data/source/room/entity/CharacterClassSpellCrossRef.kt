package pl.dzielins42.spells.data.source.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "character_classes_spells_cross_ref",
    primaryKeys = ["spellId", "characterClassId"],
    foreignKeys = [
        ForeignKey(
            entity = SpellEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("spellId"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CharacterClassEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("characterClassId"),
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = arrayOf(
        Index("spellId"),
        Index("characterClassId")
    )
)
data class CharacterClassSpellCrossRef(
    val spellId: Long,
    val characterClassId: Long
)