package ExemploUtilizandoParallelStreams;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class App {

    static final int _NUMERO = 100000;

    public static void main(String[] args) {
        // Se atentar que o println reduz a performance
        ExecutarParallelStream();
        ExecucaoIntStream();
        // ExemploAplicado();
    }

    // Utilizado normalmente para operações que não dependam de ordem de execução
    // Separa os processos automáticamente por váris threads e trabalha de forma
    // concorrente.
    public static void ExecutarParallelStream() {
        long inicio = System.currentTimeMillis();

        IntStream.range(1, _NUMERO).parallel().forEach(number -> fatorial(number));

        long fim = System.currentTimeMillis();
        System.out.println("tempo de execução ParallelStream = " + (fim - inicio));

    }

    public static void ExecucaoIntStream() {
        long inicio = System.currentTimeMillis();

        IntStream.range(1, _NUMERO).forEach(number -> fatorial(number));

        long fim = System.currentTimeMillis();
        System.out.println("tempo de execução = " + (fim - inicio));

    }

    public static void ExemploAplicado() {
        List<String> LinguagensDeProgramacao = Arrays.asList("Javascript", "Rust", "Ada", "C");
        LinguagensDeProgramacao.parallelStream().parallel().forEach(ln -> AprenderUmaLinguagemNova(ln));
    }

    public static long fatorial(long num) {
        long fat = 1;

        for (int i = 2; i <= num; i++) {
            fat *= i;
        }
        ;

        return fat;
    };

    public static void AprenderUmaLinguagemNova(String linguagem) {
        System.out.println("Aprendendo " + linguagem);
        for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep(3500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.print('.');
        };

        System.out.println();
        System.out.println(linguagem + " Aprendida :)");
    }
}
