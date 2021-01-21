
#include <SoftwareSerial.h>


int compteur = 0;
int simu;
int maxL = 500;
int maxP = 1000;
int minL = 0;
int minP = 501;
int tempsL = 10000;

SoftwareSerial bluetooth(10,11); // (RX, TX) (pin Rx BT, pin Tx BT)

void setup()
{
                       // Ouvre la voie série avec l'ordinateur
    Serial.begin(9600);
                      // Ouvre la voie série avec le module BT
    bluetooth.begin(9600);
    Serial.print("début");
}

void loop() {       // Ici on simule les variations des ondes éléctromagnétiques avec un temps donné (au bout de 10000 répétition on passe du sommeil léger a profond) 
  compteur +=1;     // En l'absence des capteurs on est forcés de définir un "temps" de sommeil léger et un "temps" de sommeil profond représenté ici par le compteur
  if (compteur < tempsL){
    bluetooth.print(random(minL,maxL));
  }else{           // Passage au sommeil profond
    bluetooth.print(random(minP,maxP));
  }
}

// Si nous étions en possésion du capteur, il aurais falu ici importer la blibliothèque "arduino-api-master" la fonction loop aurais été :
/*
 * void loop(){
 * 
 *  BITalinoFrame frame;
 *  bluetooth.print(BITalino.read(1, &frame));
 *  if (n != 1)
 *  {
 *    statusPort.println("Error while receiving a frame");
 *    return;
 *  }
 * 
 */
