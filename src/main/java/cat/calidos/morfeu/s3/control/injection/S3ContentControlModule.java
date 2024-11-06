package cat.calidos.morfeu.s3.control.injection;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.BiFunction;

import javax.inject.Named;

import cat.calidos.morfeu.webapp.injection.ControlComponent;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;


@Module
public class S3ContentControlModule {

private static final String PATH = "/content/(.+)";

@Provides @IntoMap @Named("GET") @StringKey(PATH)
public static BiFunction<List<String>, Map<String, String>, String> get(@Named("Configuration") Properties config) {
	return (pathElems,
			params) -> { return ""; };
}


@Provides @IntoMap @Named("POST") @StringKey(PATH)
public static BiFunction<List<String>, Map<String, String>, String> post(@Named("Configuration") Properties config) {
	return (pathElems,
			params) -> { return ""; };
}


@Provides @IntoMap @Named("Content-Type") @StringKey("/content/(.+)")
public static String contentType() {
	return ControlComponent.TEXT;
}

}

/*
 * Copyright 2024 Daniel Giribet
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
