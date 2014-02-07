/*
 * #%L
 * Curve Fitter library for fitting exponential decay curves to sample data.
 * %%
 * Copyright (C) 2010 - 2014 Board of Regents of the University of
 * Wisconsin-Madison.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

package loci.curvefitter;

/**
 * Abstract base class for curve fitters.
 *
 * @author Aivar Grislis
 */
public abstract class AbstractCurveFitter implements ICurveFitter {
    IFitterEstimator m_estimator = new DummyFitterEstimator();
    FitAlgorithm m_fitAlgorithm;
    FitFunction m_fitFunction;
    NoiseModel m_noiseModel;
    double m_xInc = ICurveFitter.DEFAULT_X_INC;
    boolean[] m_free;
    double[] m_instrumentResponse;

    @Override
    public IFitterEstimator getEstimator() {
        return m_estimator;
    }
    
    @Override
    public void setEstimator(IFitterEstimator estimator) {
        m_estimator = estimator;
    }
    
    @Override
    public FitAlgorithm getFitAlgorithm() {
        return m_fitAlgorithm;
    }
    
    @Override
    public void setFitAlgorithm(FitAlgorithm algorithm) {
        m_fitAlgorithm = algorithm;
    }
    
    @Override
    public FitFunction getFitFunction() {
        return m_fitFunction;
    }
    
    @Override
    public void setFitFunction(FitFunction function) {
        m_fitFunction = function;
    }

    @Override
    public NoiseModel getNoiseModel() {
        return m_noiseModel;
    }

    @Override
    public void setNoiseModel(NoiseModel noiseModel) {
        m_noiseModel = noiseModel;
    }

    @Override
    public int getNumberComponents() {
        int number = 0;
        if (null != m_fitFunction) {
            int fitFunctionComponents[] = { 1, 2, 3, 1 };
            number = fitFunctionComponents[m_fitFunction.ordinal()];
        }
        return number;
    }

    @Override
    public double getXInc() {
        return m_xInc;
    }

    @Override
    public void setXInc(double xInc) {
        m_xInc = xInc;
    }

    @Override
    public boolean[] getFree() {
        return m_free;
    }

    @Override
    public void setFree(boolean[] free) {
        m_free = free;
    }

    @Override
    public double[] getInstrumentResponse(int pixels) {
        double[] instrumentResponse = null;
        if (null != m_instrumentResponse) {
            instrumentResponse = new double[m_instrumentResponse.length];
            for (int i = 0; i < instrumentResponse.length; ++i) {
                instrumentResponse[i] = pixels * m_instrumentResponse[i];
            }
        }
        return instrumentResponse;
    }

    @Override
    public void setInstrumentResponse(double response[]) {
        m_instrumentResponse = response;
    }

    @Override
    public abstract int fitData(ICurveFitData[] data);
}

