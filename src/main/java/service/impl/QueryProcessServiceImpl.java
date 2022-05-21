package service.impl;

import exception.DataProcessingException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import model.Query;
import service.DataValidateService;
import service.QueryProcessService;

public class QueryProcessServiceImpl implements QueryProcessService {
    private static final String SIGN_TO_SPLIT_LINE = " ";
    private static final String SIGN_TO_SPLIT_ELEMENT = "\\.";
    private static final String SIGN_TO_SPLIT_DATE = "-";
    private static final int SERVICE_ID_INDEX = 0;
    private static final int SERVICE_VARIATION_INDEX = 1;
    private static final int QUESTION_TYPE_INDEX = 0;
    private static final int QUESTION_CATEGORY_INDEX = 1;
    private static final int QUESTION_SUBCATEGORY_INDEX = 2;
    private static final int DATE_DATE_FROM_INDEX = 0;
    private static final int DATE_DATE_TO_INDEX = 1;
    private static final int DEFAULT_VALUE = 0;

    private final DataValidateService dataValidateService = new DataValidateServiceImpl();

    @Override
    public Query processQuery(String line) {
        String[] dataArray = line.substring(2).split(SIGN_TO_SPLIT_LINE);
        Query query = new Query();
        for (int i = 0; i < dataArray.length; i++) {
            switch (i) {
                case 0:
                    processServiceInformation(dataArray[i], query);
                    continue;
                case 1:
                    processQuestionInformation(dataArray[i], query);
                    continue;
                case 2:
                    processResponseType(dataArray[i], query);
                    continue;
                case 3:
                    processDate(dataArray[i], query);
                    continue;
            }
        }
        return query;
    }

    private Query processServiceInformation(String serviceInformation, Query query) {
        if (serviceInformation.isEmpty()) {
            throw new DataProcessingException("Query service information is empty");
        }
        if (serviceInformation.equals("*")) {
            query.setServiceId(DEFAULT_VALUE);
            query.setServiceVariationId(DEFAULT_VALUE);
            return query;
        }
        String[] serviceArray = serviceInformation.split(SIGN_TO_SPLIT_ELEMENT);
        if (serviceArray.length == 1) {
            if (dataValidateService.isValidServiceInformation(serviceArray[SERVICE_ID_INDEX])) {
                query.setServiceId(Integer.parseInt(serviceArray[SERVICE_ID_INDEX]));
                query.setServiceVariationId(DEFAULT_VALUE);
            } else {
                throw new DataProcessingException("Query service information should contain "
                        + "service_id (value from 1 to 10) or \"*\"");
            }
        } else if (serviceArray.length == 2) {
            if (dataValidateService.isValidServiceInformation(serviceArray[SERVICE_ID_INDEX],
                    serviceArray[SERVICE_VARIATION_INDEX])) {
                query.setServiceId(Integer.parseInt(serviceArray[SERVICE_ID_INDEX]));
                query.setServiceVariationId(Integer.parseInt(
                        serviceArray[SERVICE_VARIATION_INDEX]));
            } else {
                throw new DataProcessingException("Query service information should contain "
                        + "service_id (value from 1 to 10) and service_variation_id"
                        + "(value from 1 to 3) or \"*\"");
            }
        } else {
            throw new DataProcessingException("Query service information has invalid format");
        }
        return query;
    }

