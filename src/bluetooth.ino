/*
  L'arduino sert éssentiellement à faire la connexion entre le module EEG et l'application Android.
*/

int analogPin = A3; // Entrée de données de l'EEG

void setup() {
  Serial.begin(38400); // Débit par défaut du module bluetooth HC-05
}

void loop() {
  // Lis le flux de donnée venant de l'entrée analogique, et l'envoit vers le module bluetooth qui l'envoit vers le téléphone
  Serial.println(analogRead(analogPin)); 
  
  // Sleep(), potentiellement pas besoin de récolter les donnée de l'EEG continuellement, à tester
}
