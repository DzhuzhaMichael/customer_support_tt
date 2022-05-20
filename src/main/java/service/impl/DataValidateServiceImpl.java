package service.impl;

import service.DataValidateService;

public class DataValidateServiceImpl implements DataValidateService {
    private static final String DATE_PATTERN = "^(0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)\\d\\d$";

    @Override
    public boolean isValidServiceInformation(String serviceId) {
        if (serviceId.chars().allMatch(Character::isDigit)) {
            return Integer.parseInt(serviceId) > 0 && Integer.parseInt(serviceId) <= 10;
        }
        return false;
    }

    @Override
    public boolean isValidServiceInformation(String serviceId, String serviceVariationId) {
        if (serviceId.chars().allMatch(Character::isDigit)
                && serviceId.chars().allMatch(Character::isDigit)) {
            return Integer.parseInt(serviceId) > 0 && Integer.parseInt(serviceId) <= 10
                    && Integer.parseInt(serviceVariationId) > 0 && Integer.parseInt(serviceVariationId) <= 3;
        }
        return false;
    }

    @Override
    public boolean isValidQuestionInformation(String questionTypeId) {
        if (questionTypeId.chars().allMatch(Character::isDigit)) {
            return Integer.parseInt(questionTypeId) > 0 && Integer.parseInt(questionTypeId) <= 10;
        }
        return false;
    }

    @Override
    public boolean isValidQuestionInformation(String questionTypeId, String categoryId) {
        if (questionTypeId.chars().allMatch(Character::isDigit)
                && categoryId.chars().allMatch(Character::isDigit)) {
            return Integer.parseInt(questionTypeId) > 0 && Integer.parseInt(questionTypeId) <= 10
                    && Integer.parseInt(categoryId) > 0 && Integer.parseInt(categoryId) < 20;
        }
        return false;
    }

    @Override
    public boolean isValidQuestionInformation(String questionTypeId, String categoryId, String subCategoryId) {
        if (questionTypeId.chars().allMatch(Character::isDigit)
                && categoryId.chars().allMatch(Character::isDigit)
                && subCategoryId.chars().allMatch(Character::isDigit)) {
            return Integer.parseInt(questionTypeId) > 0 && Integer.parseInt(questionTypeId) <= 10
                    && Integer.parseInt(categoryId) > 0 && Integer.parseInt(categoryId) <= 20
                    && Integer.parseInt(subCategoryId) > 0 && Integer.parseInt(subCategoryId) <= 5;
        }
        return false;
    }

    @Override
    public boolean isValidResponseType(String responseTypeInformation) {
        return (responseTypeInformation.equals("P") || responseTypeInformation.equals("N"));
    }

    @Override
    public boolean isValidDate(String dateInformation) {
        return dateInformation.matches(DATE_PATTERN);
    }

    @Override
    public boolean isValidTime(String timeInformation) {
        return timeInformation.chars().allMatch(Character::isDigit);
    }
}
