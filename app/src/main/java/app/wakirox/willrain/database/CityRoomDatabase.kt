package app.wakirox.willrain.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import app.wakirox.willrain.dao.CityDao
import app.wakirox.willrain.model.CityEntity
import dagger.Provides

// Annotates class to be a Room Database with a table (entity) of the Word class

@Database(entities = [CityEntity::class], version = 2, exportSchema = false)
abstract class CityRoomDatabase : RoomDatabase() {

   abstract fun cityDao(): CityDao

   companion object {
       private const val DATABASE_NAME = "worldcities"
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
                    ).fallbackToDestructiveMigration().createFromAsset("db/$DATABASE_NAME.sql").allowMainThreadQueries().build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
   }
}