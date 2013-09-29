/**
 * Wifly RN-XV 
 * This code help us to communicate with the wifly in order to configure it.
 * the configure the wifly we need to upload this script open the console
 * and change the data rate to 9600 and then we can start sent commands to the 
 * wifly in order to configure it.
 *
 *  | arduino mega 250                           wifly    |
 *  |      TX  pin 14              <=====>     pin 3 RX  |
 *  |       RX pin 15              <=====>     pin 2 TX  |
 *
 * @author      Shmulik Melamed
 * @author      Lital Motola
 * @version     v5.0
 * @since       1.0
 */


//************************** ..:: setup ::.. **************************//
/**
* setup called once when the code start running and he initialize
* the wifly and arduino serial to data rate 9600
*/
void setup()  
{
  // Open serial communications and wait for port to open:
  Serial.begin(9600);
  Serial3.begin(9600);
  while (!Serial) {
    ; // wait for serial port to connect. 
  }

  Serial.println("Goodnight moon!"); //print on arduino
  Serial3.println("Hello, world?"); //print on wifly
}
//************************************************************************//   

//**************************** ..:: loop ::.. **************************//
/**
* loop called over and over always 
* this loop checking if we got respone from the user(arduino serail) if yes then print it and send it to the wifly
* this loop checking if we got response from the wifly if yes print it and send it to the arduino serail
*/
void loop() // run over and over
{
  if (Serial3.available())
    Serial.write(Serial3.read());
  if (Serial.available())
    Serial3.write(Serial.read());
}
//************************************************************************//   

