package exception;

public class CategoriaProductoNotFoundException extends RuntimeException {

    private final Integer errorCode;

    public CategoriaProductoNotFoundException(String message) {
        super(message);
        this.errorCode = 1001;
    }

    @Override
    public String getMessage() {
        return "Error code: " + this.errorCode + ", message: " + super.getMessage();
    }

    public Integer getErrorCode() {
        return errorCode;
    }
} 