import exception.DataProcessingException;
import java.util.ArrayList;
import java.util.List;
import model.Query;
import model.Timeline;
import service.DataAnalysisService;
import service.FileReaderService;
import service.QueryProcessService;
import service.TimelineProcessService;
import service.impl.DataAnalysisServiceImpl;
import service.impl.FileReaderServiceImpl;
import service.impl.QueryProcessServiceImpl;
import service.impl.TimelineProcessServiceImpl;

public class MainApp {
    private static final String SOURCE_FILE_PATH
            = "src/main/resources/input.csv";
    private static final FileReaderService fileReaderService
            = new FileReaderServiceImpl();
    private static final TimelineProcessService timelineProcessService
            = new TimelineProcessServiceImpl();
    private static final QueryProcessService queryProcessService
            = new QueryProcessServiceImpl();
    private static final DataAnalysisService dataAnalysisService
            = new DataAnalysisServiceImpl();

    public static void main(String[] args) {
        List<String> rawDataFromFile = fileReaderService.read(SOURCE_FILE_PATH);
        List<Timeline> timelines = new ArrayList<>();
        List<Query> queries = new ArrayList<>();
        List<String> analyticalResults = new ArrayList<>();
        for (int i = 1; i < rawDataFromFile.size(); i++) {
            if (rawDataFromFile.get(i).startsWith("C")) {
                Timeline timeline = timelineProcessService.processTimeline(rawDataFromFile.get(i));
                timelines.add(timeline);
            } else if (rawDataFromFile.get(i).startsWith("D")) {
                Query query = queryProcessService.processQuery(rawDataFromFile.get(i));
                queries.add(query);
                String result = dataAnalysisService.evaluate(timelines, queries);
                analyticalResults.add(result);
            } else {
                throw new DataProcessingException("Input line should starts with "
                        + "\"C\" or \"D\"");
            }
        }
        for (String element : analyticalResults) {
            System.out.println(element);
        }
    }
}

