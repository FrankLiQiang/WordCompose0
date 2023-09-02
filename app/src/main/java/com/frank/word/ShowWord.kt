package com.frank.word

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.frank.word.ui.currentSentence1
import com.frank.word.ui.currentSentence2
import com.frank.word.ui.currentShowWord
import com.frank.word.ui.currentWordClass
import com.frank.word.ui.isAdjust
import com.frank.word.ui.isDEL
import com.frank.word.ui.isFAVORITE
import com.frank.word.ui.isNORMAL
import kotlin.math.abs

const val SHOW_ALL = 0
const val SHOW_FOREIGN = 1
const val SHOW_PRONUNCIATION = 2
const val SHOW_NATIVE = 3
const val SHOW_NONE = 4

const val SHOW_RANGE_NORMAL = 0
const val SHOW_FAVORITE = 1
const val SHOW_DEL = 2
const val SHOW_CHOSEN = 3
const val SHOW_RANGE_ALL = 4
const val SHOW_RANGE_CLASS = 5

var showWordType: Int = SHOW_ALL
var iShowRange:Int = SHOW_RANGE_ALL
var removed_num:Int = 0
var normal_num:Int = 0
var favorite_num:Int = 0
var chosen_num:Int = 0
var normalIndex:Int = 0
var delIndex:Int = 0
var favoriteIndex:Int = 0
var chosenIndex:Int = 0
var iEnd:Int = 0
var isNextLesson: Boolean = false
var allIndex by mutableStateOf(0)
var all_num by mutableStateOf(0)
var CurrentWordClass by mutableStateOf(0)

fun showWord() {
    if (showCurrentWord()) {
        showSeekTo()
    }
}

fun showSeekTo() {
    if (wordIndex < wordIndexArray.size && wordIndexArray[wordIndex] < iListTime.size) {
        iStart = iListTime[wordIndexArray[wordIndex]]
        if (wordIndexArray[wordIndex] + 1 < iListTime.size) {
            iEnd = iListTime[wordIndexArray[wordIndex] + 1]
        }
        if (!isFirstTime && abs(iStart - mediaPlayer.currentPosition) > 1000) {
            mediaPlayer.seekTo(iStart)
        }
    }
}

fun showFirstWord() {
    if (sortType == 1 || sortType == 2) {
        sortWords()
    }
    var isFound = false
    val isPrev = wordIndex == -1
    if (iShowRange == SHOW_RANGE_ALL) {
        if (wordIndex == -1) {
            allIndex = -1
            for (i in iListTime.size - 2 downTo 0) {
                if (iFavorite[wordIndexArray[i]] != 3) {
                    allIndex++
                }
            }
            wordIndex = iListTime.size - 2
        } else {
            allIndex = 0
            wordIndex = 0
        }
    } else if (iShowRange == SHOW_RANGE_CLASS) {
        all_num = iListTime.size - 1
        if (wordIndex == -1) {
            for (i in iListTime.size - 2 downTo 0) {
                val iwClass = iWordClass[wordIndexArray[i]]
                if (CurrentWordClass == 16) {
                    if (iwClass in 23..25) {
                        wordIndex = i
                        isFound = true
                        break
                    }
                } else if (CurrentWordClass == 17) {
                    if (iwClass in 43..45) {
                        wordIndex = i
                        isFound = true
                        break
                    }
                } else {
                    if (iwClass % 20 == CurrentWordClass) {
                        wordIndex = i
                        isFound = true
                        break
                    }
                }
            }
        } else {
            for (i in iListTime.indices) {
                val iwClass = iWordClass[wordIndexArray[i]]
                if (CurrentWordClass == 16) {
                    if (iwClass in 23..25) {
                        wordIndex = i
                        isFound = true
                        break
                    }
                } else if (CurrentWordClass == 17) {
                    if (iwClass in 43..45) {
                        wordIndex = i
                        isFound = true
                        break
                    }
                } else {
                    if (iwClass % 20 == CurrentWordClass) {
                        wordIndex = i
                        isFound = true
                        break
                    }
                }
            }
        }
        if (isFound) {
            showWord()
        } else {
            if (isNextLesson) {
                showNextLesson()
            } else {
                showPrevLesson()
            }
        }
        return
    } else if (iShowRange == SHOW_CHOSEN) {
        if (wordIndex == -1) {
            for (i in iListTime.size - 2 downTo 0) {
                if (iFavorite[wordIndexArray[i]] == SHOW_RANGE_NORMAL
                    || iFavorite[wordIndexArray[i]] == SHOW_FAVORITE
                ) {
                    wordIndex = i
                    break
                }
            }
        } else {
            for (i in iListTime.indices) {
                if (iFavorite[wordIndexArray[i]] == SHOW_RANGE_NORMAL
                    || iFavorite[wordIndexArray[i]] == SHOW_FAVORITE
                ) {
                    wordIndex = i
                    break
                }
            }
        }
    } else {
        if (wordIndex == -1) {
            for (i in iListTime.size - 2 downTo 0) {
                if (iFavorite[wordIndexArray[i]] == iShowRange) {
                    wordIndex = i
                    break
                }
            }
        } else {
            for (i in iListTime.indices) {
                if (iFavorite[wordIndexArray[i]] == iShowRange) {
                    wordIndex = i
                    break
                }
            }
        }
    }
    val offset = if (isAdjust) 0 else 1
    removed_num = 0
    normal_num = 0
    favorite_num = 0
    for (i in 0 until iFavorite.size - offset) {
        if (iFavorite[wordIndexArray[i]] == SHOW_RANGE_NORMAL) {
            normal_num++
        } else if (iFavorite[wordIndexArray[i]] == SHOW_FAVORITE) {
            favorite_num++
        } else if (iFavorite[wordIndexArray[i]] == SHOW_DEL) {
            removed_num++
        }
    }
    chosen_num = normal_num + favorite_num
    all_num = chosen_num + removed_num
    if (isPrev) {
        normalIndex = normal_num - 1
        delIndex = removed_num - 1
        favoriteIndex = favorite_num - 1
        chosenIndex = chosen_num - 1
    } else {
        normalIndex = 0
        delIndex = 0
        favoriteIndex = 0
        chosenIndex = 0
    }
    if (isPlayFolder) {
        if (iShowRange == SHOW_CHOSEN && chosen_num == 0) {
            if (isNextLesson) {
                showNextLesson()
            } else {
                showPrevLesson()
            }
            return
        }
        if (iShowRange == SHOW_DEL && removed_num == 0) {
            if (isNextLesson) {
                showNextLesson()
            } else {
                showPrevLesson()
            }
            return
        }
        if (iShowRange == SHOW_FAVORITE && favorite_num == 0) {
            if (isNextLesson) {
                showNextLesson()
            } else {
                showPrevLesson()
            }
            return
        }
    }
    if (wordIndex == -1) {
        wordIndex = 0
    }
    showWord()
}

