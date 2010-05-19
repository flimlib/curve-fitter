/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import cern.colt.matrix.DoubleFactory1D;
import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;

import org.akutan.optimization.LMObjective;
import org.akutan.optimization.LMSolver;

/**
 *
 * @author Aivar Grislis
 */
public class AkutanCurveFitter extends AbstractCurveFitter {

    /**
     * @inheritDoc
     */
    public int fitData(ICurveFitData[] dataArray, int start, int stop) {
        int length = stop - start + 1;
        DoubleMatrix1D x = new DenseDoubleMatrix1D(length);
        DoubleMatrix1D y = new DenseDoubleMatrix1D(length);
        DoubleMatrix1D yFitted = new DenseDoubleMatrix1D(length);
        LMSolver lmSolver = new LMSolver();
        LMObjective lmObjective = new MyObjective(x, y, yFitted);

        // initialize x array (same for all data)
        double x_value = start * m_xInc;
        for (int i = 0; i < length; ++i) {
            x.set(i, x_value);
            x_value += m_xInc;
        }

        for (ICurveFitData data: dataArray) {
            // initialize y array
            double yData[] = data.getYData();
            for (int i = 0; i < length; ++i) {
                y.set(i, yData[start + i]);
            }

            // find the solution
            DoubleMatrix1D solution = lmSolver.solve(lmObjective, x);

            // return params and yFitted
            double params[] = data.getParams();
            int paramsLength = params.length;
            for (int i = 0; i <  paramsLength; ++i) {
                params[i] = (double) solution.get(i);
            }
            double yFittedX[] = data.getYFitted(); //TODO better name; was 'yFitted', collided with 'yFitted', formerly 'm_yFitted'
            for (int i = 0; i < length; ++i) {
                yFittedX[start + i] = (double) yFitted.get(i);
            }
        }
        return 0;
    }
    
   /**
    * Inner class for LM optimization.
    */
    //TODO the LMObjective class could be passed in to the constructor, for different
    // flavors of curve fit criteria
    static class MyObjective implements LMObjective {
        DoubleMatrix1D m_x;
        DoubleMatrix1D m_y;
        DoubleMatrix1D m_yFitted;
        DoubleMatrix2D m_jacobean;

        /**
         * Constructor
         *
         * @param x
         * @param y
         * @param yFitted
         */
        MyObjective(DoubleMatrix1D x, DoubleMatrix1D y, DoubleMatrix1D yFitted) {
            m_x = x;
            m_y = y;
            m_yFitted = yFitted;
            m_jacobean = new DenseDoubleMatrix2D(5,5);
        }

        /**
         * Computes the value we want to minimize for parameters a.<p>
         * Also computes 'm_jacobean', used by 'gradient()' and 'hessian()' methods.
         *
         * @param a parameters of the fit
         */
        public double value(DoubleMatrix1D a) {
            double a0 = a.get(0);
            double a1 = a.get(1);
            return 0.0;
        }

        /**
         * Computes the gradient of the function at point x for each member of x.
         *
         * Our function is y = (x1 - A1) ^ 2 + (x2 - A2) ^ 3, so the analytic
         * gradient is dy/dx1 = 2 * (x1 - A1) and dy/dx2 = 4 * (x2 - A2) ^ 3
         *
         * @param x
         * @return
         */
        public DoubleMatrix1D gradient(DoubleMatrix1D x) {
            DoubleMatrix1D g = new DenseDoubleMatrix1D(x.size());
            double betaSum;
            for (int j = 0; j < x.size(); ++j) {
                betaSum = 0.0;
                for (int k = 0; k <= m_y.size(); ++k) {
                    betaSum += (m_y.get(k) - m_yFitted.get(k)) * m_jacobean.get(k, j); //TODO s/b weighted by 1.0/(sigma[k]*sigma[k])
                }
                g.set(j, betaSum);
            }
            return g;
        }

        /**
         * Computes an exact or approximated Hessian of the function at x.
         *
         * @param x
         * @return
         */
        public DoubleMatrix2D hessian(DoubleMatrix1D x) {
            DoubleMatrix2D H = new DenseDoubleMatrix2D(x.size(), x.size());
            double dotProduct;
            for (int j = 0; j < x.size(); ++j) {
                for (int i = 0; i <= j; ++i) {
                    dotProduct = 0.0;
                    for (int k = 0; k <= m_y.size(); ++k) {
                        dotProduct += m_jacobean.get(k, i) * m_jacobean.get(k, j); //TODO s/b weighted by 1.0/(sigma[k]*sigma[k])
                    }
                    H.set(i, j, dotProduct);
                    if (i != j) {
                        H.set(j, i, dotProduct);
                    }
                }
            }
            return H;
        }
    }
    /*
			for (j = 0; j < nParams; ++j) {                                       // for all columns
				betaSum = 0.0;
				for (i = 0; i <= j; ++i) {                                        // row loop, need only consider lower triangle
					dotProduct = 0.0;
					for (k = 0; k < nData; ++k) {                                 // for all data
						weight = 1.0 / (sigma[k] * sigma[k]);                       // NOTE inefficiency here!  c/b computed one time only!
						dotProduct += J[k][i] * J[k][j] * weight;                 // get dot product of row i of J^T and col j of J
						if (0 == i) {                                             // once per column for every data index
							yDiff = y[k] - yCalc[k];                              // compute difference between data and model
							betaSum += yDiff * J[k][j] * weight;                 // accumulate approximate gradient
							if (0 == j) {                                         // once per data index
								chiSquare += (yDiff * yDiff) * weight;          // calculate chi square
							}
						}
					}
					alpha[i][j] = dotProduct;
					if (i != j) {
						alpha[j][i] = dotProduct;                                 // matrix is symmetrical
					}
				}
				beta[j] = betaSum;

     */
}
