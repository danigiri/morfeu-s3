package cat.calidos.morfeu.s3.control.injection;

import dagger.Component;

import cat.calidos.morfeu.control.injection.PingControlModule;
import cat.calidos.morfeu.webapp.injection.ControlComponent;
import cat.calidos.morfeu.webapp.injection.ControlConfigurationModule;
import cat.calidos.morfeu.webapp.injection.ControlModule;

import cat.calidos.morfeu.s3.control.injection.S3ContentControlModule;

@Component(modules = { ControlModule.class, ControlConfigurationModule.class,
		S3ContentControlModule.class, PingControlModule.class })
public interface S3ControlComponent extends ControlComponent {

//@formatter:off
@Component.Builder
interface Builder extends ControlComponent.Builder {}
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
