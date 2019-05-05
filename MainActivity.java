package com.idtech.annebailey.finalproject;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Button;
//import android.widget.ToggleButton;
//import android.widget.CompoundButton;
import java.util.Random;
import java.lang.Math;
import android.graphics.Color;
import java.lang.Thread;
import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    // Delare variables for setting up and controlling buttons and text fields
    final Button[] keys = new Button[12];
    final MediaPlayer[] sounds = new MediaPlayer[12];
    String[] noteNames = new String[12];
    int[] chordNotes = new int[4];
    boolean[] clicked = new boolean[4];
    TextView note;
    TextView chord;
    TextView score;
    Button coverCorrect;
    Button coverWrong;
    Button coverLose;
    Button coverNewGame;
    Button majorButton;
    Button minorButton;
    Button dominantButton;
    Button diminishedButton;
    Button allButton;
    Button playButton;
    Button introButton;
    boolean playMajor = false;
    boolean playMinor = false;
    boolean playDominant = false;
    boolean playDiminished = false;
    boolean playAll = false;
    boolean justPlay = false;
    boolean correct = false;
    int buttonSelected = 0;
    int chordScore = 0;
    int timesWrong = 0;
    int i = 0;
    int r = 0;
    private Timer myTimer;
    private TimerTask myTimerTask;
    private Handler mTimerHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        // Buttons for the 12 piano keys
        keys[0] = (Button) findViewById(R.id.C);
        keys[1] = (Button) findViewById(R.id.Csharp);
        keys[2] = (Button) findViewById(R.id.D);
        keys[3] = (Button) findViewById(R.id.Dsharp);
        keys[4] = (Button) findViewById(R.id.E);
        keys[5] = (Button) findViewById(R.id.F);
        keys[6] = (Button) findViewById(R.id.Fsharp);
        keys[7] = (Button) findViewById(R.id.G);
        keys[8] = (Button) findViewById(R.id.Gsharp);
        keys[9] = (Button) findViewById(R.id.A);
        keys[10] = (Button) findViewById(R.id.Asharp);
        keys[11] = (Button) findViewById(R.id.B);

        // The sounds made by pressing each key
        sounds[0] = MediaPlayer.create(this, R.raw.c4);
        sounds[1] = MediaPlayer.create(this, R.raw.csharp4);
        sounds[2] = MediaPlayer.create(this, R.raw.d4);
        sounds[3] = MediaPlayer.create(this, R.raw.dsharp4);
        sounds[4] = MediaPlayer.create(this, R.raw.e4);
        sounds[5] = MediaPlayer.create(this, R.raw.f4);
        sounds[6] = MediaPlayer.create(this, R.raw.fsharp4);
        sounds[7] = MediaPlayer.create(this, R.raw.g4);
        sounds[8] = MediaPlayer.create(this, R.raw.gsharp4);
        sounds[9] = MediaPlayer.create(this, R.raw.a4);
        sounds[10] = MediaPlayer.create(this, R.raw.asharp4);
        sounds[11] = MediaPlayer.create(this, R.raw.b4);

        // The names of the 12 keys
        noteNames[0] = "C";
        noteNames[1] = "Db";
        noteNames[2] = "D";
        noteNames[3] = "Eb";
        noteNames[4] = "E";
        noteNames[5] = "F";
        noteNames[6] = "Gb";
        noteNames[7] = "G";
        noteNames[8] = "Ab";
        noteNames[9] = "A";
        noteNames[10] = "Bb";
        noteNames[11] = "B";

        note = (TextView) findViewById(R.id.note);
        chord = (TextView) findViewById(R.id.chord);
        score = (TextView) findViewById(R.id.score);
        score.setText("Score: " + chordScore);

        // The various screens and buttons to play the game and select different options
        // Full-screen button displayed if the player gets a chord correct
        coverCorrect = (Button) findViewById(R.id.coverCorrect);
        coverCorrect.setVisibility(View.GONE);
        // Full-screen button displayed if the player gets a chord wrong
        coverWrong = (Button) findViewById(R.id.coverWrong);
        coverWrong.setVisibility(View.GONE);
        coverLose = (Button) findViewById(R.id.coverLose);
        coverLose.setVisibility(View.GONE);
        // Full-screen button displayed to give the option of playing a new game
        coverNewGame = (Button) findViewById(R.id.coverNewGame);
        coverNewGame.setVisibility(View.GONE);
        // Button to select a game with only major chords
        majorButton = (Button) findViewById(R.id.majorButton);
        // Button to select a game with only minor chords
        minorButton = (Button) findViewById(R.id.minorButton);
        // Button to select a game with only dominant chords
        dominantButton = (Button) findViewById(R.id.dominantButton);
        // Button to select a game with only diminished chords
        diminishedButton = (Button) findViewById(R.id.diminishedButton);
        // Button to select a game with only major chords
        allButton = (Button) findViewById(R.id.allButton);
        // Button to just play the keyboard like a normal piano, without playing the chord game
        playButton = (Button) findViewById(R.id.playButton);
        // Display that the game is beginning
        introButton = (Button) findViewById(R.id.introButton);

        // Display that the chord was correct then continue with the game
        coverCorrect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                coverCorrect.setVisibility(View.GONE);
            }
        });
        // Display that the chord was incorrect as soon as one of the notes is wrong, then continue with the game
        coverWrong.setOnClickListener(new View.OnClickListener() {
            // Reset the keys that have been clicked so far
            public void onClick(View v) {
                for (i = 0; i < clicked.length; i++) {
                    clicked[i] = false;
                }
                // Reset the color of the keys back to white and black (as opposed to red or green)
                for (i = 0; i < keys.length; i++) {
                    if ((i != 1) && (i != 3) && (i != 6) && (i != 8) && (i != 10)) {
                        keys[i].setBackgroundColor(Color.WHITE);
                    }
                    if ((i == 1) || (i == 3) || (i == 6) || (i == 8) || (i == 10)) {
                        keys[i].setBackgroundColor(Color.BLACK);
                    }
                }
                coverWrong.setVisibility(View.GONE);
            }
        });
        // Reset the game when the player loses or selects a new game
        coverLose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                resetGame();
            }
        });
        coverNewGame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                resetGame();
            }
        });
        // Set the game up for only major chords
        majorButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                playMajor = true;
                setChords();
                majorButton.setVisibility(View.GONE);
                minorButton.setVisibility(View.GONE);
                dominantButton.setVisibility(View.GONE);
                diminishedButton.setVisibility(View.GONE);
                allButton.setVisibility(View.GONE);
                playButton.setVisibility(View.GONE);
                introButton.setVisibility(View.GONE);
                startTimer();
            }
        });
        // Set the game up for only minor chords
        minorButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                playMinor = true;
                setChords();
                majorButton.setVisibility(View.GONE);
                minorButton.setVisibility(View.GONE);
                dominantButton.setVisibility(View.GONE);
                diminishedButton.setVisibility(View.GONE);
                allButton.setVisibility(View.GONE);
                playButton.setVisibility(View.GONE);
                introButton.setVisibility(View.GONE);
                startTimer();
            }
        });
        // Set the game up for only dominant chords
        dominantButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                playDominant = true;
                setChords();
                majorButton.setVisibility(View.GONE);
                minorButton.setVisibility(View.GONE);
                dominantButton.setVisibility(View.GONE);
                diminishedButton.setVisibility(View.GONE);
                allButton.setVisibility(View.GONE);
                playButton.setVisibility(View.GONE);
                introButton.setVisibility(View.GONE);
                startTimer();
            }
        });
        // Set the game up for only diminished chords
        diminishedButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                playDiminished = true;
                setChords();
                majorButton.setVisibility(View.GONE);
                minorButton.setVisibility(View.GONE);
                dominantButton.setVisibility(View.GONE);
                diminishedButton.setVisibility(View.GONE);
                allButton.setVisibility(View.GONE);
                playButton.setVisibility(View.GONE);
                introButton.setVisibility(View.GONE);
                startTimer();
            }
        });
        // Set the game up for all chords
        allButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                playAll = true;
                setChords();
                majorButton.setVisibility(View.GONE);
                minorButton.setVisibility(View.GONE);
                dominantButton.setVisibility(View.GONE);
                diminishedButton.setVisibility(View.GONE);
                allButton.setVisibility(View.GONE);
                playButton.setVisibility(View.GONE);
                introButton.setVisibility(View.GONE);
                startTimer();
            }
        });
        // Let the user just play the keyboard like a normal piano
        playButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                justPlay = true;
                majorButton.setVisibility(View.GONE);
                minorButton.setVisibility(View.GONE);
                dominantButton.setVisibility(View.GONE);
                diminishedButton.setVisibility(View.GONE);
                allButton.setVisibility(View.GONE);
                playButton.setVisibility(View.GONE);
                introButton.setVisibility(View.GONE);
                note.setVisibility(View.GONE);
                chord.setVisibility(View.GONE);
                score.setVisibility(View.GONE);
            }
        });

        // Buttons for each of the piano keys (each button follows this format)
        keys[0].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonSelected = 0;
                sounds[0].start();
                if(justPlay == false){ // If the user is playing the game
                    correct = check(); // Check if the note was correct
                    if (correct == true) {
                        reset();
                    }
                    if (correct == false) {
                        coverWrong.setVisibility(View.VISIBLE);
                    }
                    // If this is the third time the user gets it worng, they lose and the game is over
                    if (timesWrong == 3) {
                        coverLose.setText("You lose! Your score was " + chordScore + ". Click to start new game.");
                        coverLose.setVisibility(View.VISIBLE);
                        coverWrong.setVisibility(View.GONE);
                    }
                }
            }
        });
        keys[1].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonSelected = 1;
                sounds[1].start();
                if(justPlay == false){
                    correct = check();
                    if (correct == true) {
                        reset();
                    }
                    if (correct == false) {
                        coverWrong.setVisibility(View.VISIBLE);
                    }
                    if (timesWrong == 3) {
                        coverLose.setText("You lose! Your score was " + chordScore + ". Click to start new game.");
                        coverLose.setVisibility(View.VISIBLE);
                        coverWrong.setVisibility(View.GONE);
                    }
                }
            }
        });
        keys[2].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonSelected = 2;
                sounds[2].start();
                if(justPlay == false){
                    correct = check();
                    if (correct == true) {
                        reset();
                    }
                    if (correct == false) {
                        coverWrong.setVisibility(View.VISIBLE);
                    }
                    if (timesWrong == 3) {
                        coverLose.setText("You lose! Your score was " + chordScore + ". Click to start new game.");
                        coverLose.setVisibility(View.VISIBLE);
                        coverWrong.setVisibility(View.GONE);
                    }
                }
            }
        });
        keys[3].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonSelected = 3;
                sounds[3].start();
                if(justPlay == false){
                    correct = check();
                    if (correct == true) {
                        reset();
                    }
                    if (correct == false) {
                        coverWrong.setVisibility(View.VISIBLE);
                    }
                    if (timesWrong == 3) {
                        coverLose.setText("You lose! Your score was " + chordScore + ". Click to start new game.");
                        coverLose.setVisibility(View.VISIBLE);
                        coverWrong.setVisibility(View.GONE);
                    }
                }
            }
        });
        keys[4].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonSelected = 4;
                sounds[4].start();
                if(justPlay == false){
                    correct = check();
                    if (correct == true) {
                        reset();
                    }
                    if (correct == false) {
                        coverWrong.setVisibility(View.VISIBLE);
                    }
                    if (timesWrong == 3) {
                        coverLose.setText("You lose! Your score was " + chordScore + ". Click to start new game.");
                        coverLose.setVisibility(View.VISIBLE);
                        coverWrong.setVisibility(View.GONE);
                    }
                }
            }
        });
        keys[5].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonSelected = 5;
                sounds[5].start();
                if(justPlay == false){
                    correct = check();
                    if (correct == true) {
                        reset();
                    }
                    if (correct == false) {
                        coverWrong.setVisibility(View.VISIBLE);
                    }
                    if (timesWrong == 3) {
                        coverLose.setText("You lose! Your score was " + chordScore + ". Click to start new game.");
                        coverLose.setVisibility(View.VISIBLE);
                        coverWrong.setVisibility(View.GONE);
                    }
                }
            }
        });
        keys[6].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonSelected = 6;
                sounds[6].start();
                if(justPlay == false){
                    correct = check();
                    if (correct == true) {
                        reset();
                    }
                    if (correct == false) {
                        coverWrong.setVisibility(View.VISIBLE);
                    }
                    if (timesWrong == 3) {
                        coverLose.setText("You lose! Your score was " + chordScore + ". Click to start new game.");
                        coverLose.setVisibility(View.VISIBLE);
                        coverWrong.setVisibility(View.GONE);
                    }
                }
            }
        });
        keys[7].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonSelected = 7;
                sounds[7].start();
                if(justPlay == false){
                    correct = check();
                    if (correct == true) {
                        reset();
                    }
                    if (correct == false) {
                        coverWrong.setVisibility(View.VISIBLE);
                    }
                    if (timesWrong == 3) {
                        coverLose.setText("You lose! Your score was " + chordScore + ". Click to start new game.");
                        coverLose.setVisibility(View.VISIBLE);
                        coverWrong.setVisibility(View.GONE);
                    }
                }
            }
        });
        keys[8].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonSelected = 8;
                sounds[8].start();
                if(justPlay == false){
                    correct = check();
                    if (correct == true) {
                        reset();
                    }
                    if (correct == false) {
                        coverWrong.setVisibility(View.VISIBLE);
                    }
                    if (timesWrong == 3) {
                        coverLose.setText("You lose! Your score was " + chordScore + ". Click to start new game.");
                        coverLose.setVisibility(View.VISIBLE);
                        coverWrong.setVisibility(View.GONE);
                    }
                }
            }
        });
        keys[9].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonSelected = 9;
                sounds[9].start();
                if(justPlay == false){
                    correct = check();
                    if (correct == true) {
                        reset();
                    }
                    if (correct == false) {
                        coverWrong.setVisibility(View.VISIBLE);
                    }
                    if (timesWrong == 3) {
                        coverLose.setText("You lose! Your score was " + chordScore + ". Click to start new game.");
                        coverLose.setVisibility(View.VISIBLE);
                        coverWrong.setVisibility(View.GONE);
                    }
                }
            }
        });
        keys[10].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonSelected = 10;
                sounds[10].start();
                if(justPlay == false){
                    correct = check();
                    if (correct == true) {
                        reset();
                    }
                    if (correct == false) {
                        coverWrong.setVisibility(View.VISIBLE);
                    }
                    if (timesWrong == 3) {
                        coverLose.setText("You lose! Your score was " + chordScore + ". Click to start new game.");
                        coverLose.setVisibility(View.VISIBLE);
                        coverWrong.setVisibility(View.GONE);
                    }
                }
            }
        });
        keys[11].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonSelected = 11;
                sounds[11].start();
                if(justPlay == false){
                    correct = check();
                    if (correct == true) {
                        reset();
                    }
                    if (correct == false) {
                        coverWrong.setVisibility(View.VISIBLE);
                    }
                    if (timesWrong == 3) {
                        coverLose.setText("You lose! Your score was " + chordScore + ". Click to start new game.");
                        coverLose.setVisibility(View.VISIBLE);
                        coverWrong.setVisibility(View.GONE);
                    }
                }
            }
        });

    }

    // Set up what chords the game will prompt for based on what the user selected
    public void setChords(){
        if(playMajor == true){
            major();
        }
        if(playMinor == true){
            minor();
        }
        if(playDominant == true){
            dominant();
        }
        if(playDiminished == true){
            diminished();
        }
        if(playMajor == true){
            major();
        }
        else if(playMinor == true){
            minor();
        }
        else if(playDominant == true){
            dominant();
        }
        else if(playDiminished == true){
            diminished();
        }
        // If the user selects playAll, chose what type of chord to prompt for randomly
        else if(playAll == true){
            Random randomChord = new Random();
            int ranChord = randomChord.nextInt(4);
            if(ranChord == 0){
                major();
            }
            if(ranChord == 1){
                minor();
            }
            if(ranChord == 2){
                dominant();
            }
            if(ranChord == 3){
                diminished();
            }
        }
        else if(justPlay == true); // Do nothing, just let the user play notes
    }

    // Check if the user played the right key
    public boolean check(){
        // Color the key green if it was correct
        for(i = 0; i < chordNotes.length; i++){
            if (buttonSelected == chordNotes[i]) {
                clicked[i] = true;
                //android.os.SystemClock.sleep(200);
                keys[buttonSelected].setBackgroundColor(Color.GREEN);
                return true;
            }
        }
        // Color the key red and increment the number of errors if it was incorrect
        keys[buttonSelected].setBackgroundColor(Color.RED);
        timesWrong++;
        return false;
    }

    // Reset everything keeping track of the current chord and set up for a new chord
    // if the user gets it right
    public void reset(){
        int allNotesSelected = 0;
        for(i = 0; i < clicked.length; i++){
            if(clicked[i] == true){
                allNotesSelected++;
            }
        }
        if(allNotesSelected == 4){
            coverCorrect.setVisibility(View.VISIBLE);
            for(i = 0; i < clicked.length; i++){
                clicked[i] = false;
            }
            for(i = 0; i < keys.length; i++){
                if((i != 1) && (i != 3) && (i != 6) && (i != 8) && (i != 10)) {
                    keys[i].setBackgroundColor(Color.WHITE);
                }
                if((i == 1) || (i == 3) || (i == 6) || (i == 8) || (i == 10)) {
                    keys[i].setBackgroundColor(Color.BLACK);
                }
            }
            // Set up the next chord
            setChords();
            // Increment the score
            chordScore++;
            score.setText("Score: " + chordScore);
        }
    }

    // Called once time is up or once the user makes three mistakes to reset the game
    public void resetGame() {
        for (i = 0; i < clicked.length; i++) {
            clicked[i] = false;
        }
        for (i = 0; i < keys.length; i++) {
            if ((i != 1) && (i != 3) && (i != 6) && (i != 8) && (i != 10)) {
                keys[i].setBackgroundColor(Color.WHITE);
            }
            if ((i == 1) || (i == 3) || (i == 6) || (i == 8) || (i == 10)) {
                keys[i].setBackgroundColor(Color.BLACK);
            }
        }
        stopTimer();
        playMajor = false;
        playMinor = false;
        playDominant = false;
        playDiminished = false;
        playAll = false;
        timesWrong = 0;
        chordScore = 0;
        score.setText("Score: " + chordScore);
        coverNewGame.setVisibility(View.GONE);
        coverLose.setVisibility(View.GONE);
        majorButton.setVisibility(View.VISIBLE);
        minorButton.setVisibility(View.VISIBLE);
        dominantButton.setVisibility(View.VISIBLE);
        diminishedButton.setVisibility(View.VISIBLE);
        allButton.setVisibility(View.VISIBLE);
        playButton.setVisibility(View.VISIBLE);
        introButton.setVisibility(View.VISIBLE);
    }

    // Timer for the game - the user gets only 2 minutes to play as many chords as they can
    private void startTimer() {
        myTimer = new Timer();
        myTimerTask = new TimerTask() {
            public void run() {
                mTimerHandler.post(new Runnable() {
                    public void run() {
                        if(timesWrong < 3){
                            coverNewGame.setText("Game Over! Your score was " + chordScore + ". Click to start new game.");
                            coverNewGame.setVisibility(View.VISIBLE);
                            stopTimer();
                        }
                        else{
                            stopTimer();
                        }
                    }
                });
            }
        };
        myTimer.schedule(myTimerTask, 120000);
    }

    // Stop the timer
    private void stopTimer(){
        if(myTimer != null){
            myTimer.cancel();
            myTimer.purge();
        }
    }

    // Prompt for a major chord and check if the notes played are correct 
    // based on the interval sizes in a major chord
    public void major(){
        // Text prompt
        chord.setText("Major 7");
        // Randomly select the starting note
        Random random = new Random();
        int r = random.nextInt(12);
        note.setText(noteNames[r]);
        chordNotes = new int[4];
        if(r <= 0){
            chordNotes[0] = r;
            chordNotes[1] = r+4;
            chordNotes[2] = r+7;
            chordNotes[3] = r+11;
        }
        else if(r <= 4){
            chordNotes[0] = r;
            chordNotes[1] = r+4;
            chordNotes[2] = r+7;
            chordNotes[3] = r-1;
        }
        else if(r <= 7){
            chordNotes[0] = r;
            chordNotes[1] = r+4;
            chordNotes[2] = r-5;
            chordNotes[3] = r-1;
        }
        else{
            chordNotes[0] = r;
            chordNotes[1] = r-8;
            chordNotes[2] = r-5;
            chordNotes[3] = r-1;
        }
    }

    // Prompt for a minor chord and check if the notes played are correct 
    // based on the interval sizes in a minor chord
    public void minor(){
        chord.setText("Minor 7");
        Random random = new Random();
        int r = random.nextInt(12);
        note.setText(noteNames[r]);
        chordNotes = new int[4];
        if(r <= 1){
            chordNotes[0] = r;
            chordNotes[1] = r+3;
            chordNotes[2] = r+7;
            chordNotes[3] = r+10;
        }
        else if(r <= 4){
            chordNotes[0] = r;
            chordNotes[1] = r+3;
            chordNotes[2] = r+7;
            chordNotes[3] = r-2;
        }
        else if(r <= 8){
            chordNotes[0] = r;
            chordNotes[1] = r+3;
            chordNotes[2] = r-5;
            chordNotes[3] = r-2;
        }
        else{
            chordNotes[0] = r;
            chordNotes[1] = r-9;
            chordNotes[2] = r-5;
            chordNotes[3] = r-2;
        }
    }

    // Prompt for a dominant chord and check if the notes played are correct 
    // based on the interval sizes in a dominant chord
    public void dominant(){
        chord.setText("Dominant 7");
        Random random = new Random();
        int r = random.nextInt(12);
        note.setText(noteNames[r]);
        chordNotes = new int[4];
        if(r <= 1){
            chordNotes[0] = r;
            chordNotes[1] = r+4;
            chordNotes[2] = r+7;
            chordNotes[3] = r+10;
        }
        else if(r <= 4){
            chordNotes[0] = r;
            chordNotes[1] = r+4;
            chordNotes[2] = r+7;
            chordNotes[3] = r-2;
        }
        else if(r <= 7){
            chordNotes[0] = r;
            chordNotes[1] = r+4;
            chordNotes[2] = r-5;
            chordNotes[3] = r-2;
        }
        else{
            chordNotes[0] = r;
            chordNotes[1] = r-8;
            chordNotes[2] = r-5;
            chordNotes[3] = r-2;
        }
    }

    // Prompt for a diminished chord and check if the notes played are correct 
    // based on the interval sizes in a diminshed chord
    public void diminished(){
        chord.setText("Diminished 7");
        Random random = new Random();
        int r = random.nextInt(12);
        note.setText(noteNames[r]);
        chordNotes = new int[4];
        if(r <= 2){
            chordNotes[0] = r;
            chordNotes[1] = r+3;
            chordNotes[2] = r+6;
            chordNotes[3] = r+9;
        }
        else if(r <= 5){
            chordNotes[0] = r;
            chordNotes[1] = r+3;
            chordNotes[2] = r+6;
            chordNotes[3] = r-3;
        }
        else if(r <= 8){
            chordNotes[0] = r;
            chordNotes[1] = r+3;
            chordNotes[2] = r-6;
            chordNotes[3] = r-3;
        }
        else{
            chordNotes[0] = r;
            chordNotes[1] = r-9;
            chordNotes[2] = r-6;
            chordNotes[3] = r-3;
        }
    }

}
