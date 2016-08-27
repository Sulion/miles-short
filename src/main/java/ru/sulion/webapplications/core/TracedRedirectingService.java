package ru.sulion.webapplications.core;

import com.google.inject.Inject;
import ru.sulion.webapplications.api.Redirect;
import ru.sulion.webapplications.api.RedirectingService;
import ru.sulion.webapplications.db.RedirectDictionary;
import ru.sulion.webapplications.db.StatisticsStore;

/**
 * Created by sulion on 27.08.16.
 */
public class TracedRedirectingService implements RedirectingService {
    private final RedirectDictionary dictionary;
    private final StatisticsStore statisticsStore;
    private final OverheadTaskManager taskManager;

    @Inject
    public TracedRedirectingService(RedirectDictionary dictionary,
                                    StatisticsStore statisticsStore,
                                    OverheadTaskManager taskManager) {
        this.dictionary = dictionary;
        this.statisticsStore = statisticsStore;
        this.taskManager = taskManager;
    }


    @Override
    public Redirect resolveURI(String shortUrl) {
        taskManager.execute(shortUrl, statisticsStore::registerRequest);
        return dictionary.find(shortUrl);
    }
}
