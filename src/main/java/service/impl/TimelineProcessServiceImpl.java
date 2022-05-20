package service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import exception.DataProcessingException;
import model.Timeline;
import service.DataValidateService;
import service.TimelineProcessService;

public class TimelineProcessServiceImpl implements TimelineProcessService {
    private static final String SIGN_TO_SPLIT_LINE = " ";
    private static final String SIGN_TO_SPLIT_ELEMENT = "\\.";
    private static final int SERVICE_ID_INDEX = 0;
    private static final int SERVICE_VARIATION_INDEX = 1;
    private static final int QUESTION_TYPE_INDEX = 0;
    private static final int QUESTION_CATEGORY_INDEX = 1;
    private static final int QUESTION_SUBCATEGORY_INDEX = 2;
    private static final int DEFAULT_VALUE = 0;
    private final DataValidateService dataValidateService = new DataValidateServiceImpl();

    @Override
    public Timeline processTimeline(String line) {
        String[] dataArray = line.substring(2).split(SIGN_TO_SPLIT_LINE);
        Timeline timeline = new Timeline();
        for (int i = 0; i < dataArray.length; i++) {
            switch (i) {
                case 0:
                    processServiceInformation(dataArray[i], timeline);
                    continue;
                case 1:
                    processQuestionInformation(dataArray[i], timeline);
                    continue;
                case 2:
                    processResponseType(dataArray[i], timeline);
                    continue;
                case 3:
                    processDate(dataArray[i], timeline);
                    continue;
                case 4:
                    processTime(dataArray[i], timeline);
                    continue;
            }
        }
        return timeline;
    }

    private Timeline processServiceInformation(String serviceInformation, Timeline timeline) {
        if (serviceInformation.isEmpty()) {
            throw new DataProcessingException("Service information is empty");
        }
        String[] serviceArray = serviceInformation.split(SIGN_TO_SPLIT_ELEMENT);
        if (serviceArray.length == 1) {
            if (dataValidateService.isValidServiceInformation(serviceArray[SERVICE_ID_INDEX])) {
                timeline.setServiceId(Integer.parseInt(serviceArray[SERVICE_ID_INDEX]));
                timeline.setServiceVariationId(DEFAULT_VALUE);
            } else {
                throw new DataProcessingException("Service information should contain service_id (value from 1 to 10)");
            }
        } else if (serviceArray.length == 2) {
            if (dataValidateService.isValidServiceInformation(serviceArray[SERVICE_ID_INDEX],
                    serviceArray[SERVICE_VARIATION_INDEX])) {
                timeline.setServiceId(Integer.parseInt(serviceArray[SERVICE_ID_INDEX]));
                timeline.setServiceVariationId(Integer.parseInt(serviceArray[SERVICE_VARIATION_INDEX]));
            } else {
                throw new DataProcessingException("Service information should contain service_id (value from 1 to 10) "
                        + "and service_variation_id(value from 1 to 3)");
            }
        } else {
                throw new DataProcessingException("Service information has invalid format");
        }
        return timeline;
    }

    private Timeline processQuestionInformation(String questionInformation, Timeline timeline) {
        if (questionInformation.isEmpty()) {
            throw new DataProcessingException("Question information is empty");
        }
        String[] questionArray = questionInformation.split(SIGN_TO_SPLIT_ELEMENT);
        if (questionArray.length == 1) {
            if (dataValidateService.isValidQuestionInformation(questionArray[QUESTION_TYPE_INDEX])) {
                timeline.setQuestionTypeId(Integer.parseInt(questionArray[QUESTION_TYPE_INDEX]));
                timeline.setCategoryId(DEFAULT_VALUE);
                timeline.setSubCategoryId(DEFAULT_VALUE);
            } else {
                throw new DataProcessingException("Question information may contain "
                        + "question_type_id (value from 1 to 10)");
            }
        } else if (questionArray.length == 2) {
            if (dataValidateService.isValidQuestionInformation(questionArray[QUESTION_TYPE_INDEX],
                    questionArray[QUESTION_CATEGORY_INDEX])) {
                timeline.setQuestionTypeId(Integer.parseInt(questionArray[QUESTION_TYPE_INDEX]));
                timeline.setCategoryId(Integer.parseInt(questionArray[QUESTION_CATEGORY_INDEX]));
                timeline.setSubCategoryId(DEFAULT_VALUE);
            } else {
                throw new DataProcessingException("Question information may contain question_type_id "
                        + "(value from 1 to 10), category_id (value from 1 to 20)");
            }
        } else if (questionArray.length == 3) {
            if (dataValidateService.isValidQuestionInformation(questionArray[QUESTION_TYPE_INDEX],
                    questionArray[QUESTION_CATEGORY_INDEX], questionArray[QUESTION_SUBCATEGORY_INDEX])) {
                timeline.setQuestionTypeId(Integer.parseInt(questionArray[QUESTION_TYPE_INDEX]));
                timeline.setCategoryId(Integer.parseInt(questionArray[QUESTION_CATEGORY_INDEX]));
                timeline.setSubCategoryId(Integer.parseInt(questionArray[QUESTION_SUBCATEGORY_INDEX]));
            } else {
                throw new DataProcessingException("Question information may contain question_type_id "
                        + "(value from 1 to 10), category_id (value from 1 to 20) and sub-category_id "
                        + "(value from 1 to 5)");
            }
        } else {
            throw new DataProcessingException("Question information has invalid format");
        }
        return timeline;
    }

    private Timeline processResponseType(String responseTypeInformation, Timeline timeline) {
        if (responseTypeInformation.isEmpty()) {
            throw new DataProcessingException("Response type is empty");
        }
        if (dataValidateService.isValidResponseType(responseTypeInformation)) {
            timeline.setResponseType(responseTypeInformation);
        } else {
            throw new DataProcessingException("Response type should contain \"P\"(first answer) "
                    + "or \"N\"(next answer)");
        }
        return timeline;
    }

    private Timeline processDate(String dateInformation, Timeline timeline) {
        if (dateInformation.isEmpty()) {
            throw new DataProcessingException("Date is empty");
        }
        if (dataValidateService.isValidDate(dateInformation)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            timeline.setDate(LocalDate.parse(dateInformation, formatter));
        } else {
            throw new DataProcessingException("Date should have following format dd.MM.yyyy");
        }
        return timeline;
    }

    private Timeline processTime(String timeInformation, Timeline timeline) {
       if (timeInformation.isEmpty()) {
           throw new DataProcessingException("Time is empty");
       }
       if (dataValidateService.isValidTime(timeInformation)) {
           timeline.setTime(Integer.parseInt(timeInformation));
       } else {
           throw new DataProcessingException("Time should be numeric value");
       }
       return timeline;
    }
}
