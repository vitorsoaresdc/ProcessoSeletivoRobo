package br.com.automacao;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.io.IOException;
import java.nio.file.*;
import static br.com.automacao.Log.logs;
public class Navegador {
    public static void AcessaNavegador() throws IOException {

                try {
             logs("Abre o navegador");
            // Obtenha o diretório "Downloads" padrão do usuário
            FileSystem fs = FileSystems.getDefault();
            Path downloadDir = fs.getPath(System.getProperty("user.home"),"Downloads");

            ChromeOptions options = new ChromeOptions();
            options.addArguments("download.default_directory=" + downloadDir);
            options.addArguments("download.prompt_for_download=false");

            System.setProperty("webdriver.http.factory", "jdk-http-client");
            WebDriver browser = new ChromeDriver(options);

            browser.get("https://doweb.rio.rj.gov.br/");
            browser.findElement(By.id("imagemCapa")).click();

            Thread.sleep(10000);
            browser.close();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
