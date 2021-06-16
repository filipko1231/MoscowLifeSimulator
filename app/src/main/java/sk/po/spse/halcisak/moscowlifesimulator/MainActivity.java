package sk.po.spse.halcisak.moscowlifesimulator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import java.sql.Timestamp;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {






    private SharedPreferences settings;
    private Timer timerB;
    private Timer timerO;
    private Handler handler = new Handler();
    private DisplayMetrics displayMetrics;
    private MediaPlayer mediaPlayer;

    private ImageView pozadie3;
    private FrameLayout pozadie;
    private ImageView opica;
    private ImageView budova,budova2,budova3,budova4;
    private TextView HscoreView;
    private TextView ScoreView;
    private TextView play;
    private double rychlost = 1.5;
    private double aktualnarychlost = 1.5;

    private int dashcount=0,generationcoun=0;

    private float budovaX, budovaY, opicaY, budova2x, budova2y,budova3x, budova3y,budova4x, budova4y;
    private boolean start=true;
    private boolean idemhore;
    private boolean bezim;
    private boolean firstStart=false;

    private boolean dash;


    private int hScore;
    private int score;

    private boolean prehra;
    Random rn=new Random();
    float vyskaSkoku=35;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        pozadie = findViewById(R.id.pozadie);


        opica = findViewById(R.id.opica);
        budova = findViewById(R.id.auto);
        budova2 = findViewById(R.id.auto2);
        budova3 = findViewById(R.id.auto3);
        budova4 = findViewById(R.id.auto4);
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
        rychlost = 1.5;
        aktualnarychlost = 1.5;
        generationcoun=0;
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        play.setVisibility(View.VISIBLE);
        opica.setVisibility(View.GONE);
        budova.setVisibility(View.GONE);
        budova2.setVisibility(View.GONE);
        budova3.setVisibility(View.GONE);
        budova4.setVisibility(View.GONE);
        HscoreView.setText("High score : "+hScore);



        //budovaX = opica.getX()+2100;
        //budova.setX(budovaX);
        //budovaY =(int)((displayMetrics.heightPixels/2)+(displayMetrics.heightPixels/8));
        //budova.setY(budovaY);
        if (firstStart){
            opica.setY((displayMetrics.heightPixels/2)+(displayMetrics.heightPixels/8));
            opica.setImageResource(R.drawable.idle_monkey_ver2);
            budova2x=opica.getX();
            budova2y=opica.getY()+opica.getWidth();
            budova2.setY(budova2y);
            budova2.setX(budova2x);
            opica.setVisibility(View.VISIBLE);
            budova2.setVisibility(View.VISIBLE);
        }

        firstStart=true;
        //dash=true;

    }
    public void start(View view){
        budovaX = opica.getX()+1500;
        budova.setX(budovaX);
        budovaY =(int)((displayMetrics.heightPixels/2)+(displayMetrics.heightPixels/8));
        budova.setY(budovaY);
        budova2x=budovaX+900;
        budova2y=budovaY+200;
        budova2.setY(budova2y);
        budova2.setX(budova2x);
        budova3x=budovaX+2000;
        budova3y=budovaY+100;
        budova3.setY(budova3y);
        budova3.setX(budova3x);
        budova4x=budovaX+2900;
        budova4y=budovaY+150;
        budova4.setY(budova4y);
        budova4.setX(budova4x);
        dash=true;

        score = 0;
        ScoreView.setText(score+" : Score");
        rychlost=aktualnarychlost;
        bezim=false;
        dash=true;
        opicaY = -500;
        opica.setY(opicaY);
        opica.setImageResource(R.drawable.running_monkey);
        play.setVisibility(View.GONE);
        opica.setVisibility(View.VISIBLE);
        budova.setVisibility(View.VISIBLE);
        budova2.setVisibility(View.VISIBLE);
        budova3.setVisibility(View.VISIBLE);
        budova4.setVisibility(View.VISIBLE);
        prehra=false;
        timerB = new Timer();
        timerB.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        posunAuto();
                        posunAuto2();
                        posunAuto3();
                        posunAuto4();
                        dashcount--;

                        if (!idemhore && !bezim && ((opica.getX() + opica.getWidth() >= budovaX && opica.getX() <= budovaX + budova.getWidth() && opica.getY() - opica.getHeight() >= budova.getY() - budova.getHeight()) || (opica.getX() + opica.getWidth() >= budova2x && opica.getX() <= budova2x + budova2.getWidth() && opica.getY() - opica.getHeight() >= budova2.getY() - budova2.getHeight()) || (opica.getX() + opica.getWidth() >= budova3x && opica.getX() <= budova3x + budova3.getWidth() && opica.getY() - opica.getHeight() >= budova3.getY() - budova3.getHeight()) || (opica.getX() + opica.getWidth() >= budova4x && opica.getX() <= budova4x + budova4.getWidth() && opica.getY() - opica.getHeight() >= budova4.getY() - budova4.getHeight())))
                        {
                            bezim=true;
                            rychlost=aktualnarychlost;
                            dash=false;
                            opica.setImageResource(R.drawable.running_monkey);
                        }
                        else if (!idemhore &&  !(opica.getX()+opica.getWidth()>=budovaX && opica.getX()<=budovaX+budova.getWidth() && opica.getY()-opica.getHeight()==budova.getY()-budova.getHeight()) && !(opica.getX()+opica.getWidth()>=budova2x && opica.getX()<=budova2x+budova.getWidth() && opica.getY()-opica.getHeight()==budova2.getY()-budova2.getHeight())&& !(opica.getX()+opica.getWidth()>=budova3x && opica.getX()<=budova3x+budova.getWidth() && opica.getY()-opica.getHeight()==budova3.getY()-budova3.getHeight())&& !(opica.getX()+opica.getWidth()>=budova4x && opica.getX()<=budova4x+budova.getWidth() && opica.getY()-opica.getHeight()==budova4.getY()-budova4.getHeight())) {
                            spadni();
                            bezim=false;
                        }

                        if ((opica.getX()+opica.getWidth()>=budovaX-3 && opica.getX()+opica.getWidth()-budova.getWidth()<=budovaX && opica.getY()-opica.getHeight()>budovaY-budova.getHeight())) {
                            rychlost=0;
                        }
                        else if ((opica.getX()+opica.getWidth()>=budova2x-3 && opica.getX()+opica.getWidth()-budova2.getWidth()<=budova2x && opica.getY()-opica.getHeight()>budova2y-budova2.getHeight())){
                            rychlost=0;
                        }
                        else if ((opica.getX()+opica.getWidth()>=budova3x-3 && opica.getX()+opica.getWidth()-budova3.getWidth()<=budova3x && opica.getY()-opica.getHeight()>budova3y-budova3.getHeight())){
                            rychlost=0;
                        }
                        else if ((opica.getX()+opica.getWidth()>=budova4x-3 && opica.getX()+opica.getWidth()-budova4.getWidth()<=budova4x && opica.getY()-opica.getHeight()>budova4y-budova4.getHeight())){
                            rychlost=0;
                        }else if (dashcount<=0){
                            rychlost=aktualnarychlost;
                        }else if (dash){

                            if ((opica.getX()+opica.getWidth()>=budovaX-11 && opica.getX()+opica.getWidth()-budova.getWidth()<=budovaX && opica.getY()-opica.getHeight()>budovaY-budova.getHeight())) {
                                rychlost=0;
                            }
                            else if ((opica.getX()+opica.getWidth()>=budova2x-11 && opica.getX()+opica.getWidth()-budova2.getWidth()<=budova2x && opica.getY()-opica.getHeight()>budova2y-budova2.getHeight())){
                                rychlost=0;
                            }
                            else if ((opica.getX()+opica.getWidth()>=budova3x-11 && opica.getX()+opica.getWidth()-budova3.getWidth()<=budova3x && opica.getY()-opica.getHeight()>budova3y-budova3.getHeight())){
                                rychlost=0;
                            }
                            else if ((opica.getX()+opica.getWidth()>=budova4x-11 && opica.getX()+opica.getWidth()-budova4.getWidth()<=budova4x && opica.getY()-opica.getHeight()>budova4y-budova4.getHeight())){
                                rychlost=0;
                            }
                            else{
                                rychlost=10;
                            }
                        }




                        HscoreView.setText("High score : "+hScore);
                        if(koniec()){
                            if (score>hScore)
                                hScore=score;
                            prehra=true;
                            novaHra();
                        }

                    }
                });
            }
        }, 0, 1);

        if (!prehra){
            pozadie.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (bezim) {
                        if ((opica.getX() + opica.getWidth() >= budovaX && opica.getX()-opica.getWidth() <= budovaX + budova.getWidth() && opica.getY() - opica.getHeight() >= budova.getY() - budova.getHeight()) || (opica.getX() + opica.getWidth() >= budova2x && opica.getX()-opica.getWidth() <= budova2x + budova2.getWidth() && opica.getY() - opica.getHeight() >= budova2.getY() - budova2.getHeight()) || (opica.getX() + opica.getWidth() >= budova3x && opica.getX()-opica.getWidth() <= budova3x + budova3.getWidth() && opica.getY() - opica.getHeight() >= budova3.getY() - budova3.getHeight()) || (opica.getX() + opica.getWidth() >= budova4x && opica.getX()-opica.getWidth() <= budova4x + budova4.getWidth() && opica.getY() - opica.getHeight() >= budova4.getY() - budova4.getHeight())) {
                            bezim = false;
                            idemhore = true;
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
                            opica.setImageResource(R.drawable.jumping_monkey);
                        }
                    }
                    else if(!dash) {
                        dash=true;
                        dashcount=100;

                    }

                }
            });

        }


    }

    public void posunAuto(){
        if (budovaX < -budova.getWidth()*3){
            budovaX =displayMetrics.widthPixels+500;
            budovaY = rn.nextInt((displayMetrics.heightPixels-50)-(displayMetrics.heightPixels-budova.getHeight()))+displayMetrics.heightPixels-budova.getHeight();
            score++;
            ScoreView.setText(score+" : Score");
            if (aktualnarychlost<3){
                aktualnarychlost=aktualnarychlost+0.05;
            }
            if (budova4x+opica.getWidth()<=budovaX && budova4x+budova.getWidth()>=budovaX)
            {
                budovaX=budova4x+budova.getWidth();

                generationcoun++;
                //budova.setImageResource(R.drawable.budova12);
                if (generationcoun>=3)
                {
                    budovaX=budovaX+budova.getWidth();
                    generationcoun=0;
                }else
                {
                    budovaY=budova4y;
                }
            }else
            {
                generationcoun=0;
                //budova.setImageResource(R.drawable.budova1);
            }

            budova.setY(budovaY);
            budova.setX(budovaX);

        }

        budovaX -= rychlost;


        budova.setX(budovaX);

    }
    public void posunAuto2(){
        if (budova2x < -budova2.getWidth()*2){
            budova2x =budovaX+rn.nextInt(900)+350;
            budova2y = rn.nextInt((displayMetrics.heightPixels-50)-(displayMetrics.heightPixels-budova.getHeight()))+displayMetrics.heightPixels-budova.getHeight();
            if (budova2x-budovaX<budova.getWidth())
            {
                budova2x=budovaX+budova.getWidth();

                //budova2.setImageResource(R.drawable.budova1);
                generationcoun++;
                if (generationcoun>=3)
                {
                    budova2x=budova2x+budova.getWidth();
                    generationcoun=0;
                }else {
                    budova2y=budovaY;
                }
            }else
            {
                budova2x+=opica.getWidth();
                generationcoun=0;
                //budova2.setImageResource(R.drawable.budova12);
            }

            budova2.setY(budova2y);
            budova2.setX(budova2x);
            score++;
            ScoreView.setText(score+" : Score");
        }

        budova2x -= rychlost;
        budova2.setX(budova2x);

    }
    public void posunAuto3(){
        if (budova3x < -budova3.getWidth()*2){
            budova3x =budova2x+rn.nextInt(900)+350;
            budova3y = rn.nextInt((displayMetrics.heightPixels-50)-(displayMetrics.heightPixels-budova.getHeight()))+displayMetrics.heightPixels-budova.getHeight();
            if (budova3x-budova2x<budova.getWidth())
            {
                budova3x=budova2x+budova.getWidth();

                generationcoun++;
                //budova3.setImageResource(R.drawable.budova12);
                if (generationcoun>=3)
                {
                    budova3x=budova3x+budova.getWidth();
                    generationcoun=0;
                }else
                {
                    budova3y=budova2y;
                }
            }else
            {
                budova3x+=opica.getWidth();
                //budova3.setImageResource(R.drawable.budova13);
                generationcoun=0;
            }

            budova3.setX(budova3x);
            budova3.setY(budova3y);
            score++;
            ScoreView.setText(score+" : Score");
        }

        budova3x -= rychlost;
        budova3.setX(budova3x);

    }
    public void posunAuto4(){
        if (budova4x < -budova4.getWidth()*2){
            budova4x =budova3x+rn.nextInt(900)+350;
            budova4y = rn.nextInt((displayMetrics.heightPixels-50)-(displayMetrics.heightPixels-budova.getHeight()))+displayMetrics.heightPixels-budova.getHeight();
            if (budova4x-budova3x<budova.getWidth())
            {
                budova4x=budova3x+budova.getWidth();

                //budova4.setImageResource(R.drawable.budova13);
                generationcoun++;
                if (generationcoun>=3)
                {
                 budova4x=budova4x+budova.getWidth();
                 generationcoun=0;
                }else
                {
                    budova4y=budova3y;
                }
            }else
            {
                budova4x+=opica.getWidth();
                generationcoun=0;
                //budova4.setImageResource(R.drawable.budova12);
            }
            budova4.setX(budova4x);
            budova4.setY(budova4y);
            score++;
            ScoreView.setText(score+" : Score");
        }

        budova4x -= rychlost;
        budova4.setX(budova4x);

    }
    //450
    public void vyskoc(){
        if (idemhore){
            opicaY -=vyskaSkoku;
            vyskaSkoku-=1;
            opica.setY(opicaY);
            if (vyskaSkoku<=0){
                idemhore=false;
                timerO.cancel();
                vyskaSkoku=30;
                opica.setImageResource(R.drawable.falling_monkey_v1);
            }
        }

    }
    public void spadni(){
        opicaY +=1;
        opica.setY(opicaY);

    }
    public boolean koniec(){
        if (opica.getY()-opica.getHeight()>displayMetrics.heightPixels ){
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