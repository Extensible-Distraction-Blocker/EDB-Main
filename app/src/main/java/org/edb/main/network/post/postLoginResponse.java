package org.edb.main.network.post;



import com.google.gson.JsonObject;
import org.edb.main.User;

public class postLoginResponse {
    private int status;
    private Boolean success;
    private String message;


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
