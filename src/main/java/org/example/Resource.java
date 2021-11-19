package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Resource {
    private String resourceName;
    private List<String> permissions;

    public Resource(String resourceName, List<String> permissions) {
        this.resourceName = resourceName;
        this.permissions = permissions;
    }

    public List<String> getPermissions() {
        return permissions;
    }
}
