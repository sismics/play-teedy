package helpers.api.teedy.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author jtremeaux
 */
public class Document {

    public String id;

    public String title;

    public String description;

    @SerializedName("create_date")
    public Long createDate;

    @SerializedName("update_date")
    public Long updateDate;

    public String language;

    public Boolean shared;

//    @SerializedName("active_route")
//    public Boolean activeRoute;

//    @SerializedName("current_step_name")
//    public Object currentStepName;

    @SerializedName("file_count")
    public Integer fileCount;

    public List<Tag> tags;
}
