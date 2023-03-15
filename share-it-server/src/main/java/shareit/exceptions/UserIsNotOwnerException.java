package shareit.exceptions;

public class UserIsNotOwnerException extends RuntimeException {

    public UserIsNotOwnerException(String message) {
        super(message);
    }
}
