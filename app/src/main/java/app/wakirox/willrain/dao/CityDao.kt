package app.wakirox.willrain.dao

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.wakirox.willrain.model.CityEntity

@Dao
interface CityDao {

    @Query("select * from cities order by city desc")
    fun getAllCities() : List<CityEntity>

    @Query("select city, state, cityAlternative, id as _id from cities order by city desc")
    fun getAllCitiesCursor(): Cursor

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word: CityEntity)

    @Query("""select * from cities where city LIKE '%' || :cityName || '%' order by city desc limit 10""")
    fun getCities(cityName : String) : List<CityEntity>

}