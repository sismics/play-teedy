package helpers.api.teedy.service;

import com.google.gson.Gson;
import helpers.api.teedy.TeedyClient;
import helpers.api.teedy.model.Document;
import helpers.api.teedy.model.File;
import helpers.api.teedy.model.FileListResponse;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * @author jtremeaux
 */
public class FileService {
    public TeedyClient teedyClient;

    public FileService(TeedyClient teedyClient) {
        this.teedyClient = teedyClient;
    }

    /**
     * Create a file.
     *
     * @param documentId The document Id
     * @return The file
     */
    public File createFile(String documentId, String fileName, String mimeType, byte[] bytes) {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id", documentId)
                .addFormDataPart("file", fileName, RequestBody.create(MediaType.parse(mimeType), bytes))
                .build();
        Request request = teedyClient.authRequest(new Request.Builder()
                .url(teedyClient.getUrl("/file"))
                .put(requestBody)
                .build());
        return teedyClient.execute(request,
                (response) -> new Gson().fromJson(response.body().string(), File.class),
                (response) -> {
                    throw new RuntimeException("Error creating file, response was: " + response.body().string());
                });
    }

    /**
     * Get a file list.
     *
     * @param id The document ID
     * @return The list of files
     */
    public FileListResponse getFileList(String id) {
        Request request = teedyClient.authRequest(new Request.Builder()
                .url(teedyClient.getUrl("/file/list?id=" + id))
                .get()
                .build());
        return teedyClient.execute(request,
                (response) -> new Gson().fromJson(response.body().string(), FileListResponse.class),
                (response) -> {
                    throw new RuntimeException("Error getting file list, response was: " + response.body().string());
                });
    }

    /**
     * Get a file data contents.
     *
     * @param id The file ID
     * @return The file data contents
     */
    public String getFileDataContent(String id) {
        Request request = teedyClient.authRequest(new Request.Builder()
                .url(teedyClient.getUrl("/file/" + id + "/data?size=content"))
                .get()
                .build());
        return teedyClient.execute(request,
                (response) -> response.body().string(),
                (response) -> {
                    throw new RuntimeException("Error getting file contents, response was: " + response.body().string());
                });
    }

    /**
     * Get a file data bytes.
     *
     * @param id The file ID
     * @return The file data bytes
     */
    public byte[] getFileDataByte(String id) {
        Request request = teedyClient.authRequest(new Request.Builder()
                .url(teedyClient.getUrl("/file/" + id + "/data"))
                .get()
                .build());
        return teedyClient.execute(request,
                (response) -> response.body().bytes(),
                (response) -> {
                    throw new RuntimeException("Error getting file contents, response was: " + response.body().string());
                });
    }

    /**
     * Wait for the document processing completion.
     *
     * @param document The document to wait for
     */
    public void waitForDocumentCompletion(Document document) {
        int tries = 10;
        while (tries > 0) {
            FileListResponse response = getFileList(document.id);
            if (response.files.stream().allMatch(e -> !e.processing)) {
                return;
            }
            try {
                Thread.sleep(10_000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            tries--;
        }
        throw new RuntimeException("Timed out waiting for document processing: ID=" + document.id + " title=" + document.title);
    }
}
