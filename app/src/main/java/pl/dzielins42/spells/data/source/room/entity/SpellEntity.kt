package pl.dzielins42.spells.data.source.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "spells")
data class SpellEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val schoolId: Long,
    val name: String,
    val level: Int,
    val castingTime: String,
    val range: String,
    val hasVerbalComponent: Boolean = false,
    val hasSomaticComponent: Boolean = false,
    val hasMaterialComponent: Boolean = false,
    val duration: String,
    val isRitual: Boolean = false
)