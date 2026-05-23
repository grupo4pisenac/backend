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

    private static final String FROM_EMAIL = "sistema@senac.com";

    @Async
    public void enviarEmailNovaSolicitacao(String emailCoordenador, String nomeAluno, String area) {
        log.info("Enviando email de nova solicitação para coordenador={}, aluno={}, area={}", emailCoordenador, nomeAluno, area);
        enviar(emailCoordenador,
                "Nova solicitação de atividade complementar",
                "O aluno " + nomeAluno + " enviou uma nova solicitação na área de " + area + ". Acesse o sistema para analisar.");
    }

    @Async
    public void enviarEmailStatusAtualizado(String emailAluno, String nomeAluno, StatusSolicitacao status) {
        log.info("Enviando email de status atualizado para aluno={}, status={}", emailAluno, status);
        String textoStatus = status == StatusSolicitacao.APROVADA ? "aprovada" : "reprovada";
        enviar(emailAluno,
                "Atualização da sua solicitação de atividade complementar",
                "Olá " + nomeAluno + ", sua solicitação foi " + textoStatus + ". Acesse o sistema para mais detalhes.");
    }

    @Async
    public void enviarEmailLimiteSemestralUltrapassado(String emailAluno, String nomeAluno, String area, Integer semestre, int horasAprovadas, int limiteSemestral) {
        log.info("Enviando email de limite semestral para aluno={}, area={}, semestre={}", emailAluno, area, semestre);
        enviar(emailAluno,
                "Limite semestral de horas atingido - " + area,
                "Olá " + nomeAluno + ", suas horas aprovadas na área de " + area +
                        " no semestre " + semestre + " totalizam " + horasAprovadas + "h, " +
                        "ultrapassando o limite semestral de " + limiteSemestral + "h. " +
                        "Acesse o sistema para mais detalhes.");
    }

    private void enviar(String para, String assunto, String texto) {
        try {
            SimpleMailMessage mensagem = new SimpleMailMessage();
            mensagem.setFrom(FROM_EMAIL);
            mensagem.setTo(para);
            mensagem.setSubject(assunto);
            mensagem.setText(texto);
            mailSender.send(mensagem);
            log.debug("Email enviado com sucesso para={}", para);
        } catch (Exception e) {
            log.error("Falha ao enviar email para={}: {}", para, e.getMessage());
        }
    }
}