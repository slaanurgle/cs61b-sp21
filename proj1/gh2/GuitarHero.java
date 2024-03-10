package gh2;
import edu.princeton.cs.algs4.StdAudio;
import edu.princeton.cs.algs4.StdDraw;

/* bugs here */
public class GuitarHero {
    public static String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
    public static final double CONCERT_A = 440.0;
    public static final double CONCERT_C = CONCERT_A * Math.pow(2, 3.0 / 12.0);

    public static void main(String[] args) {
        /* create two guitar strings, for concert A and C */
        GuitarString stringA = new GuitarString(CONCERT_A);
        GuitarString stringC = new GuitarString(CONCERT_C);

        while (true) {

            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                int indexOfKey = keyboard.indexOf(key);
                if (indexOfKey == -1) {
                    continue;
                }
                double concertKey = 440 * Math.pow(2,(indexOfKey - 24) / 12.0);
                GuitarString stringKey = new GuitarString(concertKey);
                stringKey.pluck();
            }

            double sample = 0.0;
            for (int i = 0; i < 37; i += 1) {
                GuitarString s = new GuitarString(440 * Math.pow(2,(i - 24) / 12.0));
                sample += s.sample();
            }

            StdAudio.play(sample);

            for (int i = 0; i < 37; i++) {
                GuitarString s = new GuitarString(440 * Math.pow(2,(i - 24) / 12.0));
                s.tic();
            }
        }
    }
}
