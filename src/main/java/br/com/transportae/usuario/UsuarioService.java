package br.com.transportae.usuario;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.transportae.auth.AutenticacaoService;
import br.com.transportae.email.EmailService;
import br.com.transportae.endereco.EnderecoModel;
import br.com.transportae.endereco.EnderecoService;
import br.com.transportae.instituicao.InstituicaoModel;
import br.com.transportae.instituicao.InstituicaoService;
import br.com.transportae.usuario.exceptions.UsuarioExistenteException;
import br.com.transportae.usuarioLinha.UsuarioLinhaService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final EnderecoService enderecoService;
    private final InstituicaoService instituicaoService;
    private final UsuarioLinhaService usuarioLinhaService;
    private final AutenticacaoService autenticacaoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public UsuarioDto cadastrarUsuario(UsuarioDto usuarioDto) {
        if (isUsuarioCadastrado(usuarioDto.getEmail(), usuarioDto.getCpf())) {
            throw new UsuarioExistenteException("Usuário já existente");
        }

        UsuarioModel novoUsuario = converterDtoParaDomain(usuarioDto);
        
        vincularEndereco(usuarioDto, novoUsuario);
        vincularInstituicao(usuarioDto, novoUsuario);
        
        novoUsuario.setSenha("*****");

        UsuarioModel usuarioCadastrado = usuarioRepository.save(novoUsuario);
        usuarioLinhaService.vincularUsuarioLinha(usuarioCadastrado, usuarioDto.getLinhaTransporteId());

        return converterDomainParaDto(usuarioCadastrado);
    }

    private void vincularEndereco(UsuarioDto usuarioDto, UsuarioModel usuario) {
        if (Objects.nonNull(usuarioDto.getEndereco())) {
            EnderecoModel endereco = enderecoService.cadastrarEndereco(usuarioDto.getEndereco());
            usuario.setEndereco(endereco);
        }
    }

    private void vincularInstituicao(UsuarioDto usuarioDto, UsuarioModel usuario) {
        if (Objects.nonNull(usuarioDto.getInstituicaoId()) && usuarioDto.getPerfil().equals(Perfil.ALUNO)) {
            InstituicaoModel instituicao = instituicaoService.getInstituicao(usuarioDto.getInstituicaoId());
            usuario.setInstituicao(instituicao);
        }
    }

    public void atualizarInstituicao(UsuarioModel usuario, InstituicaoModel instituicao) {
        usuario.setInstituicao(instituicao);
        usuarioRepository.save(usuario);
    }

    @Transactional
    public UsuarioDto atualizarUsuario(Long usuarioId, UsuarioDto usuarioDto) {
        UsuarioModel usuarioAtual  = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        
        BeanUtils.copyProperties(usuarioDto, usuarioAtual);
        usuarioAtual.setId(usuarioId);

        EnderecoModel enderecoAtualizado = enderecoService.atualizarEndereco(usuarioDto.getEndereco());

        usuarioAtual.setEndereco(enderecoAtualizado);

        UsuarioModel usuarioAtualizado = usuarioRepository.save(usuarioAtual);
        return converterDomainParaDto(usuarioAtualizado);
    }

    public Page<UsuarioDto> listar(Pageable pageable, String pesquisa) {
        Page<UsuarioModel> usuarios = pesquisa.length() > 0 
            ? usuarioRepository.findByNomeContainingIgnoreCaseOrEmailContainingIgnoreCaseOrCpfContaining(pesquisa, pesquisa, pesquisa, pageable)
            : usuarioRepository.findAll(pageable);

        return usuarios.map(this::converterDomainParaDto);
    }

    public List<UsuarioModel> listarMotoristas() {
        List<UsuarioModel> motoristas = usuarioRepository.findAllByPerfil(Perfil.MOTORISTA);
        return motoristas;
    }
    
    public UsuarioModel getUsuario(Long id) {
        return usuarioRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
    }

    public void liberarAcessoUsuario(Long id) {
        String senha = gerarSenhaPrimeiroAcesso();

        UsuarioModel usuario = usuarioRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
            
        usuario.setSenha(passwordEncoder.encode(senha));
        
        UsuarioModel usuarioAtualizado = usuarioRepository.save(usuario);

        emailService.enviarEmailPrimeiroAcesso(usuarioAtualizado, senha);        
    }

    public UsuarioModel converterDtoParaDomain(UsuarioDto usuarioDto) {
        return UsuarioModel.builder()
            .matricula(usuarioDto.getMatricula())
            .dataNascimento(usuarioDto.getDataNascimento())
            .nome(usuarioDto.getNome())
            .telefone(usuarioDto.getTelefone())
            .email(usuarioDto.getEmail())
            .cpf(usuarioDto.getCpf())
            .perfil(usuarioDto.getPerfil())
            .build();
    }

    public UsuarioDto converterDomainParaDto(UsuarioModel usuario) {
        UsuarioDto usuarioDto = new UsuarioDto();
        BeanUtils.copyProperties(usuario, usuarioDto);

        if (Objects.nonNull(usuario.getEndereco())) {
            usuarioDto.setEndereco(enderecoService.converterDomainParaDto(usuario.getEndereco()));
        }

        if (Objects.nonNull(usuario.getInstituicao())) {
            usuarioDto.setInstituicao(instituicaoService.converterDomainParaDto(usuario.getInstituicao()));
        }

        return usuarioDto;
    }

    public UsuarioDto getUsuarioLogado(Principal principal) {
        UsuarioModel usuarioModel = autenticacaoService.getUsuarioLogado(principal);
        return converterDomainParaDto(usuarioModel);
    }

    public Boolean isUsuarioCadastrado(String email, String cpf) {
        return usuarioRepository.findByEmailOrCpf(email, cpf).isPresent();
    }
    
	public boolean hasUsuarioAdmin() {
        Optional<UsuarioModel> admin = usuarioRepository.findByPerfil(Perfil.ADMIN);
		return admin.isPresent();
	}

	public void cadastrarUsuarioAdmin() {
        UsuarioModel admin = UsuarioModel.builder()
            .perfil(Perfil.ADMIN)
            .nome("Mirer Balbino de Andrade Junior")
            .email("mirer.rmj@gmail.com")
            .telefone("(00) 00000-0000")
            .matricula("01")
            .cpf("00000000000")
            .senha("$2a$12$FJve86hShTAnCXXjHjVHNOB7nA7B/0DEc.jeUGzP3TcQYqPehFl.a")
            .emailVerificado(true)
            .build();
        
        usuarioRepository.save(admin);
	}

    public List<UsuarioModel> cadastrarUsuarioMock(int quantidade) {
        Faker faker = new Faker(Locale.forLanguageTag("pt-BR"));
        List<UsuarioModel> usuarios = new ArrayList<>();

        for (int pos = 1; pos <= quantidade; pos++) {
            String nomeCompleto = faker.name().fullName();
            
            LocalDate nascimento = faker.date()
                .birthday(18, 30)
                .toLocalDateTime()
                .toLocalDate();

            String matricula = LocalDate.now().getYear() + "" + faker.number().digits(6);
            Perfil perfil = faker.bool().bool() ? Perfil.ALUNO : Perfil.MOTORISTA;

            UsuarioModel usuario = UsuarioModel.builder()
                .perfil(perfil)
                .nome(nomeCompleto)
                .email(formatEmailMock(nomeCompleto))
                .telefone(faker.phoneNumber().cellPhone())
                .dataNascimento(nascimento)
                .matricula(matricula)
                .endereco(enderecoService.getEnderecoMock(pos))
                .cpf(faker.cpf().valid(false))
                .emailVerificado(true)
                .senha("$2a$12$FJve86hShTAnCXXjHjVHNOB7nA7B/0DEc.jeUGzP3TcQYqPehFl.a")
                .build();
            
            UsuarioModel usuarioCadastrado = usuarioRepository.save(usuario);
            usuarios.add(usuarioCadastrado);
        }

        return usuarios;
	}

    private String formatEmailMock(String nomeCompleto) {
        List<String> nomes = List.of(nomeCompleto.split(" "))
            .stream()
            .map(String::toLowerCase)
            .map(StringUtils::stripAccents)
            .map(nome -> nome.replace(".", ""))
            .toList();

        String primeiroNome = nomes.get(0);
        String ultimoNome = nomes.get(nomes.size() - 1);

        return primeiroNome + "." + ultimoNome + "@gmail.com";
    }
    
    public String gerarSenhaPrimeiroAcesso() {
        int tamanhoSenha = 10;
        boolean hasLetras = true;
        boolean hasDigitos = true;

        return RandomStringUtils.random(tamanhoSenha, hasLetras, hasDigitos);        
    }
}
