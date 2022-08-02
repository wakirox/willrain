package app.wakirox.rainy.repository

import app.wakirox.rainy.dao.CityDao
import app.wakirox.rainy.model.CityEntity
import javax.inject.Inject

class CityRepository @Inject constructor(private val cityDao: CityDao) {

    fun cities(query : String?) : List<CityEntity> {
        //name,country,subcountry,geonameid
        return query?.let { cityDao.getCities(query) } ?: cityDao.getAllCities()
    }

    fun cityByState(city : String, state : String) = cityDao.getCityByState(city,state)


    fun cursor() = cityDao.getAllCitiesCursor()

    fun cursorFilter(query : String) = cityDao.getCitiesCursor(query)

}