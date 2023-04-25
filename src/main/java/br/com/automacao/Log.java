package br.com.automacao;

import javax.imageio.IIOException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {
    public static void logs(String msg) throws IOException {
        Date dataHoraAtual = new Date();
        String data = new SimpleDateFormat("dd/MM/yyyy").format(dataHoraAtual);
        String hora = new SimpleDateFormat("HH:mm:ss").format(dataHoraAtual);

        Path path = Paths.get(System.getProperty("user.home"), "Downloads/");

        if (!Files.exists(path)) {
            Files.createDirectory(path);
        }
        File log = new File(System.getProperty("user.home"), "Downloads/logs.txt");
        if (!log.exists()) {
            log.createNewFile();
        }

        FileWriter fw = new FileWriter(log, true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(msg+" - "+data+" "+hora);
        bw.newLine();
        bw.close();
        fw.close();

    }
}
