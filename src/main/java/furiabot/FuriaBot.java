package furiabot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class FuriaBot extends TelegramLongPollingBot {

    // Retorna o nome de usuário do bot (necessário para iniciar a comunicação com o Telegram)
    @Override
    public String getBotUsername() {
        return "FURiAssistent_bot";  // Nome de usuário do bot
    }

    // Retorna o token do bot (necessário para autorizar o bot a enviar mensagens)
    @Override
    public String getBotToken() {
        return "7810007924:AAE_dMoaNMtGfT_KSC5rF2csb8GMcEqPe1c";  // Token de autenticação do bot
    }

    // Método que é acionado sempre que o bot recebe uma atualização (mensagem)
    @Override
    public void onUpdateReceived(Update update) {
        // Verifica se a atualização contém uma mensagem com texto
        if (update.hasMessage() && update.getMessage().hasText()) {
            // Obtém a mensagem do usuário e o chatId para responder ao usuário
            String mensagem = update.getMessage().getText();
            String chatId = update.getMessage().getChatId().toString();
            String resposta;

            // Switch para identificar qual comando foi enviado pelo usuário
            switch (mensagem) {
                case "/start":
                    // Resposta inicial com os comandos disponíveis para o usuário
                    resposta = "Fala, FURIOSO(A)! 🔥 Sou o bot oficial do processo seletivo da FURIA!\n\n" +
                               "Meus comandos são:\n" +
                               "🎮 /jogos - Próximos jogos da FURIA\n" +
                               "📊 /ranking - Ranking atual da FURIA\n" +
                               "🤓 /curiosidades - Fatos curiosos sobre a FURIA\n" +
                               "📰 /noticias - Últimas notícias\n" +
                               "📈 /estatisticas - Estatísticas da equipe\n" +
                               "💪 /motivação - Frases motivacionais\n" +
                               "🆘 /ajuda - Mostra todos os comandos disponíveis para interagir com o bot.";
                    			
                    break;
                case "/jogos":
                    // Chama o método para buscar os próximos jogos da FURIA
                    resposta = getProximosJogos();
                    break;
                case "/ranking":
                    // Chama o método para buscar o ranking atual da FURIA
                    resposta = getRankingFuria();
                    break;
                case "/curiosidades":
                    // Chama o método para fornecer curiosidades aleatórias sobre a FURIA
                    resposta = getCuriosidadesAleatorias();
                    break;
                case "/noticias":
                    // Chama o método para buscar as últimas notícias da FURIA
                    resposta = getNoticiasFuria();
                    break;
                case "/estatisticas":
                    // Chama o método para fornecer as estatísticas da FURIA
                    resposta = getEstatisticasFuria();
                    break;
                case "/motivacao":
                case "/motivação":
                    // Chama o método para fornecer frases motivacionais
                    resposta = getFrasesMotivacionais();
                    break;
                case "/elenco":
                    // Chama o método para fornecer informações sobre o elenco da FURIA
                    resposta = getElenco();
                    break;
                case "/ajuda":
                    resposta = "🔧 Comandos do FuriaBot:\n\n" +
                               "🎮 /jogos - Exibe os próximos jogos da FURIA.\n" +
                               "📊 /ranking - Mostra o ranking atual da FURIA no HLTV.\n" +
                               "🤓 /curiosidades - Apresenta curiosidades sobre a FURIA.\n" +
                               "📰 /noticias - Exibe as últimas notícias sobre a FURIA.\n" +
                               "📈 /estatisticas - Estatísticas detalhadas da FURIA.\n" +
                               "💪 /motivação - Frases motivacionais para te inspirar.\n" +
                               "📣 /elenco - Mostra os jogadores atuais da FURIA (CS2).\n" +
                               "🆘 /ajuda - Mostra todos os comandos disponíveis para interagir com o bot.";
                    break;          
                default:
                    // Caso o comando não seja reconhecido, retorna uma mensagem de erro
                    resposta = "❌ Comando desconhecido.";
            }

            // Envia a resposta de volta para o usuário
            SendMessage mensagemDeResposta = new SendMessage();
            mensagemDeResposta.setChatId(chatId);
            mensagemDeResposta.setText(resposta);

            try {
                // Executa o envio da mensagem
                execute(mensagemDeResposta);
            } catch (Exception e) {
                // Trata exceções em caso de falha ao enviar a mensagem
                e.printStackTrace();
            }
        }
    }

    // Método que busca os próximos jogos da FURIA em diferentes ligas
    public String getProximosJogos() {
        try {
            String[] idsDasLigas = {"5425", "3543", "12039", "5457"};
            StringBuilder respostaJogos = new StringBuilder();

            for (String id : idsDasLigas) {
                URL url = new URL("https://www.thesportsdb.com/api/v1/json/3/eventsnextleague.php?id=" + id);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JSONObject json = new JSONObject(response.toString());

                // 🛡️ Verificação adicional para evitar erro
                if (!json.has("events") || json.isNull("events")) {
                    continue; // Pula se não houver eventos nessa liga
                }

                JSONArray events = json.getJSONArray("events");

                for (int i = 0; i < events.length(); i++) {
                    JSONObject event = events.getJSONObject(i);
                    String homeTeam = event.getString("strHomeTeam");
                    String awayTeam = event.getString("strAwayTeam");

                    if (homeTeam.equalsIgnoreCase("FURIA") || awayTeam.equalsIgnoreCase("FURIA") ||
                        homeTeam.equalsIgnoreCase("FURIA Esports") || awayTeam.equalsIgnoreCase("FURIA Esports")) {
                        String dateEvent = event.optString("dateEvent", "Data desconhecida");
                        String strEvent = event.optString("strEvent", "Evento sem nome");
                        respostaJogos.append("🎮 ").append(strEvent).append(" em ").append(dateEvent).append("\n");
                    }
                }
            }

            if (respostaJogos.length() == 0) {
                return "❌ Nenhum jogo da FURIA encontrado nos próximos eventos.";
            }

            return respostaJogos.toString();

        } catch (Exception e) {
            e.printStackTrace(); // Mostra erro no console (útil para debug)
            return "❌ Ocorreu um erro ao buscar os próximos jogos. Tente novamente mais tarde.";
        }
    }


    // Método que retorna curiosidades aleatórias sobre a FURIA
    public String getCuriosidadesAleatorias() {
        String[] curiosidades = {
            "🎯 Origem brasileira: A FURIA Esports foi fundada em 2017 por Jaime \"raizen\" Pádua e André Akkari.",
            "🐍 Estilo agressivo: A FURIA ficou famosa pelo estilo de jogo super agressivo no CS:GO.",
            "👕 Uniforme icônico: A pantera preta virou símbolo da organização.",
            "🚀 Primeira explosão internacional: DreamHack Masters Dallas 2019 foi um marco para a FURIA.",
            "🌎 Mudança para os EUA: Em 2019, a equipe se mudou para os Estados Unidos para competir melhor.",
            "🧠 arT como IGL: Conhecido por jogadas ousadas e estratégias inusitadas.",
            "🎮 KSCERATO e yuurih: A dupla que forma a alma da equipe.",
            "🏆 Top 5 mundial: Em 2020, a FURIA esteve entre os cinco melhores do mundo.",
            "📈 Presença em vários jogos: A FURIA também atua em Valorant, LoL, Rocket League e mais.",
            "💬 Contato com fãs: A organização investe bastante em conteúdo e interação com o público."
        };
        return curiosidades[new Random().nextInt(curiosidades.length)];
    }

    // Método que retorna as últimas notícias sobre a FURIA
    public String getNoticiasFuria() {
        try {
            String apiKey = "fd88de827e0a41e29402199ec9a53d1a";
            String query = "FURIA esports";
            String urlString = "https://newsapi.org/v2/everything?q=" +
                URLEncoder.encode(query, "UTF-8") +
                "&language=pt&pageSize=3&apiKey=" + apiKey;

            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Lê a resposta da API de notícias
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Processa a resposta para exibir as notícias
            JSONObject json = new JSONObject(response.toString());
            JSONArray artigos = json.getJSONArray("articles");

            StringBuilder resultado = new StringBuilder();
            for (int i = 0; i < Math.min(3, artigos.length()); i++) {
                JSONObject artigo = artigos.getJSONObject(i);
                String titulo = artigo.getString("title");
                String urlNoticia = artigo.getString("url");

                resultado.append("📰 *")
                         .append(titulo.replace("*", ""))  // Corrige formatação da notícia
                         .append("*\n")
                         .append("[Leia aqui](").append(urlNoticia).append(")\n\n");
            }

            // Caso não haja notícias, retorna uma mensagem informando
            if (resultado.length() == 0) {
                return "Nenhuma notícia recente sobre a FURIA foi encontrada.";
            }

            return resultado.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Não foi possível buscar as notícias.";
        }
    }

    // Método que retorna as estatísticas da FURIA com um link direto para o HLTV
    public String getEstatisticasFuria() {
        return "📊 *Estatísticas da FURIA*\n\n" +
               "Acesse as estatísticas atualizadas da FURIA no HLTV:\n" +
               "🔗 https://www.hltv.org/stats/teams/8297/furia";
    }

    // Método que retorna uma frase motivacional aleatória
    public String getFrasesMotivacionais() {
        String[] frases = {
            "A FURIA não é só um time, é uma mentalidade. Lutar, vencer e sempre buscar mais!",
            "Na FURIA, cada obstáculo é uma oportunidade para mostrar a verdadeira força!",
            "A FURIA é feita de garra, paixão e resiliência. Quando caímos, levantamos mais fortes!",
            "Não é sobre ser o melhor no início, mas sim ser o melhor até o fim. FURIA sempre!",
            "O verdadeiro espírito da FURIA está em nunca desistir, nunca recuar e sempre avançar com coragem!",
            "Na FURIA, a batalha não é apenas sobre vencer, mas sobre deixar sua marca no mundo!",
            "Somos mais que jogadores, somos uma família. E juntos, ninguém pode nos parar!",
            "Onde há FURIA, há fogo! Vamos acender o jogo e mostrar a força de nossa paixão!",
            "Cada partida é uma nova oportunidade de lutar com garra, e na FURIA, nunca paramos de lutar!",
            "O espírito da FURIA é feito de coragem, união e a certeza de que somos imbatíveis, sempre!"
        };
        return frases[new Random().nextInt(frases.length)];
    }

    // Método que retorna as informações sobre o elenco da FURIA (CS2)
    public String getElenco() {
        return "🔥 Elenco atual da FURIA (CS2):\n\n" +
               "👑 KSCERATO - Rifler\n" +
               "🎯 yuurih - Rifler\n" +
               "🧠 arT - In-Game Leader (IGL)\n" +
               "⚔️ chelo - Entry Fragger\n" +
               "💣 FalleN - AWPer\n\n" +
               "🧑‍🏫 Coach: guerri";
    }

    // Método que retorna o ranking atual da FURIA
    public String getRankingFuria() {
        return "📊 O ranking atual da FURIA no HLTV é o seguinte:\n\n" +
               "🔗 [Confira o ranking atualizado da FURIA no HLTV](https://www.hltv.org/ranking/teams)";
    }
}

