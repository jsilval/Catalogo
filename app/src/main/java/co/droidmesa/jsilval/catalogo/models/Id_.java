
package co.droidmesa.jsilval.catalogo.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Id_ {

    @SerializedName("label")
    @Expose
    private String label;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

}
