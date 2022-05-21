package service.impl;

import java.util.List;
import model.Query;
import model.Timeline;
import service.DataAnalysisService;

public class DataAnalysisServiceImpl implements DataAnalysisService {
    @Override
    public String evaluate(List<Timeline> timelines, List<Query> queries) {
        Query query = queries.get(queries.size() - 1);
        int timeSum = 0;
        int count = 0;
        for (Timeline timeline : timelines) {
            if (evaluateServiceInformation(query, timeline)
                    && evaluateQuestionInformation(query, timeline)
                    && evaluateResponseInformation(query, timeline)
                    && evaluateDate(query, timeline)) {
                timeSum += timeline.getTime();
                count++;
            }
        }
        if (count != 0) {
            return String.valueOf(timeSum / count);
        } else {
            return "-";
        }
    }

    private boolean evaluateServiceInformation(Query query, Timeline timeline) {
        return ((query.getServiceId() == timeline.getServiceId()
                && query.getServiceVariationId() == timeline.getServiceVariationId())
                || (query.getServiceId() == timeline.getServiceId()
                && query.getServiceVariationId() == 0)
                || (query.getServiceId() == timeline.getServiceId()
                && timeline.getServiceId() == 0)
                || query.getServiceId() == 0);
    }

    private boolean evaluateQuestionInformation(Query query, Timeline timeline) {
        return ((query.getQuestionTypeId() == timeline.getQuestionTypeId()
                && query.getCategoryId() == timeline.getCategoryId()
                && query.getSubCategoryId() == timeline.getSubCategoryId())
                || (query.getQuestionTypeId() == timeline.getQuestionTypeId()
                && query.getCategoryId() == timeline.getCategoryId()
                && query.getSubCategoryId() == 0)
                || (query.getQuestionTypeId() == timeline.getQuestionTypeId()
                && query.getCategoryId() == timeline.getCategoryId()
                && timeline.getSubCategoryId() == 0)
                || (query.getQuestionTypeId() == timeline.getQuestionTypeId()
                && query.getCategoryId() == 0)
                || (query.getQuestionTypeId() == timeline.getQuestionTypeId()
                && timeline.getCategoryId() == 0)
                || query.getQuestionTypeId() == 0);

    }

    private boolean evaluateResponseInformation(Query query, Timeline timeline) {
        return query.getResponseType().equals(timeline.getResponseType());
    }

    private boolean evaluateDate(Query query, Timeline timeline) {
        return (query.getDateFrom().isEqual(timeline.getDate())
                || (query.getDateFrom().isBefore(timeline.getDate())
                && query.getDateTo().isAfter(timeline.getDate()))
                || (query.getDateTo().isEqual(timeline.getDate())));
    }
}
