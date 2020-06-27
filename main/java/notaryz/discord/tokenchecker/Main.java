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
        System.out.println("Discord Token Checker by nttZx" + "\n Cod");

        List<String> TokenValid = new ArrayList<>();
        List<String> TokenUnverified = new ArrayList<>();
        List<String> TokenInvalid = new ArrayList<>();
        Path path = Paths.get("./tokens.txt");
        long nbTok = Files.lines(path).count();
        List<String> TokenToVerify = new ArrayList<>(Files.readAllLines(path));
        FileWriter validFile = new FileWriter("valid.txt");
        FileWriter invalidFile = new FileWriter("invalid.txt");
        FileWriter unverifiedFile = new FileWriter("unverified.txt");
        OkHttpClient client = new OkHttpClient();
        int i;
        for(i = 1; i < nbTok; i++){
            int tooken = 0;
            Request request = new Request.Builder()
                    .url("https://discord.com/api/v6/users/@me")
                    .header("Authorization", TokenToVerify.get(0))
                    .build();

            Response response = client.newCall(request).execute();
            if(response.code() == 200)
            {
                TokenToVerify.remove(tooken);
                TokenValid.add(TokenToVerify.get(0));
                validFile.write(TokenToVerify.get(tooken) + "\n");
                clearScreen();
                System.out.println("Valid: " + TokenValid.size() + " | Invalid: " + TokenInvalid.size() + " | Unverified: " + TokenUnverified.size());

            }
            else if(response.code() == 401){
                TokenToVerify.remove(tooken);
                TokenInvalid.add(TokenToVerify.get(tooken));
                invalidFile.write(TokenToVerify.get(tooken) + "\n");
                clearScreen();
                System.out.println("Valid: " + TokenValid.size() + " | Invalid: " + TokenInvalid.size() + " | Unverified: " + TokenUnverified.size());
            }
            else if(response.code() != 401 || response.code() != 200){
                TokenToVerify.remove(tooken);
                TokenUnverified.add(TokenToVerify.get(tooken));
                unverifiedFile.write(TokenToVerify.get(tooken) + "\n");
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
