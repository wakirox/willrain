package app.wakirox.willrain

import android.content.Context
import androidx.room.Room
import app.wakirox.willrain.application.WillRainApplication
import app.wakirox.willrain.dao.CityDao
import app.wakirox.willrain.database.CityRoomDatabase
import app.wakirox.willrain.repository.CityRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideRoomDatabase(@ApplicationContext appContext: Context) = CityRoomDatabase.getDatabase(appContext).cityDao()
}