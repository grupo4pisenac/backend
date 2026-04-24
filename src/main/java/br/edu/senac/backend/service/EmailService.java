package br.edu.senac.backend.service;

import br.edu.senac.backend.model.enums.StatusSolicitacao;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void enviarEmailNovaSolicitacao(String emailCoordenador, String nomeAluno, String area) {
        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setTo(emailCoordenador);
        mensagem.setSubject("Nova solicitação de atividade complementar");
        mensagem.setText("O aluno " + nomeAluno + " enviou uma nova solicitação na área de " + area + ". Acesse o sistema para analisar.");
        mailSender.send(mensagem);
    }

    public void enviarEmailStatusAtualizado(String emailAluno, String nomeAluno, StatusSolicitacao status) {
        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setTo(emailAluno);
        mensagem.setSubject("Atualização da sua solicitação de atividade complementar");
        String textoStatus = status == StatusSolicitacao.APROVADA ? "aprovada" : "reprovada";
        mensagem.setText("Olá " + nomeAluno + ", sua solicitação foi " + textoStatus + ". Acesse o sistema para mais detalhes.");
        mailSender.send(mensagem);
    }
}
