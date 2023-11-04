package ks.assignment.tummoc.models

import ks.assignment.tummoc.db.entity.ItemEntity

data class Item(
    val id: Int,
    val name: String,
    val icon: String,
    val price: Double,
    var isFavorite: Boolean = false,
    var quantity: Int = 0
)

fun ItemEntity.toItem(): Item {
    return Item(id, name, icon, price, isFavorite)
}
