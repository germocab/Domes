package com.example.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "domes")
data class DomeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "dome_colors",
    foreignKeys = [
        ForeignKey(
            entity = DomeEntity::class,
            parentColumns = ["id"],
            childColumns = ["domeId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("domeId")]
)
data class DomeColorEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val domeId: Long,
    val name: String,
    val hex: String, // 6 hex digits without '#' e.g. "D10F0F"
    val r: Int,
    val g: Int,
    val b: Int,
    val sortOrder: Int = 0
)

data class DomeWithColors(
    @Embedded val dome: DomeEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "domeId"
    )
    val colors: List<DomeColorEntity>
)
