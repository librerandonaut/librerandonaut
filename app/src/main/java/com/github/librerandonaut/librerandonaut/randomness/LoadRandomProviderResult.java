package com.github.librerandonaut.librerandonaut.randomness;

public class LoadRandomProviderResult {
    public LoadRandomProviderResult(IRandomProvider randomProvider, boolean status, String message)
    {
        this.randomProvider = randomProvider;
        this.status = status;
        this.message = message;
    }

    private boolean status;
    private String message;
    private IRandomProvider randomProvider;

    public boolean getStatus(){
        return status;
    }
    public String getMessage(){
        return message;
    }
    public IRandomProvider getRandomProvider(){
        return randomProvider;
    }
}
