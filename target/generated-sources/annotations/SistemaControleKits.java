import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class SistemaControleKits {

    static class Professor {
        private String cpf;
        private String nome;

        public Professor(String cpf, String nome) {
            this.cpf = cpf;
            this.nome = nome;
        }

        public String getCpf() {
            return cpf;
        }

        public String getNome() {
            return nome;
        }
    }

    static class Kit {
        private String codigo;
        private String descricao;
        private boolean disponivel = true;

        public Kit(String codigo, String descricao) {
            this.codigo = codigo;
            this.descricao = descricao;
        }

        public String getCodigo() {
            return codigo;
        }

        public String getDescricao() {
            return descricao;
        }

        public boolean isDisponivel() {
            return disponivel;
        }

        public void setDisponivel(boolean disponivel) {
            this.disponivel = disponivel;
        }
    }

    static class Registro {
        private Professor professor;
        private Kit kit;
        private String descricao;

        public Registro(Professor professor, Kit kit) {
            this.professor = professor;
            this.kit = kit;
            this.descricao = "Retirada: " + professor.getNome() + " retirou " + kit.getDescricao();
        }

        public Professor getProfessor() {
            return professor;
        }

        public Kit getKit() {
            return kit;
        }

        public Registro(String descricao) {
            this.descricao = descricao;
        }

        @Override
        public String toString() {
            return descricao;
        }
    }

    static class AcaoRegistro {
    private Professor professor;
    private Kit kit;
    private String tipo; 
    private LocalDateTime dataHora;

    public AcaoRegistro(String descricao) {
        this.professor = null;
        this.kit = null;
        this.tipo = descricao;
        this.dataHora = LocalDateTime.now();
    }

    public AcaoRegistro(Professor professor, Kit kit, String tipo) {
        this.professor = professor;
        this.kit = kit;
        this.tipo = tipo;
        this.dataHora = LocalDateTime.now();
    }

    @Override
public String toString() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    if (professor == null && kit == null) {
        return tipo + " - " + dataHora.format(formatter);
    }
    return tipo + ": " + professor.getNome() + " - Kit " + kit.getDescricao() +
           " - " + dataHora.format(formatter);
}
}

    private List<Professor> professores = new ArrayList<>();
    private List<Kit> kits = new ArrayList<>();
    private List<AcaoRegistro> historico = new ArrayList<>();

    private Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        SistemaControleKits sistema = new SistemaControleKits();
        sistema.run();
    }

    private void run() {
        int opcao;
        do {
            System.out.println("\nMenu:");
            System.out.println("1. Cadastrar Professor");
            System.out.println("2. Cadastrar Kit");
            System.out.println("3. Registrar Retirada de Kit");
            System.out.println("4. Registrar Devolução de Kit");
            System.out.println("5. Ver Histórico");
            System.out.println("6. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine(); // consumir a quebra de linha

            switch (opcao) {
                case 1:
                    cadastrarProfessor();
                    break;
                case 2:
                    cadastrarKit();
                    break;
                case 3:
                    registrarRetirada();
                    break;
                case 4:
                    registrarDevolucao();
                    break;
                case 5:
                    verHistorico();
                    break;    
                case 6:
                    System.out.println("Saindo do sistema...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } while (opcao != 6);
    }

    private void cadastrarProfessor() {
        System.out.print("CPF: ");
        String cpf = scanner.nextLine();
        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        professores.add(new Professor(cpf, nome));
        historico.add(new AcaoRegistro("Professor cadastrado: " + nome));
        System.out.println("Professor cadastrado com sucesso!");
    }

    private void cadastrarKit() {
        System.out.print("Código do Kit: ");
        String codigo = scanner.nextLine();
        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();

        kits.add(new Kit(codigo, descricao));
        historico.add(new AcaoRegistro("Kit cadastrado: " + codigo));
        System.out.println("Kit cadastrado com sucesso!");
    }

    private void registrarRetirada() {
        System.out.print("CPF do Professor: ");
        String cpf = scanner.nextLine();
        Optional<Professor> professor = professores.stream()
                .filter(p -> p.getCpf().equals(cpf))
                .findFirst();

        if (professor.isEmpty()) {
            System.out.println("Professor não encontrado!");
            return;
        }

        System.out.print("Código do Kit: ");
        String codigo = scanner.nextLine();

        Optional<Kit> kit = kits.stream()
                .filter(k -> k.getCodigo().equalsIgnoreCase(codigo) && k.isDisponivel())
                .findFirst();

        if (kit.isEmpty()) {
            System.out.println("Kit não disponível ou não encontrado!");
            return;
        }

        kit.get().setDisponivel(false);
        historico.add(new AcaoRegistro(professor.get(), kit.get(), "Retirada"));
        System.out.println("Retirada registrada com sucesso!");
    }

    private void registrarDevolucao() {
    System.out.print("CPF do Professor: ");
    String cpf = scanner.nextLine();
    Optional<Professor> professor = professores.stream()
            .filter(p -> p.getCpf().equals(cpf))
            .findFirst();

    if (professor.isEmpty()) {
        System.out.println("Professor não encontrado!");
        return;
    }

    System.out.print("Código do Kit: ");
    String codigo = scanner.nextLine();

    Optional<Kit> kit = kits.stream()
            .filter(k -> k.getCodigo().equalsIgnoreCase(codigo))
            .findFirst();

    if (kit.isEmpty()) {
        System.out.println("Kit não encontrado!");
        return;
    }

    kit.get().setDisponivel(true);
    historico.add(new AcaoRegistro(professor.get(), kit.get(), "Devolução"));
    System.out.println("Devolução registrada com sucesso!");
}

private void verHistorico() {
    System.out.println("\nHistórico de Ações:");
    for (AcaoRegistro acao : historico) {
        System.out.println(acao);
    }
}

}
