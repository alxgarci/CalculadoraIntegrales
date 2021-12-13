package amatematico.calcintegral.obj;

public class Integral {
    private String parteIntegral;
    private double extremoDcho;
    private double extremoIzqdo;

    public Integral() {
    }

    public Integral(String parteIntegral, double extremoDcho, double extremoIzqdo) {
        this.parteIntegral = parteIntegral;
        this.extremoDcho = extremoDcho;
        this.extremoIzqdo = extremoIzqdo;
    }


    public String getParteIntegral() {
        return this.parteIntegral;
    }

    public void setParteIntegral(String parteIntegral) {
        this.parteIntegral = parteIntegral;
    }

    public double getExtremoDcho() {
        return this.extremoDcho;
    }

    public void setExtremoDcho(double extremoDcho) {
       
        this.extremoDcho = extremoDcho;
    }    

    public double getExtremoIzqdo() {
        return this.extremoIzqdo;
    }

    public void setExtremoIzqdo(double extremoIzqdo) {
        this.extremoIzqdo = extremoIzqdo;
    }

    @Override
    public String toString() {
        return "'" + getParteIntegral() + "'. Extremo derecho " + getExtremoDcho() + 
            " y extremo izquierdo " + getExtremoIzqdo();
    }

    
}
