package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.ViewStream
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.ShowChart
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.MarketData
import com.example.model.MarketRegion
import com.example.ui.MainNavTab
import com.example.ui.MainViewModel
import com.example.ui.components.TradingViewWebView
import com.example.ui.screens.AnalysisScreen
import com.example.ui.screens.ChartScreen
import com.example.ui.screens.HeatmapScreen
import com.example.ui.screens.MacroScreen
import com.example.ui.screens.ScreenerScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.widgets.TradingViewHtmlBuilder
import java.io.File

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cleanupCorruptedWebViewCache()
        enableEdgeToEdge()
        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            MyApplicationTheme(darkTheme = uiState.isDarkTheme) {
                MainScreen(
                    viewModel = viewModel,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }

    private fun cleanupCorruptedWebViewCache() {
        try {
            val webViewCache = File(cacheDir, "WebView")
            if (webViewCache.exists()) {
                val codeCache = File(webViewCache, "Default/HTTP Cache/Code Cache")
                if (codeCache.exists()) {
                    codeCache.deleteRecursively()
                }
            }
        } catch (_: Throwable) {
            // Silently ignore cache cleanup errors
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showRegionMenu by remember { mutableStateOf(false) }

    val tickerTapeHtml = remember(uiState.selectedRegion, uiState.isDarkTheme) {
        val symbols = MarketData.getTickerSymbolsForRegion(uiState.selectedRegion)
        TradingViewHtmlBuilder.buildTickerTapeHtml(
            symbols = symbols,
            isDark = uiState.isDarkTheme
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            if (!uiState.isChartExpanded) {
                Column {
                    TopAppBar(
                        title = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = uiState.selectedRegion.flag,
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "Indonesia Market Tracker",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = uiState.selectedRegion.title,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        },
                        actions = {
                            // Region Selector
                            Box {
                                IconButton(
                                    onClick = { showRegionMenu = true },
                                    modifier = Modifier.testTag("region_selector_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Public,
                                        contentDescription = "Switch Market Region",
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                DropdownMenu(
                                    expanded = showRegionMenu,
                                    onDismissRequest = { showRegionMenu = false }
                                ) {
                                    MarketRegion.values().forEach { region ->
                                        DropdownMenuItem(
                                            text = {
                                                Text("${region.flag} ${region.title}")
                                            },
                                            onClick = {
                                                viewModel.setRegion(region)
                                                showRegionMenu = false
                                            }
                                        )
                                    }
                                }
                            }

                            // Ticker Tape Toggle
                            IconButton(
                                onClick = { viewModel.toggleTicker() },
                                modifier = Modifier.testTag("ticker_toggle_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ViewStream,
                                    contentDescription = "Toggle Ticker Tape",
                                    tint = if (uiState.isTickerVisible) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            // Theme Toggle
                            IconButton(
                                onClick = { viewModel.toggleTheme() },
                                modifier = Modifier.testTag("theme_toggle_button")
                            ) {
                                Icon(
                                    imageVector = if (uiState.isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                                    contentDescription = "Toggle Light/Dark Theme",
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            titleContentColor = MaterialTheme.colorScheme.onSurface
                        )
                    )

                    // Live Ticker Tape Bar
                    AnimatedVisibility(
                        visible = uiState.isTickerVisible,
                        enter = expandVertically(),
                        exit = shrinkVertically()
                    ) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            tonalElevation = 1.dp
                        ) {
                            TradingViewWebView(
                                htmlContent = tickerTapeHtml,
                                modifier = Modifier.fillMaxSize(),
                                testTag = "ticker_tape_webview"
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {
            if (!uiState.isChartExpanded) {
                NavigationBar(
                    modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 4.dp
                ) {
                    val items = listOf(
                        Triple(MainNavTab.CHART, Icons.Default.ShowChart, Icons.Outlined.ShowChart),
                        Triple(MainNavTab.HEATMAP, Icons.Default.GridView, Icons.Outlined.GridView),
                        Triple(MainNavTab.SCREENER, Icons.Default.FilterList, Icons.Outlined.FilterList),
                        Triple(MainNavTab.ANALYSIS, Icons.Default.Speed, Icons.Outlined.Speed),
                        Triple(MainNavTab.MACRO, Icons.Default.CalendarMonth, Icons.Outlined.CalendarMonth)
                    )

                    items.forEach { (tab, selectedIcon, unselectedIcon) ->
                        val isSelected = uiState.currentTab == tab
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { viewModel.setTab(tab) },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) selectedIcon else unselectedIcon,
                                    contentDescription = tab.title
                                )
                            },
                            label = { Text(tab.title) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState.currentTab) {
                MainNavTab.CHART -> {
                    val isFav = uiState.favorites.contains(uiState.chartSymbol)
                    ChartScreen(
                        symbol = uiState.chartSymbol,
                        interval = uiState.chartInterval,
                        region = uiState.selectedRegion,
                        isDark = uiState.isDarkTheme,
                        isExpanded = uiState.isChartExpanded,
                        isFavorite = isFav,
                        onSymbolChange = { viewModel.setChartSymbol(it) },
                        onIntervalChange = { viewModel.setChartInterval(it) },
                        onToggleFavorite = { viewModel.toggleFavorite(uiState.chartSymbol) },
                        onToggleExpand = { viewModel.toggleChartExpanded() }
                    )
                }
                MainNavTab.HEATMAP -> {
                    HeatmapScreen(
                        region = uiState.selectedRegion,
                        isDark = uiState.isDarkTheme
                    )
                }
                MainNavTab.SCREENER -> {
                    ScreenerScreen(
                        region = uiState.selectedRegion,
                        filter = uiState.screenerFilter,
                        isDark = uiState.isDarkTheme,
                        onFilterChange = { viewModel.setScreenerFilter(it) }
                    )
                }
                MainNavTab.ANALYSIS -> {
                    AnalysisScreen(
                        symbol = uiState.analysisSymbol,
                        interval = uiState.analysisInterval,
                        subTab = uiState.analysisSubTab,
                        region = uiState.selectedRegion,
                        isDark = uiState.isDarkTheme,
                        onSymbolChange = { viewModel.setAnalysisSymbol(it) },
                        onIntervalChange = { viewModel.setAnalysisInterval(it) },
                        onSubTabChange = { viewModel.setAnalysisSubTab(it) }
                    )
                }
                MainNavTab.MACRO -> {
                    MacroScreen(
                        subTab = uiState.macroSubTab,
                        region = uiState.selectedRegion,
                        isDark = uiState.isDarkTheme,
                        onSubTabChange = { viewModel.setMacroSubTab(it) }
                    )
                }
            }
        }
    }
}

// Kept for screenshot/robolectric testing compatibility
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}
