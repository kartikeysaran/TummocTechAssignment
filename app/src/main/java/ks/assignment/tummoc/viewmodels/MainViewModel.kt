package ks.assignment.tummoc.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ks.assignment.tummoc.db.entity.CategoryEntity
import ks.assignment.tummoc.models.Category
import ks.assignment.tummoc.models.Item
import ks.assignment.tummoc.repository.AppRepository


class MainViewModel(private val repository: AppRepository) : ViewModel() {
    private val _categoriesWithItems = MutableLiveData<List<Category>>()
    val categoriesWithItems: LiveData<List<Category>> get() = _categoriesWithItems

    private val _cartItems = MutableLiveData<List<Item>>()
    val cartItems: LiveData<List<Item>> get() = _cartItems

    private val _favItems = MutableLiveData<List<Item>>()
    val favItems: LiveData<List<Item>> get() = _favItems
    private val _favCount = MutableLiveData<Int>()
    val favCount: LiveData<Int>
        get() = _favCount

    private val _cartCount = MutableLiveData<Int>()
    val cartCount: LiveData<Int>
        get() = _cartCount

    private val _categoryNames = MutableLiveData<List<String>>()
    val categoryNames: LiveData<List<String>>
        get() = _categoryNames

    init {
        viewModelScope.launch {
            repository.insertCategoriesAndItems() // Insert data from assets when the app launches
            fetchCategoriesWithItems() // Fetch categories with items after inserting data
            _cartCount.value = repository.getCartCount()
            _favCount.value = repository.getFavCount()
            _categoryNames.value = repository.getCategoryNames()
        }
    }

    fun fetchCategoriesWithItems() {
        viewModelScope.launch {
            val categoriesWithItems = repository.getCategoriesWithItems()
            _categoriesWithItems.postValue(categoriesWithItems)
        }
    }

    fun updateCount(){
        viewModelScope.launch {
            _cartCount.value = repository.getCartCount()
            _favCount.value = repository.getFavCount()
        }
    }

    suspend fun fetchCartItems() {
        val cartItems = repository.getCartItems()
        _cartItems.postValue(cartItems)
    }

    fun fetchFavItems() {
        viewModelScope.launch {
            val favItems = repository.getFavItems()
            _favItems.postValue(favItems)
        }
    }

    fun toggleFavorite(itemId: Int) {
        viewModelScope.launch {
            repository.toggleFavorite(itemId)
        }
    }

    fun addToCart(itemId: Int) {
        viewModelScope.launch {
            repository.addItemToCart(itemId)
        }
    }

    fun removeFromCart(itemId: Int) {
        viewModelScope.launch {
            repository.removeItemFromCart(itemId)
        }
    }

}
