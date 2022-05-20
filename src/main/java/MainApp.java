import java.util.ArrayList;
import java.util.List;
import exception.DataProcessingException;
import model.Query;
import model.Timeline;
import service.QueryProcessService;
import service.TimelineProcessService;
import service.FileReaderService;
import service.impl.QueryProcessServiceImpl;
import service.impl.TimelineProcessServiceImpl;
import service.impl.FileReaderServiceImpl;

public class MainApp {
    private static final String SOURCE_FILE_PATH
            = "src/main/resources/input.csv";
    private static final FileReaderService fileReaderService
            = new FileReaderServiceImpl();
    private static final TimelineProcessService timelineProcessService
            = new TimelineProcessServiceImpl();
    private static final QueryProcessService queryProcessService
            = new QueryProcessServiceImpl();

    public static void main(String[] args) {
        List<String> rawDataFromFile = fileReaderService.read(SOURCE_FILE_PATH);
        List<Timeline> timelines = new ArrayList<>();
        List<Query> queries = new ArrayList<>();

        for (int i = 1; i < rawDataFromFile.size(); i++) {
            if (rawDataFromFile.get(i).startsWith("C")) {
                Timeline timeline = timelineProcessService.processTimeline(rawDataFromFile.get(i));
                timelines.add(timeline);
            } else if (rawDataFromFile.get(i).startsWith("D")) {
                Query query = queryProcessService.processQuery(rawDataFromFile.get(i));
                queries.add(query);
            } else {
                throw new DataProcessingException("Input line should starts with "
                        + "\"C\" or \"D\"");
            }

            }

    }
}

