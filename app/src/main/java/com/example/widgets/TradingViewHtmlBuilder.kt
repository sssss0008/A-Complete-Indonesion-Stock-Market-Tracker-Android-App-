package com.example.widgets

object TradingViewHtmlBuilder {

    private fun getBgColor(isDark: Boolean): String = if (isDark) "#0A0E17" else "#FFFFFF"
    private fun getTheme(isDark: Boolean): String = if (isDark) "dark" else "light"

    fun buildAdvancedChartHtml(
        symbol: String,
        interval: String = "D",
        isDark: Boolean = true
    ): String {
        val theme = getTheme(isDark)
        val bgColor = getBgColor(isDark)
        val gridColor = if (isDark) "rgba(255, 255, 255, 0.06)" else "rgba(46, 46, 46, 0.1)"

        return """
            <!DOCTYPE html>
            <html>
            <head>
              <meta charset="utf-8">
              <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
              <style>
                html, body {
                  margin: 0;
                  padding: 0;
                  width: 100%;
                  height: 100%;
                  overflow: hidden;
                  background-color: $bgColor;
                  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
                }
                .tradingview-widget-container {
                  width: 100% !important;
                  height: 100% !important;
                }
                .tradingview-widget-container__widget {
                  width: 100% !important;
                  height: 100% !important;
                }
              </style>
            </head>
            <body>
              <div class="tradingview-widget-container">
                <div class="tradingview-widget-container__widget"></div>
                <script type="text/javascript" src="https://s3.tradingview.com/external-embedding/embed-widget-advanced-chart.js" async>
                {
                  "autosize": true,
                  "symbol": "$symbol",
                  "interval": "$interval",
                  "timezone": "America/Sao_Paulo",
                  "theme": "$theme",
                  "style": "1",
                  "locale": "en",
                  "enable_publishing": false,
                  "allow_symbol_change": true,
                  "calendar": false,
                  "details": true,
                  "hide_side_toolbar": false,
                  "hide_top_toolbar": false,
                  "hide_legend": false,
                  "hide_volume": false,
                  "hotlist": true,
                  "save_image": true,
                  "backgroundColor": "$bgColor",
                  "gridColor": "$gridColor",
                  "withdateranges": true,
                  "range": "YTD",
                  "support_host": "https://www.tradingview.com"
                }
                </script>
              </div>
            </body>
            </html>
        """.trimIndent()
    }

    fun buildTickerTapeHtml(
        symbols: String,
        isDark: Boolean = true
    ): String {
        val theme = getTheme(isDark)
        val bgColor = getBgColor(isDark)

        return """
            <!DOCTYPE html>
            <html>
            <head>
              <meta charset="utf-8">
              <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
              <style>
                html, body {
                  margin: 0;
                  padding: 0;
                  width: 100%;
                  height: 100%;
                  overflow: hidden;
                  background-color: $bgColor;
                }
                tv-ticker-tape {
                  width: 100% !important;
                  height: 100% !important;
                  display: block;
                }
              </style>
              <script type="module" src="https://widgets.tradingview-widget.com/w/en/tv-ticker-tape.js"></script>
            </head>
            <body>
              <tv-ticker-tape
                symbols="$symbols"
                color-theme="$theme"
                show-hover>
              </tv-ticker-tape>
            </body>
            </html>
        """.trimIndent()
    }

    fun buildStockHeatmapHtml(
        dataSource: String = "AllID",
        isDark: Boolean = true
    ): String {
        val theme = getTheme(isDark)
        val bgColor = getBgColor(isDark)

        return """
            <!DOCTYPE html>
            <html>
            <head>
              <meta charset="utf-8">
              <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
              <style>
                html, body {
                  margin: 0;
                  padding: 0;
                  width: 100%;
                  height: 100%;
                  overflow: hidden;
                  background-color: $bgColor;
                }
                .tradingview-widget-container {
                  width: 100% !important;
                  height: 100% !important;
                }
                .tradingview-widget-container__widget {
                  width: 100% !important;
                  height: 100% !important;
                }
              </style>
            </head>
            <body>
              <div class="tradingview-widget-container">
                <div class="tradingview-widget-container__widget"></div>
                <script type="text/javascript" src="https://s3.tradingview.com/external-embedding/embed-widget-stock-heatmap.js" async>
                {
                  "dataSource": "$dataSource",
                  "blockSize": "market_cap_basic",
                  "blockColor": "change",
                  "grouping": "sector",
                  "locale": "en",
                  "symbolUrl": "",
                  "colorTheme": "$theme",
                  "exchanges": [],
                  "hasTopBar": true,
                  "isDataSetEnabled": true,
                  "isZoomEnabled": true,
                  "hasSymbolTooltip": true,
                  "isMonoSize": false,
                  "width": "100%",
                  "height": "100%"
                }
                </script>
              </div>
            </body>
            </html>
        """.trimIndent()
    }

