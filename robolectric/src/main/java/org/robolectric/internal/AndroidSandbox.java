package org.robolectric.internal;

import javax.inject.Named;
import javax.inject.Provider;
import org.robolectric.internal.bytecode.Sandbox;
import org.robolectric.pluginapi.Sdk;

/**
 * Container simulating an Android device.
 */
@SuppressWarnings("NewApi")
public class AndroidSandbox extends Sandbox {
  private final Sdk sdk;
  private final Environment environment;

  public AndroidSandbox(Provider<Environment> environment, ClassLoader robolectricClassLoader,
      @Named("runtimeSdk") Sdk runtimeSdk) {
    super(
        robolectricClassLoader,
        r ->
            new Thread(
                new ThreadGroup("SDK " + runtimeSdk.getApiLevel()),
                r,
                "SDK " + runtimeSdk.getApiLevel() + " Main Thread"));
    this.environment = runOnMainThread(environment::get);
    sdk = runtimeSdk;
  }

  public Sdk getSdk() {
    return sdk;
  }

  public Environment getEnvironment() {
    return environment;
  }

  @Override
  public String toString() {
    return "AndroidSandbox[SDK " + sdk + "]";
  }
}
