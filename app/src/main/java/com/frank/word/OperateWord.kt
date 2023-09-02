package com.frank.word

import com.frank.word.ui.isDEL
import com.frank.word.ui.isFAVORITE
import com.frank.word.ui.isNORMAL

fun addFavoriteWord() {
    if (iFavorite[wordIndexArray[wordIndex]] == SHOW_FAVORITE) {
        return
    }
    if (iFavorite[wordIndexArray[wordIndex]] == SHOW_DEL) {
        removed_num--
    }
    if (iFavorite[wordIndexArray[wordIndex]] == SHOW_RANGE_NORMAL) {
        normal_num--
    }
    favorite_num++

    chosen_num = normal_num + favorite_num
    iFavorite[wordIndexArray[wordIndex]] = SHOW_FAVORITE
    isFAVORITE = true
    isDEL = false
    isNORMAL = false
    saveFile("")
}

fun setWordNormal() {
    if (iFavorite[wordIndexArray[wordIndex]] == SHOW_RANGE_NORMAL) {
        return
    }
    if (iFavorite[wordIndexArray[wordIndex]] == SHOW_FAVORITE) {
        favorite_num--
    }
    if (iFavorite[wordIndexArray[wordIndex]] == SHOW_DEL) {
        removed_num--
    }
    normal_num++
    chosen_num = normal_num + favorite_num
    iFavorite[wordIndexArray[wordIndex]] = SHOW_RANGE_NORMAL

    isFAVORITE = false
    isDEL = false
    isNORMAL = true
    saveFile("")
}

fun delWord() {
    if (iFavorite[wordIndexArray[wordIndex]] == SHOW_DEL) {
        return
    }
    if (iFavorite[wordIndexArray[wordIndex]] == SHOW_FAVORITE) {
        favorite_num--
    }
    if (iFavorite[wordIndexArray[wordIndex]] == SHOW_RANGE_NORMAL) {
        normal_num--
    }
    removed_num++

    chosen_num = normal_num + favorite_num
    iFavorite[wordIndexArray[wordIndex]] = SHOW_DEL
    saveFile("")
    if (iShowRange == SHOW_RANGE_ALL) {
        isFAVORITE = false
        isDEL = true
        isNORMAL = false
    } else {
        showNext()
    }
}
