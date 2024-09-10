package com.observatudo.backend.service;

import com.observatudo.backend.domain.model.Eixo;
import com.observatudo.backend.domain.model.EixoPadrao;
import com.observatudo.backend.domain.model.EixoUsuario;
import com.observatudo.backend.domain.model.Indicador;
import com.observatudo.backend.domain.model.IndicadorId;
import com.observatudo.backend.domain.model.ValorIndicador;
import com.observatudo.backend.domain.repository.EixoPadraoRepository;
import com.observatudo.backend.domain.repository.EixoUsuarioRepository;
import com.observatudo.backend.domain.repository.IndicadorRepository;
import com.observatudo.backend.domain.repository.ValorIndicadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IndicadorService {

    private final IndicadorRepository indicadorRepository;
    private final ValorIndicadorRepository valorIndicadorRepository;
    private final EixoUsuarioRepository eixoUsuarioRepository;
    private final EixoPadraoRepository eixoPadraoRepository;

    @Autowired
    public IndicadorService(IndicadorRepository indicadorRepository,
                            ValorIndicadorRepository valorIndicadorRepository,
                            EixoUsuarioRepository eixoUsuarioRepository,
                            EixoPadraoRepository eixoPadraoRepository) {
        this.indicadorRepository = indicadorRepository;
        this.valorIndicadorRepository = valorIndicadorRepository;
        this.eixoUsuarioRepository = eixoUsuarioRepository;
        this.eixoPadraoRepository = eixoPadraoRepository;
    }

    public Eixo getEixoByUsuarioId(Long usuarioId) {
        Optional<EixoUsuario> optionalEixoUsuario = eixoUsuarioRepository.findByUsuarioId(usuarioId);
        if (optionalEixoUsuario.isPresent()) {
            return optionalEixoUsuario.get().getEixo();
        } else {
            return eixoPadraoRepository.findSingletonEixoPadrao()
                    .map(EixoPadrao::getEixo)
                    .orElseThrow(() -> new RuntimeException("Eixo padrão não encontrado"));
        }
    }

    public List<Indicador> listarIndicadores() {
        return indicadorRepository.findAll();
    }

    public List<Indicador> findByLocalidadeCodigo(Integer codigoLocalidade) {
        List<ValorIndicador> valorIndicadores = valorIndicadorRepository.findByLocalidadeCodigo(codigoLocalidade);
        return valorIndicadores.stream()
                .map(ValorIndicador::getIndicador)
                .collect(Collectors.toList());
    }

    public Indicador findByNome(String nomeIndicador) {
        return indicadorRepository.findByNome(nomeIndicador);
    }

    public void associarIndicadorAoEixo(Integer fonteId, String codIndicador, Long usuarioId) {
        IndicadorId indicadorId = new IndicadorId(fonteId, codIndicador);
        Indicador indicador = indicadorRepository.findById(indicadorId)
                .orElseThrow(() -> new EntityNotFoundException("Indicador não encontrado"));

        Eixo eixo = eixoUsuarioRepository.findByUsuarioId(usuarioId)
                .map(EixoUsuario::getEixo)
                .orElseGet(() -> eixoPadraoRepository.findSingletonEixoPadrao()
                        .map(EixoPadrao::getEixo)
                        .orElseThrow(() -> new RuntimeException("Eixo padrão não encontrado")));

        if (!eixo.getIndicadores().contains(indicador)) {
            eixo.getIndicadores().add(indicador);
            if (eixo instanceof EixoUsuario) {
                eixoUsuarioRepository.save((EixoUsuario) eixo);
            } else if (eixo instanceof EixoPadrao) {
                eixoPadraoRepository.save((EixoPadrao) eixo);
            }
        }
    }

    public void setEixoParaIndicador(Integer fonteId, String codIndicador, Long usuarioId, Eixo eixoSelecionado) {
        IndicadorId indicadorId = new IndicadorId(fonteId, codIndicador);
        Indicador indicador = indicadorRepository.findById(indicadorId)
                .orElseThrow(() -> new EntityNotFoundException("Indicador não encontrado"));

        Eixo eixo = eixoUsuarioRepository.findByUsuarioId(usuarioId)
                .map(EixoUsuario::getEixo)
                .orElseGet(() -> eixoPadraoRepository.findAll().stream()
                        .filter(eixoPadrao -> eixoPadrao.getEixo().equals(eixoSelecionado))
                        .findFirst()
                        .orElseThrow(() -> new EntityNotFoundException("Eixo padrão não encontrado para o indicador.")));

        associarIndicadorAoEixo(fonteId, codIndicador, usuarioId);
    }

    public void desassociarIndicadorDoEixo(Integer fonteId, String codIndicador) {
        IndicadorId indicadorId = new IndicadorId(fonteId, codIndicador);
        Indicador indicador = indicadorRepository.findById(indicadorId)
                .orElseThrow(() -> new EntityNotFoundException("Indicador não encontrado"));

        List<Eixo> todosEixos = eixoUsuarioRepository.findAll().stream()
                .map(EixoUsuario::getEixo)
                .collect(Collectors.toList());
        todosEixos.addAll(eixoPadraoRepository.findAll().stream()
                .map(EixoPadrao::getEixo)
                .collect(Collectors.toList()));

        for (Eixo eixo : todosEixos) {
            eixo.getIndicadores().remove(indicador);
            if (eixo instanceof EixoUsuario) {
                eixoUsuarioRepository.save((EixoUsuario) eixo);
            } else if (eixo instanceof EixoPadrao) {
                eixoPadraoRepository.save((EixoPadrao) eixo);
            }
        }
    }
}
