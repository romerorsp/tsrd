package br.com.javapi.beertime.tsrd.input;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.javapi.beertime.tsrd.Constants;

@Component
public class ScheduledInputReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledInputReader.class);
    
    @Value("${tsrd.input.path:/opt/tsrd/inputs}")
    private String inputPath;
    
    @Resource(name=Constants.TIME_SERIES_OUTPUT_CHANNEL_NAME)
    private MessageChannel outputChannel;
    
    @Scheduled(fixedDelayString="${tsrd.input.schedule.ms:5000}")
    private void readInput() {
        try {
            Path processedDir = createProcessedDirectoryIfNotExists();
            Files.newDirectoryStream(Paths.get(inputPath), path -> !Files.isDirectory(path))
                   .forEach(path -> {
                       try (BufferedReader reader = Files.newBufferedReader(path)) {
                           String processId = String.format("%s-%s", path.getFileName().toString().replaceAll(File.separator, "-"), UUID.randomUUID());
                           reader.lines()
                                      .forEach(line -> outputChannel.send(MessageBuilder.withPayload(line).setHeader(Constants.PROCESS_ID, processId).build()));
                       } catch (IOException e) {
                           LOGGER.warn("It wasn't possible to read the file given by [{}]", path.getFileName(), e);
                       }
                       try {
                           Files.move(path, Paths.get(processedDir.toFile().getAbsolutePath().concat(File.separator.concat(path.getFileName().toString()))));
                       } catch (IOException e) {
                           LOGGER.warn("It wasn't possible to move the processed file [{}] to the processed directory [{}]", path.getFileName(), processedDir.getFileName(), e);
                       }
                   });
        } catch (IOException e) {
            LOGGER.warn("It wasn't possible to read the directory given by [{}]", inputPath, e);
        }
    }

    private Path createProcessedDirectoryIfNotExists() throws IOException {
        Path processedDir = Paths.get(inputPath.concat(File.separator.concat("processed")));
        if(!Files.exists(processedDir)) {
            Files.createDirectory(processedDir);
        }
        return processedDir;
    }
}
