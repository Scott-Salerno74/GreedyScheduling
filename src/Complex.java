public class Complex {
    private final double re;
    private final double ir;
    public Complex(double r, double r2){
        re = r;
        ir = r2;


    }
    public Complex(){
        re = 0;
        ir = 0;
    }

    public double abs(){
        return Math.hypot(re,ir);
    }

    public Complex times(Complex x){
        Complex y = this;
        double sr = ((y.re * x.re)- (y.ir*x.ir));
        double lr = ((y.re*x.ir)+(y.ir*x.re));
        return new Complex(sr,lr);
    }

    public Complex plus(Complex y){
        Complex x = this;
        double sr = (x.re+y.re);
        double lr = (x.ir+y.ir);
        return new Complex(sr,lr);
    }

}
