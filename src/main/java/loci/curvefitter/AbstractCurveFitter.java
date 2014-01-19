/*
 * #%L
 * Curve Fitter library for fitting exponential decay curves to sample data.
 * %%
 * Copyright (C) 2010 - 2014 Board of Regents of the University of
 * Wisconsin-Madison.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
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

