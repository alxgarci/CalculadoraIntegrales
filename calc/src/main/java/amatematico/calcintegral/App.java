package amatematico.calcintegral;

import java.text.DecimalFormat;
import java.util.Scanner;
import amatematico.calcintegral.obj.Integral;
import org.mariuszgromada.math.mxparser.*;

public class App 
{
    static final String OPCION_RECTANGULOS = "R";
    static final String OPCION_EXACTA = "E";
    static final String OPCION_SALIR = "S";
    static final String OPCION_DOC = "DOC";
    static final double MIN2_NOCERO = 0.000001;
    static Scanner sc;
    public static void main( String[] args )
    {

        System.out.println("[S] INICIANDO CALCULADORA . . .");
        System.out.println("Esta aplicacion usa la libreria mXparser, para mas informacion mira la seccion DOC");
        sc = new Scanner(System.in);

        String opcion = "";
       
        while (!opcion.equals(OPCION_SALIR)) {

            opcion = getOpcion();
            
            switch (opcion) {
                case OPCION_RECTANGULOS:
                    resolverRectangulos();
                    break;

                case OPCION_EXACTA:
                    resolverExacta();
                    break;

                case OPCION_DOC:
                    System.out.println("--------------------------------------------------------------------------------");
                    System.out.println("----------------------------- LICENCIA mXparser --------------------------------");
                    System.out.println("--------------------------------------------------------------------------------");
                    mXparser.consolePrint(mXparser.LICENSE);
                    System.out.println("--------------------------------------------------------------------------------");
                    System.out.println("---------------------------- GRUPO A. MATEMATICO -------------------------------");
                    System.out.println("--------------------------------------------------------------------------------");
                    System.out.println("Trabajo realizado por . . . . . . . . . .");
                    break;
            
                default:
                    break;
            }
        }
    }

    private static void resolverExacta() {
        Integral integral = readIntegral();
        String fncX = integral.getParteIntegral().replaceAll("dx", "1");
        String eString = "int(" + fncX + ", x, " + checkCero(integral.getExtremoIzqdo()) + ", " + checkCero(integral.getExtremoDcho()) + ")";
        Expression e = new Expression(eString);
        System.out.println("[RESULTADO INTEGRAL] '"+ e.getExpressionString() + "': "+ e.calculate());
    }

    private static double checkCero(double d) {
        if (d == 0) {
            return MIN2_NOCERO;
        } else {
            return d;
        }
    }

