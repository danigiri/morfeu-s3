package cat.calidos.morfeu.s3.control.injection;

import javax.inject.Named;

import dagger.BindsInstance;
import dagger.producers.ProductionComponent;

import cat.calidos.morfeu.utils.injection.ListeningExecutorServiceModule;

@ProductionComponent(modules = {S3ContentGetControlModule.class, ListeningExecutorServiceModule.class})
public interface S3ContentGETControlComponent {

String content();

//@formatter:off
@ProductionComponent.Builder
interface Builder {

	@BindsInstance Builder fromKey(@Named("Key") String key);
	@BindsInstance Builder inBucket(@Named("Bucket") String bucket);


	S3ContentGETControlComponent build();

}
//@formatter:on



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
