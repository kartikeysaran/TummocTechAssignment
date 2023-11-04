package ks.assignment.tummoc.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import javax.annotation.processing.Generated

@Entity("cart_items")
data class CartEntity (
    @PrimaryKey(autoGenerate = true)  val id: Int = 0,
    val itemId: Int,
    val quantity: Int
)