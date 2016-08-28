package ru.sulion.webapplications;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import ru.sulion.webapplications.api.AccountService;
import ru.sulion.webapplications.api.RedirectingService;
import ru.sulion.webapplications.api.StatisticsStore;
import ru.sulion.webapplications.core.OverheadTaskManager;
import ru.sulion.webapplications.core.SingleThreadTaskQueueManager;
import ru.sulion.webapplications.core.TracedRedirectingService;
import ru.sulion.webapplications.db.MapDBAccountService;
import ru.sulion.webapplications.db.MapDBRedirectDictionary;
import ru.sulion.webapplications.db.MapDBStatisticsStore;
import ru.sulion.webapplications.db.RedirectDictionary;

/**
 * Created by sulion on 27.08.16.
 */
public class MilesShortModule extends AbstractModule {
    private final MilesShortConfiguration configuration;
    private final DB db;

    public MilesShortModule(MilesShortConfiguration configuration) {
        this.configuration = configuration;
        db = DBMaker.fileDB(configuration.getDbFileName()).closeOnJvmShutdown()
                .concurrencyScale(Runtime.getRuntime().availableProcessors()).fileMmapEnableIfSupported()
                .transactionEnable().cleanerHackEnable().make();
    }

    @Override
    protected void configure() {
        bind(AccountService.class).to(MapDBAccountService.class);
        bind(RedirectingService.class).to(TracedRedirectingService.class);
        bind(StatisticsStore.class).to(MapDBStatisticsStore.class).in(Singleton.class);
        bind(RedirectDictionary.class).to(MapDBRedirectDictionary.class);
        bind(OverheadTaskManager.class).to(SingleThreadTaskQueueManager.class).in(Singleton.class);
        bind(DB.class).toInstance(db);
        bind(String.class).annotatedWith(Names.named("HTTP_URL")).toInstance(configuration.getHTTPUrl());
    }
}
