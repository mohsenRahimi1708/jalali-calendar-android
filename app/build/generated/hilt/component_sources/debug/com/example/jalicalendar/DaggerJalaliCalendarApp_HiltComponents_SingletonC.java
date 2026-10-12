package com.example.jalicalendar;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.hilt.work.HiltWorkerFactory;
import androidx.hilt.work.WorkerAssistedFactory;
import androidx.hilt.work.WorkerFactoryModule_ProvideFactoryFactory;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;
import com.example.jalicalendar.auth.AuthManager;
import com.example.jalicalendar.auth.TokenStore;
import com.example.jalicalendar.data.local.JalaliDatabase;
import com.example.jalicalendar.data.local.dao.AccountDao;
import com.example.jalicalendar.data.local.dao.CalendarDao;
import com.example.jalicalendar.data.local.dao.EventDao;
import com.example.jalicalendar.data.local.dao.ReminderDao;
import com.example.jalicalendar.data.local.dao.TaskDao;
import com.example.jalicalendar.data.prefs.SettingsRepositoryImpl;
import com.example.jalicalendar.data.remote.AuthInterceptor;
import com.example.jalicalendar.data.remote.CalendarApi;
import com.example.jalicalendar.data.remote.TasksApi;
import com.example.jalicalendar.data.remote.TokenAuthenticator;
import com.example.jalicalendar.data.remote.TokenRefresher;
import com.example.jalicalendar.data.repo.AccountRepositoryImpl;
import com.example.jalicalendar.data.repo.CalendarRepositoryImpl;
import com.example.jalicalendar.data.repo.EventRepositoryImpl;
import com.example.jalicalendar.data.repo.TaskRepositoryImpl;
import com.example.jalicalendar.di.DatabaseModule_ProvideAccountDaoFactory;
import com.example.jalicalendar.di.DatabaseModule_ProvideCalendarDaoFactory;
import com.example.jalicalendar.di.DatabaseModule_ProvideDatabaseFactory;
import com.example.jalicalendar.di.DatabaseModule_ProvideEventDaoFactory;
import com.example.jalicalendar.di.DatabaseModule_ProvideReminderDaoFactory;
import com.example.jalicalendar.di.DatabaseModule_ProvideTaskDaoFactory;
import com.example.jalicalendar.di.NetworkModule_ProvideCalendarApiFactory;
import com.example.jalicalendar.di.NetworkModule_ProvideJsonFactory;
import com.example.jalicalendar.di.NetworkModule_ProvideOkHttpFactory;
import com.example.jalicalendar.di.NetworkModule_ProvideTasksApiFactory;
import com.example.jalicalendar.reminders.ReminderScheduler;
import com.example.jalicalendar.sync.ConflictStore;
import com.example.jalicalendar.sync.SyncCursorStore;
import com.example.jalicalendar.sync.SyncEngine;
import com.example.jalicalendar.sync.SyncStatusStore;
import com.example.jalicalendar.sync.SyncWorker;
import com.example.jalicalendar.sync.SyncWorker_AssistedFactory;
import com.example.jalicalendar.sync.WatchManager;
import com.example.jalicalendar.ui.screens.calendars.CalendarsViewModel;
import com.example.jalicalendar.ui.screens.calendars.CalendarsViewModel_HiltModules;
import com.example.jalicalendar.ui.screens.eventedit.EventEditViewModel;
import com.example.jalicalendar.ui.screens.eventedit.EventEditViewModel_HiltModules;
import com.example.jalicalendar.ui.screens.main.MainViewModel;
import com.example.jalicalendar.ui.screens.main.MainViewModel_HiltModules;
import com.example.jalicalendar.ui.screens.search.SearchViewModel;
import com.example.jalicalendar.ui.screens.search.SearchViewModel_HiltModules;
import com.example.jalicalendar.ui.screens.settings.SettingsViewModel;
import com.example.jalicalendar.ui.screens.settings.SettingsViewModel_HiltModules;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.IdentifierNameString;
import dagger.internal.KeepFieldType;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.MapBuilder;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.SingleCheck;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;
import kotlinx.serialization.json.Json;
import okhttp3.OkHttpClient;

