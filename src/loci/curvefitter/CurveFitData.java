/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package loci.curvefitter;

/**
 *
 * @author aivar
 */
public class CurveFitData implements ICurveFitData {
    double[] m_params;
    double[] m_yData;
    double[] m_yFitted;
    Object m_userData;

    /**
     * @inheritDoc
     */
    public double[] getParams() {
        return m_params;
    }

    /**
     * @inheritDoc
     */
    public void setParams(double[] params) {
        m_params = params;
    }

    /**
     * @inheritDoc
     */
    public double[] getYData() {
        return m_yData;
    }

    /**
     * @inheritDoc
     */
    public void setYData(double yData[]) {
        m_yData = yData;
    }

    /**
     * @inheritDoc
     */
    public double[] getYFitted() {
        return m_yFitted;
    }

    /**
     * @inheritDoc
     */
    public void setYFitted(double yFitted[]) {
        m_yFitted = yFitted;
    }

    /**
     * @inheritDoc
     */
    public Object getUserData() {
        return m_userData;
    }

    /**
     * @inheritDoc
     */
    public void setUserData(Object userData) {
        m_userData = userData;
    }
}
