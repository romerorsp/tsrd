package br.com.javapi.beertime.tsrd.algorithm;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MeanOfValues {

    private int count;
    
    private double totalValues;
    
    public synchronized void compute(double value) {
        count++;
        totalValues = Double.valueOf(String.format("%.4f", totalValues + value));
    }
    
    public synchronized double getMeanValue() {
        return totalValues/count;
    }

    public int getCount() {
        return count;
    }
}
