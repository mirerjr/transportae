package br.com.transportae.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import br.com.transportae.auth.AutenticacaoService;
import br.com.transportae.usuario.UsuarioModel;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Autowired
    private EmailRepository emailRepository;
    
    @Autowired
    private JavaMailSender emailSender;
    

    public void enviarEmail (EmailModel emailModel, Boolean limparConteudo) {
        SimpleMailMessage mensagem = formatarMensagemEmail(emailModel);

        if (limparConteudo) {
            emailModel.setConteudo("Conteúdo retirado por questões de segurança.");
        }

        emailSender.send(mensagem);
        emailRepository.save(emailModel);
    }

    private SimpleMailMessage formatarMensagemEmail(EmailModel emailModel) {
        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setFrom(emailModel.getRemetente());
        mensagem.setTo(emailModel.getDestinatario());
        mensagem.setSubject(emailModel.getAssunto());
        mensagem.setText(emailModel.getConteudo());

        return mensagem;     
    }

    
    public void enviarEmailPrimeiroAcesso(UsuarioModel usuarioModel, String senha) {
        String mensagem = formatarMensagemPrimeiroAcesso(usuarioModel, senha);

        EmailModel email = EmailModel.builder()
            .remetente("mirer.rmj@gmail.com")
            .destinatario(usuarioModel.getEmail())
            .assunto("Transportaê - Instruções para acesso ao sistema")
            .conteudo(mensagem)
            .build();

        enviarEmail(email, true);
    }

    private String formatarMensagemPrimeiroAcesso(UsuarioModel usuarioModel, String senha) {
        return new StringBuilder()
            .append("Prezado ").append(usuarioModel.getNome())
            .append(",\nPara realizar o primeiro acesso, por favor siga os passos abaixo:")
            .append("\n\nLink: ").append("http://localhost:4000")
            .append("\nLogin: ").append(usuarioModel.getEmail())
            .append("\nSenha: ").append(senha)
            .append("\n\nApós o login, será preciso a alteração da senha")
            .append("\n\nAtenciosamente, ")
            .append("\nEquipe Transportaê.")
            .toString();
    }
}
