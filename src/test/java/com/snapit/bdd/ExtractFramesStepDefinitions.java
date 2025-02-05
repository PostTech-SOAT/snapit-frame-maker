package com.snapit.bdd;

import com.snapit.application.interfaces.BucketService;
import com.snapit.application.interfaces.FramesExtractionEventSender;
import com.snapit.application.usecase.ExtractFramesUseCase;
import com.snapit.framework.javacv.FramesExtractorService;
import com.snapit.framework.rabbitmq.FramesExtractionSenderService;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import lombok.SneakyThrows;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class ExtractFramesStepDefinitions {
    private FramesExtractionSenderService senderService;
    private ExtractFramesUseCase useCase;

    private RabbitTemplate rabbitTemplate;
    private BucketService bucketService;
    private FramesExtractionEventSender eventSender;

    private String videoId;
    private String filename;
    private InputStream videoFile;

    @Dado("um ID de vídeo válido e um nome de arquivo")
    public void umIDDeVideoValidoENomeDeArquivo() {
        videoId = UUID.randomUUID().toString();
        filename = "video.mp4";
        rabbitTemplate = mock(RabbitTemplate.class);
        senderService = new FramesExtractionSenderService(rabbitTemplate);
    }

    @Dado("um ID de vídeo válido")
    public void umIDDeVideoValido() {
        videoId = UUID.randomUUID().toString();
        rabbitTemplate = mock(RabbitTemplate.class);
        senderService = new FramesExtractionSenderService(rabbitTemplate);
    }

    @Quando("o evento de conclusão da extração for enviado")
    public void oEventoDeConclusaoDaExtracaoForEnviado() {
        senderService.sendFinishedEvent(videoId, filename);
    }

    @Quando("o evento de falha na extração for enviado")
    public void oEventoDeFalhaNaExtracaoForEnviado() {
        senderService.sendFailedEvent(videoId);
    }

    @Então("a mensagem deve ser publicada no RabbitMQ no exchange {string}")
    public void aMensagemDeveSerPublicadaNoRabbitMQNoExchange(String exchange) {
        verify(rabbitTemplate).convertAndSend(eq(exchange), eq("frames"), Optional.ofNullable(any()));
    }

    @Dado("um vídeo válido no formato MP4")
    @SneakyThrows
    public void umVideoValidoNoFormatoMP4() {
        videoFile = new FileInputStream("src/test/resources/video/dummy.mp4");
        bucketService = mock(BucketService.class);
        eventSender = mock(FramesExtractionEventSender.class);
        useCase = new ExtractFramesUseCase(new FramesExtractorService(), bucketService, eventSender);
    }

    @Dado("um arquivo inválido no formato TXT")
    @SneakyThrows
    public void umArquivoInvalidoNoFormatoTXT() {
        videoFile = new FileInputStream("src/test/resources/video/dummy.txt");
        bucketService = mock(BucketService.class);
        eventSender = mock(FramesExtractionEventSender.class);
        useCase = new ExtractFramesUseCase(new FramesExtractorService(), bucketService, eventSender);
    }

    @Quando("o vídeo for processado para extração de frames")
    public void oVideoForProcessadoParaExtracaoDeFrames() {
        useCase.processVideoToFrames(UUID.randomUUID().toString(), videoFile, "email@test.com", "dummy", 5);
    }

    @Então("o evento de extração concluída deve ser enviado")
    public void oEventoDeExtracaoConcluidaDeveSerEnviado() {
        verify(eventSender, times(1)).sendFinishedEvent(any(String.class), any(String.class));
        verify(eventSender, times(0)).sendFailedEvent(any(String.class));
    }

    @Então("o evento de falha na extração deve ser enviado")
    public void oEventoDeFalhaNaExtracaoDeveSerEnviado() {
        verify(eventSender, times(1)).sendFailedEvent(any(String.class));
        verify(eventSender, times(0)).sendFinishedEvent(any(String.class), any(String.class));
    }

    @Então("o arquivo temporário não deve existir após o processamento")
    public void oArquivoTemporarioNaoDeveExistirAposOProcessamento() {
        assertFalse(new File("email@test.com-dummy").exists());
    }
}
