package loci.curvefitter;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Interface to the curve fitting data.
 *
 * @author Aivar Grislis
 */
//TODO fix params
//TODO num components?
//TODO SP has int for y data, x c/b int too; double m/b useful for advanced binning
public interface ICurveFitData {

    /**
     * Gets parameters of the fit.  Could represent multiple components.
     * Input and output to the fit.
     *
     * @return array of parameters
     */
    public double[] getParams();

    /**
     * Sets parameters of the fit.  Could represent multiple components.
     * Input and output to the fit.
     *
     * @param params array of parameters
     */
    public void setParams(double[] params);

    /**
     * Gets which parameters are free (vs. fixed). Input to fit only.
     *
     * @return array of booleans
     */
    public boolean[] getFree();

    /**
     * Sets which parameters are free (vs. fixed).  Input to fit only.
     */
    public void setFree(boolean[] free);

    /**
     * Get input data for the fit.  Input to fit only.
     *
     * @return array of data
     */
    public double[] getYData();

    /**
     * Set input data for the fit.  Input to fit only.
     *
     * @param yData array of data
     */
    public void setYData(double yData[]);
    
    /**
     * Gets fitted data from the fit.  Output from fit only.
     *
     * @return array of fitted data
     */
    public double[] getYFitted(); //TODO is this efficient for OpenCL???? s/b asynchronous, don't wait for results???

    /**
     * Sets fitted data from the fit.  Output from fit only.
     *
     * @param yFit array of fitted data
     */
    public void setYFitted(double yFit[]);

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

}
