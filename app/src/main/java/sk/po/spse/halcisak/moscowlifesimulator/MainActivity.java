package sk.po.spse.halcisak.moscowlifesimulator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {






        private SharedPreferences settings;
        private Timer timerB;
        private Timer timerO;
        private Handler handler = new Handler();
        private DisplayMetrics displayMetrics;
        private MediaPlayer mediaPlayer;

        private FrameLayout pozadie;
        private ImageView opica;
        private ImageView budova;
        private TextView HscoreView;
        private TextView ScoreView;
        private TextView play;

        private float budovaX, budovaY, opicaY;
        private boolean skacem, idemhore;
        private int perioda = 10;

        private int hScore;
        private int score;

        private boolean prehra;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getSupportActionBar().hide();
            pozadie = findViewById(R.id.pozadie);
            opica = findViewById(R.id.spiderman);
            budova = findViewById(R.id.auto);
            HscoreView = findViewById(R.id.highScoreView);
            ScoreView = findViewById(R.id.scoreView);
            play = findViewById(R.id.play);
            settings = getSharedPreferences("SKAKACKA_DATA", Context.MODE_PRIVATE);
            hScore = settings.getInt("HIGH_SCORE", 0);
            HscoreView.setText("High score : "+hScore);
            ScoreView.setText("0 : Score");
            novaHra();
        }
        public void novaHra(){
            if (timerB !=null){
                timerB.cancel();
            }
            if (timerO !=null){
                timerO.cancel();
            }


            displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

            play.setVisibility(View.VISIBLE);
            opica.setVisibility(View.GONE);
            budova.setVisibility(View.GONE);

           // HscoreView.setText("High score : "+hScore);
            HscoreView.setText("High score : "+opica.getY());



            budovaX = opica.getX();
            budova.setX(budovaX);
            budovaY =(int)((displayMetrics.heightPixels/2)+(displayMetrics.heightPixels/8));
            budova.setY(budovaY);

        }
        public void start(View view){
          //  opica.setImageResource(R.drawable.neskace);
            score = 0;
            ScoreView.setText(score+" : Score");
            skacem=false;
            opicaY = 0;
            opica.setY(opicaY);
            play.setVisibility(View.GONE);
            opica.setVisibility(View.VISIBLE);
            budova.setVisibility(View.VISIBLE);
            prehra=false;
            timerB = new Timer();
            timerB.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            posunAuto();
                            if (!idemhore &&  !(opica.getX()+opica.getWidth()>=budovaX && opica.getX()<=budovaX+budova.getWidth() && opica.getY()-opica.getHeight()==budova.getY()-budova.getHeight())) {
                                spadni();
                            }
                            HscoreView.setText("High score : "+opica.getY());
                            if(koniec()){
                                if (score>hScore)
                                    hScore=score;
                                prehra=true;
                                novaHra();
                            }

                        }
                    });
                }
            }, 0, 10);
            if (!prehra){
                pozadie.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (!skacem){
                         //   opica.setImageResource(R.drawable.skace);
                            skacem=true;
                            idemhore=true;

                        /*final Handler handl = new Handler();
                        Runnable runnable = new Runnable() {

                            public void run() {
                                if (idemhore)
                                    vyskoc();

                                else spadni();
                                handl.postDelayed(this, 10); // for interval...
                            }
                        };
                        handl.postDelayed(runnable, 1);*/

                            timerO = new Timer();
                            timerO.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (idemhore)
                                                vyskoc();

                                        }
                                    });

                                }

                            }, 0, 10);
                        }
                    }
                });
            }

        }

        public void posunAuto(){
            if (budovaX < -budova.getWidth()*2){
                budovaX =displayMetrics.widthPixels;
                budova.setX(budovaX);
                score++;
           //     mediaPlayer = MediaPlayer.create(this ,R.raw.coin_effect);
 //               mediaPlayer.start();
                ScoreView.setText(score+" : Score");
            }

            budovaX -= 2;
            budova.setX(budovaX);

        }
        //450
        public void vyskoc(){
            //mediaPlayer = MediaPlayer.create(this ,R.raw.jump_effect);
            //mediaPlayer.start();
            if (idemhore){
                opicaY -=4;
                opica.setY(opicaY);
                if (opicaY < 50){
                    idemhore=false;
                    timerO.cancel();
                    skacem=false;
                }
            }

        }
        public void spadni(){
            opicaY +=2;
            opica.setY(opicaY);


           //     opica.setImageResource(R.drawable.neskace);

               // opica.setY(o);

        }
        public boolean koniec(){
            if (opica.getY()>displayMetrics.heightPixels ){
           //     mediaPlayer = MediaPlayer.create(this ,R.raw.splat_effect);
         //       mediaPlayer.start();
                return true;
            }
            return false;
        }
        @Override
        protected void onStop() {
            super.onStop();
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("HIGH_SCORE", hScore);
            editor.apply();
            if (timerB != null || timerO != null) {
                timerO.cancel();
                timerB.cancel();
                timerO = null;
                timerB = null;

            }
        }
    }