fun showCurrentWord(): Boolean {
    if (wordIndex >= iListTime.size || wordIndex >= wordIndexArray.size) {
        return false
    }
    var str = ""
    if (showWordType == SHOW_ALL) {
        str = if (list_foreign[wordIndexArray[wordIndex]]
            == list_pronunciation[wordIndexArray[wordIndex]]
        ) {
            """
     ${list_foreign[wordIndexArray[wordIndex]]}
     ${list_native[wordIndexArray[wordIndex]]}
     """.trimIndent()
        } else {
            """
     ${list_foreign[wordIndexArray[wordIndex]]}
     ${list_pronunciation[wordIndexArray[wordIndex]]}
     ${list_native[wordIndexArray[wordIndex]]}
     """.trimIndent()
        }
    } else if (showWordType == SHOW_FOREIGN) {
        str = list_foreign[wordIndexArray[wordIndex]]
    } else if (showWordType == SHOW_PRONUNCIATION) {
        str = list_pronunciation[wordIndexArray[wordIndex]]
    } else if (showWordType == SHOW_NATIVE) {
        str = list_native[wordIndexArray[wordIndex]]
    }
    currentShowWord = str
    currentSentence1 = ""
    currentSentence2 = ""
    if (wordIndexArray[wordIndex] < list_sentence1.size) {
        currentSentence1 = list_sentence1[wordIndexArray[wordIndex]]
        currentSentence2 = list_sentence2[wordIndexArray[wordIndex]]
    }

    isFAVORITE = iFavorite[wordIndexArray[wordIndex]] == SHOW_FAVORITE
    isDEL = iFavorite[wordIndexArray[wordIndex]] == SHOW_DEL
    isNORMAL = !isFAVORITE && !isFAVORITE

    showTitle()
    showWordClass()
    return true
}

fun showTitle() {
    var show_num = 0
    var show_index = wordIndex
    if (iShowRange == SHOW_RANGE_NORMAL) {
        show_index = normalIndex
        show_num = normal_num
    } else if (iShowRange == SHOW_FAVORITE) {
        show_index = favoriteIndex
        show_num = favorite_num
    } else if (iShowRange == SHOW_DEL) {
        show_index = delIndex
        show_num = removed_num
    } else if (iShowRange == SHOW_CHOSEN) {
        show_index = chosenIndex
        show_num = chosen_num
    } else if (iShowRange == SHOW_RANGE_ALL) {
        show_index = allIndex
        show_num = all_num
    } else if (iShowRange == SHOW_RANGE_CLASS) {
        show_index = wordIndexArray[wordIndex]
        show_num = all_num
    }
    titleString = if (isAdjust) {
        "$fileName(${wordIndex + 1}/${listTime.size})"
    } else {
        "$fileName(${show_index + 1}/$show_num)"
    }
    musicStep = (show_index + 1).toFloat() / show_num.toFloat()
}

