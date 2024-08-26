package brokenBinding
import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration

import geb.spock.*
import groovy.json.JsonSlurper
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.Response
import org.springframework.beans.factory.annotation.Value

import java.util.concurrent.TimeUnit

/**
 * See https://www.gebish.org/manual/current/ for more instructions
 */
@Integration
@Rollback
class BrokenBindingSpec extends GebSpec {

    String baseUrl;
    OkHttpClient client;
    @Value('${local.server.port}')
    Integer serverPort

    void "test item"() {
        def responseContent, responseBody;

        baseUrl = "http://localhost:${serverPort}";
        this.initUrl();
        when:
        responseContent = this.getUrl("/example");
        responseBody = new JsonSlurper().parseText(responseContent.responseJson);

        then:
        responseContent.responseCode == 200;
    }

    def getUrl(String url) {
        Response response;
        okhttp3.Request request;
        def responseJson;
        Integer responseCode;

        request = new okhttp3.Request.Builder()
                .url(baseUrl + url)
                .method("GET", null)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .build();
        response = client.newCall(request).execute();
        responseCode = response.code();
        responseJson = response.body()?.string();
        response.close();
        return [responseCode: responseCode, responseJson: responseJson];
    }

    def initUrl() {
        CookieJar cookieJar;

        cookieJar = new CookieJar() {
            private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                cookieStore.put(url.host(), cookies);
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = cookieStore.get(url.host());
                return cookies != null ? cookies : new ArrayList<Cookie>();
            }
        };
        client = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .connectTimeout(10, TimeUnit.MINUTES)
                .readTimeout(10, TimeUnit.MINUTES)
                .writeTimeout(10, TimeUnit.MINUTES)
                .build();
    }

}
