package com.hexaenna.avm.model;

import java.util.ArrayList;

/**
 * Created by admin on 10/21/2017.
 */

public class ProductResponse {

    public String status_code,image_path,success;

    public ArrayList image_array  = new ArrayList();

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public ArrayList getImage_array() {
        return image_array;
    }

    public void setImage_array(ArrayList image_array) {
        this.image_array = image_array;
    }
}
