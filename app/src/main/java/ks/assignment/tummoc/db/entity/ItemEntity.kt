package ks.assignment.tummoc.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ks.assignment.tummoc.models.Item

@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey val id: Int,
    val categoryId: Int,
    val name: String,
    val icon: String,
    val price: Double,
    var isFavorite: Boolean = false,
    var quantity: Int = 0
)

fun Item.toItemEntity(categoryId: Int): ItemEntity {
    return ItemEntity(id, categoryId, name, icon, price)
}