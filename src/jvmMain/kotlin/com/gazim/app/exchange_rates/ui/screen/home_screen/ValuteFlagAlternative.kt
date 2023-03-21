package com.gazim.app.exchange_rates.ui.screen.home_screen

enum class ValuteFlagAlternative(val valute: String, val flagResName: String) {
    Australia("AUD", "au.svg"),
    Azerbaijan("AZN", "azerbaijan.svg"),
    UK("GBP", "uk.svg"),
    Armenia("AMD", "armenia.svg"),
    Belarus("BYN", "belarus.svg"),
    Bulgaria("BGN", "bulgaria.svg"),
    Brazil("BRL", "brazil.svg"),
    Hungary("HUF", "hungary.svg"),
    Vietnam("VND", "vietnam.svg"),
    HongKong("HKD", "hong_kong.svg"),
    Georgia("GEL", "georgia.svg"),
    Denmark("DKK", "denmark.svg"),
    UAE("AED", "uae.svg"),
    US("USD", "us.svg"),
    Europe("EUR", "europe.svg"),
    Egypt("EGP", "egypt.svg"),
    India("INR", "india.svg"),
    Indonesia("IDR", "indonesia.svg"),
    Kazakhstan("KZT", "kazakhstan.svg"),
    Canada("CAD", "canada.svg"),
    Qatar("QAR", "qatar.svg"),
    Kyrgyzstan("KGS", "kyrgyzstan.svg"),
    China("CNY", "china.svg"),
    Moldova("MDL", "moldova.svg"),
    NewZealand("NZD", "new_zealand.svg"),
    Norway("NOK", "norway.svg"),
    Poland("PLN", "poland.svg"),
    Romania("RON", "romania.svg"),
    InternationalMonetaryFund("XDR", "international_monetary_fund.svg")
    ;

    enum class AspectRatio(val ratio: String) {
        OneToOne("1x1"), FourToThree("4x3")
    }

    val flagsResFolder = "flags"
    fun getPath(aspectRatio: AspectRatio) = "$flagsResFolder/${aspectRatio.ratio}/$flagResName"
}


