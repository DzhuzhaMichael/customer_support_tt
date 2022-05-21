package service;

import java.util.List;
import model.Query;
import model.Timeline;

public interface DataAnalysisService {
    String evaluate(List<Timeline> timelines, List<Query> queries);
}
