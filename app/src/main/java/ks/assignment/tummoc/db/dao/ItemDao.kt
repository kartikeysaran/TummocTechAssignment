package ks.assignment.tummoc.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ks.assignment.tummoc.db.entity.ItemEntity

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<ItemEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ItemEntity)

    @Query("SELECT * FROM items WHERE categoryId = :categoryId")
    suspend fun getItemsByCategory(categoryId: Int): List<ItemEntity>

    @Query("SELECT * FROM items WHERE isFavorite = 1")
    suspend fun getFavoriteItems(): List<ItemEntity>

    @Query("SELECT * FROM items WHERE id = :itemId")
    suspend fun getItemById(itemId: Int): ItemEntity

    @Query("SELECT * FROM items")
    suspend fun getAllItems(): List<ItemEntity>

    @Query("SELECT COUNT(*) FROM items where isFavorite = 1")
    suspend fun getFavouriteCount(): Int

    @Query("SELECT * FROM items WHERE id IN (:itemIds)")
    suspend fun getItemsByIds(itemIds: List<Int>): List<ItemEntity>

    @Query("UPDATE items SET isFavorite = CASE WHEN isFavorite = 1 THEN 0 ELSE 1 END WHERE id = :itemId")
    suspend fun toggleFavorite(itemId: Int)

}