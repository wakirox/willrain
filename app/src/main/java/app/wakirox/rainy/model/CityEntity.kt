package app.wakirox.rainy.model

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
    val coords : String?,
    val countryCode : String?
    ){


    companion object {
        val default = CityEntity(0,"Roma","Roma","Roma","Italy",2318895,"Europe/Rome","41.89193,12.51133","IT")
    }

}
