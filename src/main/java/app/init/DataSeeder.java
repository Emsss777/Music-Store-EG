package app.init;

public interface DataSeeder {

    void seed();
    default int order() { return 0; }
}
