package ks.assignment.tummoc.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ks.assignment.tummoc.db.dao.CartDao
import ks.assignment.tummoc.db.dao.CategoryDao
import ks.assignment.tummoc.db.dao.ItemDao
import ks.assignment.tummoc.db.entity.CartEntity
import ks.assignment.tummoc.db.entity.CategoryEntity
import ks.assignment.tummoc.db.entity.ItemEntity

@Database(
    entities = [CategoryEntity::class, ItemEntity::class, CartEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
    abstract fun itemDao(): ItemDao
    abstract fun categoryDao(): CategoryDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        private val DATABASE_NAME = "SHOP_DATABASE"


        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {

                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        DATABASE_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}