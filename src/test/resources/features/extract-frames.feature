# language: pt

Funcionalidade: Extração de frames de vídeos
  Como um sistema de processamento de vídeos
  Quero extrair frames de vídeos e enviar eventos de conclusão ou falha
  Para notificar corretamente o processamento dos arquivos

  Cenário: Enviar evento de extração concluída
    Dado um ID de vídeo válido e um nome de arquivo
    Quando o evento de conclusão da extração for enviado
    Então a mensagem deve ser publicada no RabbitMQ no exchange "frames-extraction-finished-exchange"

  Cenário: Enviar evento de falha na extração
    Dado um ID de vídeo válido
    Quando o evento de falha na extração for enviado
    Então a mensagem deve ser publicada no RabbitMQ no exchange "frames-extraction-failed-exchange"

  Cenário: Processar vídeo e extrair frames com sucesso
    Dado um vídeo válido no formato MP4
    Quando o vídeo for processado para extração de frames
    Então o evento de extração concluída deve ser enviado
    E o arquivo temporário não deve existir após o processamento

  Cenário: Falha ao processar um arquivo inválido
    Dado um arquivo inválido no formato TXT
    Quando o vídeo for processado para extração de frames
    Então o evento de falha na extração deve ser enviado
    E o arquivo temporário não deve existir após o processamento