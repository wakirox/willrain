package app.wakirox.willrain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "city_table")
data class CityEntity(val city : String, val country : String, val subcountry : String, @PrimaryKey val geoId : Int)
