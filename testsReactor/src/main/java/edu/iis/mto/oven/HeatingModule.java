package edu.iis.mto.oven;

public interface HeatingModule {

    void termalCircuit(HeatingSettings settings) throws HeatingException;

    void heater(HeatingSettings settings) throws HeatingException;

    void grill(HeatingSettings settings) throws HeatingException;
}
