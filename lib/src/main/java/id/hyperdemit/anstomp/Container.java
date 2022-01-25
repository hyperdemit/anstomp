package id.hyperdemit.anstomp;

import java.util.HashMap;
import java.util.Map;

public class Container {
    private final Map<String, Object> objects = new HashMap<>();

    public Container(Object... objects) {
        for(Object object: objects) {
            add(object);
        }
    }

    public void add(Object object) {
        String name = object.getClass().getName();
        if (!objects.containsKey(name)) {
            objects.put(name, object);
        }
    }

    public <T> T require(Class<T> object) {
        if (objects.containsKey(object.getName())) {
            return object.cast(objects.get(object.getName()));
        }

        throw new IllegalArgumentException(String.format("Object %s not exists in container.", object.getName()));
    }
}
