package cordova.plugin.mmlogfile;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class echoes a string called from JavaScript.
 */
public class mmLogFile extends CordovaPlugin {
    private static Context context;
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("createLogFile")) {
            String message = args.getString(0);
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    callbackContext.sendPluginResult(createLogFile(message));
                }
            });
            return true;
        }
        return false;
    }

    private PluginResult createLogFile(String message) {
        if (message != null && message.length() > 0) {
             String filename = "logcat_" + System.currentTimeMillis() + ".txt";
             File outputFile = new File(message, filename);
             try {
                // regEx, max count, -T most recent count of logs eg: -e=io.MM.AGP, --max-count=10, -T 100
                 Process process = Runtime.getRuntime().exec("logcat -v threadtime -T 200 -f "+outputFile.getAbsolutePath());
                 process.waitFor();
                 if (process.exitValue() != 0) {
                     System.out.println();
                     InputStream errorStream = process.getErrorStream();
                     int c = 0;
                     while ((c = errorStream.read()) != -1) {
                         System.out.print((char)c);
                     }
                     return new PluginResult(PluginResult.Status.ERROR);
                 }
                 return new PluginResult(PluginResult.Status.OK);
             }
            catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                 e.printStackTrace();
                 return new PluginResult(PluginResult.Status.JSON_EXCEPTION);
             }
        } else {
            return new PluginResult(PluginResult.Status.ERROR);
        }
        return new PluginResult(PluginResult.Status.JSON_EXCEPTION);
    }
}
