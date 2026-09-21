package com.example.model

enum class MarketRegion(
    val title: String,
    val flag: String,
    val defaultBenchmark: String,
    val screenerMarket: String,
    val calendarCountry: String,
    val mapRegion: String,
    val heatmapSource: String
) {
    INDONESIA(
        title = "Indonesia (IDX)",
        flag = "🇮🇩",
        defaultBenchmark = "IDX:COMPOSITE",
        screenerMarket = "indonesia",
        calendarCountry = "id",
        mapRegion = "asia",
        heatmapSource = "AllID"
    ),
    GLOBAL(
        title = "Global & Tech",
        flag = "🌐",
        defaultBenchmark = "NASDAQ:AAPL",
        screenerMarket = "america",
        calendarCountry = "us",
        mapRegion = "north-america",
        heatmapSource = "AllUSA"
    ),
    BRAZIL(
        title = "Brazil (B3)",
        flag = "🇧🇷",
        defaultBenchmark = "BMFBOVESPA:IBOV",
        screenerMarket = "brazil",
        calendarCountry = "br",
        mapRegion = "south-america",
        heatmapSource = "Brazil"
    )
}

data class StockItem(
    val symbol: String,
    val name: String,
    val sector: String,
    val isIndex: Boolean = false,
    val isCurrency: Boolean = false,
    val isCrypto: Boolean = false
)

object MarketData {
    val BRAZIL_STOCKS = listOf(
        StockItem("BMFBOVESPA:IBOV", "Ibovespa Index", "Benchmark Index", isIndex = true),
        StockItem("BMFBOVESPA:PETR4", "Petrobras PN", "Energy & Oil"),
        StockItem("BMFBOVESPA:VALE3", "Vale ON", "Mining & Metals"),
        StockItem("BMFBOVESPA:ITUB4", "Itaú Unibanco PN", "Financials & Banking"),
        StockItem("BMFBOVESPA:BBDC4", "Banco Bradesco PN", "Financials & Banking"),
        StockItem("BMFBOVESPA:ABEV3", "Ambev ON", "Consumer Staples"),
        StockItem("BMFBOVESPA:BBAS3", "Banco do Brasil ON", "Financials & Banking"),
        StockItem("BMFBOVESPA:WEGE3", "WEG ON", "Industrial Goods"),
        StockItem("BMFBOVESPA:B3SA3", "B3 Brasil Bolsa", "Financial Exchange"),
        StockItem("BMFBOVESPA:RENT3", "Localiza Rent a Car", "Consumer Services"),
        StockItem("BMFBOVESPA:SUZB3", "Suzano Papel ON", "Materials & Paper"),
        StockItem("BMFBOVESPA:MGLU3", "Magazine Luiza ON", "Retail & E-commerce"),
        StockItem("BMFBOVESPA:GGBR4", "Gerdau PN", "Steel & Metals"),
        StockItem("BMFBOVESPA:PRIO3", "PetroRio ON", "Oil & Gas E&P"),
        StockItem("BMFBOVESPA:ELET3", "Eletrobras ON", "Utilities & Energy"),
        StockItem("BMFBOVESPA:RADL3", "Raia Drogasil ON", "Healthcare & Retail"),
        StockItem("BMFBOVESPA:JBSS3", "JBS ON", "Consumer Food"),
        StockItem("BMFBOVESPA:IFIX", "IFIX FII Index", "Real Estate Funds", isIndex = true),
        StockItem("FX_IDC:USDBRL", "USD / Brazilian Real", "Foreign Exchange", isCurrency = true),
        StockItem("BITSTAMP:BTCUSD", "Bitcoin / USD", "Cryptocurrency", isCrypto = true)
    )

    val INDONESIA_STOCKS = listOf(
        StockItem("IDX:COMPOSITE", "IHSG Composite", "Benchmark Index", isIndex = true),
        StockItem("IDX:BBCA", "Bank Central Asia", "Banking & Finance"),
        StockItem("IDX:BBRI", "Bank Rakyat Indonesia", "Banking & Finance"),
        StockItem("IDX:BMRI", "Bank Mandiri", "Banking & Finance"),
        StockItem("IDX:TLKM", "Telkom Indonesia", "Telecommunications"),
        StockItem("IDX:ASII", "Astra International", "Automotive & Conglomerate"),
        StockItem("IDX:BBNI", "Bank Negara Indonesia", "Banking & Finance"),
        StockItem("IDX:BRIS", "Bank Syariah Indonesia", "Islamic Banking"),
        StockItem("IDX:UNVR", "Unilever Indonesia", "Consumer Goods"),
        StockItem("IDX:ICBP", "Indofood CBP", "Food & Beverage"),
        StockItem("IDX:INDF", "Indofood Sukses Makmur", "Consumer Staples"),
        StockItem("IDX:AMMN", "Amman Mineral Internasional", "Mining & Copper/Gold"),
        StockItem("IDX:BREN", "Barito Renewables", "Renewable Energy"),
        StockItem("IDX:ADRO", "Adaro Energy", "Coal & Energy"),
        StockItem("IDX:PGAS", "Perusahaan Gas Negara", "Utilities & Gas"),
        StockItem("IDX:GOTO", "GoTo Gojek Tokopedia", "Tech & E-Commerce"),
        StockItem("IDX:ANTM", "Aneka Tambang (Antam)", "Nickel & Metals"),
        StockItem("FX_IDC:USDIDR", "USD / Indonesian Rupiah", "Foreign Exchange", isCurrency = true),
        StockItem("BITSTAMP:BTCUSD", "Bitcoin / USD", "Cryptocurrency", isCrypto = true)
    )

