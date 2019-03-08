package helpers.api.teedy;

import com.sismics.sapparot.function.CheckedConsumer;
import com.sismics.sapparot.function.CheckedFunction;
import com.sismics.sapparot.okhttp.OkHttpHelper;
import helpers.api.teedy.service.DocumentService;
import helpers.api.teedy.service.FileService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import play.Play;

import static org.mockito.Mockito.mock;

/**
 * @author jtremeaux
 */
public class TeedyClient {
    private OkHttpClient client;

    private static TeedyClient teedyClient;

    private DocumentService documentService;

    private FileService fileService;

    public static TeedyClient get() {
        if (teedyClient == null) {
            teedyClient = new TeedyClient();
        }
        return teedyClient;
    }

    public TeedyClient() {
        client = createClient();
        if (isMock()) {
            documentService = mock(DocumentService.class);
            fileService = mock(FileService.class);
        } else {
            documentService = new DocumentService(this);
            fileService = new FileService(this);
        }
    }

    private boolean isMock() {
        return Boolean.parseBoolean(Play.configuration.getProperty( "teedy.mock", "false"));
    }

    private static OkHttpClient createClient() {
        return new OkHttpClient.Builder()
                .build();
    }

    public String getDocsApiUrl() {
        return Play.configuration.getProperty("teedy.api.url");
    }

    public String getAuthToken() {
        return Play.configuration.getProperty("teedy.authToken");
    }

    public String getUrl(String url) {
        return getDocsApiUrl() + url;
    }

    public OkHttpClient getClient() {
        return client;
    }

    public DocumentService getDocumentService() {
        return documentService;
    }

    public FileService getFileService() {
        return fileService;
    }

    public Request authRequest(Request request) {
        return request.newBuilder()
                .addHeader("Cookie", "auth_token=" + getAuthToken())
                .build();
    }

    public <T> T execute(Request request, CheckedFunction<Response, T> onSuccess, CheckedConsumer<Response> onFailure) {
        return OkHttpHelper.execute(getClient(), request, onSuccess, onFailure);
    }
}
