package app.wakirox.rainy.dao

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.wakirox.rainy.model.CityEntity

@Dao
interface CityDao {

    @Query("select * from cities order by city desc")
    fun getAllCities() : List<CityEntity>

    @Query("select city, state, cityAlternative, id as _id, coords from cities order by city desc")
    fun getAllCitiesCursor(): Cursor

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word: CityEntity)

    @Query("""select * from cities where city LIKE '%' || :cityName || '%' order by city desc limit 50""")
    fun getCities(cityName : String) : List<CityEntity>

    @Query("""select * from cities where city = :cityName AND state = :state order by city desc limit 10""")
    fun getCityByState(cityName : String, state : String) : List<CityEntity>

    @Query("""select city, state, cityAlternative, id as _id from cities where city LIKE '%' || :query || '%' OR cityAlternative LIKE '%' || :query || '%' order by population desc limit 10""")
    fun getCitiesCursor(query: String): Cursor

}