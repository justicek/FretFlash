import java.util.*;


public class NoteGenerator
{
    private Random random;
    private String[] notes = {"A", "A#", "B", "C", "C#", "D", "D#", "E",
            "F", "F#", "G", "G#"};
    private ArrayList<String> noteSet;
    private String lastResult;
    private boolean first;

    public NoteGenerator()
    {
        random = new Random();
        noteSet = new ArrayList<String>();
    }

    public String randomizeNoRpt()
    {
        if (first) {
            lastResult = notes[random.nextInt(12)];
            return lastResult;
        }
        String thisResult = notes[random.nextInt(12)];
        while (thisResult.equals(lastResult)) {
            thisResult = notes[random.nextInt(12)];
        }
        return thisResult;
    }

    public void accidentals(boolean sharps) {
        if (sharps) {
            notes[1] = "A#";
            notes[4] = "C#";
            notes[6] = "D#";
            notes[9] = "F#";
            notes[11] = "G#";
        }
        else {
            notes[1] = "Bb";
            notes[4] = "Db";
            notes[6] = "Eb";
            notes[9] = "Gb";
            notes[11] = "Ab";
        }
    }




}
