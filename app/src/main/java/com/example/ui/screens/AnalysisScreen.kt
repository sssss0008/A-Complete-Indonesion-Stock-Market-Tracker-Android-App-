package com.example.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.model.MarketData
import com.example.model.MarketRegion
import com.example.ui.components.TradingViewWebView
import com.example.widgets.TradingViewHtmlBuilder

@Composable
fun AnalysisScreen(
    symbol: String,
    interval: String,
    subTab: Int,
    region: MarketRegion,
    isDark: Boolean,
    onSymbolChange: (String) -> Unit,
    onIntervalChange: (String) -> Unit,
    onSubTabChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var showCustomSearchDialog by remember { mutableStateOf(false) }
    var customSymbolInput by remember { mutableStateOf("") }
    val stocks = remember(region) { MarketData.getStocksForRegion(region) }

    val technicalIntervals = listOf("1m", "5m", "15m", "1h", "1D", "1W", "1M")

    val currentHtml = remember(symbol, interval, subTab, isDark) {
        when (subTab) {
            0 -> TradingViewHtmlBuilder.buildTechnicalAnalysisHtml(
                symbol = symbol,
                interval = interval,
                isDark = isDark
            )
            1 -> TradingViewHtmlBuilder.buildSymbolInfoHtml(
                symbol = symbol,
                isDark = isDark
            )
            2 -> TradingViewHtmlBuilder.buildFinancialsHtml(
                symbol = symbol,
                isDark = isDark
            )
            else -> TradingViewHtmlBuilder.buildCompanyProfileHtml(
                symbol = symbol,
                isDark = isDark
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("analysis_screen")
    ) {
        // Stock Picker Chips Row
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    stocks.forEach { stock ->
                        val isSelected = stock.symbol == symbol
                        FilterChip(
                            selected = isSelected,
                            onClick = { onSymbolChange(stock.symbol) },
                            label = {
                                val shortName = stock.symbol.substringAfter(":")
                                Text(
                                    text = shortName,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            ),
                            modifier = Modifier.testTag("analysis_chip_${stock.symbol}")
                        )
                    }
                }

                IconButton(
                    onClick = { showCustomSearchDialog = true },
                    modifier = Modifier.testTag("analysis_search_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search symbol",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        // Sub-tabs: Technicals, Info, Financials, Profile
        val tabs = listOf(
            Triple("Technicals", Icons.Default.Speed, 0),
            Triple("Quote Info", Icons.Default.Info, 1),
            Triple("Financials", Icons.Default.QueryStats, 2),
            Triple("Profile", Icons.Default.Business, 3)
        )

        ScrollableTabRow(
            selectedTabIndex = subTab,
            edgePadding = 12.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            tabs.forEach { (title, icon, index) ->
                Tab(
                    selected = subTab == index,
                    onClick = { onSubTabChange(index) },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                modifier = Modifier.padding(end = 6.dp)
                            )
                            Text(title)
                        }
                    },
                    modifier = Modifier.testTag("subtab_$index")
                )
            }
        }

        // If in Technicals sub-tab, show interval selector (1m, 5m, 15m, 1h, 1D, 1W, 1M)
        if (subTab == 0) {
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Timeframe:",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    technicalIntervals.forEach { timeInterval ->
                        val isSelected = timeInterval == interval
                        TextButton(
                            onClick = { onIntervalChange(timeInterval) },
                            modifier = Modifier.height(30.dp)
                        ) {
                            Text(
                                text = timeInterval,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // WebView displaying the selected widget
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            TradingViewWebView(
                htmlContent = currentHtml,
                modifier = Modifier.fillMaxSize(),
                testTag = "analysis_widget_webview"
            )
        }
    }

    // Custom Symbol Search Dialog
    if (showCustomSearchDialog) {
        AlertDialog(
            onDismissRequest = { showCustomSearchDialog = false },
            title = {
                Text(
                    text = "Analyze Any Stock / Ticker",
                    style = MaterialTheme.typography.titleMedium
                )
            },
            text = {
                Column {
                    Text(
                        text = "Enter stock symbol (e.g. BMFBOVESPA:PETR4, BMFBOVESPA:ITUB4, NASDAQ:NVDA):",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = customSymbolInput,
                        onValueChange = { customSymbolInput = it.uppercase() },
                        placeholder = { Text("e.g. BMFBOVESPA:VALE3") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("analysis_custom_input")
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val trimmed = customSymbolInput.trim()
                        if (trimmed.isNotEmpty()) {
                            val formatted = if (!trimmed.contains(":")) {
                                "BMFBOVESPA:$trimmed"
                            } else {
                                trimmed
                            }
                            onSymbolChange(formatted)
                        }
                        showCustomSearchDialog = false
                        customSymbolInput = ""
                    }
                ) {
                    Text("Analyze")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCustomSearchDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
