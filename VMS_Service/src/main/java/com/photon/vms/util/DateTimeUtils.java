package com.photon.vms.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.photon.vms.constants.CommonConstants;
import com.photon.vms.vo.RevocationEmailVO;
import com.photon.vms.vo.SuccessResponseVO;

@Service
public class DateTimeUtils {

	public String convertStringDateFormat(String stringDate) throws ParseException {
		SimpleDateFormat format1 = new SimpleDateFormat(CommonConstants.YYYY_MM_DD);
		SimpleDateFormat format2 = new SimpleDateFormat("dd-MMM-yyyy");
		Date date = format1.parse(stringDate);
		return format2.format(date);
	}

	public String convertUnfreezeDates(String stringDate) throws ParseException {
		SimpleDateFormat format1 = new SimpleDateFormat(CommonConstants.YYYY_MM_DD);
		SimpleDateFormat format2 = new SimpleDateFormat("MM/dd/yyyy");
		Date date = format1.parse(stringDate);
		return format2.format(date);
	}

	public String convertReverseDateString(String stringDate) throws ParseException {
		SimpleDateFormat format1 = new SimpleDateFormat("dd-MMM-yyyy");
		SimpleDateFormat format2 = new SimpleDateFormat(CommonConstants.YYYY_MM_DD);
		Date date = format1.parse(stringDate);
		return format2.format(date);
	}
	
	public String convertDateForDB(String stringDate) throws ParseException {
		String returnValue = null;
		try {
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
//			format1.setLenient(false);
			SimpleDateFormat format2 = new SimpleDateFormat(CommonConstants.YYYY_MM_DD);
			Date date = format1.parse(stringDate);
			System.out.println(date);
			returnValue = format2.format(date);
		} catch (Exception e) {
			returnValue = "false";
			return returnValue;
		}
		return returnValue;
	}

	public String convertDateFormat(Date date) throws ParseException {
		String returnValue = null;
		try {
			SimpleDateFormat format2 = new SimpleDateFormat(CommonConstants.YYYY_MM_DD);
			returnValue = format2.format(date);
		} catch (Exception e) {
			returnValue = "false";
			return returnValue;
		}
		return returnValue;
	}
	
	public String convertStringFormat(String date) throws ParseException{
		String returnValue = null;
		SimpleDateFormat format1 = new SimpleDateFormat(CommonConstants.YYYY_MM_DD);
		SimpleDateFormat format2 = new SimpleDateFormat("MM/dd/yyyy");
		Date dateToChange = format1.parse(date);
		returnValue = format2.format(dateToChange);
		return returnValue;
	}
	
	public String convertStringFormatRemainder(String date) throws ParseException{
		String returnValue = null;
		SimpleDateFormat format1 = new SimpleDateFormat(CommonConstants.YYYY_MM_DD);
		SimpleDateFormat format2 = new SimpleDateFormat("dd-MMM-yyyy");
		Date dateToChange = format1.parse(date);
		returnValue = format2.format(dateToChange);
		return returnValue;
	}
		
	public boolean checkForPastDate(String fromDate, String toDate) throws ParseException {
		boolean result = false;
		Date date = new SimpleDateFormat("dd-MMM-yyyy").parse(fromDate);
		Date to = new SimpleDateFormat("dd-MMM-yyyy").parse(toDate);
	    boolean from =  new Date().after(date);  
	    boolean toD = new Date().after(to);
	    
	    if(from || toD)
	    	result = true;	         
		return result;
	}
	
}
