package com.example.ubersockerservice.configurations;

import com.example.ubersockerservice.apis.BookingServiceApi;
import com.example.ubersockerservice.configurations.interceptors.ServiceDiscoveryInterceptor;
import com.netflix.discovery.EurekaClient;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Configuration
public class RetrofitConfig {
//    private final EurekaClient eurekaClient;
//
//    public RetrofitConfig(EurekaClient eurekaClient) {
//        this.eurekaClient = eurekaClient;
//    }
//    private String getServiceUrl(String serviceName){
//        return eurekaClient.getNextServerFromEureka(serviceName,false).getHomePageUrl();
    @Bean
    public OkHttpClient okHttpClient(ServiceDiscoveryInterceptor interceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
    }
    @Bean
    public BookingServiceApi bookingServiceApi(OkHttpClient okHttpClient){
        return new Retrofit.Builder()
                .baseUrl("http://UBERBOOKINGSERVICE/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(BookingServiceApi.class);
    }
}
