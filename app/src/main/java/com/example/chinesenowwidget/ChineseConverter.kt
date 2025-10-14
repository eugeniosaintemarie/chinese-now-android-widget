package com.example.chinesenowwidget

import java.util.*

object ChineseConverter {
    
    private val chineseNumbers = arrayOf("零", "一", "二", "三", "四", "五", "六", "七", "八", "九")
    private val pronunciation = arrayOf("líng", "yī", "èr", "sān", "sì", "wǔ", "liù", "qī", "bā", "jiǔ", "shí")
    
    private val daysInChinese = arrayOf("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")
    private val daysPronunciation = arrayOf(
        "xīng qī rì SUNDAY",
        "xīng qī yī MONDAY",
        "xīng qī èr TUESDAY",
        "xīng qī sān WEDNESDAY",
        "xīng qī sì THURSDAY",
        "xīng qī wǔ FRIDAY",
        "xīng qī liù SATURDAY"
    )
    
    private val monthsInChinese = arrayOf("一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二")
    
    private val monthNamesInChinese = arrayOf(
        "一月", "二月", "三月", "四月", "五月", "六月",
        "七月", "八月", "九月", "十月", "十一月", "十二月"
    )
    
    private val monthNamesInEnglish = arrayOf(
        "JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE",
        "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"
    )
    
    private val monthNamesPronunciation = arrayOf(
        "yī yuè", "èr yuè", "sān yuè", "sì yuè", "wǔ yuè", "liù yuè",
        "qī yuè", "bā yuè", "jiǔ yuè", "shí yuè", "shí yī yuè", "shí èr yuè"
    )
    
    private val seasons = mapOf(
        "春季" to "chūn jì SPRING",
        "夏季" to "xià jì SUMMER",
        "秋季" to "qiū jì AUTUMN",
        "冬季" to "dōng jì WINTER"
    )
    
    fun convertToChineseNumber(number: Int): String {
        val numStr = number.toString()
        val result = StringBuilder()
        
        for (i in numStr.indices) {
            if (numStr[i] == '1' && i < numStr.length - 1) {
                result.append("十")
            } else {
                result.append(chineseNumbers[numStr[i].toString().toInt()])
            }
        }
        
        return result.toString()
    }
    
    fun getPronunciationHourMonth(number: Int): String {
        val numStr = number.toString()
        val result = StringBuilder()
        
        for (i in numStr.indices) {
            result.append(pronunciation[numStr[i].toString().toInt()])
            if (i < numStr.length - 1) {
                result.append(" ")
            }
        }
        
        return result.toString()
    }
    
    fun getDayInChinese(dayOfWeek: Int): String {
        return daysInChinese[dayOfWeek]
    }
    
    fun getDayPronunciation(dayOfWeek: Int): String {
        return daysPronunciation[dayOfWeek]
    }
    
    fun getMonthName(month: Int): String {
        return monthNamesInChinese[month - 1]
    }
    
    fun getMonthNameInEnglish(month: Int): String {
        return monthNamesInEnglish[month - 1]
    }
    
    fun getMonthNamePronunciation(month: Int): String {
        return monthNamesPronunciation[month - 1]
    }
    
    fun getSeason(month: Int): String {
        return when (month) {
            12, 1, 2 -> "夏季" // summer
            3, 4, 5 -> "秋季" // autumn
            6, 7, 8 -> "冬季" // winter
            else -> "春季" // spring
        }
    }
    
    fun getSeasonPronunciation(season: String): String {
        return seasons[season] ?: ""
    }
    
    fun getCurrentChineseData(): ChineseData {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Argentina/Buenos_Aires"))
        
        val hours = calendar.get(Calendar.HOUR_OF_DAY)
        val minutes = calendar.get(Calendar.MINUTE)
        val seconds = calendar.get(Calendar.SECOND)
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)
        val season = getSeason(month)
        
        return ChineseData(
            time = "${convertToChineseNumber(hours)}时:${convertToChineseNumber(minutes)}分:${convertToChineseNumber(seconds)}秒",
            timePinyin = "${getPronunciationHourMonth(hours)} shí : ${getPronunciationHourMonth(minutes)} fēn : ${getPronunciationHourMonth(seconds)} miǎo $hours:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}",
            
            day = getDayInChinese(dayOfWeek),
            dayPinyin = getDayPronunciation(dayOfWeek),
            
            month = "${convertToChineseNumber(month)}月",
            monthPinyin = "${getPronunciationHourMonth(month)} yuè $month",
            
            monthName = "",
            monthNamePinyin = "",
            
            date = "${convertToChineseNumber(dayOfMonth)}日",
            datePinyin = "${getPronunciationHourMonth(dayOfMonth)} rì $dayOfMonth",
            
            year = convertToChineseNumber(year).toString(),
            yearPinyin = "${getPronunciationHourMonth(year)} $year",
            
            season = season,
            seasonPinyin = getSeasonPronunciation(season)
        )
    }
}

data class ChineseData(
    val time: String,
    val timePinyin: String,
    val day: String,
    val dayPinyin: String,
    val month: String,
    val monthPinyin: String,
    val monthName: String,
    val monthNamePinyin: String,
    val date: String,
    val datePinyin: String,
    val year: String,
    val yearPinyin: String,
    val season: String,
    val seasonPinyin: String
)
