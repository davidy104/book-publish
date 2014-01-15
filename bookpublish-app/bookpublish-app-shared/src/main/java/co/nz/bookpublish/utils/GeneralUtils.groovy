package co.nz.bookpublish.utils

import java.text.DateFormat
import java.text.SimpleDateFormat

import org.apache.commons.codec.binary.Base64

class GeneralUtils {
	static final Integer DEFAULT_ERROR_LENGTH = 255
	static final String DEFAULT_FORMAT = "yyyy-MM-dd hh:mm:ss"
	static final String DEFAULT_TIME_ZONE = "UTC"

	static String pwdEncode(String password){
		return password.bytes.encodeBase64().toString()
	}

	static String pwdDecode(String password){
		return new String(password.decodeBase64(),"UTF-8")
	}

	static Date strToDate(String dateStr) {
		TimeZone tz = TimeZone.getTimeZone(DEFAULT_TIME_ZONE)
		DateFormat dateFormat = new SimpleDateFormat(DEFAULT_FORMAT)
		dateFormat.setTimeZone(tz)
		Date date = dateFormat.parse(dateStr)
		return date
	}

	static Date strToDate(String dateStr,String dateFormatStr) {
		TimeZone tz = TimeZone.getTimeZone(DEFAULT_TIME_ZONE)
		DateFormat dateFormat = new SimpleDateFormat(dateFormatStr)
		dateFormat.setTimeZone(tz)
		Date date = dateFormat.parse(dateStr)
		return date
	}

	static String dateToStr(Date date) {
		TimeZone tz = TimeZone.getTimeZone(DEFAULT_TIME_ZONE)
		DateFormat dateFormat = new SimpleDateFormat(DEFAULT_FORMAT)
		dateFormat.setTimeZone(tz)
		String result = dateFormat.format(date)
		return result
	}

	static String dateToStr(Date date,String dateFormatStr) {
		TimeZone tz = TimeZone.getTimeZone(DEFAULT_TIME_ZONE)
		DateFormat dateFormat = new SimpleDateFormat(dateFormatStr)
		dateFormat.setTimeZone(tz)
		String result = dateFormat.format(date)
		return result
	}

	def static getExceptionInfo = {Throwable error->
		def stackStr
		def errorStr
		def getErrorStack = {Throwable e->
			StringWriter sw = new StringWriter()
			e.printStackTrace(new PrintWriter(sw))
			stackStr = sw.toString()
		}
		getErrorStack(error)
		def stackLength=DEFAULT_ERROR_LENGTH
		if(stackStr.length()<stackLength){
			stackLength = stackStr.length()
		}
		errorStr = stackStr[0..stackLength]
		errorStr
	}

	static String formatLongToTimeStr(Long ms) {
		int ss = 1000
		int mi = ss * 60
		int hh = mi * 60
		int dd = hh * 24

		long day = ms / dd
		long hour = (ms - day * dd) / hh
		long minute = (ms - day * dd - hour * hh) / mi
		long second = (ms - day * dd - hour * hh - minute * mi) / ss
		long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss

		String strDay = day < 10 ? "0" + day : "" + day
		String strHour = hour < 10 ? "0" + hour : "" + hour
		String strMinute = minute < 10 ? "0" + minute : "" + minute;
		String strSecond = second < 10 ? "0" + second : "" + second
		String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : ""
		+ milliSecond
		strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : ""
		+ strMilliSecond
		return strDay + " " + strHour + ":" + strMinute + ":" + strSecond + " "
		+ strMilliSecond
	}
}
