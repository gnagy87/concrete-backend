package com.concrete.poletime.utils.dateservice;

import com.concrete.poletime.exceptions.DateConversionException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
public class DateServiceImpl implements DateService {

  @Override
  public LocalDate convertTrainingDateToLocalDate(Date trainingFrom) throws DateConversionException {
    try {
//      DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
//      return LocalDate.parse(trainingFrom.toString());
      return new java.sql.Date(trainingFrom.getTime()).toLocalDate();
    } catch (Exception e) {
      throw new DateConversionException("Could not parse given training date!", e);
    }
  }

  @Override
  public LocalDate ticketDateParser(String date) throws DateConversionException {
    try {
      return LocalDate.parse(date);
    } catch (Exception e) {
      throw new DateConversionException("Could not parse given ticket date!", e);
    }
  }

  @Override
  public Date trainingDateParser(String dateToParse) throws DateConversionException {
    try {
      LocalDateTime ldt = LocalDateTime.parse(dateToParse, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
      return java.sql.Timestamp.valueOf(ldt);
//      return new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dateToParse);
    } catch (Exception e) {
      throw new DateConversionException("Could not parse training date!", e);
    }
  }
}