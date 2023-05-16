package com.example.testapp;

import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import java.util.Random;

public class Utilities {
    static String[] tips = {
        "Assicurati di impostare correttamente il tuo peso nell'app per ottenere una stima accurata delle calorie bruciate.",
        "Mantieni una postura corretta sulla sedia a rotelle per massimizzare il numero di calorie bruciate durante l'attività fisica.",
        "Se possibile, programma le tue attività fisiche in luoghi sicuri e ben illuminati per garantire la tua sicurezza.",
        "Ricorda di bere molta acqua durante l'esercizio fisico per evitare la disidratazione.",
        "Prova ad aumentare gradualmente la durata e l'intensità delle tue attività fisiche per evitare lesioni o fatiche eccessive.",
        "Usa l'app per monitorare la tua attività fisica nel tempo e impostare obiettivi realistici per te stesso.",
        "Se hai bisogno di aiuto o supporto, non esitare a contattare un professionista sanitario o un allenatore qualificato.",
        "Ricorda di fare sempre un riscaldamento adeguato prima di iniziare l'esercizio fisico per evitare lesioni.",
        "Se noti sintomi di affaticamento eccessivo o dolore, interrompi l'esercizio fisico e contatta un professionista sanitario."
    };
    public static String randomTips() {
        return tips[new Random().nextInt(tips.length)];
    }
    public static boolean checkPermission(Context context, String permission){
        return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }
}
