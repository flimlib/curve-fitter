/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package loci.curvefitter;

/**
 * This is a dummy implementation of the IFitterEstimator interface that turns
 * off fit-time TRI2 behavior.
 * 
 * @author Aivar Grislis
 */
public class DummyFitterEstimator implements IFitterEstimator {
    
    @Override
    public boolean usePrompt() {
        return true;
    }

    @Override
    public int getEstimateStartIndex(double[] yCount, int start, int stop) {
        return start;
    }

    @Override
    public double getEstimateAValue(double A, double[] yCount, int start, int stop) {
        return A;
    } 
}
