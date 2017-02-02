
package co.droidmesa.jsilval.catalogo.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Catalogo {

    @SerializedName("feed")
    @Expose
    private Feed feed;

    public Feed getFeed() {
        return feed;
    }

    public void setFeed(Feed feed) {
        this.feed = feed;
    }

}