    val GLOBAL_STOCKS = listOf(
        StockItem("NASDAQ:AAPL", "Apple Inc.", "Consumer Tech"),
        StockItem("NASDAQ:NVDA", "NVIDIA Corporation", "Semiconductors & AI"),
        StockItem("NASDAQ:TSLA", "Tesla Inc.", "Automotive & EV"),
        StockItem("NASDAQ:MSFT", "Microsoft Corp.", "Software & Cloud"),
        StockItem("NASDAQ:AMZN", "Amazon.com Inc.", "E-commerce & Cloud"),
        StockItem("NASDAQ:GOOGL", "Alphabet Inc.", "Internet & Search"),
        StockItem("NASDAQ:META", "Meta Platforms", "Social Media"),
        StockItem("NASDAQ:PLTR", "Palantir Technologies", "AI & Enterprise"),
        StockItem("BITSTAMP:BTCUSD", "Bitcoin", "Crypto Asset", isCrypto = true),
        StockItem("BITSTAMP:ETHUSD", "Ethereum", "Crypto Asset", isCrypto = true)
    )

    fun getStocksForRegion(region: MarketRegion): List<StockItem> {
        return when (region) {
            MarketRegion.BRAZIL -> BRAZIL_STOCKS
            MarketRegion.INDONESIA -> INDONESIA_STOCKS
            MarketRegion.GLOBAL -> GLOBAL_STOCKS
        }
    }

    fun getTickerSymbolsForRegion(region: MarketRegion): String {
        return when (region) {
            MarketRegion.INDONESIA -> "IDX:COMPOSITE,IDX:BBCA,IDX:BBRI,IDX:BMRI,IDX:TLKM,IDX:ASII,IDX:BBNI,IDX:AMMN,IDX:BREN,IDX:UNVR,IDX:ICBP,IDX:GOTO,FX_IDC:USDIDR,BITSTAMP:BTCUSD"
            MarketRegion.GLOBAL -> "NASDAQ:AAPL,NASDAQ:NVDA,NASDAQ:TSLA,NASDAQ:META,NASDAQ:MSFT,NASDAQ:AMZN,NASDAQ:GOOGL,NASDAQ:PLTR,BITSTAMP:BTCUSD,BITSTAMP:ETHUSD"
            MarketRegion.BRAZIL -> "BMFBOVESPA:IBOV,BMFBOVESPA:PETR4,BMFBOVESPA:VALE3,BMFBOVESPA:ITUB4,BMFBOVESPA:BBDC4,FX_IDC:USDBRL,BITSTAMP:BTCUSD"
        }
    }
}

enum class ScreenerScreenFilter(val code: String, val label: String) {
    MOST_CAPITALIZED("most_capitalized", "Most capitalized"),
    VOLUME_LEADERS("volume_leaders", "Volume leaders"),
    TOP_GAINERS("top_gainers", "Top gainers"),
    TOP_LOSERS("top_losers", "Top losers"),
    ALL_TIME_HIGH("ath", "All-time high"),
    ALL_TIME_LOW("atl", "All-time low"),
    HIGH_DIVIDEND("high_dividend", "High-dividend"),
    NEW_52_WEEK_HIGH("new_52_week_high", "New 52-week high"),
    NEW_52_WEEK_LOW("new_52_week_low", "New 52-week low"),
    NEW_MONTHLY_HIGH("new_monthly_high", "New monthly high"),
    NEW_MONTHLY_LOW("new_monthly_low", "New monthly low"),
    MOST_VOLATILE("most_volatile", "Most volatile"),
    UNUSUAL_VOLUME("unusual_volume", "Unusual volume"),
    OVERBOUGHT("overbought", "Overbought"),
    OVERSOLD("oversold", "Oversold"),
    OUTPERFORMING_SMA50("outperforming_SMA50", "Outperforming SMA50")
}

enum class ChartInterval(val code: String, val label: String) {
    ONE_MINUTE("1", "1m"),
    FIVE_MINUTES("5", "5m"),
    FIFTEEN_MINUTES("15", "15m"),
    ONE_HOUR("60", "1h"),
    ONE_DAY("D", "1D"),
    ONE_WEEK("W", "1W"),
    ONE_MONTH("M", "1M")
}
