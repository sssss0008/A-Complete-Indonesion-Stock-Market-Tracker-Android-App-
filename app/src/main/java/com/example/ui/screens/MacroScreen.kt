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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.MarketRegion
import com.example.ui.components.TradingViewWebView
import com.example.widgets.TradingViewHtmlBuilder

data class MacroIndicator(
    val title: String,
    val value: String,
    val change: String,
    val description: String,
    val isPositive: Boolean
)

@Composable
fun MacroScreen(
    subTab: Int,
    region: MarketRegion,
    isDark: Boolean,
    onSubTabChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val countryFilter = region.calendarCountry
    val mapRegion = region.mapRegion

    val calendarHtml = remember(countryFilter, isDark) {
        TradingViewHtmlBuilder.buildEconomicCalendarHtml(
            countryFilter = countryFilter,
            isDark = isDark
        )
    }

    val mapHtml = remember(mapRegion, isDark) {
        TradingViewHtmlBuilder.buildEconomicMapHtml(
            region = mapRegion,
            isDark = isDark
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("macro_screen")
    ) {
        // Sub Tabs Row: Economic Calendar, Economic Map, Key Rates
        val tabs = listOf(
            Triple("Economic Calendar", Icons.Default.CalendarMonth, 0),
            Triple("Economic Map", Icons.Default.Map, 1),
            Triple("Macro Indicators", Icons.Default.TrendingUp, 2)
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
                    modifier = Modifier.testTag("macro_subtab_$index")
                )
            }
        }

        when (subTab) {
            0 -> {
                // Economic Calendar
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    TradingViewWebView(
                        htmlContent = calendarHtml,
                        modifier = Modifier.fillMaxSize(),
                        testTag = "economic_calendar_webview"
                    )
                }
            }
            1 -> {
                // Economic Map
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    TradingViewWebView(
                        htmlContent = mapHtml,
                        modifier = Modifier.fillMaxSize(),
                        testTag = "economic_map_webview"
                    )
                }
            }
            else -> {
                // Macro Indicators & Rate Overview
                MacroIndicatorsList(region = region)
            }
        }
    }
}

@Composable
fun MacroIndicatorsList(region: MarketRegion) {
    val indicators = if (region == MarketRegion.BRAZIL) {
        listOf(
            MacroIndicator("Taxa Selic (Banco Central do Brasil)", "10.50% a.a.", "Copom Benchmark", "Brazil's basic interest rate set by the Monetary Policy Committee (Copom).", true),
            MacroIndicator("IPCA (Inflação Oficial)", "4.23% (12M)", "Meta: 3.00% ± 1.5%", "National Consumer Price Index measured by IBGE, central to monetary policy targets.", false),
            MacroIndicator("Taxa CDI", "10.40% a.a.", "Interbank Deposit", "Certificate of Interbank Deposit rate used as benchmark for fixed income & CDBs.", true),
            MacroIndicator("PIB Brasil (Crescimento Anual)", "+2.9% a.a.", "Expansão Econômica", "Gross Domestic Product performance of Brazil's agriculture, services & industry.", true),
            MacroIndicator("Câmbio USD / BRL", "R$ 5.42", "Cotação Comercial", "US Dollar to Brazilian Real exchange rate, highly sensitive to commodity exports.", false),
            MacroIndicator("Dívida Bruta do Governo Geral", "77.8% PIB", "Fiscal Balance", "Federal government public debt ratio tracked by market analysts & rating agencies.", false)
        )
    } else {
        listOf(
            MacroIndicator("Central Bank Rate", "6.00% p.a.", "Policy Benchmark", "Official monetary policy interest rate of the central bank.", true),
            MacroIndicator("CPI Inflation Rate", "2.8% (12M)", "Target Range", "Headline consumer price inflation index over the last 12 months.", true),
            MacroIndicator("GDP Growth Rate", "+5.05% yoy", "Economic Output", "Year-over-year gross domestic product growth rate.", true),
            MacroIndicator("Foreign Exchange Reserve", "\$140B+", "External Buffer", "Foreign currency reserve holdings providing macroeconomic stability.", true)
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Macroeconomic overview for ${region.title}. These economic factors drive equity valuations, interest curves, and corporate earnings.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        items(indicators) { item ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = item.value,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = item.change,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = item.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
