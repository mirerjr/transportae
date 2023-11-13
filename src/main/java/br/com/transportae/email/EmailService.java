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
    private IEmailRepository emailRepository;
    
    @Autowired
    private JavaMailSender emailSender;
    
    private final AutenticacaoService autenticacaoService;

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

}
