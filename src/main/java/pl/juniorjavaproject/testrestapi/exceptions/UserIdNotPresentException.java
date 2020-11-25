package pl.juniorjavaproject.testrestapi.exceptions;

public class UserIdNotPresentException extends Exception {
    public UserIdNotPresentException(String message){
        super(message);
    }
}
