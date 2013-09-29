/**
 * Wifly RN-XV 
 * This code communicate with the android device over USB Socket and with the wifly over Serial3 connection.
 * This code implement us a mini client sever between the phone and wifly in order to send massages from one 
 * device to the other device.
 *
 *  | arduino mega 250                           wifly              |
 *  |       TX  pin 14              <=====>     pin 3 -  RX      |
 *  |       RX  pin 15               <=====>    pin 2 -  TX      |
 *  |       3.3V                            <=====>    Pin 1 - 3.3V    |
 *  |       GND                            <=====>    Pin 10 - GND  |
 *
 * @author      Shmulik Melamed
 * @author      Lital Motola
 * @version     v5.0
 * @since       1.0
 */

#include <SPI.h>
#include <Adb.h>
 
 
//.: Global variables.
Connection * connection;
int num = 0;
boolean SendToAndroid = false;
//===========================

//*************** ..:: adbEventHandler ::.. *********************//
/**
*  Event handler for shell connection; called whenever data sent from Android to Microcontroller.
* This methos get all of the user commands and send them to the drone over the wifly.
          if(data[0] == 1 ) { seq++; Serial.println("Up");                   stringOne="AT*PCMD=";  stringTwo=stringOne  + seq; stringThree=stringTwo + "301,1,0,0,1036831949,0\r"; Serial.println("AT*PCMD=301,1,0,0,1036831949,0\r");      Serial3.print("AT*PCMD=301,1,0,0,1036831949,0\r");dataLed(); }
*/ 
void adbEventHandler(Connection * connection, adb_eventType event, uint16_t length, uint8_t * data)
{
  // In this event Data packets contain three bytes:we use only one for all of the drone commands.
  if (event == ADB_CONNECTION_RECEIVE)
  {
          //Actions 
          
          if(data[0] == 1 ) {  Serial.println("Up");                     Serial.println("AT*PCMD=301,1,0,0,1036831949,0");      Serial3.print("AT*PCMD=301,1,0,0,1036831949,0\r");dataLed(); }
          if(data[0] == 2 ) {  Serial.println("Down");               Serial.println("AT*PCMD=302,1,0,0,-1110651699,0");    Serial3.print("AT*PCMD=302,1,0,0,-1110651699,0\r");dataLed();}          
          if(data[0] == 3 ) {  Serial.println("R_Right");       Serial.println("AT*PCMD=305,1,0,0,0,1036831949");      Serial3.print("AT*PCMD=305,1,0,0,0,1036831949\r");dataLed();}
          if(data[0] == 4 ) {  Serial.println("R_Left");          Serial.println("AT*PCMD=306,1,0,0,0,-1110651699");    Serial3.print("AT*PCMD=306,1,0,0,0,-1110651699\r");dataLed();}       
          if(data[0] == 5 ) {  Serial.println("Forward");       Serial.println("AT*PCMD=303,1,1036831949,0,0,0");      Serial3.print("AT*PCMD=303,1,1036831949,0,0,0\r"); dataLed();}
          if(data[0] == 6 ) {  Serial.println("Backward");    Serial.println("AT*PCMD=304,1,-1110651699,0,0,0");     Serial3.print("AT*PCMD=304,1,-1110651699,0,0,0\r"); dataLed();}          
          if(data[0] == 7 ) {  Serial.println("Right");             Serial.println("AT*PCMD=307,1,0,1036831949,0,0");      Serial3.print("AT*PCMD=307,1,0,1036831949,0,0\r");dataLed();}
          if(data[0] == 8 ) {  Serial.println("Left");                Serial.println("AT*PCMD=308,1,0,-1110651699,0,0");    Serial3.print("AT*PCMD=308,1,0,-1110651699,0,0\r");dataLed();}          
          if(data[0] == 9 ) {   Serial.println("Take_Off");     Serial.println("AT*REF=101,290718208");                        Serial.println("AT*PCMD=201,1,0,0,0,0\r");   Serial3.print("AT*REF=101,290718208\r");  Serial3.print("AT*PCMD=201,1,0,0,0,0\r"); dataLed();}  
          if(data[0] == 10 ) {  Serial.println("Lend");               Serial.println("AT*REF=102,290717696");                        Serial3.print("AT*REF=102,290717696\r");dataLed();}          
          if(data[0] == 11 ) {  Serial.println("Hovering");    Serial.println("AT*PCMD=201,1,0,0,0,0");                         Serial3.print("AT*PCMD=201,1,0,0,0,0\r");dataLed();}
          if(data[0] == 12 ) {  Serial.println("Emergency");  Serial.println("AT*REF=1,290717952");                           Serial3.print("AT*REF=1,290717952\r");dataLed();}
          
          
          
          //Testing
          if(data[0] == 30 ) { Serial.println("CheckADK"); SendToAndroid = true;}          
          Serial.println(data[0] );
  }
  else if (event == ADB_CONNECTION_OPEN) Serial.println("ADB connection open");
  else if (event == ADB_CONNECTION_CLOSE) Serial.println("ADB connection close");
}
 //************************************************************************//   

//************************ ..:: dataLed ::.. **************************//
/**
* dataLed methos is called when we want to indicate that data is transfer from one device to another 
* by blink up the pin 13 led on the arduino mega for three seconds.
*/ 
 void dataLed(){
  digitalWrite(13, HIGH);
  delay(3000);
  digitalWrite(13, LOW);
 }
 //************************************************************************//   

//******************** ..:: sendAndroid ::.. ***********************//
/**
* Called when we want to send massage to the android phone that indicate that there is 
* a connection between the two devices.
*/ 
 void sendAndroid() {
  SendToAndroid = false;
   num = 30;
  connection->write(2,(uint8_t*)&num);  // Send 2 bytes to Android
  digitalWrite(13, HIGH);
  delay(3000);
  digitalWrite(13, LOW);
}
 //************************************************************************//   

//************************** ..:: setup ::.. **************************//
/**
* setup called once when the code start running and he initialize
* the wifly to data rate 57600 and the  arduino serial to data rate 9600
* also he initialize and Open a tcp socket on port 4568 in the ADB.
*/
void setup()
{
  digitalWrite(13, LOW);
  // Init serial port for debugging
  Serial.begin(57600);
  Serial3.begin(9600);
 
  // Init the ADB subsystem.  
  ADB::init();
 
  // Open an ADB stream to the phone's shell. Auto-reconnect.
  connection = ADB::addConnection("tcp:4568", true, adbEventHandler);  
}
 //************************************************************************//   

//**************************** ..:: loop ::.. **************************//
 /**
* loop called over and over always 
* this loop Poll the ADB subsystem.
* this loop checking if we need to send something to the android device, if yes then we start the sendAndroid method.
*/
void loop()
{
  if(SendToAndroid == true) sendAndroid();
  // Poll the ADB subsystem.
  ADB::poll();
}
 //************************************************************************//   