    fun buildScreenerHtml(
        market: String = "indonesia",
        defaultScreen: String = "most_capitalized",
        isDark: Boolean = true
    ): String {
        val theme = getTheme(isDark)
        val bgColor = getBgColor(isDark)

        return """
            <!DOCTYPE html>
            <html>
            <head>
              <meta charset="utf-8">
              <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
              <style>
                html, body {
                  margin: 0;
                  padding: 0;
                  width: 100%;
                  height: 100%;
                  background-color: $bgColor;
                  overflow-x: hidden;
                }
                .tradingview-widget-container {
                  width: 100% !important;
                  min-height: 100% !important;
                }
                .tradingview-widget-container__widget {
                  width: 100% !important;
                  min-height: 600px !important;
                }
              </style>
            </head>
            <body>
              <div class="tradingview-widget-container">
                <div class="tradingview-widget-container__widget"></div>
                <script type="text/javascript" src="https://s3.tradingview.com/external-embedding/embed-widget-screener.js" async>
                {
                  "market": "$market",
                  "showToolbar": true,
                  "defaultColumn": "overview",
                  "defaultScreen": "$defaultScreen",
                  "isTransparent": false,
                  "locale": "en",
                  "colorTheme": "$theme",
                  "width": "100%",
                  "height": "100%"
                }
                </script>
              </div>
            </body>
            </html>
        """.trimIndent()
    }

    fun buildEconomicCalendarHtml(
        countryFilter: String = "id",
        isDark: Boolean = true
    ): String {
        val theme = getTheme(isDark)
        val bgColor = getBgColor(isDark)

        return """
            <!DOCTYPE html>
            <html>
            <head>
              <meta charset="utf-8">
              <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
              <style>
                html, body {
                  margin: 0;
                  padding: 0;
                  width: 100%;
                  height: 100%;
                  background-color: $bgColor;
                  overflow-y: auto;
                }
                .tradingview-widget-container {
                  width: 100% !important;
                  min-height: 100% !important;
                }
                .tradingview-widget-container__widget {
                  width: 100% !important;
                  min-height: 650px !important;
                }
              </style>
            </head>
            <body>
              <div class="tradingview-widget-container">
                <div class="tradingview-widget-container__widget"></div>
                <script type="text/javascript" src="https://s3.tradingview.com/external-embedding/embed-widget-events.js" async>
                {
                  "colorTheme": "$theme",
                  "isTransparent": false,
                  "locale": "en",
                  "countryFilter": "$countryFilter",
                  "importanceFilter": "-1,0,1",
                  "width": "100%",
                  "height": "100%"
                }
                </script>
              </div>
            </body>
            </html>
        """.trimIndent()
    }

    fun buildEconomicMapHtml(
        region: String = "south-america",
        isDark: Boolean = true
    ): String {
        val theme = getTheme(isDark)
        val bgColor = getBgColor(isDark)

        return """
            <!DOCTYPE html>
            <html>
            <head>
              <meta charset="utf-8">
              <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
              <style>
                html, body {
                  margin: 0;
                  padding: 0;
                  width: 100%;
                  height: 100%;
                  overflow: hidden;
                  background-color: $bgColor;
                }
                tv-economic-map {
                  width: 100% !important;
                  height: 100% !important;
                  display: block;
                }
              </style>
              <script type="module" src="https://widgets.tradingview-widget.com/w/en/tv-economic-map.js"></script>
            </head>
            <body>
              <tv-economic-map
                region="$region"
                color-theme="$theme"
                hide-legend>
              </tv-economic-map>
            </body>
            </html>
        """.trimIndent()
    }

