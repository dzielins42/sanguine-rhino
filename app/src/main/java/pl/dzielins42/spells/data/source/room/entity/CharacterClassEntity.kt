package pl.dzielins42.spells.data.source.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "character_classes")
data class CharacterClassEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String
)