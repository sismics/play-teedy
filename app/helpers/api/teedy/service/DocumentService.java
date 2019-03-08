package helpers.api.teedy.service;

import com.google.gson.Gson;
import helpers.api.teedy.TeedyClient;
import helpers.api.teedy.model.Document;
import helpers.api.teedy.model.DocumentListResponse;
import okhttp3.FormBody;
import okhttp3.Request;

import java.util.List;

/**
 * @author jtremeaux
 */
public class DocumentService {
    public TeedyClient teedyClient;

    public DocumentService(TeedyClient teedyClient) {
        this.teedyClient = teedyClient;
    }

    /**
     * Get the list of documents.
     *
     * @param search The search query string
     * @return The list of documents
     */
    public DocumentListResponse listDocument(String search) {
        Request request = teedyClient.authRequest(new Request.Builder()
                .url(teedyClient.getUrl("/document/list?search=" + search))
                .get()
                .build());
        return teedyClient.execute(request,
                (response) -> new Gson().fromJson(response.body().string(), DocumentListResponse.class),
                (response) -> {
                    throw new RuntimeException("Error getting document list, response was: " + response.body().string());
                });
    }

    /**
     * Create a document.
     *
     * @param title The title
     * @param tags The tags
     * @param description The description
     * @param language The language
     * @return The document
     */
    public Document createDocument(String title, List<String> tags, String description, String language) {
        FormBody.Builder builder = new FormBody.Builder()
                .add("title", title)
                .add("description", description)
                .add("language", language);
        for (String tag: tags) {
            builder.add("tags", tag);
        }

        FormBody formBody = builder.build();
        Request request = teedyClient.authRequest(new Request.Builder()
                .url(teedyClient.getUrl("/document"))
                .put(formBody)
                .build());
        return teedyClient.execute(request,
                (response) -> new Gson().fromJson(response.body().string(), Document.class),
                (response) -> {
                    throw new RuntimeException("Error creating document, response was: " + response.body().string());
                });
    }

    /**
     * Update a document.
     *
     * @param id The ID
     * @param title The title
     * @param tags The tags
     * @param description The description
     * @param language The language
     */
    public void updateDocument(String id, String title, List<String> tags, String description, String language) {
        FormBody.Builder builder = new FormBody.Builder()
                .add("title", title)
                .add("description", description)
                .add("language", language);
        for (String tag: tags) {
            builder.add("tags", tag);
        }

        FormBody formBody = builder.build();
        Request request = teedyClient.authRequest(new Request.Builder()
                .url(teedyClient.getUrl("/document/" + id))
                .post(formBody)
                .build());
        teedyClient.execute(request,
                (response) -> null,
                (response) -> {
                    throw new RuntimeException("Error updating document: " + id + ", response was: " + response.body().string());
                });
    }

    /**
     * Delete a document.
     *
     * @param id The document to delete
     */
    public void deleteDocument(String id) {
        Request request = teedyClient.authRequest(new Request.Builder()
                .url(teedyClient.getUrl("/document/" + id))
                .delete()
                .build());
        teedyClient.execute(request,
                (response) -> null,
                (response) -> {
                    throw new RuntimeException("Error deleting document: " + id + ", response was: " + response.body().string());
                });
    }
}
