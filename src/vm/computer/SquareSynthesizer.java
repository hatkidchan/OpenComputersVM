package vm.computer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class SquareSynthesizer {
    public float volume = 0.5f;
    final double TWO_PI = Math.PI * 2;
    public AudioFormat format = new AudioFormat( 44100, 8, 1, true, false );
    public SourceDataLine sdl;

    public boolean checkDataLine() {
        try {
            sdl = AudioSystem.getSourceDataLine(format);
            sdl.open();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void tone(int frequency, int duration) {
        sdl.start();  // Стартуемс
        int frames = duration * 44100 / 1000;
        byte[] buf = new byte[ frames ];
        for( int i = 1; i <= frames; i++ ) {
            double angle = (double)i / (double)44100 * frequency * TWO_PI;  // Ебаная магия
            double value = Math.sin(angle);
            buf[i - 1] = (byte)(volume * 127 * (value < 0 ? -1 : 1));  // БЗЗЗЗ
        }
        sdl.write( buf, 0, frames );  // Пишемс....
    }
}
