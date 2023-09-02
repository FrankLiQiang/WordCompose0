package com.frank.word

import android.widget.Toast
import com.frank.word.ui.currentWordClass
import com.frank.word.ui.isAdjust
import com.frank.word.ui.wordClassColor

val wClass = arrayOf(
    "名词",
    "代词",
    "数词",
    "动1",
    "动2",
    "动3",
    "形1",
    "形2",
    "连体词",
    "副词",
    "接续词",
    "叹词",
    "助动词",
    "助词",
    "专有名词",
    "动词",
    ""
)

fun showWordClass() {
    val classId0 = iWordClass[wordIndexArray[wordIndex]]
    wordClassColor = classId0
    val classId = classId0 % 20
    if (classId0 in 43..45) {
        currentWordClass = "自${wClass[classId]}"
    } else if (classId0 in 23..25) {
        currentWordClass = "他${wClass[classId]}"
    } else {
        currentWordClass = wClass[classId]
    }
    menuWordClass[classId]!!.isChecked = true
}

fun doTask() {
    if (!isLRC_Time_OK || !isLRC_Format_OK) {
        iEnd = mediaPlayer.duration
        if (isLRC_Time_OK || !isLRC_Format_OK) {
            return
        }
    }
    if(isFirstTime) {
        mediaPlayer.seekTo(iStart)
        isFirstTime = false
    }
    val currentPosition = mediaPlayer.currentPosition
    if (loopNumber == 0) {
        if (currentPosition >= iEnd) {
            mediaPlayer.seekTo(iStart)
        }
        return
    } else if (loopNumber == 1) {
        if (wordIndexArray[wordIndex] + 1 < iListTime.size) {
            if (currentPosition > iListTime[wordIndexArray[wordIndex] + 1]) {
                showNext()
            }
        } else {
            showNext()
        }
    } else {
        if (currentPosition < iEnd) {
            return
        }
        if (loopIndex < loopNumber - 1) {
            loopIndex++
            mediaPlayer.seekTo(iStart)
            return
        }
        loopIndex = 0
        showNext()
    }
}

fun showClassPrev() {
    val offset = if (isAdjust) 1 else 2
    if (wordIndex > 0) {
        var isFound = false
        for (i in wordIndex - 1 downTo 0) {
            val wClass = iWordClass[wordIndexArray[i]]
            if (CurrentWordClass == 16) {
                if (wClass in 23..25) {
                    wordIndex = i
                    isFound = true
                    break
                }
            } else if (CurrentWordClass == 17) {
                if (wClass >= 43 && wClass <= 45) {
                    wordIndex = i
                    isFound = true
                    break
                }
            } else {
                if (wClass % 20 == CurrentWordClass) {
                    wordIndex = i
                    isFound = true
                    break
                }
            }
        }
        if (!isFound) {
            if (isPlayFolder) {
                showPrevLesson()
            } else {
                for (i in iFavorite.size - offset downTo wordIndex + 1) {
                    val wClass = iWordClass[wordIndexArray[i]]
                    if (CurrentWordClass == 16) {
                        if (wClass >= 23 && wClass <= 25) {
                            wordIndex = i
                            break
                        }
                    } else if (CurrentWordClass == 17) {
                        if (wClass >= 43 && wClass <= 45) {
                            wordIndex = i
                            break
                        }
                    } else {
                        if (wClass % 20 == CurrentWordClass) {
                            wordIndex = i
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
            showClassPrev()
        }
    }
}

fun showClassNext() {
    val offset = if (isAdjust) 1 else 2
    if (wordIndex < iListTime.size - offset) {
        var isFound = false
        for (i in wordIndex + 1..iListTime.size - offset) {
            val wClass = iWordClass[wordIndexArray[i]]
            if (CurrentWordClass == 16) {
                if (wClass in 23..25) {
                    wordIndex = i
                    isFound = true
                    break
                }
            } else if (CurrentWordClass == 17) {
                if (wClass in 43..45) {
                    wordIndex = i
                    isFound = true
                    break
                }
            } else {
                if (wClass % 20 == CurrentWordClass) {
                    wordIndex = i
                    isFound = true
                    break
                }
            }
        }
        if (!isFound) {
            if (isPlayFolder) {
                showNextLesson()
                return
            } else {
                for (i in 0 until wordIndex) {
                    val wClass = iWordClass[wordIndexArray[i]]
                    if (CurrentWordClass == 16) {
                        if (wClass in 23..25) {
                            wordIndex = i
                            break
                        }
                    } else if (CurrentWordClass == 17) {
                        if (wClass in 43..45) {
                            wordIndex = i
                            break
                        }
                    } else {
                        if (wClass % 20 == CurrentWordClass) {
                            wordIndex = i
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
            sortWords()
            showClassNext()
        }
    }
}

fun showClassVt():Boolean {
    CurrentWordClass = 16
    if (iShowRange == SHOW_RANGE_CLASS) {
        showClassNext()
    } else {
        val cls = iWordClass[wordIndexArray[wordIndex]] % 20
        if (cls < 3 || cls > 5) {
            Toast.makeText(mainActivity, "请选择动词", Toast.LENGTH_LONG).show()
            return false
        }
        iWordClass[wordIndexArray[wordIndex]] = 20 + cls
        saveFile("")
        showCurrentWord()
    }
    return true
}

fun showClassVi():Boolean {
    CurrentWordClass = 17
    if (iShowRange == SHOW_RANGE_CLASS) {
        showClassNext()
    } else {
        val cls = iWordClass[wordIndexArray[wordIndex]] % 20
        if (cls < 3 || cls > 5) {
            Toast.makeText(mainActivity, "请选择动词", Toast.LENGTH_LONG).show()
            return false
        }
        iWordClass[wordIndexArray[wordIndex]] = 40 + cls
        saveFile("")
        showCurrentWord()
    }
    return true
}