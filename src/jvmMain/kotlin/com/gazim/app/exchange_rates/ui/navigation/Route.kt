package com.gazim.app.exchange_rates.ui.navigation

import com.gazim.library.exchange_rates.model.IExchange

interface Route

object HomeScreenRoute : Route

class ExchangeScreenRoute(val exchange: IExchange) : Route