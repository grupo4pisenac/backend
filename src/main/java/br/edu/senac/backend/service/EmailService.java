package br.edu.senac.backend.service;

import br.edu.senac.backend.model.enums.StatusSolicitacao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    private static final String FROM_EMAIL = "grupo4pisenac26@gmail.com";

    @Async
    public void enviarEmailNovaSolicitacao(String emailCoordenador, String nomeAluno, String area) {
        log.info("Enviando email de nova solicitação para coordenador={}, aluno={}, area={}", emailCoordenador, nomeAluno, area);
        try {
            SimpleMailMessage mensagem = new SimpleMailMessage();
            mensagem.setFrom(FROM_EMAIL);
            mensagem.setTo(emailCoordenador);
            mensagem.setSubject("Nova solicitação de atividade complementar");
            mensagem.setText("O aluno " + nomeAluno + " enviou uma nova solicitação na área de " + area + ". Acesse o sistema para analisar.");
            mailSender.send(mensagem);
            log.debug("Email de nova solicitação enviado com sucesso para={}", emailCoordenador);
        } catch (Exception e) {
            log.error("Falha ao enviar email de nova solicitação para={}: {}", emailCoordenador, e.getMessage());
        }
    }

    @Async
    public void enviarEmailStatusAtualizado(String emailAluno, String nomeAluno, StatusSolicitacao status) {
        log.info("Enviando email de status atualizado para aluno={}, status={}", emailAluno, status);
        try {
            SimpleMailMessage mensagem = new SimpleMailMessage();
            mensagem.setFrom(FROM_EMAIL);
            mensagem.setTo(emailAluno);
            mensagem.setSubject("Atualização da sua solicitação de atividade complementar");
            String textoStatus = status == StatusSolicitacao.APROVADA ? "aprovada" : "reprovada";
            mensagem.setText("Olá " + nomeAluno + ", sua solicitação foi " + textoStatus + ". Acesse o sistema para mais detalhes.");
            mailSender.send(mensagem);
            log.debug("Email de status atualizado enviado com sucesso para={}", emailAluno);
        } catch (Exception e) {
            log.error("Falha ao enviar email de status para={}: {}", emailAluno, e.getMessage());
        }
    }

    @Async
    public void enviarEmailLimiteSemestralUltrapassado(String emailAluno, String nomeAluno, String area, Integer semestre, int horasAprovadas, int limiteSemestral) {
        log.info("Enviando email de limite semestral para aluno={}, area={}, semestre={}", emailAluno, area, semestre);
        try {
            SimpleMailMessage mensagem = new SimpleMailMessage();
            mensagem.setFrom(FROM_EMAIL);
            mensagem.setTo(emailAluno);
            mensagem.setSubject("Limite semestral de horas atingido - " + area);
            mensagem.setText("Olá " + nomeAluno + ", suas horas aprovadas na área de " + area +
                    " no semestre " + semestre + " totalizam " + horasAprovadas + "h, " +
                    "ultrapassando o limite semestral de " + limiteSemestral + "h. " +
                    "Acesse o sistema para mais detalhes.");
            mailSender.send(mensagem);
            log.debug("Email de limite semestral enviado com sucesso para={}", emailAluno);
        } catch (Exception e) {
            log.error("Falha ao enviar email de limite semestral para={}: {}", emailAluno, e.getMessage());
        }
    }
}