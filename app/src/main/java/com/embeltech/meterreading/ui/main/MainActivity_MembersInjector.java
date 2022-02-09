package com.embeltech.meterreading.ui.main;

import com.embeltech.meterreading.data.preferences.AppPreferences;
import dagger.MembersInjector;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class MainActivity_MembersInjector implements MembersInjector<MainActivity> {
  private final Provider<AppPreferences> appPreferencesProvider;

  public MainActivity_MembersInjector(Provider<AppPreferences> appPreferencesProvider) {
    this.appPreferencesProvider = appPreferencesProvider;
  }

  public static MembersInjector<MainActivity> create(
      Provider<AppPreferences> appPreferencesProvider) {
    return new MainActivity_MembersInjector(appPreferencesProvider);
  }

  @Override
  public void injectMembers(MainActivity instance) {
    injectAppPreferences(instance, appPreferencesProvider.get());
  }

  public static void injectAppPreferences(MainActivity instance, AppPreferences appPreferences) {
    instance.appPreferences = appPreferences;
  }
}
