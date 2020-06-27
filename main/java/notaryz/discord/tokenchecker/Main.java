package notaryz.discord.tokenchecker;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Discord Token Checker by notaryz");
        List<String> TokenValid = new ArrayList<>();
        List<String> TokenUnverified = new ArrayList<>();
        List<String> TokenInvalid = new ArrayList<>();
        Path path = Paths.get("./tokens.txt");
        FileWriter validFile = new FileWriter("valid.txt");
        FileWriter invalidFile = new FileWriter("invalid.txt");
        FileWriter unverifiedFile = new FileWriter("unverified.txt");
        OkHttpClient client = new OkHttpClient();
        if(!Files.exists(path)){
            Files.createFile(path);
            System.out.println("Plz insert your tokens in tokens.txt !");
        }
        int i;
        long nbTok = Files.lines(path).count();
        List<String> TokenToVerify = new ArrayList<>(Files.readAllLines(path));
        if(nbTok == 0){
            System.out.println("Plz insert your tokens in tokens.txt !");
        }
        for(i = 0; i < nbTok; i++){
            int tooken = 0;
            Request request = new Request.Builder()
                    .url("https://discord.com/api/v6/users/@me")
                    .header("Authorization", TokenToVerify.get(0))
                    .build();

            Response response = client.newCall(request).execute();
            if(response.code() == 200)
            {
                TokenValid.add(TokenToVerify.get(0));
                TokenToVerify.remove(tooken);
                validFile.write(TokenValid.get(tooken) + "\n");
                clearScreen();
                System.out.println("Valid: " + TokenValid.size() + " | Invalid: " + TokenInvalid.size() + " | Unverified: " + TokenUnverified.size());

            }
            else if(response.code() == 401){
                TokenInvalid.add(TokenToVerify.get(tooken));
                TokenToVerify.remove(tooken);
                invalidFile.write(TokenInvalid.get(tooken) + "\n");
                clearScreen();
                System.out.println("Valid: " + TokenValid.size() + " | Invalid: " + TokenInvalid.size() + " | Unverified: " + TokenUnverified.size());
            }
            else if(response.code() != 401 || response.code() != 200){
                TokenUnverified.add(TokenToVerify.get(tooken));
                TokenToVerify.remove(tooken);
                unverifiedFile.write(TokenUnverified.get(tooken) + "\n");
                clearScreen();
                System.out.println("Valid: " + TokenValid.size() + " | Invalid: " + TokenInvalid.size() + " | Unverified: " + TokenUnverified.size());
            }
        }
        invalidFile.close();
        validFile.close();
        unverifiedFile.close();
        }

    public static void clearScreen() {
        try {

            if (System.getProperty("os.name").contains("Windows"))

                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();

            else

                Runtime.getRuntime().exec("clear");

        } catch (IOException | InterruptedException ex) {}
    }
}
