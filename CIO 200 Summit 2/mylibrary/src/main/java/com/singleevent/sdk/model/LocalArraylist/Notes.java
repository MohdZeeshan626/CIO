package com.singleevent.sdk.model.LocalArraylist;

import java.io.Serializable;

/**
 * Created by Admin on 6/9/2017.
 */

public class Notes implements Serializable {


    int id;
    String notes;
    String type;
    String name;
    String last_updated;

    public Notes(int id, String notes, String type, String name, String last_updated) {
        this.type = type;
        this.id = id;
        this.notes = notes;
        this.name = name;
        this.last_updated = last_updated;
    }

    public Notes() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (this.getClass() != obj.getClass()) {
            return false;
        }

        Notes employeeId = (Notes) obj;

        if (this.id == employeeId.id) {
            return true;
        }
        return false;
    }

    public String getLast_updated() {
        return last_updated;
    }

    public void setLast_updated(String last_updated) {
        this.last_updated = last_updated;
    }
}
