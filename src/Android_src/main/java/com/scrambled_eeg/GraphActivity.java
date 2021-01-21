/** GraphActivity
 *  Activité affichant les données en temps réel
 */

package com.scrambled_eeg;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.*;
import android.content.*;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.UUID;

public class GraphActivity extends AppCompatActivity implements Runnable {

    // Liste des différents bouttons
    private Button back;
    private Button plus;
    private Button minus;
    private Button p30;
    private Button connect;

    private FileWriter fw;


    private TextView data;
    private TextView state;

    private long dodoTime = 0;
    private long dodoNeed = 30000L;
    private int valState = 0;
    private boolean isConnected = false;

    Thread thread;

    private int[] stateBorder = {1,40,75,120,300,1000};

    private long time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_graph);

        // Initialisation des Views
        this.back = (Button) findViewById(R.id.back);
        this.plus = (Button) findViewById(R.id.plus);
        this.minus = (Button) findViewById(R.id.minus);
        this.p30 = (Button) findViewById(R.id.add30min);
        this.connect = (Button) findViewById(R.id.connect);
        this.state = (TextView) findViewById(R.id.state);
        this.data = (TextView) findViewById(R.id.data);

        fw = new FileWriter("Sleep_" + new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).toString()+ ".eeg");

        // Définition de ce que font les bouttons
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MainActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(MainActivity);
                finish();
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str;
                if (valState < 4) valState+=1;
                if (!isConnected) {
                    return;
                }
                switch(valState) {
                    case 0:
                        str = "Sommeil Profond";
                        break;
                    case 1:
                        str = "Sommeil Paradoxal";
                        break;
                    case 2:
                        str = "Sommeil Léger";
                        break;
                    case 3:
                        str = "Éveil";
                        break;
                    case 4:
                        str = "Éveillé";
                        break;
                    default:
                        str = "broken";
                }
                state.setText("Etat = " + str);

            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str;
                if (valState > 0) valState -=1;
                switch(valState) {
                    case 0:
                        str = "Sommeil Profond";
                        break;
                    case 1:
                        str = "Sommeil Paradoxal";
                        break;
                    case 2:
                        str = "Sommeil Léger";
                        break;
                    case 3:
                        str = "Éveil";
                        break;
                    case 4:
                        str = "Éveillé";
                        break;
                    default:
                        str = "broken";
                }
                state.setText("Etat = " + str);
            }
        });

        p30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dodoTime += 30000000L;
            }
        });

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isConnected = !isConnected;
            }
        });

        time = System.currentTimeMillis();

        thread = new Thread(this);

        // lancement de la loop
        thread.run();


    }


    /**
     * MediaPlayer
     *
     * Fonction pour le réveil
     * */

    public void playSound(){

        MediaPlayer medp = MediaPlayer.create(getApplicationContext(), R.raw.beep);
        medp.setVolume(100f,100f);
        medp.start();

    }

    /** Thread
     *
     *  Loop Principal, active la liaison bluetooth et enregistre la data échangée
     *
     * */
    @Override
    public void run() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothServerSocket bluetoothServerSocket;
        BluetoothSocket bluetoothSocket;
        try {
            bluetoothServerSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord(bluetoothAdapter.getName(), UUID.randomUUID());
            bluetoothSocket = bluetoothServerSocket.accept();


            int length = 0;
            byte[] buffer = new byte[256];


            while (true) {
                time = System.currentTimeMillis();
                length = bluetoothSocket.getInputStream().read(buffer);
                if(buffer.length < length) length = buffer.length;
                fw.writeIn(buffer, length);

                data.setText(buffer[length-1]);

                if(buffer[0] < stateBorder[1] && buffer[length-1] < stateBorder[1]){

                    dodoTime += System.currentTimeMillis() - time;
                    setValState(buffer[length-1]);
                    

                }
                if(valState >=2 && dodoTime >= dodoNeed){
                    playSound();
                    System.out.println("test");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Met à jour la valeur State et la longueur d'onde
    public void setValState(byte ret){

        String str;;
        if(ret < stateBorder[1]) {
            str = "Sommeil Profond";
            valState = 0;
        }else if (ret < stateBorder[2]) {
                str = "Sommeil Paradoxal";
            valState = 1;
        }else if(ret < stateBorder[3]) {
            str = "Sommeil Léger";
            valState = 2;
        }else if( ret < stateBorder[4]) {
            str = "Éveil";
            valState = 3;
        }else{
            str = "Éveillé";
            valState = 4;
        }
        state.setText("Etat = " + str);
        data.setText((float)ret/10);

    }

}