    private Query processQuestionInformation(String questionInformation, Query query) {
        if (questionInformation.isEmpty()) {
            throw new DataProcessingException("Query question information is empty");
        }
        if (questionInformation.equals("*")) {
            query.setQuestionTypeId(DEFAULT_VALUE);
            query.setCategoryId(DEFAULT_VALUE);
            query.setSubCategoryId(DEFAULT_VALUE);
            return query;
        }
        String[] questionArray = questionInformation.split(SIGN_TO_SPLIT_ELEMENT);
        if (questionArray.length == 1) {
            if (dataValidateService.isValidQuestionInformation(
                    questionArray[QUESTION_TYPE_INDEX])) {
                query.setQuestionTypeId(Integer.parseInt(questionArray[QUESTION_TYPE_INDEX]));
                query.setCategoryId(DEFAULT_VALUE);
                query.setSubCategoryId(DEFAULT_VALUE);
            } else {
                throw new DataProcessingException("Query question information may contain "
                        + "question_type_id (value from 1 to 10) or \"*\"");
            }
        } else if (questionArray.length == 2) {
            if (dataValidateService.isValidQuestionInformation(questionArray[QUESTION_TYPE_INDEX],
                    questionArray[QUESTION_CATEGORY_INDEX])) {
                query.setQuestionTypeId(Integer.parseInt(questionArray[QUESTION_TYPE_INDEX]));
                query.setCategoryId(Integer.parseInt(questionArray[QUESTION_CATEGORY_INDEX]));
                query.setSubCategoryId(DEFAULT_VALUE);
            } else {
                throw new DataProcessingException("Query question information may contain "
                        + "question_type_id (value from 1 to 10), category_id "
                        + "(value from 1 to 20) or \"*\"");
            }
        } else if (questionArray.length == 3) {
            if (dataValidateService.isValidQuestionInformation(
                    questionArray[QUESTION_TYPE_INDEX],
                    questionArray[QUESTION_CATEGORY_INDEX],
                    questionArray[QUESTION_SUBCATEGORY_INDEX])) {
                query.setQuestionTypeId(Integer.parseInt(questionArray[QUESTION_TYPE_INDEX]));
                query.setCategoryId(Integer.parseInt(questionArray[QUESTION_CATEGORY_INDEX]));
                query.setSubCategoryId(Integer.parseInt(questionArray[QUESTION_SUBCATEGORY_INDEX]));
            } else {
                throw new DataProcessingException("Query question information may contain "
                        + "question_type_id (value from 1 to 10), category_id "
                        + "(value from 1 to 20) and sub-category_id "
                        + "(value from 1 to 5) or \"*\"");
            }
        } else {
            throw new DataProcessingException("Query question information has invalid format");
        }
        return query;
    }

    private Query processResponseType(String responseTypeInformation, Query query) {
        if (responseTypeInformation.isEmpty()) {
            throw new DataProcessingException("Query response type is empty");
        }
        if (dataValidateService.isValidResponseType(responseTypeInformation)) {
            query.setResponseType(responseTypeInformation);
        } else {
            throw new DataProcessingException("Query response type should contain \"P\""
                    + "(first answer) or \"N\"(next answer)");
        }
        return query;
    }

    private Query processDate(String dateInformation, Query query) {
        if (dateInformation.isEmpty()) {
            throw new DataProcessingException("Query date is empty");
        }
        String[] dateArray = dateInformation.split(SIGN_TO_SPLIT_DATE);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.MM.yyyy");
        if (dateArray.length == 1) {
            if (dataValidateService.isValidDate(dateArray[DATE_DATE_FROM_INDEX])) {
                query.setDateFrom(LocalDate.parse(dateArray[DATE_DATE_FROM_INDEX], formatter));
                query.setDateTo(LocalDate.parse(dateArray[DATE_DATE_FROM_INDEX], formatter));
            } else {
                throw new DataProcessingException("Query date_from and date_to should have the "
                        + "following formats: dd.MM.yyyy/d.MM.yyyy");
            }
        } else if (dateArray.length == 2) {
            if (dataValidateService.isValidDate(dateArray[DATE_DATE_FROM_INDEX])
                    && dataValidateService.isValidDate(dateArray[DATE_DATE_TO_INDEX])) {
                query.setDateFrom(LocalDate.parse(dateArray[DATE_DATE_FROM_INDEX], formatter));
                query.setDateTo(LocalDate.parse(dateArray[DATE_DATE_TO_INDEX], formatter));
            } else {
                throw new DataProcessingException("Query date_from and date_to should have the "
                        + "following format dd.MM.yyyy/d.MM.yyyy");
            }
        } else {
            throw new DataProcessingException("Query date information has invalid format");
        }
        return query;
    }
}
