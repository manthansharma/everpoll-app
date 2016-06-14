package com.manthansharma.everpoll.api;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
	public static final String TAG = ServiceGenerator.class.getSimpleName();

	public static Retrofit retrofit;
	public static final String API_BASE_URL = "";
	private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

	private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

	private static Retrofit.Builder builder =
			new Retrofit.Builder()
					.baseUrl(API_BASE_URL)
					.addConverterFactory(GsonConverterFactory.create());

	public static <S> S createService(Class<S> serviceClass) {
		System.out.println("asdfasdfsdf");
		return createService(serviceClass, null);
	}

	public static <S> S createService(Class<S> serviceClass, final String authToken) {
		if (authToken != null) {
			httpClient.addInterceptor(new Interceptor() {
				@Override
				public Response intercept(Chain chain) throws IOException {
					Request original = chain.request();

					// Request customization: add request headers
					Request.Builder requestBuilder = original.newBuilder()
							.header("Authorization", "Token "+authToken)
							.method(original.method(), original.body());

					Request request = requestBuilder.build();
					return chain.proceed(request);
				}
			});
		}

		OkHttpClient client = httpClient.build();
		httpClient.interceptors().add(logging);
		logging.setLevel(HttpLoggingInterceptor.Level.BODY);

		retrofit = builder.client(client).build();
		builder.client(client);
		return retrofit.create(serviceClass);
	}
}