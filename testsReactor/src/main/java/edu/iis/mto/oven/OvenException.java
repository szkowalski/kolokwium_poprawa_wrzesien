package edu.iis.mto.oven;

public class OvenException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public OvenException(HeatingException cause) {
        super(cause);
    }

}
