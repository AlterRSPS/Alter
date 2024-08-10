package dev.openrune.cache.util

import me.tongfei.progressbar.ProgressBar
import me.tongfei.progressbar.ProgressBarStyle
import org.apache.commons.io.FileUtils
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.nio.file.Path
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.zip.GZIPInputStream

fun LocalDateTime.toEchochUTC() : Long {
    return this.toEpochSecond(ZoneOffset.UTC)
}

fun String.capitalizeFirstLetter(): String {
    return this.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase() else it.toString()
    }
}

fun progress(task : String, amt : Long) : ProgressBar {
    return ProgressBar(
        task,
        amt,
        1,
        System.err,
        ProgressBarStyle.ASCII,
        "",
        1,
        false,
        null,
        ChronoUnit.SECONDS,
        0L,
        Duration.ZERO
    )
}

fun progress(task : String, amt : Int) : ProgressBar {
    return ProgressBar(
        task,
        amt.toLong(),
        1,
        System.err,
        ProgressBarStyle.ASCII,
        "",
        1,
        false,
        null,
        ChronoUnit.SECONDS,
        0L,
        Duration.ZERO
    )
}

fun String.stringToTimestamp(): LocalDateTime {
    val formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss")
    val time = this.replace("T", " ").replaceAfterLast(".", "")
    return LocalDateTime.parse(time.replaceLastLetter(""), formatter)
}

fun String.replaceLastLetter(newLetter: String): String {
    val substring = this.substring(0, this.length - 1)
    return substring + newLetter
}

/*
 * Finds all Files in a Dir
 * @dir Base Dir
 * @typeList List of Extension names no '.'
 */
fun getFiles(dir : File, vararg typeList : String) : List<File> {
    if (dir.listFiles() == null) {
        println("Unable to find dir: $dir")
    }
    return FileUtils.listFiles(dir,typeList,true).toMutableList()

}
fun getFilesNoFilter(dir : File) : List<File> = dir.listFiles()!!.toList()

fun decompressGzipToBytes(source: Path): ByteArray {
    val output = ByteArrayOutputStream()
    GZIPInputStream(
        FileInputStream(source.toFile())
    ).use { gis ->

        // copy GZIPInputStream to ByteArrayOutputStream
        val buffer = ByteArray(1024)
        var len: Int
        while (gis.read(buffer).also { len = it } > 0) {
            output.write(buffer, 0, len)
        }
    }
    return output.toByteArray()
}