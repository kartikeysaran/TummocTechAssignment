package ks.assignment.tummoc.models

import ks.assignment.tummoc.db.entity.CategoryEntity
import ks.assignment.tummoc.db.entity.ItemEntity

data class Category (
    val id: Int,
    val name: String,
    val items: List<Item>
)

fun CategoryEntity.toCategory(items: List<ItemEntity>): Category {
    return Category(id, name, items.map { it.toItem() })
}