fun showPrev() {
    if (iShowRange == SHOW_RANGE_CLASS) {
        showClassPrev()
        return
    }
    val offset = if (isAdjust) 1 else 2
    if (wordIndex > 0) {
        if (iShowRange == SHOW_RANGE_ALL) {
            wordIndex--
            allIndex--
            if (!isAdjust && iFavorite[wordIndexArray[wordIndex]] == 3) {
                wordIndex--
            }
        } else if (iShowRange == SHOW_CHOSEN) {
            var isFound = false
            for (i in wordIndex - 1 downTo 0) {
                if (iFavorite[wordIndexArray[i]] == SHOW_RANGE_NORMAL
                    || iFavorite[wordIndexArray[i]] == SHOW_FAVORITE
                ) {
                    wordIndex = i
                    chosenIndex--
                    isFound = true
                    break
                }
            }
            if (!isFound) {
                if (isPlayFolder) {
                    showPrevLesson()
                } else {
                    for (i in iFavorite.size - offset downTo wordIndex + 1) {
                        if (iFavorite[wordIndexArray[i]] == SHOW_RANGE_NORMAL
                            || iFavorite[wordIndexArray[i]] == SHOW_FAVORITE
                        ) {
                            wordIndex = i
                            chosenIndex = chosen_num - 1
                            break
                        }
                    }
                }
            }
        } else {
            var isFound = false
            for (i in wordIndex - 1 downTo 0) {
                if (iFavorite[wordIndexArray[i]] == iShowRange) {
                    wordIndex = i
                    if (iShowRange == SHOW_FAVORITE) {
                        favoriteIndex--
                    } else if (iShowRange == SHOW_DEL) {
                        delIndex--
                    } else if (iShowRange == SHOW_RANGE_NORMAL) {
                        normalIndex--
                    }
                    isFound = true
                    break
                }
            }
            if (!isFound) {
                if (isPlayFolder) {
                    showPrevLesson()
                } else {
                    for (i in iFavorite.size - offset downTo wordIndex + 1) {
                        if (iFavorite[wordIndexArray[i]] == iShowRange) {
                            wordIndex = i
                            if (iShowRange == SHOW_FAVORITE) {
                                favoriteIndex = favorite_num - 1
                            } else if (iShowRange == SHOW_DEL) {
                                delIndex = removed_num - 1
                            } else if (iShowRange == SHOW_RANGE_NORMAL) {
                                normalIndex = normal_num - 1
                            }
                            break
                        }
                    }
                }
            }
        }
        showWord()
    } else {
        if (isPlayFolder) {
            showPrevLesson()
        } else {
            wordIndex = iListTime.size - offset + 1
            allIndex = all_num
            favoriteIndex = favorite_num
            delIndex = removed_num
            normalIndex = normal_num
            showPrev()
        }
    }
}

fun showNext() {
    if (iShowRange == SHOW_RANGE_CLASS) {
        showClassNext()
        return
    }
    val offset = if (isAdjust) 1 else 2
    if (wordIndex < iListTime.size - offset) {
        if (iShowRange == SHOW_RANGE_ALL) {
            wordIndex++
            allIndex++
            if (!isAdjust && iFavorite[wordIndexArray[wordIndex]] == 3) {
                wordIndex++
            }
        } else if (iShowRange == SHOW_CHOSEN) {
            var isFound = false
            for (i in wordIndex + 1..iListTime.size - offset) {
                if (iFavorite[wordIndexArray[i]] == SHOW_RANGE_NORMAL
                    || iFavorite[wordIndexArray[i]] == SHOW_FAVORITE
                ) {
                    wordIndex = i
                    chosenIndex++
                    isFound = true
                    break
                }
            }
            if (!isFound) {
                if (isPlayFolder) {
                    showNextLesson()
                } else {
                    for (i in 0 until wordIndex) {
                        if (iFavorite[wordIndexArray[i]] == SHOW_RANGE_NORMAL
                            || iFavorite[wordIndexArray[i]] == SHOW_FAVORITE
                        ) {
                            wordIndex = i
                            chosenIndex = 0
                            break
                        }
                    }
                }
            }
        } else {
            var isFound = false
            for (i in wordIndex + 1..iListTime.size - offset) {
                if (iFavorite[wordIndexArray[i]] == iShowRange) {
                    wordIndex = i
                    if (iShowRange == SHOW_FAVORITE) {
                        favoriteIndex++
                    } else if (iShowRange == SHOW_DEL) {
                        delIndex++
                    } else if (iShowRange == SHOW_RANGE_NORMAL) {
                        normalIndex++
                    }
                    isFound = true
                    break
                }
            }
            if (!isFound) {
                if (isPlayFolder) {
                    showNextLesson()
                    return
                } else {
                    for (i in 0 until wordIndex) {
                        if (iFavorite[wordIndexArray[i]] == iShowRange) {
                            wordIndex = i
                            if (iShowRange == SHOW_FAVORITE) {
                                favoriteIndex = 0
                            } else if (iShowRange == SHOW_DEL) {
                                delIndex = 0
                            } else if (iShowRange == SHOW_RANGE_NORMAL) {
                                normalIndex = 0
                            }
                            break
                        }
                    }
                }
            }
        }
        showWord()
    } else {
        if (isPlayFolder) {
            showNextLesson()
        } else {
            wordIndex = -1
            allIndex = -1
            favoriteIndex = -1
            delIndex = -1
            normalIndex = -1
            chosenIndex = -1
            sortWords()
            showNext()
        }
    }
}

