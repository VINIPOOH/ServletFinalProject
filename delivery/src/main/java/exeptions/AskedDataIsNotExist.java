package exeptions;

public class AskedDataIsNotExist extends Exception {
    public AskedDataIsNotExist() {
    }

    public AskedDataIsNotExist(String message) {
        super(message);
    }
}
