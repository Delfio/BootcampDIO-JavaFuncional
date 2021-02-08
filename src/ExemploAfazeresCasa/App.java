package ExemploAfazeresCasa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
// import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class App {
    private static final ExecutorService pessoasParaExecutarMinhasTarefas = Executors.newFixedThreadPool(5);

    public static void main(String[] args) throws InterruptedException {
        Casa casa = new Casa(new Quarto());

        // Só mandando executar sem obter a resposta de cada thread
        // casa.obterAfazeresDaCasa()
        // .forEach(atividade -> pessoasParaExecutarMinhasTarefas.execute(() ->
        // atividade.realizar()));

        // Mandando executar e escutando o retorno de cada thread
        List<? extends Future<String>> futuros = casa.obterAfazeresDaCasa()
            .stream()
            .map(atividade -> pessoasParaExecutarMinhasTarefas.submit(atividade::realizar))
            .collect(Collectors.toList());

        /**
         * Por padrão o List não é thread safe. Por tanto posso obter erros de concorrência.
        List<? extends Future<?>> futuros = casa.obterAfazeresDaCasa()
            .stream()
            .map(atividade -> pessoasParaExecutarMinhasTarefas.submit(atividade::realizar))
            .collect(Collectors.toList());
         * 
         * Exemplo: percorrendo a lista de excluindo ela da lista de execução -> Temos um problema de concorrência.
         * Há outra implementação que segue um modelo thread safe
         * Este modelo se encarrega de gerenciar automáticamente as concorrências.
        List<? extends Future<?>> futuros = new CopyOnWriteArrayList<>(
            casa.obterAfazeresDaCasa()
                .stream()
                .map(atividade -> pessoasParaExecutarMinhasTarefas.submit(atividade::realizar))
                .collect(Collectors.toList())
        );
         * 
         */
        int numeroDeFuturosNaoConcluidos = futuros.size();

        try {
            while (true) {
                List<? extends Future<?>> futurosTerminados = futuros.stream()
                    .filter(futuro -> {
                        if (futuro.isDone()) {
                            try {
                                System.out.println("Parabéns uma tarefa foi concluida - " + futuro.get());
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }

                        return futuro.isDone();
                    })
                    .collect(Collectors.toList());

                if(futurosTerminados.size() > 0) {
                    futuros.removeIf(futuro -> {
                        boolean veradadeiro = futurosTerminados
                            .stream()
                            .filter(futuroTerminado -> futuroTerminado.equals(futuro))
                            .count() > 0;

                        return veradadeiro;
                    });

                    numeroDeFuturosNaoConcluidos -= futurosTerminados.size();
                }

                System.out.println("Ainda restão " + numeroDeFuturosNaoConcluidos + " atividades para serem finalizadas");

                if(numeroDeFuturosNaoConcluidos == 0) System.out.println("Uhuul!");

                if(futuros.stream().allMatch(Future::isDone)) break;
                
                Thread.sleep(500);

            };
        } finally {
            if (!pessoasParaExecutarMinhasTarefas.isShutdown()) {
                pessoasParaExecutarMinhasTarefas.shutdown();
            }
        }

    }
}

interface Atividade {
    String realizar();
}

class Casa {
    private List<Comodo> comodos;

    public Casa(Comodo... comodos) {
        this.comodos = Arrays.asList(comodos);
    }

    public List<Atividade> obterAfazeresDaCasa() {
        return this.comodos.stream().map(Comodo::obterAfazeresDoComodo).reduce(new ArrayList<Atividade>(),
                (pivo, atividades) -> {
                    pivo.addAll(atividades);
                    return pivo;
                });
    }
}

abstract class Comodo {
    abstract List<Atividade> obterAfazeresDoComodo();
}

class Quarto extends Comodo {
    @Override
    List<Atividade> obterAfazeresDoComodo() {

        /**
         * Mesma implementação de forma procedural: ArrayList<Atividade> objcts = new
         * ArrayList<>(); objcts.add(this::arrumarACama); objcts.add(() ->
         * this.varrerOQuarto()); ...
         */

        return Arrays.asList(this::arrumarACama, this::varrerOQuarto, this::arrumarGuardaRoupa);
    };

    private String arrumarGuardaRoupa() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "Arrumar o guarda roupa";
    }

    private String varrerOQuarto() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "Varrer o quarto";
    }

    private String arrumarACama() {
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "Arrumar a cama";
    }
}