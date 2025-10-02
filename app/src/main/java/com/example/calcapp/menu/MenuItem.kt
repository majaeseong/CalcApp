package com.example.calcapp.menu

data class MenuItem(
    val name: String,
    val price: Int,
    var count: Int = 0,
    val isSeparator: Boolean = false // 구분선 여부
)
