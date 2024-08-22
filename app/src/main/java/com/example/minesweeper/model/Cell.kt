package com.example.minesweeper.model

data class Cell(
    val x: Int,
    val y: Int,
    var isMine: Boolean,
    var isRevealed: Boolean,
    var mineCount: Int,
    var isFlagged: Boolean = false
)