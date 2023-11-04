package ks.assignment.tummoc.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ks.assignment.tummoc.models.Category

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey val id: Int,
    val name: String,
)

fun Category.toCategoryEntity(): CategoryEntity {
    return CategoryEntity(id, name)
}