package ks.assignment.tummoc.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import ks.assignment.tummoc.db.entity.CartEntity

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateCartItem(cartItem: CartEntity)

    @Query("SELECT quantity FROM cart_items WHERE itemId = :itemId")
    suspend fun getCartItemQuantity(itemId: Int): Int?

    @Query("SELECT * from cart_items")
    suspend fun getCartItems(): List<CartEntity>

    @Delete
    suspend fun delete(item: CartEntity)

    @Query("DELETE FROM cart_items WHERE itemId = :itemId")
    suspend fun deleteCartItemByItemId(itemId: Int)

    @Query("SELECT COUNT(*) FROM cart_items")
    suspend fun getCartItemCount(): Int

    @Query("UPDATE cart_items SET quantity = :quantity WHERE itemId = :itemId")
    suspend fun updateCartItemQuantity(itemId: Int, quantity: Int)

    @Transaction
    suspend fun addItem(itemId: Int) {
        val existingQuantity = getCartItemQuantity(itemId) ?: 0
        if (existingQuantity > 0) {
            // If item exists in cart, increase quantity by 1
            updateCartItemQuantity(itemId, existingQuantity + 1)
        } else {
            // If item doesn't exist in cart, add a new cart item
            insertOrUpdateCartItem(CartEntity(itemId = itemId, quantity = 1))
        }
    }

    @Transaction
    suspend fun removeItem(itemId: Int) {
        val existingQuantity = getCartItemQuantity(itemId) ?: 0
        if (existingQuantity > 1) {
            // If quantity is greater than 1, decrease it by 1
            updateCartItemQuantity(itemId, existingQuantity - 1)
        } else if (existingQuantity == 1) {
            // If quantity is 1, remove the item from the cart
            deleteCartItemByItemId(itemId)
        }
    }

}