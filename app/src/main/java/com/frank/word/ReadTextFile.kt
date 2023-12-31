package com.frank.word

import android.widget.Toast
import com.frank.word.ui.inputText
import com.frank.word.ui.isAdjust
import com.frank.word.ui.isToAddTime
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader

var wordIndexArray: ArrayList<Int> = arrayListOf()
var iListTime: ArrayList<Int> = arrayListOf()
var iFavorite: ArrayList<Int> = arrayListOf()
var iWordClass: ArrayList<Int> = arrayListOf()

var list_foreign: ArrayList<String> = arrayListOf()
var list_pronunciation: ArrayList<String> = arrayListOf()
var list_native: ArrayList<String> = arrayListOf()
var list_sentence1: ArrayList<String> = arrayListOf()
var list_sentence2: ArrayList<String> = arrayListOf()
var listTime: ArrayList<String> = arrayListOf()

var lrcFile: File? = null
var isLRC_Time_OK: Boolean = false
var isLRC_Format_OK: Boolean = false
var iStart: Int = 0
var wordIndex: Int = 0
var loopIndex: Int = 0
var loopNumber: Int = 1
var sortType: Int = 0

fun readTextFile(index: Int) {
    var start = pathAndName.lastIndexOf("/")
    var end = pathAndName.lastIndexOf(".")
    if (start != -1 && end != -1) {
        fileName = pathAndName.substring(start + 1, end)
        val tmp = pathAndName.substring(0, start)
        end = start
        start = tmp.lastIndexOf("/")
        folderName = pathAndName.substring(start + 1, end)
    }
    lrcPath = mainActivity.getExternalFilesDir(null).toString()
    val pathName = "$lrcPath/$folderName/$fileName.txt"
    try {
        isPlay = true
        isLRC_Format_OK = false
        isLRC_Time_OK = false
        iStart = 0
        wordIndex = index
        loopIndex = 0
        isAdjust = false
        isToAddTime = false
        list_foreign.clear()
        list_pronunciation.clear()
        list_native.clear()
        list_sentence1.clear()
        list_sentence2.clear()
        listTime.clear()
        iListTime.clear()
        iFavorite.clear()
        iWordClass.clear()
        wordIndexArray.clear()
        lrcFile = File(pathName)
        if (lrcFile!!.exists()) {
            isLRC_Time_OK = true
            readTxtFileIntoStringArrList(lrcFile!!, false)
            sortWords()
            if (isLRC_Time_OK) {
                if (isLRC_Format_OK) {
                    showWordsNormal()
                } else {
                    val str: String = readRawTxtFile(lrcFile!!)
                    editWords(str)
                }
            } else {
                readTxtFileIntoStringArrList(lrcFile!!, true)
                if (isLRC_Format_OK) {
                    loopNumber = 0
                    addTimeInit()
                } else {
                    val str: String = readRawTxtFile(lrcFile!!)
                    editWords(str)
                }
            }
        } else {
            editWords("")
        }
    } catch (_: java.lang.Exception) {
    }
}

