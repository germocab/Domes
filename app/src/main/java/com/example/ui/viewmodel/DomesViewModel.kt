package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.db.DomeRepository
import com.example.data.model.DomeColorEntity
import com.example.data.model.DomeWithColors
import com.example.ui.components.BoardViewMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DomesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: DomeRepository

    val domes: StateFlow<List<DomeWithColors>>

    private val _viewMode = MutableStateFlow(BoardViewMode.MAP)
    val viewMode: StateFlow<BoardViewMode> = _viewMode.asStateFlow()

    init {
        val database = AppDatabase.getInstance(application)
        repository = DomeRepository(database.domeDao())

        domes = repository.allDomesWithColors
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

        viewModelScope.launch {
            repository.checkAndSeedInitialData()
        }
    }

    fun setViewMode(mode: BoardViewMode) {
        _viewMode.value = mode
    }

    fun getDomeWithColors(domeId: Long) = repository.getDomeWithColors(domeId)

    fun getColorById(colorId: Long) = repository.getColorById(colorId)

    fun createDome(name: String, colors: List<DomeColorEntity>, onCreated: (Long) -> Unit = {}) {
        viewModelScope.launch {
            val finalName = if (name.trim().isEmpty()) "My first dome" else name.trim()
            val id = repository.createDome(finalName, colors)
            onCreated(id)
        }
    }

    fun updateDomeName(domeId: Long, name: String) {
        viewModelScope.launch {
            if (name.isNotBlank()) {
                repository.updateDomeName(domeId, name.trim())
            }
        }
    }

    fun addColorToDome(domeId: Long, name: String, hex: String, r: Int, g: Int, b: Int) {
        viewModelScope.launch {
            val color = DomeColorEntity(
                domeId = domeId,
                name = name,
                hex = hex.removePrefix("#").uppercase(),
                r = r,
                g = g,
                b = b
            )
            repository.addColorToDome(color)
        }
    }

    fun updateColor(color: DomeColorEntity) {
        viewModelScope.launch {
            repository.updateColor(color)
        }
    }

    fun deleteColor(colorId: Long, onDeleted: () -> Unit = {}) {
        viewModelScope.launch {
            repository.deleteColor(colorId)
            onDeleted()
        }
    }

    fun deleteDome(domeId: Long, onDeleted: () -> Unit = {}) {
        viewModelScope.launch {
            repository.deleteDome(domeId)
            onDeleted()
        }
    }

    // Interactive custom positions for Domes on the "Your Domes" board map (id -> (xDp, yDp))
    private val _domePositions = MutableStateFlow<Map<Long, Pair<Float, Float>>>(emptyMap())
    val domePositions: StateFlow<Map<Long, Pair<Float, Float>>> = _domePositions.asStateFlow()

    fun updateDomePosition(domeId: Long, x: Float, y: Float) {
        _domePositions.value = _domePositions.value + (domeId to Pair(x, y))
    }

    // Interactive custom positions for Colors in Dome Details (colorId -> (xDp, yDp))
    private val _colorPositions = MutableStateFlow<Map<Long, Pair<Float, Float>>>(emptyMap())
    val colorPositions: StateFlow<Map<Long, Pair<Float, Float>>> = _colorPositions.asStateFlow()

    fun updateColorPosition(colorId: Long, x: Float, y: Float) {
        _colorPositions.value = _colorPositions.value + (colorId to Pair(x, y))
    }
}
