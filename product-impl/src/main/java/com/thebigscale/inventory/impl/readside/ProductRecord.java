package com.thebigscale.inventory.impl.readside;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ProductRecord {
    private String id;
    private String message;

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
