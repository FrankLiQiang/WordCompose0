package com.frank.word

import android.widget.Toast
import com.frank.word.ui.inputText
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter
import java.text.DecimalFormat

fun saveFile(msg: String) {
    var stringBuffer: StringBuilder? = null
    val decimalFormat = DecimalFormat("00")
    if (isLRC_Time_OK) {
        if (msg.isEmpty()) {
            stringBuffer = StringBuilder()
            for (i in iListTime.indices) {
                stringBuffer.append(iFavorite[i])
                stringBuffer.append(listTime[i])
                //int a = newClass(iWordClass.get(i));
                stringBuffer.append(decimalFormat.format(iWordClass[i]))
                stringBuffer.append(list_foreign[i])
                stringBuffer.append(" ")
                stringBuffer.append(list_pronunciation[i])
                stringBuffer.append(" ")
                stringBuffer.append(list_native[i])
                if (i < list_sentence1.size) {
                    stringBuffer.append(" ")
                    stringBuffer.append(list_sentence1[i])
                    stringBuffer.append(" ")
                    stringBuffer.append(list_sentence2[i])
                }
                stringBuffer.append("\n")
            }
        } else {
            val str: String = inputText
            val strArray = str.split("\n".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            if (strArray.size == iListTime.size) {
                stringBuffer = StringBuilder()
                for (i in iListTime.indices) {
                    stringBuffer.append(iFavorite[i])
                    stringBuffer.append(decimalFormat.format(iWordClass[i]))
                    stringBuffer.append(listTime[i])
                    stringBuffer.append(strArray[i])
                    stringBuffer.append("\n")
                }
            }
        }
    }

    val pathName = "$lrcPath/$folderName/$fileName.txt"
    var out: FileOutputStream? = null
    var writer: BufferedWriter? = null
    try {
        val fileImage = File(pathName)
        val parentFile = fileImage.parentFile
        if (parentFile == null) {
            Toast.makeText(mainActivity, "保存失败", Toast.LENGTH_LONG).show()
            return
        }
        if (!parentFile.exists()) {
            val dirFile = parentFile.mkdirs()
            if (!dirFile) {
                Toast.makeText(mainActivity, "创建目录失败！", Toast.LENGTH_LONG).show()
                return
            }
        }
        if (!fileImage.exists()) {
            fileImage.createNewFile()
        }
        out = FileOutputStream(fileImage)
        writer = BufferedWriter(OutputStreamWriter(out))
        if (stringBuffer == null) {
            val str: String = inputText
            writer.write(str)
        } else {
            writer.write(stringBuffer.toString())
        }
        Toast.makeText(mainActivity, msg + "保存成功", Toast.LENGTH_LONG).show()
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        if (writer != null) {
            try {
                writer.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        if (out != null) {
            try {
                out.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}
