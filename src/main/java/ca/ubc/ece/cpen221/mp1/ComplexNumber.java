package ca.ubc.ece.cpen221.mp1;

public class ComplexNumber {

    public double rval;
    public double ival;

    /**
     * @param rval real val of new complex number
     * @param ival imaginary val of complex num. assume these values have 'i' tacked onto them
     */
    public ComplexNumber(double rval, double ival){
        this.rval = rval;
        this.ival = ival;
    }

    /**
     * @param C1 Complex num 1
     * @return the Sum of the real and imaginary components in a new complex number.
     */
    public ComplexNumber Sum(ComplexNumber C1){
        double nrval = C1.rval + this.rval;
        double nival = C1.ival + this.ival;

        ComplexNumber result = new ComplexNumber(nrval, nival);
        return result;
    }

    /**
     * @return the magnitude of the real and imaginary components together.
     */
    public double Magnitude(){
        double result = Math.sqrt(Math.pow(this.ival, 2) + Math.pow(this.rval, 2));
        return result;
    }
}
