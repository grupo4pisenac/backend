UPDATE solicitacoes s
SET horas_solicitadas = GREATEST(
        0,
        LEAST(
                s.horas_solicitadas,
                r.limite_horas_semestral - COALESCE((
                                                        SELECT SUM(s2.horas_solicitadas)
                                                        FROM solicitacoes s2
                                                        WHERE s2.aluno_id = s.aluno_id
                                                          AND s2.curso_id = s.curso_id
                                                          AND s2.area = s.area
                                                          AND s2.semestre = s.semestre
                                                          AND s2.status = 'APROVADA'
                                                          AND s2.id < s.id
                                                    ), 0)
        )
                        )
    FROM regras_atividade r
WHERE s.status = 'APROVADA'
  AND r.curso_id = s.curso_id
  AND LOWER(r.area) = LOWER(s.area)
  AND s.horas_solicitadas > 0;