    private static void resolverRectangulos() {
        //Pedimos los datos de la integral, f(x) y extremos
        System.out.println("\n[INFO] Introduccion correcta de f(x)" +
        "\n  Para introducir la funcion, debes tener en cuenta la nomenclatura" +
        "\n  estandar (sin,cos,...), la incognita debe ser 'x' y sin incluir" +
        "\n  extras como extremos, que se solicitaran mas tarde. Las raices" +
        "\n  se representan como elevados a una fraccion '^(1/n)'");
        
        Integral integral = readIntegral();
        
        //Inicializamos el resultado de f(x)
        double fx = 0;

        //Inicializamos las variables fuera del bucle
        double ancho, area;
        double aTotal = 0;

        //Argument sera la clase que evalue la X a sustituir en la expresion introducida,
        //Expression sera la clase que nos 'evalue' la expresion introducida, sustituyendo
        //en ella la 'x' por el valor especificado.
        Argument x;
        Expression e;

        System.out.println("[S] Introduce el numero de rectangulos para utilizar:");
        int nRectangulos = sc.nextInt();
        sc.nextLine();

        System.out.println("[S] CALCULANDO LA INTEGRAL '" + integral.getParteIntegral() + "' (METODO DE LOS RECTANGULOS)");

        //Sacamos el ancho de base de cada rectangulo, que seria igual al extremo derecho
        //menos el extremo izquiero dividido entre el numero de rectangulos
        ancho = (integral.getExtremoDcho()-integral.getExtremoIzqdo())/nRectangulos;

        //Sustituimos 'dx' por '1' para introducirlo en el compilador, en caso de que se
        //haya introducido, para evitar errores
        String fncX = integral.getParteIntegral().replaceAll("dx", "1");
        
        for (int i = 1;i <= nRectangulos;i++)
        {
            //Con este metodo, si el extremo izquierdo es 0, lo aproximamos al siguiente valor que no es 0,
            //un valor minimo para poder realizar ciertas operaciones como aproximar la division entre 0,
            //donde la funcion no es continua.
            if (i == 1 && integral.getExtremoIzqdo() == 0) {
                x = new Argument("x", MIN2_NOCERO);
            } else {
                /*
                 * Establecemos el valor de la x para mas tarde poder calcular la altura (f(x)).
                 * Para cualquier rectangulo, tenemos que sacar la altura (= f(x)). Para saber el
                 * valor de f(x) para el 'Z' rectangulo, por ejemplo, tendriamos que despejar en la funcion
                 * la x con la distancia que llevamos recorrida del eje 'x', que sera igual al numero del intervalo
                 * en que nos encontremos multiplicado por el ancho de cada intervalo. Esta seria la distancia
                 * que nos encontramos respecto del extremo izqdo, luego tendremos que sumarselo al extremo izqdo
                 * que se ha especificado.
                 */
                
                x = new Argument("x", integral.getExtremoIzqdo() + (i-1)*ancho); 
            }
            
            //Establecemos la variable "x" para sustituir en f(x)
            e = new Expression(fncX, x);

            //Evaluamos (calculamos), f(x) con la x sustituida con el valor establecido previamente
            //para hallar la altura del rectangulo
            fx = e.calculate();

            //Sacamos el area del rectangulo actual 'i', que seria base (ancho previamente calculado) por altura (f(x))
            area = ancho*fx;

            //Mostramos en pantalla el valor de f(x) (altura) y el area del rectangulo numero 'i'
            DecimalFormat decimalFormat = new DecimalFormat("###0.0000");
            DecimalFormat decimalFormat2 = new DecimalFormat("###0.0000000000");
            DecimalFormat decimalFormat3 = new DecimalFormat("###000");
            System.out.println("[INTEGRAL R" + decimalFormat3.format(i) +"] x=" + decimalFormat.format(x.getArgumentValue()) + " f(x)=" + decimalFormat2.format(fx) + " area=" + decimalFormat2.format(area));

            //Finalmente sumamos el area del rectangulo al total ya sumado si es numero, si es NaN (Not A Number) se trata
            //de un valor que no existe, por lo tanto el valor no existe en ese punto, no se suma nada.
            if (!Double.isNaN(area)) {
                aTotal += area; 
            }

            //Simplemente un medidor de progreso, (actual / total).
            System.out.print("(" + i + "/" + nRectangulos + ")\r");
        }

        System.out.println("\n[S] Resultado de "+ integral.toString() + ":");
        if (aTotal == 0.0) {
            System.out.println("[ERROR] Error a la hora de calcular f(x), verifica la funcion introducida.");
        } else {
            System.out.println("[RESULTADO INTEGRAL] " + aTotal);
        }
        

    }

    private static Integral readIntegral() {
        Integral res = new Integral();
        System.out.println("\n[S] Introduce la f(x) de la integral:");
        res.setParteIntegral(sc.nextLine());
        System.out.println("[S] Introduce el extremo derecho:");
        res.setExtremoDcho(sc.nextDouble());
        System.out.println("[S] Introduce el extremo izquierdo:");
        res.setExtremoIzqdo(sc.nextDouble());
        sc.nextLine();
        
        return res;
    }

    private static String getOpcion() {
        System.out.println("\n[MENU] Selecciona una opcion:");
        System.out.println("  " + OPCION_RECTANGULOS + " - Calcular integral por metodo del rectangulo");
        System.out.println("  " + OPCION_EXACTA + " - Calcular integral exacta");
        System.out.println("  " + OPCION_DOC + " - Documentacion y licencia mXparser");
        System.out.println("  " + OPCION_SALIR + " - Salir de la aplicacion");
        String res = sc.nextLine().toUpperCase();
        return res;
    }
}
