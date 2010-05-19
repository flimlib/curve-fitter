/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Abstract base class for curve fitters.
 *
 * @author Aivar Grislis
 */
public abstract class AbstractCurveFitter implements ICurveFitter {
    int m_curveType = ICurveFitter.EXPONENTIAL;
    double m_xInc = ICurveFitter.DEFAULT_X_INC;
    private Object m_data = null;
    
    /**
     * @inheritDoc
     */
    public int getCurveType() {
        return m_curveType;
    }
    
    /**
     * @inheritDoc
     */
    public void setCurveType(int curveType) {
        m_curveType = curveType;
    }

    /**
     * @inheritDoc
     */
    public double getXInc() {
        return m_xInc;
    }

    /**
     * @inheritDoc
     */
    public void setXInc(double xInc) {
        m_xInc = xInc;
    }

    /**
     * @inheritDoc
     */
    public Object getUserData() {
        return m_data;
    }

    /**
     * @inheritDoc
     */
    public void setUserData(Object data) {
        m_data = data;
    }

    /**
     * @inheritDoc
     */
    public int fitData(ICurveFitData[] data) {
        int nData = data[0].getYData().length;
        return fitData(data, 0, nData - 1);
    }

    /**
     * @inheritDoc
     */
    abstract public int fitData(ICurveFitData[] data, int start, int stop);
}
