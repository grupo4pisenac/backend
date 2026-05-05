ALTER TABLE usuarios ALTER COLUMN semestre_atual SET DEFAULT 1;
ALTER TABLE regras_atividade ALTER COLUMN limite_horas_semestral SET DEFAULT 0;
ALTER TABLE cursos ALTER COLUMN total_semestres SET DEFAULT 1;