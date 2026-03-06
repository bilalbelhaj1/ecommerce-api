package org.example.ecommerceapi.exception;

/**
 * @author $(bilal belhaj)
 **/
public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String msg) {
        super(msg);
    }
}
