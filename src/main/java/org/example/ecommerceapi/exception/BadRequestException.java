package org.example.ecommerceapi.exception;

/**
 * @author $(bilal belhaj)
 **/
public class BadRequestException extends RuntimeException{
    public BadRequestException(String msg) {
        super(msg);
    }
}
