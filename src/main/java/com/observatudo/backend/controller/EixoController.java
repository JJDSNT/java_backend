@RestController
@RequestMapping("/api/eixos")
public class EixoController {

    private final EixoService eixoService;

    public EixoController(EixoService eixoService) {
        this.eixoService = eixoService;
    }

    // Endpoint para listar todos os eixos
    @GetMapping
    public ResponseEntity<List<EixoDTO>> listarEixos() {
        List<EixoDTO> eixos = eixoService.listarEixos();
        return ResponseEntity.ok(eixos);
    }

    // Endpoint para listar os indicadores por eixo
    @GetMapping("/{id}/indicadores")
    public ResponseEntity<List<IndicadorDTO>> listarIndicadoresPorEixo(@PathVariable Long id) {
        List<IndicadorDTO> indicadores = eixoService.listarIndicadoresPorEixo(id);
        return ResponseEntity.ok(indicadores);
    }
}
