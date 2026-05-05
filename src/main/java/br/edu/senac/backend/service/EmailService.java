package br.edu.senac.backend.service;

import br.edu.senac.backend.model.enums.StatusSolicitacao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final RestTemplate restTemplate;

    @Value("${mailtrap.api.token}")
    private String apiToken;

    private static final String MAILTRAP_API_URL = "https://send.api.mailtrap.io/api/send";
    private static final String FROM_EMAIL = "hello@senac.com";
    private static final String FROM_NAME = "Sistema SENAC";

    private void enviar(String para, String assunto, String texto) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiToken);

            Map<String, Object> body = Map.of(
                    "from", Map.of("email", FROM_EMAIL, "name", FROM_NAME),
                    "to", List.of(Map.of("email", para)),
                    "subject", assunto,
                    "text", texto
            );

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(MAILTRAP_API_URL, request, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                log.debug("Email enviado com sucesso para={}", para);
            } else {
                log.error("Falha ao enviar email para={}: status={}", para, response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Falha ao enviar email para={}: {}", para, e.getMessage());
        }
    }

    @Async
    public void enviarEmailNovaSolicitacao(String emailCoordenador, String nomeAluno, String area) {
        log.info("Enviando email de nova solicitação para coordenador={}, aluno={}, area={}", emailCoordenador, nomeAluno, area);
        enviar(
                emailCoordenador,
                "Nova solicitação de atividade complementar",
                "O aluno " + nomeAluno + " enviou uma nova solicitação na área de " + area + ". Acesse o sistema para analisar."
        );
    }

    @Async
    public void enviarEmailStatusAtualizado(String emailAluno, String nomeAluno, StatusSolicitacao status) {
        log.info("Enviando email de status atualizado para aluno={}, status={}", emailAluno, status);
        String textoStatus = status == StatusSolicitacao.APROVADA ? "aprovada" : "reprovada";
        enviar(
                emailAluno,
                "Atualização da sua solicitação de atividade complementar",
                "Olá " + nomeAluno + ", sua solicitação foi " + textoStatus + ". Acesse o sistema para mais detalhes."
        );
    }

    @Async
    public void enviarEmailLimiteSemestralUltrapassado(String emailAluno, String nomeAluno, String area, Integer semestre, int horasAprovadas, int limiteSemestral) {
        log.info("Enviando email de limite semestral para aluno={}, area={}, semestre={}", emailAluno, area, semestre);
        enviar(
                emailAluno,
                "Limite semestral de horas atingido - " + area,
                "Olá " + nomeAluno + ", suas horas aprovadas na área de " + area +
                        " no semestre " + semestre + " totalizam " + horasAprovadas + "h, " +
                        "ultrapassando o limite semestral de " + limiteSemestral + "h. " +
                        "Acesse o sistema para mais detalhes."
        );
    }
}