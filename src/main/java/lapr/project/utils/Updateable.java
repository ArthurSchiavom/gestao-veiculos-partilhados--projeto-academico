package lapr.project.utils;

public class Updateable<T> {
    private T value;

    public Updateable(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
