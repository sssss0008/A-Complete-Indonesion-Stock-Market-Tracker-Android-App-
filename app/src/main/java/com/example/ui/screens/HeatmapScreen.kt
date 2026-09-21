package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.example.model.MarketRegion
import com.example.ui.components.TradingViewWebView
import com.example.widgets.TradingViewHtmlBuilder

@Composable
fun HeatmapScreen(
    region: MarketRegion,
    isDark: Boolean,
    modifier: Modifier = Modifier
) {
    var selectedSource by remember(region) {
        mutableStateOf(region.heatmapSource)
    }

    val heatmapHtml = remember(selectedSource, isDark) {
        TradingViewHtmlBuilder.buildStockHeatmapHtml(
            dataSource = selectedSource,
            isDark = isDark
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("heatmap_screen")
    ) {
        // Source Selector Bar
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilterChip(
                        selected = selectedSource == "AllID",
                        onClick = { selectedSource = "AllID" },
                        label = { Text("🇮🇩 Indonesia (IDX)") },
                        modifier = Modifier.testTag("heatmap_chip_indonesia")
                    )
                    FilterChip(
                        selected = selectedSource == "AllUSA",
                        onClick = { selectedSource = "AllUSA" },
                        label = { Text("🌐 Global / USA") },
                        modifier = Modifier.testTag("heatmap_chip_allusa")
                    )
                    FilterChip(
                        selected = selectedSource == "Brazil",
                        onClick = { selectedSource = "Brazil" },
                        label = { Text("🇧🇷 Brazil") },
                        modifier = Modifier.testTag("heatmap_chip_brazil")
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.PieChart,
                        contentDescription = "Sectors",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Sector Size",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // TradingView Heatmap WebView
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            TradingViewWebView(
                htmlContent = heatmapHtml,
                modifier = Modifier.fillMaxSize(),
                testTag = "stock_heatmap_webview"
            )
        }
    }
}
