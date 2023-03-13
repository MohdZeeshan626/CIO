package com.singleevent.sdk;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.singleevent.sdk.View.RightActivity.MyProfile;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.facebook.FacebookSdk.getApplicationContext;
import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    List<String> videoId= new ArrayList<>();

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void isGettingId() throws Exception{
        videoId.add("https://www.youtube.com/watch?v=DFYRQ_zQ-gk&feature=featured"); //failed
        videoId.add("https://www.youtube.com/watch?v=DFYRQ_zQ-gk");
        videoId.add("http://www.youtube.com/watch?v=DFYRQ_zQ-gk");
        videoId.add("https://youtube.com/watch?v=DFYRQ_zQ-gk");
        videoId.add("http://youtube.com/watch?v=DFYRQ_zQ-gk");
        videoId.add("https://m.youtube.com/watch?v=DFYRQ_zQ-gk");
        videoId.add("http://m.youtube.com/watch?v=DFYRQ_zQ-gk");
        videoId.add("http://www.youtube.com/e/dQw4w9WgXcQ");//failed
        videoId.add("http://www.youtube.com/watch?feature=player_embedded&v=dQw4w9WgXcQ\n");//failed
        videoId.add("http://www.youtube.com/v/dQw4w9WgXcQ");//failed
        videoId.add("http://www.youtube.com/watch?v=dQw4w9WgXcQ");
        videoId.add("https://www.youtube.com/watch?v=EL-UCUAt8DQ");
        videoId.add("http://www.youtube.com/watch?v=dQw4w9WgXcQ");
        videoId.add("http://youtu.be/dQw4w9WgXcQ");
        videoId.add("http://www.youtube.com/embed/dQw4w9WgXcQ");
        videoId.add("https://www.youtube.com/watch?v=wz4MLJBdSpw&t=67s");
        videoId.add("http://youtu.be/DFYRQ_zQ-gk");
        videoId.add("http://www.youtube.com/watch?v=dQw4w9WgXcQ&a=GxdCwVVULXctT2lYDEPllDR0LRTutYfW");
        videoId.add("https://www.youtube.com/HamdiKickProduction?v=DFYRQ_zQ-gk");
        videoId.add("http://youtu.be/DFYRQ_zQ-gk");
        videoId.add("http://youtu.be/DFYRQ_zQ-gk");
        videoId.add("https://youtu.be/DFYRQ_zQ-gk");
        videoId.add("https://youtu.be/DFYRQ_zQ-gk?t=120");
        videoId.add("https://youtube.com/embed/DFYRQ_zQ-gk");
        videoId.add("http://youtube.com/embed/DFYRQ_zQ-gk");
        videoId.add("http://youtube.com/embed/DFYRQ_zQ-gk");
        videoId.add("https://youtube.com/embed/DFYRQ_zQ-gk");
        videoId.add("http://www.youtube.com/embed/DFYRQ_zQ-gk");
        videoId.add("http://www.youtube.com/embed/DFYRQ_zQ-gk");
        videoId.add("http://www.youtube.com/embed/DFYRQ_zQ-gk");
        videoId.add("https://www.youtube.com/v/DFYRQ_zQ-gk?fs=1&hl=en_US");
        videoId.add("https://www.youtube.com/embed/DFYRQ_zQ-gk");
        videoId.add("https://www.youtube.com/embed/DFYRQ_zQ-gk?autoplay=1");
        videoId.add("http://youtube.com/v/DFYRQ_zQ-gk?fs=1&hl=en_US");
        videoId.add("http://www.youtube.com/v/DFYRQ_zQ-gk?fs=1&hl=en_US");
        videoId.add("http://www.youtube.com/v/DFYRQ_zQ-gk?fs=1&hl=en_US");
        videoId.add("http://m.youtube.com/watch?v=DFYRQ_zQ-gk");
        videoId.add("http://m.youtube.com/watch?v=DFYRQ_zQ-gk");
        videoId.add("http://www.youtube.com/v/DFYRQ_zQ-gk?fs=1&hl=en_US");
//        for(int i=0;i<videoId.size();i++){
            assertTrue(extractYTId(videoId.get(0)));

//        }

    }

    public static boolean extractYTId(String ytUrl) {
        String vId = null;
        String regex="http(?:s)?:\\/\\/(?:m.)?(?:www\\.)?youtu(?:\\.be\\/|be\\.com\\/(?:watch\\?(?:feature=youtu.be\\&)?v=|v\\/|embed\\/|user\\/(?:[\\w#]+\\/)+))([^&#?\\n]+)";
        //http(?:s)?:\/\/(?:m.)?(?:www\.)?youtu(?:\.be\/|be\.com\/(?:watch\?(?:feature=youtu.be\&)?v=|v\/|embed\/|user\/(?:[\w#]+\/)+))([^&#?\n]+)
        Pattern pattern = Pattern.compile(
                regex,
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(ytUrl);
        if (matcher.matches()) {
            vId = matcher.group(1);
            return true;
        }
        return false;
    }


}