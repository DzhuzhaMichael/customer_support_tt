package service;

public interface DataValidateService {
    boolean isValidServiceInformation(String serviceId);

    boolean isValidServiceInformation(String serviceId, String serviceVariationId);

    boolean isValidQuestionInformation(String questionTypeId);

    boolean isValidQuestionInformation(String questionTypeId, String categoryId);

    boolean isValidQuestionInformation(String questionTypeId, String categoryId,
                                       String subCategoryId);

    boolean isValidResponseType(String responseTypeInformation);

    boolean isValidDate(String dateInformation);

    boolean isValidTime(String timeInformation);
}
