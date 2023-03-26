package com.gazim.app.exchange_rates.ui.model

enum class AspectRatio(val ratioStr: String, val width: Float, val height: Float) {
    OneToOne("1x1", 1f, 1f), FourToThree("4x3", 4f, 3f);

    fun heightOnWidth() = height / width
    fun widthOnHeight() = width / height
}