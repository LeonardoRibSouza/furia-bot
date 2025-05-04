package furiabot;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * Classe principal responsável por iniciar o bot da FURIA.
 * <p>
 * Esta classe é responsável por configurar e inicializar a API do Telegram para que o bot comece a funcionar.
 * Ela cria uma instância de {@link FuriaBot} e registra o bot na plataforma do Telegram para receber atualizações.
 * </p>
 */
public class Main {

    /**
     * Método principal que inicia o bot da FURIA.
     * 
     * @param args Argumentos de linha de comando (não utilizados neste caso).
     */
    public static void main(String[] args) {
        try {
            // Cria uma instância da API do Telegram para conectar o bot
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            
            // Registra o bot FuriaBot, que contém a lógica do funcionamento do bot
            botsApi.registerBot(new FuriaBot());
            
            // Exibe mensagem indicando que o bot foi iniciado com sucesso
            System.out.println("🤖 Bot FURIA iniciado com sucesso!");
        } catch (Exception e) {
            // Caso ocorra algum erro ao iniciar o bot, imprime a stack trace e exibe mensagem de erro
            e.printStackTrace();
            System.out.println("❌ Erro ao iniciar o Bot FURIA.");
        }
    }
}
