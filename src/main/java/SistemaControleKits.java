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

        public void setCpf(String cpf) {
            this.cpf = cpf;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        @Override
        public String toString() {
            return "Nome: " + nome + " | CPF: " + cpf;
        }
    }

    public static boolean validarCpf(String cpf) {
         String cpfNumerico = cpf.replaceAll("\\D", "");
            return cpfNumerico.length() == 11;
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

        public void setCodigo(String codigo) {
            this.codigo = codigo;
        }

        public String getDescricao() {
            return descricao;
        }

        public void setDescricao(String descricao) {
            this.descricao = descricao;
        }

        public boolean isDisponivel() {
            return disponivel;
        }

        public void setDisponivel(boolean disponivel) {
            this.disponivel = disponivel;
        }

        @Override
        public String toString() {
            return "Kit: " + descricao + " | Código: " + codigo;
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

public class Continuacao {
    public static boolean desejaContinuar(Scanner scanner, String mensagem) {
        System.out.print(mensagem + " (s/n): ");
        String resposta = scanner.nextLine().trim().toLowerCase();
        return resposta.equals("s") || resposta.equals("sim");
    }
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
            System.out.println("2. Listar Professor");
            System.out.println("3. Editar Professor");
            System.out.println("4. Remover Professor");
            System.out.println("5. Cadastrar Kit");
            System.out.println("6. Listar Kit");
            System.out.println("7. Editar Kit");
            System.out.println("8. Remover Kit");
            System.out.println("9. Registrar Retirada de Kit");
            System.out.println("10. Registrar Devolução de Kit");
            System.out.println("11. Ver Histórico");
            System.out.println("12. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine(); // consumir a quebra de linha

            switch (opcao) {
                case 1:
                    cadastrarProfessor();
                    break;
                case 2:
                    listarProfessor();
                    break;  
                case 3:
                    editarProfessor();
                    break; 
                case 4:
                    removerProfessor();
                    break;          
                case 5:
                    cadastrarKit();
                    break;
                case 6:
                    listarKit();
                    break;    
                case 7:
                    editarKit();
                    break;   
                case 8:
                    removerKit();
                    break;       
                case 9:
                    registrarRetirada();
                    break;
                case 10:
                    registrarDevolucao();
                    break;
                case 11:
                    verHistorico();
                    break;    
                case 0:
                    System.out.println("Saindo do sistema...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } while (opcao != 12);
    }

    private void cadastrarProfessor() {

        do { 
            System.out.println("=== Cadastro de Professores ===");

        String nome;
        while(true) {
            System.out.print("Nome: ");
           nome = scanner.nextLine();

           if(nome.matches("[a-zA-zÀ-ÿ\\s]+")) {
            break;
           } else {
            System.out.println("Nome inválido !");
           }
        }
        
        System.out.print("CPF: ");
        String cpf;
        do { 
            cpf = scanner.nextLine();
            if (!validarCpf(cpf)) {
                System.out.println("CPF inválido !");
                System.out.print("CPF: ");
            }
        } while (!validarCpf(cpf));

        professores.add(new Professor(cpf, nome));
        historico.add(new AcaoRegistro("Professor cadastrado: " + nome));
        System.out.println("Professor cadastrado com sucesso!");

        } while (AcaoRegistro.Continuacao.desejaContinuar(scanner, "Deseja cadastrar mais professores?"));

        System.out.println("Cadastro finalizado!");
    }

     private void listarProfessor() {

        if (professores.isEmpty()) {
            System.out.println("Nenhum professor cadastrado.");
            return;
        }
        System.out.println("\n=== Lista de Professores ===");
        for (int i = 0; i < professores.size(); i++) {
            System.out.println((i + 1) + " - " + professores.get(i));
        }
    }

     private void editarProfessor() {

        do { 
            System.out.println("=== Editar Professor ===");
        listarProfessor();
        if (professores.isEmpty()) {
            System.out.println("Nenhum professor cadastrado.");
            return;
        } 

        System.out.print("Digite o CPF do professor para editar: ");
        String cpf = scanner.nextLine();

        Professor professor = null;
        for (Professor p: professores) {
            if(p.getCpf().equals(cpf)) {
                professor = p;
                break;
            }
        }

        if (professor == null) {
            System.out.println("CPF inválido.");
            return;
        }

        System.out.print("Novo nome: ");
        String novoNome = scanner.nextLine();
        professor.setNome(novoNome);

        System.out.print("Novo CPF: ");
        String novoCpf = scanner.nextLine();
        professor.setCpf(novoCpf);

        System.out.println("Professor atualizado!");
        historico.add(new AcaoRegistro("Professor editado: " + novoNome));
        } while (AcaoRegistro.Continuacao.desejaContinuar(scanner, "Deseja editar mais algum professor?"));

        System.out.println("Edição realizada!");

        
    }

     private void removerProfessor() {

        do { 
            System.out.println("=== Remover Professor ===");
        listarProfessor();
        if (professores.isEmpty()) return;

        System.out.print("Digite o CPF do professor para remover: ");
        String cpf = scanner.nextLine();

        Professor professorRemover = null;
        for (Professor p: professores) {
            if(p.getCpf().equals(cpf)) {
                professorRemover = p;
                break;
            }
        }

        if (professorRemover == null) {
            System.out.println("CPF inválido.");
            return;
        }

        professores.remove(professorRemover);
        System.out.println("Professor removido!");
        historico.add(new AcaoRegistro("Professor removido: " + professorRemover));
        } while (AcaoRegistro.Continuacao.desejaContinuar(scanner, "Deseja remover mais algum professor?"));

        System.out.println("Remoção realizada!");
    }

    private void cadastrarKit() {

        do {
            System.out.println("=== Cadastro de Kits ===");

        System.out.print("Código do Kit: ");
        String codigo = scanner.nextLine();
        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();

        kits.add(new Kit(codigo, descricao));
        historico.add(new AcaoRegistro("Kit " + descricao + " cadastrado: " + codigo));
        System.out.println("Kit cadastrado com sucesso!");

        } while (AcaoRegistro.Continuacao.desejaContinuar(scanner, "Deseja cadastrar mais algum kit?"));

        System.out.println("Cadastro finalizado !");
    }

    private void listarKit() {
        if (kits.isEmpty()) {
            System.out.println("Nenhum kit cadastrado.");
            return;
        }
        System.out.println("\n=== Lista de Kits ===");
        for (int i = 0; i < kits.size(); i++) {
            System.out.println((i + 1) + " - " + kits.get(i));
        }
    }

    private void editarKit() {

        do { 
            System.out.println("=== Editar Kit ===");
        listarKit();
        if (kits.isEmpty()) {
            System.out.println("Nenhum Kit cadastrado.");
            return;
        } 

        System.out.print("Digite o código do kit para editar: ");
        String codigo = scanner.nextLine();

        Kit kit = null;
        for(Kit k: kits) {
            if(k.getCodigo().equals(codigo)) {
                kit = k;
                break;
            }
        }

        if (kit == null) {
            System.out.println("Código Inválido.");
            return;
        }

        System.out.print("Novo kit: ");
        String novoKit = scanner.nextLine();
        kit.setDescricao(novoKit);

        System.out.print("Novo código: ");
        String novoCodigo = scanner.nextLine();
        kit.setCodigo(novoCodigo);

        System.out.println("Kit atualizado!");
        historico.add(new AcaoRegistro("Kit editado: " + novoKit));
        } while (AcaoRegistro.Continuacao.desejaContinuar(scanner, "Deseja editar mais algum kit?"));

        System.out.println("Edição realizada!");
    }

    private void removerKit() {

        do { 
            System.out.println("=== Remover Kit ===");
        listarKit();
        if (kits.isEmpty()) return;

        System.out.print("Digite o código do kit para remover: ");
        String codigo = scanner.nextLine();

        Kit kitRemover = null;
        for (Kit k: kits) {
            if(k.getCodigo().equals(codigo)) {
                kitRemover = k;
                break;
            }
        }

        if (kitRemover == null) {
            System.out.println("Kit inválido.");
            return;
        }

        kits.remove(kitRemover);
        System.out.println("Kit removido!");
        historico.add(new AcaoRegistro("Kit removido: " + kitRemover));
        } while (AcaoRegistro.Continuacao.desejaContinuar(scanner, "Deseja remover mais algum kit?"));

        System.out.println("Remoção realizada!");
        
    }


    private void registrarRetirada() {

        do { 
            System.out.println("=== Registro de Retirada de Kit ===");
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
            
        } while (AcaoRegistro.Continuacao.desejaContinuar(scanner, "Deseja registrar mais alguma retirada de kit?"));

        System.out.println("Registro realizado!");

        
    }

    private void registrarDevolucao() {

    do { 
        System.out.println("=== Registro de Devolução de Kit ===");
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
    } while (AcaoRegistro.Continuacao.desejaContinuar(scanner, "Deseja registrar mais alguma devolução de kit?"));

    System.out.println("Registro realizado!");

}

private void verHistorico() {
    System.out.println("\nHistórico de Ações:");
    for (AcaoRegistro acao : historico) {
        System.out.println(acao);
    }
}

}
