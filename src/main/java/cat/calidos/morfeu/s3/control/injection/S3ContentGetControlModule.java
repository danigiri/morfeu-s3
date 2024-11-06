//

package cat.calidos.morfeu.s3.control.injection;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.inject.Named;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dagger.Module;
import dagger.Provides;
import dagger.Reusable;

import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.S3Exception;
import cat.calidos.morfeu.control.problem.ControlBadGatewayException;
import cat.calidos.morfeu.control.problem.ControlInternalException;
import cat.calidos.morfeu.utils.Config;
import cat.calidos.morfeu.utils.MorfeuUtils;
import cat.calidos.morfeu.view.injection.DaggerViewComponent;
import cat.calidos.morfeu.webapp.control.problem.ControlNotFoundException;
import cat.calidos.morfeu.webapp.control.problem.ControlRuntimeException;


@Module
public class S3ContentGetControlModule {

protected final static Logger log = LoggerFactory.getLogger(S3ContentGetControlModule.class);

private static final String PROBLEM = "";

@Provides
public static String content(	@Named("Bucket") String bucket,
								@Named("Key") String key,
								InputStream effectiveContent)
		throws ControlInternalException {
	long t = System.currentTimeMillis();
	try {
		return IOUtils.toString(effectiveContent, Config.DEFAULT_CHARSET);
	} catch (IOException e) {
		String em = e.getMessage();
		var message = String.format("Could not read object at 's3://[{}]{}' ({})", bucket, key, em);
		log.error(message);
		var toThrow = new ControlInternalException(message, e);
		throw addPayloadTo(toThrow, bucket, key, t, e, message);
	}
}


@Provides
public static InputStream effectiveContent(	@Named("Bucket") String bucket,
											@Named("Key") String key,
											S3Client s3Client,
											GetObjectRequest request)
		throws ControlInternalException {
	long t = System.currentTimeMillis();
	try {
		return s3Client.getObject(request);
	} catch (AwsServiceException | SdkClientException e) {
		log.error("Could not get object at 's3://[{}]{}' ({})", bucket, key, e.getMessage());
		String em = e.getMessage();
		String message;
		ControlRuntimeException toThrow;
		if (e instanceof AwsServiceException ase) {
			switch (ase) {
				case NoSuchBucketException nsbe:
					message = String.format("No such bucket at 's3://[%s]{}' (%s)", bucket, key, em);
					toThrow = new ControlNotFoundException(message, e);
					break;
				case NoSuchKeyException nsbe:
					message = String.format("No such key at 's3://[%s]{}' (%s)", bucket, key, em);
					toThrow = new ControlNotFoundException(message, e);
					break;
				default:
					message = String.format("Service problem 's3://[%s]{}' (%s)", bucket, key, em);
					toThrow = new ControlBadGatewayException(message, e);
					break;
			}
		} else { // SdkClientException case
			message = String.format("SDK issue at 's3://[%s]{}' (%s)", bucket, key, em);
			toThrow = new ControlBadGatewayException(message, e);
		}
		log.error(message);
		throw addPayloadTo(toThrow, bucket, key, t, e, message);
	}
}


@Provides @Reusable
public static S3Client s3Client() {
	return S3Client.builder().httpClientBuilder(ApacheHttpClient.builder()).build();
}


@Provides
public static GetObjectRequest request(	@Named("Bucket") String bucket,
										@Named("Key") String key) {
	return GetObjectRequest.builder().bucket(bucket).key(key).build();
}


private static ControlRuntimeException addPayloadTo(ControlRuntimeException e,
													String bucket,
													String key,
													long operationTimeStart,
													Exception sourceException,
													String message) {
	Map<String, String> params = MorfeuUtils
			.paramStringMap(
					"target",
					"s3://" + bucket + "/" + key,
					"operation",
					"GET",
					"operationTime",
					Long.toString(System.currentTimeMillis() - operationTimeStart),
					"result",
					message // probably not needed
			);
	String payload = DaggerViewComponent
			.builder()
			.withValue(params)
			.andProblem(message)
			.build()
			.render();
	e.setPayload(payload);
	return e;
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
