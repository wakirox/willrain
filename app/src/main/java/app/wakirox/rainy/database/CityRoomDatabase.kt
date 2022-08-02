package app.wakirox.rainy.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import app.wakirox.rainy.dao.CityDao
import app.wakirox.rainy.model.CityEntity

// Annotates class to be a Room Database with a table (entity) of the Word class

@Database(entities = [CityEntity::class], version = 2, exportSchema = false)
abstract class CityRoomDatabase : RoomDatabase() {

   abstract fun cityDao(): CityDao

   companion object {
       private const val DATABASE_NAME = "cities"
        // Singleton prevents multiple instances of database opening at the
        // same time. 
        @Volatile
        private var INSTANCE: CityRoomDatabase? = null

        fun getDatabase(context: Context): CityRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        CityRoomDatabase::class.java,
                    DATABASE_NAME
                    ).fallbackToDestructiveMigration().createFromAsset("db/$DATABASE_NAME.sqlite").allowMainThreadQueries().build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
   }
}