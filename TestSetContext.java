import android.media.AudioTrack;
import android.content.Context;
import android.os.Build;
public class TestSetContext {
    public void test(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
           AudioTrack.Builder b = new AudioTrack.Builder();
           // b.setContext(context); 
        }
    }
}
