package com.example.ui

import androidx.lifecycle.ViewModel
import com.example.model.ChartInterval
import com.example.model.MarketData
import com.example.model.MarketRegion
import com.example.model.ScreenerScreenFilter
import com.example.model.StockItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class MainNavTab(val title: String, val iconLabel: String) {
    CHART("Chart", "chart"),
    HEATMAP("Heatmap", "heatmap"),
    SCREENER("Screener", "screener"),
    ANALYSIS("Analysis", "analysis"),
    MACRO("Macro", "macro")
}

data class MainUiState(
    val currentTab: MainNavTab = MainNavTab.CHART,
    val selectedRegion: MarketRegion = MarketRegion.INDONESIA,
    val chartSymbol: String = "IDX:COMPOSITE",
    val chartInterval: ChartInterval = ChartInterval.ONE_DAY,
    val analysisSymbol: String = "IDX:BBCA",
    val analysisInterval: String = "1D",
    val analysisSubTab: Int = 0, // 0: Technicals, 1: Info, 2: Financials, 3: Profile
    val screenerFilter: ScreenerScreenFilter = ScreenerScreenFilter.MOST_CAPITALIZED,
    val macroSubTab: Int = 0, // 0: Calendar, 1: Economic Map, 2: Brazil Indicators
    val isDarkTheme: Boolean = true,
    val isTickerVisible: Boolean = true,
    val isChartExpanded: Boolean = false,
    val favorites: Set<String> = setOf(
        "IDX:COMPOSITE",
        "IDX:BBCA",
        "IDX:BBRI",
        "IDX:BMRI",
        "FX_IDC:USDIDR",
        "BITSTAMP:BTCUSD"
    )
)

class MainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    fun setTab(tab: MainNavTab) {
        _uiState.update { it.copy(currentTab = tab) }
    }

    fun setRegion(region: MarketRegion) {
        _uiState.update { state ->
            val newDefaultSymbol = when (region) {
                MarketRegion.BRAZIL -> "BMFBOVESPA:IBOV"
                MarketRegion.INDONESIA -> "IDX:COMPOSITE"
                MarketRegion.GLOBAL -> "NASDAQ:AAPL"
            }
            val newAnalysisSymbol = when (region) {
                MarketRegion.BRAZIL -> "BMFBOVESPA:PETR4"
                MarketRegion.INDONESIA -> "IDX:BBCA"
                MarketRegion.GLOBAL -> "NASDAQ:NVDA"
            }
            state.copy(
                selectedRegion = region,
                chartSymbol = newDefaultSymbol,
                analysisSymbol = newAnalysisSymbol
            )
        }
    }

    fun setChartSymbol(symbol: String) {
        _uiState.update { it.copy(chartSymbol = symbol) }
    }

    fun setChartInterval(interval: ChartInterval) {
        _uiState.update { it.copy(chartInterval = interval) }
    }

    fun setAnalysisSymbol(symbol: String) {
        _uiState.update { it.copy(analysisSymbol = symbol) }
    }

    fun setAnalysisInterval(interval: String) {
        _uiState.update { it.copy(analysisInterval = interval) }
    }

    fun setAnalysisSubTab(index: Int) {
        _uiState.update { it.copy(analysisSubTab = index) }
    }

    fun setScreenerFilter(filter: ScreenerScreenFilter) {
        _uiState.update { it.copy(screenerFilter = filter) }
    }

    fun setMacroSubTab(index: Int) {
        _uiState.update { it.copy(macroSubTab = index) }
    }

    fun toggleTheme() {
        _uiState.update { it.copy(isDarkTheme = !it.isDarkTheme) }
    }

    fun toggleTicker() {
        _uiState.update { it.copy(isTickerVisible = !it.isTickerVisible) }
    }

    fun toggleChartExpanded() {
        _uiState.update { it.copy(isChartExpanded = !it.isChartExpanded) }
    }

    fun toggleFavorite(symbol: String) {
        _uiState.update { state ->
            val updated = if (state.favorites.contains(symbol)) {
                state.favorites - symbol
            } else {
                state.favorites + symbol
            }
            state.copy(favorites = updated)
        }
    }

    fun selectStockForChartAndAnalysis(stock: StockItem) {
        _uiState.update {
            it.copy(
                chartSymbol = stock.symbol,
                analysisSymbol = stock.symbol
            )
        }
    }
}
