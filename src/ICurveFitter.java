/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * Interface for a curve fitter.
 *
 * @author Aivar Grislis
 */
//TODO max iterations; c/b used in lieu of 'iterate()'??
public interface ICurveFitter {
    /**
     * Fitting a Gaussian curve.
     */
    public int GAUSSIAN = 0; //TODO s/b enums

    /**
     * Fitting an Exponential curve.
     */
    public int EXPONENTIAL = 1;

    /**
     * Default increment along x axis (evenly spaced).
     */
    public double DEFAULT_X_INC = 1.0f;

    /**
     * Get curve shape we are fitting.
     *
     * @return curve type
     */
    int getCurveType();

    /**
     * Set curve shape we are fitting.
     *
     * @param curveType type of curve
     */
    public void setCurveType(int curveType);

    /**
     * Get increment along x axis (evenly spaced).
     *
     * @return x increment
     */
    public double getXInc();

    /**
     * Set increment along x axis (evenly spaced).
     *
     * @param xInc x increment
     */
    public void setXInc(double xInc);

    /**
     * Keeps track of user data on behalf of caller.
     * 
     * @return user data
     */
    public Object getUserData();

    /**
     * Keeps track of user data on behalf of caller.
     *
     * @param data user data
     */
    public void setUserData(Object data);

    /**
     * Do the fit.
     *
     * @param data array of data to fit
     * @return status code
     */
    public int fitData(ICurveFitData[] data);

    /**
     * Do the fit.
     *
     * @param data array of data to fit
     * @param start first index to fit
     * @param stop last index to fit (inclusive)
     * @return status code
     */
    public int fitData(ICurveFitData[] data, int start, int stop);
}
