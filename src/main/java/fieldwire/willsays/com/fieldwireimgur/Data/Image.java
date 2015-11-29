package fieldwire.willsays.com.fieldwireimgur.Data;

/**
 * Created by jingweiwang on 11/28/15.
 */
public class Image {
    String url;
    String name;
    String description;

    public Image(String url, String name, String description) {
        this.url = url;
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