    fun buildSymbolInfoHtml(
        symbol: String,
        isDark: Boolean = true
    ): String {
        val theme = getTheme(isDark)
        val bgColor = getBgColor(isDark)

        return """
            <!DOCTYPE html>
            <html>
            <head>
              <meta charset="utf-8">
              <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
              <style>
                html, body {
                  margin: 0;
                  padding: 0;
                  width: 100%;
                  background-color: $bgColor;
                  overflow: hidden;
                }
                .tradingview-widget-container {
                  width: 100% !important;
                }
              </style>
            </head>
            <body>
              <div class="tradingview-widget-container">
                <div class="tradingview-widget-container__widget"></div>
                <script type="text/javascript" src="https://s3.tradingview.com/external-embedding/embed-widget-symbol-info.js" async>
                {
                  "symbol": "$symbol",
                  "colorTheme": "$theme",
                  "isTransparent": false,
                  "locale": "en",
                  "width": "100%"
                }
                </script>
              </div>
            </body>
            </html>
        """.trimIndent()
    }

    fun buildTechnicalAnalysisHtml(
        symbol: String,
        interval: String = "1D",
        isDark: Boolean = true
    ): String {
        val theme = getTheme(isDark)
        val bgColor = getBgColor(isDark)

        return """
            <!DOCTYPE html>
            <html>
            <head>
              <meta charset="utf-8">
              <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
              <style>
                html, body {
                  margin: 0;
                  padding: 0;
                  width: 100%;
                  height: 100%;
                  background-color: $bgColor;
                  overflow-y: auto;
                  display: flex;
                  justify-content: center;
                }
                tv-technical-analysis {
                  width: 100% !important;
                  max-width: 450px !important;
                  display: block;
                }
              </style>
              <script type="module" src="https://widgets.tradingview-widget.com/w/en/tv-technical-analysis.js"></script>
            </head>
            <body>
              <tv-technical-analysis
                symbol="$symbol"
                interval="$interval"
                ratings-mode="multiple"
                color-theme="$theme">
              </tv-technical-analysis>
            </body>
            </html>
        """.trimIndent()
    }

    fun buildFinancialsHtml(
        symbol: String,
        isDark: Boolean = true
    ): String {
        val theme = getTheme(isDark)
        val bgColor = getBgColor(isDark)

        return """
            <!DOCTYPE html>
            <html>
            <head>
              <meta charset="utf-8">
              <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
              <style>
                html, body {
                  margin: 0;
                  padding: 0;
                  width: 100%;
                  height: 100%;
                  background-color: $bgColor;
                  overflow-y: auto;
                }
                .tradingview-widget-container {
                  width: 100% !important;
                  min-height: 100% !important;
                }
                .tradingview-widget-container__widget {
                  width: 100% !important;
                  min-height: 600px !important;
                }
              </style>
            </head>
            <body>
              <div class="tradingview-widget-container">
                <div class="tradingview-widget-container__widget"></div>
                <script type="text/javascript" src="https://s3.tradingview.com/external-embedding/embed-widget-financials.js" async>
                {
                  "symbol": "$symbol",
                  "colorTheme": "$theme",
                  "displayMode": "compact",
                  "isTransparent": false,
                  "locale": "en",
                  "width": "100%",
                  "height": "100%"
                }
                </script>
              </div>
            </body>
            </html>
        """.trimIndent()
    }

    fun buildCompanyProfileHtml(
        symbol: String,
        isDark: Boolean = true
    ): String {
        val theme = getTheme(isDark)
        val bgColor = getBgColor(isDark)

        return """
            <!DOCTYPE html>
            <html>
            <head>
              <meta charset="utf-8">
              <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
              <style>
                html, body {
                  margin: 0;
                  padding: 12px;
                  background-color: $bgColor;
                  box-sizing: border-box;
                  font-family: -apple-system, BlinkMacSystemFont, sans-serif;
                }
                tv-company-profile {
                  width: 100% !important;
                  display: block;
                }
              </style>
              <script type="module" src="https://widgets.tradingview-widget.com/w/en/tv-company-profile.js"></script>
            </head>
            <body>
              <tv-company-profile
                symbol="$symbol"
                color-theme="$theme">
              </tv-company-profile>
            </body>
            </html>
        """.trimIndent()
    }
}
