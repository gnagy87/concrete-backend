package com.concrete.poletime.utils.dateservice;

import com.concrete.poletime.exceptions.DateConversionException;

import java.time.LocalDate;
import java.util.Date;

public interface DateService {
  LocalDate convertTrainingDateToLocalDate(Date trainingFrom) throws DateConversionException;
  LocalDate ticketDateParser(String date) throws DateConversionException;
  Date trainingDateParser(String dateToParse) throws DateConversionException;
}