@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class DaggerJalaliCalendarApp_HiltComponents_SingletonC {
  private DaggerJalaliCalendarApp_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public JalaliCalendarApp_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements JalaliCalendarApp_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public JalaliCalendarApp_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements JalaliCalendarApp_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public JalaliCalendarApp_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements JalaliCalendarApp_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public JalaliCalendarApp_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements JalaliCalendarApp_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public JalaliCalendarApp_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements JalaliCalendarApp_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public JalaliCalendarApp_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements JalaliCalendarApp_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public JalaliCalendarApp_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements JalaliCalendarApp_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public JalaliCalendarApp_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends JalaliCalendarApp_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends JalaliCalendarApp_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends JalaliCalendarApp_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends JalaliCalendarApp_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity mainActivity) {
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(MapBuilder.<String, Boolean>newMapBuilder(5).put(LazyClassKeyProvider.com_example_jalicalendar_ui_screens_calendars_CalendarsViewModel, CalendarsViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_example_jalicalendar_ui_screens_eventedit_EventEditViewModel, EventEditViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_example_jalicalendar_ui_screens_main_MainViewModel, MainViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_example_jalicalendar_ui_screens_search_SearchViewModel, SearchViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_example_jalicalendar_ui_screens_settings_SettingsViewModel, SettingsViewModel_HiltModules.KeyModule.provide()).build());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_example_jalicalendar_ui_screens_eventedit_EventEditViewModel = "com.example.jalicalendar.ui.screens.eventedit.EventEditViewModel";

      static String com_example_jalicalendar_ui_screens_settings_SettingsViewModel = "com.example.jalicalendar.ui.screens.settings.SettingsViewModel";

      static String com_example_jalicalendar_ui_screens_calendars_CalendarsViewModel = "com.example.jalicalendar.ui.screens.calendars.CalendarsViewModel";

      static String com_example_jalicalendar_ui_screens_main_MainViewModel = "com.example.jalicalendar.ui.screens.main.MainViewModel";

      static String com_example_jalicalendar_ui_screens_search_SearchViewModel = "com.example.jalicalendar.ui.screens.search.SearchViewModel";

      @KeepFieldType
      EventEditViewModel com_example_jalicalendar_ui_screens_eventedit_EventEditViewModel2;

      @KeepFieldType
      SettingsViewModel com_example_jalicalendar_ui_screens_settings_SettingsViewModel2;

      @KeepFieldType
      CalendarsViewModel com_example_jalicalendar_ui_screens_calendars_CalendarsViewModel2;

      @KeepFieldType
      MainViewModel com_example_jalicalendar_ui_screens_main_MainViewModel2;

      @KeepFieldType
      SearchViewModel com_example_jalicalendar_ui_screens_search_SearchViewModel2;
    }
  }

  private static final class ViewModelCImpl extends JalaliCalendarApp_HiltComponents.ViewModelC {
    private final SavedStateHandle savedStateHandle;

    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<CalendarsViewModel> calendarsViewModelProvider;

    private Provider<EventEditViewModel> eventEditViewModelProvider;

    private Provider<MainViewModel> mainViewModelProvider;

    private Provider<SearchViewModel> searchViewModelProvider;

    private Provider<SettingsViewModel> settingsViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.savedStateHandle = savedStateHandleParam;
      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.calendarsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.eventEditViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.mainViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.searchViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.settingsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(MapBuilder.<String, javax.inject.Provider<ViewModel>>newMapBuilder(5).put(LazyClassKeyProvider.com_example_jalicalendar_ui_screens_calendars_CalendarsViewModel, ((Provider) calendarsViewModelProvider)).put(LazyClassKeyProvider.com_example_jalicalendar_ui_screens_eventedit_EventEditViewModel, ((Provider) eventEditViewModelProvider)).put(LazyClassKeyProvider.com_example_jalicalendar_ui_screens_main_MainViewModel, ((Provider) mainViewModelProvider)).put(LazyClassKeyProvider.com_example_jalicalendar_ui_screens_search_SearchViewModel, ((Provider) searchViewModelProvider)).put(LazyClassKeyProvider.com_example_jalicalendar_ui_screens_settings_SettingsViewModel, ((Provider) settingsViewModelProvider)).build());
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return Collections.<Class<?>, Object>emptyMap();
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_example_jalicalendar_ui_screens_calendars_CalendarsViewModel = "com.example.jalicalendar.ui.screens.calendars.CalendarsViewModel";

      static String com_example_jalicalendar_ui_screens_settings_SettingsViewModel = "com.example.jalicalendar.ui.screens.settings.SettingsViewModel";

      static String com_example_jalicalendar_ui_screens_main_MainViewModel = "com.example.jalicalendar.ui.screens.main.MainViewModel";

      static String com_example_jalicalendar_ui_screens_search_SearchViewModel = "com.example.jalicalendar.ui.screens.search.SearchViewModel";

      static String com_example_jalicalendar_ui_screens_eventedit_EventEditViewModel = "com.example.jalicalendar.ui.screens.eventedit.EventEditViewModel";

      @KeepFieldType
      CalendarsViewModel com_example_jalicalendar_ui_screens_calendars_CalendarsViewModel2;

      @KeepFieldType
      SettingsViewModel com_example_jalicalendar_ui_screens_settings_SettingsViewModel2;

      @KeepFieldType
      MainViewModel com_example_jalicalendar_ui_screens_main_MainViewModel2;

      @KeepFieldType
      SearchViewModel com_example_jalicalendar_ui_screens_search_SearchViewModel2;

      @KeepFieldType
      EventEditViewModel com_example_jalicalendar_ui_screens_eventedit_EventEditViewModel2;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.example.jalicalendar.ui.screens.calendars.CalendarsViewModel 
          return (T) new CalendarsViewModel(singletonCImpl.calendarRepositoryImplProvider.get(), singletonCImpl.accountRepositoryImplProvider.get(), singletonCImpl.authManagerProvider.get(), singletonCImpl.syncStatusStoreProvider.get(), ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 1: // com.example.jalicalendar.ui.screens.eventedit.EventEditViewModel 
          return (T) new EventEditViewModel(viewModelCImpl.savedStateHandle, singletonCImpl.eventRepositoryImplProvider.get(), singletonCImpl.calendarRepositoryImplProvider.get(), singletonCImpl.settingsRepositoryImplProvider.get(), singletonCImpl.reminderSchedulerProvider.get());

          case 2: // com.example.jalicalendar.ui.screens.main.MainViewModel 
          return (T) new MainViewModel(singletonCImpl.settingsRepositoryImplProvider.get(), singletonCImpl.eventRepositoryImplProvider.get(), singletonCImpl.calendarRepositoryImplProvider.get());

          case 3: // com.example.jalicalendar.ui.screens.search.SearchViewModel 
          return (T) new SearchViewModel(singletonCImpl.eventRepositoryImplProvider.get(), singletonCImpl.settingsRepositoryImplProvider.get());

          case 4: // com.example.jalicalendar.ui.screens.settings.SettingsViewModel 
          return (T) new SettingsViewModel(singletonCImpl.settingsRepositoryImplProvider.get(), singletonCImpl.calendarRepositoryImplProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends JalaliCalendarApp_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends JalaliCalendarApp_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends JalaliCalendarApp_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<TokenStore> tokenStoreProvider;

    private Provider<JalaliDatabase> provideDatabaseProvider;

    private Provider<AccountRepositoryImpl> accountRepositoryImplProvider;

    private Provider<AuthManager> authManagerProvider;

    private Provider<CalendarRepositoryImpl> calendarRepositoryImplProvider;

    private Provider<EventRepositoryImpl> eventRepositoryImplProvider;

    private Provider<TaskRepositoryImpl> taskRepositoryImplProvider;

    private Provider<SettingsRepositoryImpl> settingsRepositoryImplProvider;

    private Provider<AuthInterceptor> authInterceptorProvider;

    private Provider<TokenRefresher> tokenRefresherProvider;

    private Provider<TokenAuthenticator> tokenAuthenticatorProvider;

    private Provider<OkHttpClient> provideOkHttpProvider;

    private Provider<Json> provideJsonProvider;

    private Provider<CalendarApi> provideCalendarApiProvider;

    private Provider<TasksApi> provideTasksApiProvider;

    private Provider<ConflictStore> conflictStoreProvider;

    private Provider<SyncCursorStore> syncCursorStoreProvider;

    private Provider<SyncStatusStore> syncStatusStoreProvider;

    private Provider<WatchManager> watchManagerProvider;

    private Provider<ReminderScheduler> reminderSchedulerProvider;

    private Provider<SyncEngine> syncEngineProvider;

    private Provider<SyncWorker_AssistedFactory> syncWorker_AssistedFactoryProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    private AccountDao accountDao() {
      return DatabaseModule_ProvideAccountDaoFactory.provideAccountDao(provideDatabaseProvider.get());
    }

    private CalendarDao calendarDao() {
      return DatabaseModule_ProvideCalendarDaoFactory.provideCalendarDao(provideDatabaseProvider.get());
    }

    private EventDao eventDao() {
      return DatabaseModule_ProvideEventDaoFactory.provideEventDao(provideDatabaseProvider.get());
    }

    private ReminderDao reminderDao() {
      return DatabaseModule_ProvideReminderDaoFactory.provideReminderDao(provideDatabaseProvider.get());
    }

    private TaskDao taskDao() {
      return DatabaseModule_ProvideTaskDaoFactory.provideTaskDao(provideDatabaseProvider.get());
    }

    private Map<String, javax.inject.Provider<WorkerAssistedFactory<? extends ListenableWorker>>> mapOfStringAndProviderOfWorkerAssistedFactoryOf(
        ) {
      return Collections.<String, javax.inject.Provider<WorkerAssistedFactory<? extends ListenableWorker>>>singletonMap("com.example.jalicalendar.sync.SyncWorker", ((Provider) syncWorker_AssistedFactoryProvider));
    }

    private HiltWorkerFactory hiltWorkerFactory() {
      return WorkerFactoryModule_ProvideFactoryFactory.provideFactory(mapOfStringAndProviderOfWorkerAssistedFactoryOf());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.tokenStoreProvider = DoubleCheck.provider(new SwitchingProvider<TokenStore>(singletonCImpl, 3));
      this.provideDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<JalaliDatabase>(singletonCImpl, 5));
      this.accountRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<AccountRepositoryImpl>(singletonCImpl, 4));
      this.authManagerProvider = DoubleCheck.provider(new SwitchingProvider<AuthManager>(singletonCImpl, 2));
      this.calendarRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<CalendarRepositoryImpl>(singletonCImpl, 6));
      this.eventRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<EventRepositoryImpl>(singletonCImpl, 7));
      this.taskRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<TaskRepositoryImpl>(singletonCImpl, 8));
      this.settingsRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<SettingsRepositoryImpl>(singletonCImpl, 9));
      this.authInterceptorProvider = DoubleCheck.provider(new SwitchingProvider<AuthInterceptor>(singletonCImpl, 12));
      this.tokenRefresherProvider = DoubleCheck.provider(new SwitchingProvider<TokenRefresher>(singletonCImpl, 14));
      this.tokenAuthenticatorProvider = DoubleCheck.provider(new SwitchingProvider<TokenAuthenticator>(singletonCImpl, 13));
      this.provideOkHttpProvider = DoubleCheck.provider(new SwitchingProvider<OkHttpClient>(singletonCImpl, 11));
      this.provideJsonProvider = DoubleCheck.provider(new SwitchingProvider<Json>(singletonCImpl, 15));
      this.provideCalendarApiProvider = DoubleCheck.provider(new SwitchingProvider<CalendarApi>(singletonCImpl, 10));
      this.provideTasksApiProvider = DoubleCheck.provider(new SwitchingProvider<TasksApi>(singletonCImpl, 16));
      this.conflictStoreProvider = DoubleCheck.provider(new SwitchingProvider<ConflictStore>(singletonCImpl, 17));
      this.syncCursorStoreProvider = DoubleCheck.provider(new SwitchingProvider<SyncCursorStore>(singletonCImpl, 18));
      this.syncStatusStoreProvider = DoubleCheck.provider(new SwitchingProvider<SyncStatusStore>(singletonCImpl, 19));
      this.watchManagerProvider = DoubleCheck.provider(new SwitchingProvider<WatchManager>(singletonCImpl, 20));
      this.reminderSchedulerProvider = DoubleCheck.provider(new SwitchingProvider<ReminderScheduler>(singletonCImpl, 21));
      this.syncEngineProvider = DoubleCheck.provider(new SwitchingProvider<SyncEngine>(singletonCImpl, 1));
      this.syncWorker_AssistedFactoryProvider = SingleCheck.provider(new SwitchingProvider<SyncWorker_AssistedFactory>(singletonCImpl, 0));
    }

    @Override
    public void injectJalaliCalendarApp(JalaliCalendarApp jalaliCalendarApp) {
      injectJalaliCalendarApp2(jalaliCalendarApp);
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return Collections.<Boolean>emptySet();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private JalaliCalendarApp injectJalaliCalendarApp2(JalaliCalendarApp instance) {
      JalaliCalendarApp_MembersInjector.injectWorkerFactory(instance, hiltWorkerFactory());
      return instance;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.example.jalicalendar.sync.SyncWorker_AssistedFactory 
          return (T) new SyncWorker_AssistedFactory() {
            @Override
            public SyncWorker create(Context appContext, WorkerParameters params) {
              return new SyncWorker(appContext, params, singletonCImpl.syncEngineProvider.get());
            }
          };

          case 1: // com.example.jalicalendar.sync.SyncEngine 
          return (T) new SyncEngine(singletonCImpl.authManagerProvider.get(), singletonCImpl.accountRepositoryImplProvider.get(), singletonCImpl.calendarRepositoryImplProvider.get(), singletonCImpl.eventRepositoryImplProvider.get(), singletonCImpl.taskRepositoryImplProvider.get(), singletonCImpl.settingsRepositoryImplProvider.get(), singletonCImpl.provideCalendarApiProvider.get(), singletonCImpl.provideTasksApiProvider.get(), singletonCImpl.conflictStoreProvider.get(), singletonCImpl.syncCursorStoreProvider.get(), singletonCImpl.syncStatusStoreProvider.get(), singletonCImpl.watchManagerProvider.get(), singletonCImpl.reminderSchedulerProvider.get());

          case 2: // com.example.jalicalendar.auth.AuthManager 
          return (T) new AuthManager(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.tokenStoreProvider.get(), singletonCImpl.accountRepositoryImplProvider.get());

          case 3: // com.example.jalicalendar.auth.TokenStore 
          return (T) new TokenStore(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 4: // com.example.jalicalendar.data.repo.AccountRepositoryImpl 
          return (T) new AccountRepositoryImpl(singletonCImpl.accountDao());

          case 5: // com.example.jalicalendar.data.local.JalaliDatabase 
          return (T) DatabaseModule_ProvideDatabaseFactory.provideDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 6: // com.example.jalicalendar.data.repo.CalendarRepositoryImpl 
          return (T) new CalendarRepositoryImpl(singletonCImpl.calendarDao());

          case 7: // com.example.jalicalendar.data.repo.EventRepositoryImpl 
          return (T) new EventRepositoryImpl(singletonCImpl.eventDao(), singletonCImpl.reminderDao());

          case 8: // com.example.jalicalendar.data.repo.TaskRepositoryImpl 
          return (T) new TaskRepositoryImpl(singletonCImpl.taskDao());

          case 9: // com.example.jalicalendar.data.prefs.SettingsRepositoryImpl 
          return (T) new SettingsRepositoryImpl(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 10: // com.example.jalicalendar.data.remote.CalendarApi 
          return (T) NetworkModule_ProvideCalendarApiFactory.provideCalendarApi(singletonCImpl.provideOkHttpProvider.get(), singletonCImpl.provideJsonProvider.get());

          case 11: // okhttp3.OkHttpClient 
          return (T) NetworkModule_ProvideOkHttpFactory.provideOkHttp(singletonCImpl.authInterceptorProvider.get(), singletonCImpl.tokenAuthenticatorProvider.get());

          case 12: // com.example.jalicalendar.data.remote.AuthInterceptor 
          return (T) new AuthInterceptor(singletonCImpl.tokenStoreProvider.get());

          case 13: // com.example.jalicalendar.data.remote.TokenAuthenticator 
          return (T) new TokenAuthenticator(singletonCImpl.tokenStoreProvider.get(), singletonCImpl.tokenRefresherProvider.get());

          case 14: // com.example.jalicalendar.data.remote.TokenRefresher 
          return (T) new TokenRefresher(DoubleCheck.lazy(singletonCImpl.authManagerProvider), singletonCImpl.tokenStoreProvider.get());

          case 15: // kotlinx.serialization.json.Json 
          return (T) NetworkModule_ProvideJsonFactory.provideJson();

          case 16: // com.example.jalicalendar.data.remote.TasksApi 
          return (T) NetworkModule_ProvideTasksApiFactory.provideTasksApi(singletonCImpl.provideOkHttpProvider.get(), singletonCImpl.provideJsonProvider.get());

          case 17: // com.example.jalicalendar.sync.ConflictStore 
          return (T) new ConflictStore();

          case 18: // com.example.jalicalendar.sync.SyncCursorStore 
          return (T) new SyncCursorStore(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 19: // com.example.jalicalendar.sync.SyncStatusStore 
          return (T) new SyncStatusStore();

          case 20: // com.example.jalicalendar.sync.WatchManager 
          return (T) new WatchManager(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.provideCalendarApiProvider.get());

          case 21: // com.example.jalicalendar.reminders.ReminderScheduler 
          return (T) new ReminderScheduler(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
