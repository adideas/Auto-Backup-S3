package BackupS3.Clients.FlareApp;

import java.util.ArrayList;
import java.util.HashMap;

public class FlareContext {
    HashMap<String, String> context = new HashMap<>();
    ArrayList<String> arguments = new ArrayList<>();
    HashMap<String, String> env = new HashMap<>();
    FlareRequest request = new FlareRequest();
    HashMap<String, String> cookies = new HashMap<>();
    HashMap<String, String> headers = new HashMap<>();
    HashMap<String, String> user = new HashMap<>();
}
