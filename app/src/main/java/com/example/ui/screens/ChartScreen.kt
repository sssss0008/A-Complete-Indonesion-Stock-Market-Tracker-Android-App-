package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import com.example.model.ChartInterval
import com.example.model.MarketData
import com.example.model.MarketRegion
import com.example.model.StockItem
import com.example.ui.components.TradingViewWebView
import com.example.widgets.TradingViewHtmlBuilder

@Composable
fun ChartScreen(
    symbol: String,
    interval: ChartInterval,
    region: MarketRegion,
    isDark: Boolean,
    isExpanded: Boolean,
    isFavorite: Boolean,
    onSymbolChange: (String) -> Unit,
    onIntervalChange: (ChartInterval) -> Unit,
    onToggleFavorite: () -> Unit,
    onToggleExpand: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showCustomSearchDialog by remember { mutableStateOf(false) }
    var customSymbolInput by remember { mutableStateOf("") }
    val stocks = remember(region) { MarketData.getStocksForRegion(region) }

    val chartHtml = remember(symbol, interval, isDark) {
        TradingViewHtmlBuilder.buildAdvancedChartHtml(
            symbol = symbol,
            interval = interval.code,
            isDark = isDark
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("chart_screen")
    ) {
        if (!isExpanded) {
            // Top Stock Selector Chips Row
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
                                    val displayName = stock.symbol.substringAfter(":")
                                    Text(
                                        text = displayName,
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                ),
                                modifier = Modifier.testTag("chip_${stock.symbol}")
                            )
                        }
                    }

                    // Search Custom Symbol Button
                    IconButton(
                        onClick = { showCustomSearchDialog = true },
                        modifier = Modifier.testTag("custom_symbol_search_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search custom symbol",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // Interval & Controls Row
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Timeframe Chips
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ChartInterval.values().forEach { chartInterval ->
                            val isSelected = chartInterval == interval
                            TextButton(
                                onClick = { onIntervalChange(chartInterval) },
                                modifier = Modifier
                                    .height(32.dp)
                                    .testTag("interval_${chartInterval.label}")
                            ) {
                                Text(
                                    text = chartInterval.label,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    // Action Icons: Favorite & Expand
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = onToggleFavorite,
                            modifier = Modifier
                                .size(36.dp)
                                .testTag("favorite_toggle_button")
                        ) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Default.Star else Icons.Outlined.StarBorder,
                                contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                                tint = if (isFavorite) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        IconButton(
                            onClick = onToggleExpand,
                            modifier = Modifier
                                .size(36.dp)
                                .testTag("expand_chart_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Fullscreen,
                                contentDescription = "Full screen chart",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        } else {
            // Floating exit fullscreen overlay button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Full Chart: $symbol (${interval.label})",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(
                        onClick = onToggleExpand,
                        modifier = Modifier.testTag("exit_fullscreen_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.FullscreenExit,
                            contentDescription = "Exit fullscreen",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        // TradingView Interactive Chart WebView
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            TradingViewWebView(
                htmlContent = chartHtml,
                modifier = Modifier.fillMaxSize(),
                testTag = "advanced_chart_webview"
            )
        }
    }

    // Custom Symbol Search Dialog
    if (showCustomSearchDialog) {
        AlertDialog(
            onDismissRequest = { showCustomSearchDialog = false },
            title = {
                Text(
                    text = "Enter Stock / Asset Symbol",
                    style = MaterialTheme.typography.titleMedium
                )
            },
            text = {
                Column {
                    Text(
                        text = "Search or type any exchange ticker (e.g. BMFBOVESPA:PETR4, BMFBOVESPA:VALE3, BITSTAMP:BTCUSD, NASDAQ:AAPL):",
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
                            .testTag("custom_symbol_input")
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
                    },
                    modifier = Modifier.testTag("confirm_custom_symbol_button")
                ) {
                    Text("Load Chart")
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
