package ExemploGeradorPDF;

public class App {
    public static void main(String[] args) throws Exception {
        GeradorDePdf geradorDePdf = new GeradorDePdf();
        BarraDeProgresso barraDeProgresso = new BarraDeProgresso(geradorDePdf);

        geradorDePdf.start();
        barraDeProgresso.start();

        // for (int i = 0; i < 150; i++) {
        //     Thread.sleep(500);
        //     System.out.println("Executando em paralelo");
        // }

    }
}

class GeradorDePdf extends Thread {
    @Override
    public void run() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.print("PDF GERADO");

        // super.run();
    }
}

class BarraDeProgresso extends Thread {
    private Thread geradorDePDF;
    private int contador = 0;

    public BarraDeProgresso(Thread geradorDePDF) {
        this.geradorDePDF = geradorDePDF;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(500);
                if(!this.geradorDePDF.isAlive()) break;
                for (int i = 0; i < this.contador; i++) {
                    System.out.print('.');
                }
    
                this.contador ++;
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            

        };
    }
}