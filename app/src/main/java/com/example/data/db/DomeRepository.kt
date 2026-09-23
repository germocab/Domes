package com.example.data.db

import com.example.data.model.DomeColorEntity
import com.example.data.model.DomeEntity
import com.example.data.model.DomeWithColors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class DomeRepository(private val domeDao: DomeDao) {

    val allDomesWithColors: Flow<List<DomeWithColors>> = domeDao.getAllDomesWithColors()

    fun getDomeWithColors(domeId: Long): Flow<DomeWithColors?> = domeDao.getDomeWithColors(domeId)

    fun getColorById(colorId: Long): Flow<DomeColorEntity?> = domeDao.getColorById(colorId)

    suspend fun checkAndSeedInitialData() = withContext(Dispatchers.IO) {
        if (domeDao.getDomeCount() == 0) {
            // Seed "Nature Brand"
            val natureId = domeDao.insertDome(DomeEntity(name = "Nature Brand"))
            domeDao.insertColors(
                listOf(
                    DomeColorEntity(domeId = natureId, name = "Emerald Green", hex = "0E8044", r = 14, g = 128, b = 68, sortOrder = 0),
                    DomeColorEntity(domeId = natureId, name = "Pine", hex = "2D4739", r = 45, g = 71, b = 57, sortOrder = 1),
                    DomeColorEntity(domeId = natureId, name = "Obsidian", hex = "121619", r = 18, g = 22, b = 25, sortOrder = 2),
                    DomeColorEntity(domeId = natureId, name = "Antique Gold", hex = "E5C687", r = 229, g = 198, b = 135, sortOrder = 3),
                    DomeColorEntity(domeId = natureId, name = "Sage Khaki", hex = "AEAA79", r = 174, g = 170, b = 121, sortOrder = 4)
                )
            )

            // Seed "Barbie"
            val barbieId = domeDao.insertDome(DomeEntity(name = "Barbie"))
            domeDao.insertColors(
                listOf(
                    DomeColorEntity(domeId = barbieId, name = "Barbie Pink", hex = "FF0055", r = 255, g = 0, b = 85, sortOrder = 0),
                    DomeColorEntity(domeId = barbieId, name = "Wine Berry", hex = "6B0836", r = 107, g = 8, b = 54, sortOrder = 1),
                    DomeColorEntity(domeId = barbieId, name = "Vivid Violet", hex = "B9008C", r = 185, g = 0, b = 140, sortOrder = 2),
                    DomeColorEntity(domeId = barbieId, name = "Bubblegum", hex = "FF7BE5", r = 255, g = 123, b = 229, sortOrder = 3)
                )
            )

            // Seed "Ocean"
            val oceanId = domeDao.insertDome(DomeEntity(name = "Ocean"))
            domeDao.insertColors(
                listOf(
                    DomeColorEntity(domeId = oceanId, name = "Deep Navy", hex = "0C1075", r = 12, g = 16, b = 117, sortOrder = 0),
                    DomeColorEntity(domeId = oceanId, name = "Cerulean Blue", hex = "1D78BA", r = 29, g = 120, b = 186, sortOrder = 1),
                    DomeColorEntity(domeId = oceanId, name = "Deep Teal", hex = "244246", r = 36, g = 66, b = 70, sortOrder = 2)
                )
            )

            // Seed "Gold Rush"
            val goldId = domeDao.insertDome(DomeEntity(name = "Gold Rush"))
            domeDao.insertColors(
                listOf(
                    DomeColorEntity(domeId = goldId, name = "Amber Gold", hex = "D48B1C", r = 212, g = 139, b = 28, sortOrder = 0),
                    DomeColorEntity(domeId = goldId, name = "Olive Green", hex = "7A800F", r = 122, g = 128, b = 15, sortOrder = 1)
                )
            )
        }
    }

    suspend fun createDome(name: String, colors: List<DomeColorEntity>): Long = withContext(Dispatchers.IO) {
        val domeId = domeDao.insertDome(DomeEntity(name = name))
        val mappedColors = colors.mapIndexed { index, color ->
            color.copy(domeId = domeId, sortOrder = index)
        }
        domeDao.insertColors(mappedColors)
        domeId
    }

    suspend fun updateDomeName(domeId: Long, name: String) = withContext(Dispatchers.IO) {
        domeDao.updateDome(DomeEntity(id = domeId, name = name))
    }

    suspend fun addColorToDome(color: DomeColorEntity): Long = withContext(Dispatchers.IO) {
        domeDao.insertColor(color)
    }

    suspend fun updateColor(color: DomeColorEntity) = withContext(Dispatchers.IO) {
        domeDao.updateColor(color)
    }

    suspend fun deleteColor(colorId: Long) = withContext(Dispatchers.IO) {
        domeDao.deleteColorById(colorId)
    }

    suspend fun deleteDome(domeId: Long) = withContext(Dispatchers.IO) {
        domeDao.deleteDomeById(domeId)
    }
}
