package org.example;

import java.util.ArrayList;
import java.util.List;

public class Resource {
    private String resourceName;
    private List<String> permissions = new ArrayList<>();

    public Resource(String resourceName, List<String> permissions) {
        this.resourceName = resourceName;
        this.permissions = permissions;
    }

    public String getResourceName() {
        return resourceName;
    }

    public List<String> getPermissions() {
        return permissions;
    }
}
