package com.frank.word

import com.frank.word.ui.isToAddTime
import java.text.DecimalFormat

fun addTimeInit() {
    isToAddTime = true
    wordIndexArray.clear()
    sortType = 0
    musicStep = 0.0f
    showWordType = SHOW_ALL
    sortWords()
    titleString = "$fileName(/${iListTime.size - 1})"
    iStart = 0
}

fun addTime() {
    if (wordIndex < iListTime.size) {
        val decimalFormat = DecimalFormat("0000000")
        iStart = mediaPlayer.currentPosition
        showCurrentWord()
        iListTime[wordIndex] = iStart
        listTime[wordIndex] = decimalFormat.format(iStart.toLong())
        if (list_foreign[wordIndex] == "3") {
            iFavorite[wordIndex] = 3
        }
        titleString = "$fileName(${wordIndex + 1}/${iListTime.size})"
        musicStep = (wordIndex + 1).toFloat() / iListTime.size.toFloat()
        wordIndex++
    } else {
        isLRC_Time_OK = true
        saveFile("")
    }
}

fun changeTime(isToLeft: Boolean) {
    if (isToLeft) {
        if (iStart > 100) {
            val decimalFormat = DecimalFormat("0000000")
            iStart -= 100
            iListTime[wordIndexArray[wordIndex]] = iStart
            listTime[wordIndexArray[wordIndex]] = decimalFormat.format(iStart.toLong())
        }
    } else {
        if (iStart < mediaPlayer.duration - 200) {
            val decimalFormat = DecimalFormat("0000000")
            iStart += 100
            iListTime[wordIndexArray[wordIndex]] = iStart
            listTime[wordIndexArray[wordIndex]] = decimalFormat.format(iStart.toLong())
        }
    }
}