fun readTxtFileIntoStringArrList(file: File?, isMakeLRC: Boolean) {
    var read: InputStreamReader? = null
    var bufferedReader: BufferedReader? = null
    try {
        read = InputStreamReader(FileInputStream(file))
        bufferedReader = BufferedReader(read)
        var lineTxt: String
        var strArray: Array<String>
        isLRC_Time_OK = true
        isLRC_Format_OK = true
        while (bufferedReader.readLine().also { lineTxt = it?:"" } != null && !lineTxt.isEmpty()) {
            if (lineTxt.length < 10 || !isNumeric(lineTxt.substring(0, 10))) {
                isLRC_Time_OK = false
            }
            if (isMakeLRC) {
                iFavorite.add(0)
                iWordClass.add(0)
                iListTime.add(0)
                listTime.add("")
                strArray =
                    lineTxt.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            } else {
                if (lineTxt.length < 10 || !isNumeric(lineTxt.substring(0, 10))) {
                    isLRC_Time_OK = false
                    bufferedReader.close()
                    read.close()
                    return
                }
                val flag = lineTxt.substring(0, 1)
                iFavorite.add(flag.toInt())
                val time = lineTxt.substring(1, 8)
                iListTime.add(time.toInt())
                listTime.add(time)
                val wCls = lineTxt.substring(8, 10)
                iWordClass.add(wCls.toInt())
                strArray = lineTxt.substring(10).split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()
            }
            if (strArray.size == 1) {
                if (strArray[0] == "完了" || strArray[0] == "结束" || strArray[0] == "3") {
                    if (list_foreign.size == list_sentence1.size) {
                        list_sentence1.add(strArray[0])
                        list_sentence2.add(strArray[0])
                    }
                    list_foreign.add(strArray[0])
                    list_pronunciation.add(strArray[0])
                    list_native.add(strArray[0])
                } else {
                    isLRC_Format_OK = false
                    //error = "1"
                }
                continue
            }
            if (strArray.size == 2) {
                list_foreign.add(strArray[0])
                list_pronunciation.add(strArray[0])
                list_native.add(strArray[1])
                list_sentence1.add("")
                list_sentence2.add("")
                continue
            }
            if (strArray.size < 3 || lineTxt.trim { it <= ' ' }.isEmpty()) {
                isLRC_Format_OK = false
                //error = "2"
                continue
            }
            list_foreign.add(strArray[0])
            list_pronunciation.add(strArray[1])
            list_native.add(strArray[2])
            if (strArray.size >= 5) {
                list_sentence1.add(strArray[3])
                list_sentence2.add(strArray[4])
            }
        }
        bufferedReader.close()
        read.close()
        return
    } catch (e: Exception) {
        isLRC_Time_OK = false
        isLRC_Format_OK = false
        //error = e.toString()
        try {
            bufferedReader!!.close()
            read!!.close()
        } catch (_: Exception) {
        }
        e.printStackTrace()
    }
}

fun sortWords() {
    if (wordIndexArray.isEmpty()) {
        for (item: Int in 0 until iListTime.size) {
            wordIndexArray.add(item)
        }
    }
    if (sortType == 1 || sortType == 2) {
        shuffleWords()
    }
}

fun shuffleWords() {
    val length = wordIndexArray.size
    for (i in 0 until length - 1) {
        val iRandNum = (Math.random() * (length - 1)).toInt()
        val temp = wordIndexArray[iRandNum]
        wordIndexArray[iRandNum] = wordIndexArray[i]
        wordIndexArray[i] = temp
    }
}

fun showWordsNormal() {
    showFirstWord()
    menu_word_class?.isVisible = true
}

fun readRawTxtFile(file:File): String {
    val read: InputStreamReader
    val bufferedReader: BufferedReader
    val stringBuffer = StringBuilder()
    try {
        read = InputStreamReader(FileInputStream(file))
        bufferedReader = BufferedReader(read)
        var lineTxt: String
        while (bufferedReader.readLine().also { lineTxt = it } != null) {
            if (isLRC_Time_OK) {
                stringBuffer.append(lineTxt.substring(10))
            } else {
                stringBuffer.append(lineTxt)
            }
            stringBuffer.append("\n")
        }
        bufferedReader.close()
        read.close()
        return stringBuffer.toString()
    } catch (e: java.lang.Exception) {
        e.toString()
    }
    return ""
}

fun editWords(str: String) {
    if (str.isEmpty()) {
        Toast.makeText(mainActivity, "单词文件不存在！", Toast.LENGTH_LONG).show()
    } else {
        Toast.makeText(mainActivity, "单词文件格式错误！", Toast.LENGTH_LONG).show()
    }
    inputText = str
    iStart = 0
}

fun isNumeric(str: String?): Boolean {
    return try {
        str?.toDouble()
        true
    } catch (e: NumberFormatException) {
        false
    }
}
