package ks.assignment.tummoc.repository
import android.content.Context;
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ks.assignment.tummoc.db.AppDatabase
import ks.assignment.tummoc.db.entity.toCategoryEntity
import ks.assignment.tummoc.db.entity.toItemEntity
import ks.assignment.tummoc.models.Category
import ks.assignment.tummoc.models.Item
import ks.assignment.tummoc.models.toCategory
import ks.assignment.tummoc.models.toItem
import org.json.JSONObject

class AppRepository(private val context: Context, private val database: AppDatabase) {

    private val sharedPrefs: SharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    private val IS_DATA_LOADED_KEY = "is_data_loaded"

    suspend fun insertCategoriesAndItems() {
        val isDataLoaded = sharedPrefs.getBoolean(IS_DATA_LOADED_KEY, false)
        if (!isDataLoaded) {
            val categories = loadCategoriesFromAssets(context, "data.json")
            val categoryEntities = categories.map { it.toCategoryEntity() }
            val itemEntities = categories.flatMap { category ->
                category.items.map { item -> item.toItemEntity(category.id) }
            }
            database.categoryDao().insertCategories(categoryEntities)
            database.itemDao().insertItems(itemEntities)
            with(sharedPrefs.edit()) {
                putBoolean(IS_DATA_LOADED_KEY, true)
                apply()
            }
        }
    }

    fun loadCategoriesFromAssets(context: Context, fileName: String): List<Category> {
        val json = context.assets.open(fileName).bufferedReader().use { it.readText() }
        val jsonObject = JSONObject(json)
        val categoriesArray = jsonObject.getJSONArray("categories")
        return Gson().fromJson(categoriesArray.toString(), object : TypeToken<List<Category>>() {}.type)
    }

    suspend fun getCategoriesWithItems(): List<Category> {
        val categoryEntities = database.categoryDao().getCategories()
        val itemEntities = database.itemDao().getAllItems()

        return categoryEntities.map { categoryEntity ->
            val itemsForCategory = itemEntities.filter { it.categoryId == categoryEntity.id }
            categoryEntity.toCategory(itemsForCategory)
        }
    }

    suspend fun getCartItems(): List<Item> {
        val cartItems = database.cartDao().getCartItems()
        val itemIds = cartItems.map { it.itemId }
        val itemEntities = database.itemDao().getItemsByIds(itemIds)
        return itemEntities.map { entity ->
            Item(
                id = entity.id,
                name = entity.name,
                icon = entity.icon,
                price = entity.price,
                isFavorite = entity.isFavorite,
                quantity = cartItems.find { it.itemId == entity.id }?.quantity ?: 0
            )
        }
    }

    suspend fun getFavItems(): List<Item> {
        val favItemsEntity = database.itemDao().getFavoriteItems()
        return favItemsEntity.map { itemEntity->
            itemEntity.toItem()
        }
    }

    suspend fun toggleFavorite(itemId: Int) {
        database.itemDao().toggleFavorite(itemId)
    }

    suspend fun addItemToCart(itemId: Int) {
        database.cartDao().addItem(itemId)
    }

    suspend fun removeItemFromCart(itemId: Int) {
        database.cartDao().removeItem(itemId)
    }

    suspend fun getFavCount(): Int {
        return database.itemDao().getFavouriteCount()
    }

    suspend fun getCartCount(): Int {
        return database.cartDao().getCartItemCount()
    }

    suspend fun getCategoryNames(): List<String> {
        return database.categoryDao().getCategoryNames()
    }

}