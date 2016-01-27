package loci.slimCurve;

import org.scijava.ItemIO;
import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.*;

import loci.slim.SLIMCurve;

@Plugin(type = Op.class, name = "RLDFitOp", priority = Priority.LOW_PRIORITY)
public class RLDFitOp extends AbstractOp {
	SLIMCurve slimCurve = new SLIMCurve();

	@Parameter (type = ItemIO.OUTPUT)
	private int out;
	@Parameter 
	private double xInc;
	@Parameter
	private double y[];
	@Parameter
	private int fitStart;
	@Parameter
	private int fitEnd;
	@Parameter
	private double instr[];
	@Parameter
	private int nInstr;
	@Parameter
	private int noise;
	@Parameter
	private double sig[];
	@Parameter
	private double z[];
	@Parameter
	private double a[];
	@Parameter
	private double tau[];
	@Parameter
	private double fitted[];
	@Parameter
	private double chiSquare[];
	@Parameter
	private double chiSquareTarget;


	@Override
	public void run () {
		//Calls into the slimCurve library using JNI
		out = slimCurve.fitRLD(
				xInc,
				y,
				fitStart,
				fitEnd,
				instr,
				nInstr,
				noise,
				sig,
				z,
				a,
				tau,
				fitted,
				chiSquare,
				chiSquareTarget);
	}
	
}


