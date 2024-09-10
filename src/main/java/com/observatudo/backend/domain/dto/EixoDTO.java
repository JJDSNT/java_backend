public class EixoDTO {
    private Long id;
    private String nome;
    private String nomeLegivel;

    public EixoDTO(Eixo eixo) {
        this.id = eixo.getId();
        this.nome = eixo.getNome();
        this.nomeLegivel = eixo.getNomeLegivel();
    }

    // Getters e setters
}
