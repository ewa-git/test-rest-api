package pl.juniorjavaproject.testrestapi.exceptions;

public class ElementCanNotBeNull extends RuntimeException{
    public ElementCanNotBeNull(String message){
        super(message);
    }
}
