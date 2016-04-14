package com.pgmacdesign.demolinktogae.retrofitstuff;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 *
 * Created by pmacdowell on 4/11/2016.
 */
public class RetrofitClient {

    //Service Client used for our endpoint calls
    private static RetrofitService serviceClient;

    //This base URL matches the one we are using as a prefix for all of our calls
    @SerializedName("BASE_URL")
    private static final String BASE_URL = "https://com-pgmacdesign-androidtest2.appspot.com/_ah/api/";

    //This log level is used to log in the logcat what is being sent and received via the body
    @SerializedName("httpLogLevel")
    private static HttpLoggingInterceptor.Level httpLogLevel = HttpLoggingInterceptor.Level.BODY;

    //This static method is called when the class is created and runs on its own
    static {
        buildAClient();
    }

    /**
     * Simple getter method. When this is called, the static method is initiated and the client is
     * built and returned.
     * @return Fully built RetrofitService client that is ready for outbound calls
     */
    public static RetrofitService getServiceClient(){
        return serviceClient;
    }

    /**
     * This builds a client that will be used for outbound calls
     */
    private static void buildAClient(){

        //First create the interceptor, which will be used in the Retrofit call
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        //This is where you would add headers if need be. An example would be:
                        //.addHeader("Authorization", "Token:" + someAPIToken)
                        .addHeader("Content-Type", "application/json") //We do want application / json
                        .build(); //Finally, build it
                return chain.proceed(newRequest);
            }
        };

        //Next, we set the logging level
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(httpLogLevel);

        //Next, create the OkHttpClient
        OkHttpClient client = RetrofitClient.configureClient(new OkHttpClient());
        client.interceptors().add(interceptor);
        client.interceptors().add(logging);
        //Set the timeout to 1 minute for now. We can set it to more, but for simplicity, keep it at 1 min
        client.setWriteTimeout(1, TimeUnit.MINUTES);
        client.setReadTimeout(1, TimeUnit.MINUTES);

        //Next, we are making a Gson object that will be used for parsing the response from the server
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create();

        //Lastly, create the retrofit object, which will use the variables/ objects we have created above
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        //Now that it is built, create the service client, which references the interface we made
        serviceClient = retrofit.create(RetrofitService.class);
    }

    /**
     * This class will configure the OkHttpClient to add things like SSL, certs, etc.
     * @param client The client object being changes
     * @return an Altered OkHttpClient with these new features added
     */
    public static OkHttpClient configureClient(final OkHttpClient client) {
        final TrustManager[] certs = new TrustManager[]{new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            @Override
            public void checkServerTrusted(final X509Certificate[] chain,
                                           final String authType)
                                           throws CertificateException {
            }

            @Override
            public void checkClientTrusted(final X509Certificate[] chain,
                                           final String authType)
                                           throws CertificateException {
            }
        }};

        SSLContext ssl = null;
        try {
            ssl = SSLContext.getInstance("TLS");
            ssl.init(null, certs, new SecureRandom());
        } catch (final java.security.GeneralSecurityException ex) {
        }

        try {
            final HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(final String hostname,
                                      final SSLSession session) {
                    return true;
                }
            };
            client.setHostnameVerifier(hostnameVerifier);
            client.setSslSocketFactory(ssl.getSocketFactory());
        } catch (final Exception e) {
        }

        return client;
    }
}
