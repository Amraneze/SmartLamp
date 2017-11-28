package fr.simona.smartlamp.common.repository;

/**
 * Created by Amrane Ait Zeouay on 26-Nov-17.
 */

public abstract class InjectableFactory<T> {

    private T instance;
    protected abstract T createInstance();

    public T getInstance(){
        if (instance == null)
            instance = createInstance();
        return instance;
    }

    public void injectInstance(T instance){
        this.instance = instance;
    }
}