package app.wakirox.willrain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
//id INTEGER not null,
//	city TEXT not null,
//	cityAscii TEXT not null,
//	cityAlternative TEXT,
//	state TEXT,
//	population INTEGER,
//	timezone TEXT,
//	coords TEXT
@Entity(tableName = "cities")
data class CityEntity(
    @PrimaryKey val id : Int,
    val city : String,
    val cityAscii : String,
    val cityAlternative : String?,
    val state : String?,
    val population : Int?,
    val timezone : String?,
    val coords : String?
    )
