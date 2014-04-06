/*
References:
1. Arduino code example: WebServer
2. http://www.dfrobot.com/wiki/index.php?title=X-Board_V2_(SKU:DFR0162)
3. http://bildr.org/2011/06/arduino-ethernet-pin-control/
4. http://www.lauridmeyer.com/2012/04/bidirectional-tcp-communication-between-android-smartphone-and-arduino-using-the-ethernetshield/
5. http://www.arduino.cc/en/Tutorial/Switch#.Uy0Y0D_V-RZ
6. http://danthompsonsblog.blogspot.com/2011/12/arduino-push-button-onoff-example.html
7. http://www.ladyada.net/learn/arduino/lesson5.html
8. http://www.multiwingspan.co.uk/arduino.php?page=led4
9. http://www.seeedstudio.com/wiki/Arduino_Sidekick_Basic_Kit
*/

#include <SPI.h>
#include <Ethernet.h>

// Enter a MAC address and IP address for your controller below.
// The IP address will be dependent on your local network:
byte mac[]  = {0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED};
IPAddress ip(192,168,137,222);

// Initialize the Ethernet server library
// with the IP address and port you want to use
EthernetServer server(8888);
EthernetClient client;

// constants won't change. They're used here to 
// set pin numbers:
const int LED1 = 6;
const int LED2 = 7;
const int LED3 = 8;
const int LED4 = 9;
const int button1 = 2; 
const int button2 = 3;
const int button3 = 4;
const int button4 = 5;

// Variables will change:
int LED1State = LOW;
int LED2State = LOW;
int LED3State = LOW;
int LED4State = LOW;
int button1State = LOW;
int button2State = LOW;
int button3State = LOW;
int button4State = LOW;
int reading1, reading2, reading3, reading4;

// the following variables are long's because the time, measured in miliseconds,
// will quickly become a bigger number than can be stored in an int.
long time = 0;        // the last time the output pin was toggled
long debounce = 200;  // the debounce time; increase if the output flickers

const String CMD_1_ON   = "cmd=11"; // command to turn ON LED 1
const String CMD_2_ON   = "cmd=21"; // command to turn ON LED 2
const String CMD_3_ON   = "cmd=31"; // command to turn ON LED 3
const String CMD_4_ON   = "cmd=41"; // command to turn ON LED 4
const String CMD_1_OFF  = "cmd=12"; // command to turn OFF LED 1
const String CMD_2_OFF  = "cmd=22"; // command to turn OFF LED 2
const String CMD_3_OFF  = "cmd=32"; // command to turn OFF LED 3
const String CMD_4_OFF  = "cmd=42"; // command to turn OFF LED 4
const String CMD_GET_STATUS = "cmd=99"; // command to get all LEDs state

String commandline = ""; // where incoming commands are stored

void setup() { 
  Serial.begin(57600); // XBee module baud rate
  while (!Serial) {
    ; // wait for serial port to connect. Needed for Leonardo only
  }
  
  pinMode(LED1, OUTPUT);
  pinMode(LED2, OUTPUT);
  pinMode(LED3, OUTPUT);
  pinMode(LED4, OUTPUT);
  
  pinMode(button1, INPUT);
  pinMode(button2, INPUT);
  pinMode(button3, INPUT);
  pinMode(button4, INPUT);
  
  // start the Ethernet connection and the server:
  Ethernet.begin(mac, ip);
  server.begin();
  
  Serial.println("Server is at "+Ethernet.localIP());
  delay(100);
}

void loop() {
  // listen for incoming clients, and process request.
  client = server.available();
  
  if (client) {
    if (client.connected()) { // if a client is connected 
      if (client.available()) { // if client is sending
        char c = client.read(); // reading the inputs from the client
      
        if (c == '\n') { // if a newline character is sent from the client (commandline is fully received)
          processCommand(commandline);
          commandline = ""; //reset the commandline string
        }
        else {
          commandline += c; // add to the command string
        }
      }
    }
    
    delay(100); // give the client time to receive the data
    //client.stop(); // close the connection:
    //Serial.println("client disonnected");
  }
  
  processButtonAction(button1);
  processButtonAction(button2);
  processButtonAction(button3);
  processButtonAction(button4);
}

void processButtonAction(int buttonPin) {
  switch (buttonPin) {
    case button1:
      // read the state of the switch into a local variable:
      reading1 = digitalRead(button1);
      
      // if the input just went from LOW and HIGH and we've waited long enough
      // to ignore any noise on the circuit, toggle the output pin and remember
      // the time
      if (reading1 == HIGH && button1State == LOW && millis() - time > debounce) {
        if (LED1State == HIGH) {
          triggerPin(LED1, false);
          LED1State = LOW;
        }
        else {
          triggerPin(LED1, true);
          LED1State = HIGH;
        }
    
        time = millis();    
      }
      
      button1State = reading1;
      break;
    case button2:
      // read the state of the switch into a local variable:
      reading2 = digitalRead(button2);
      
      // if the input just went from LOW and HIGH and we've waited long enough
      // to ignore any noise on the circuit, toggle the output pin and remember
      // the time
      if (reading2 == HIGH && button2State == LOW && millis() - time > debounce) {
        if (LED2State == HIGH) {
          triggerPin(LED2, false);
          LED2State = LOW;
        }
        else {
          triggerPin(LED2, true);
          LED2State = HIGH;
        }
    
        time = millis();    
      }
      
      button2State = reading2; 
      break;
    case button3:
      // read the state of the switch into a local variable:
      reading3 = digitalRead(button3);
      
      // if the input just went from LOW and HIGH and we've waited long enough
      // to ignore any noise on the circuit, toggle the output pin and remember
      // the time
      if (reading3 == HIGH && button3State == LOW && millis() - time > debounce) {
        if (LED3State == HIGH) {
          triggerPin(LED3, false);
          LED3State = LOW;
        }
        else {
          triggerPin(LED3, true);
          LED3State = HIGH;
        }
    
        time = millis();    
      }
      
      button3State = reading3;
      break;
    case button4:
      // read the state of the switch into a local variable:
      reading4 = digitalRead(button4);
      
      // if the input just went from LOW and HIGH and we've waited long enough
      // to ignore any noise on the circuit, toggle the output pin and remember
      // the time
      if (reading4 == HIGH && button4State == LOW && millis() - time > debounce) {
        if (LED4State == HIGH) {
          triggerPin(LED4, false);
          LED4State = LOW;
        }
        else {
          triggerPin(LED4, true);
          LED4State = HIGH;
        }
    
        time = millis();    
      }
      
      button4State = reading4;
      break;
  }
}

void processCommand(String commandline) {
  Serial.println("incoming request command: "+commandline);
  
  if (commandline.equals(CMD_1_ON)) { // turn on LED 1
    triggerPin(LED1, true);
    LED1State = HIGH;
  }
  else if (commandline.equals(CMD_2_ON)) { // turn on LED 2
    triggerPin(LED2, true);
    LED2State = HIGH;
  }
  else if (commandline.equals(CMD_3_ON)) { // turn on LED 3
    triggerPin(LED3, true);
    LED3State = HIGH;
  }
  else if (commandline.equals(CMD_4_ON)) { // turn on LED 4
    triggerPin(LED4, true);
    LED4State = HIGH;
  }
  else if (commandline.equals(CMD_1_OFF)) { // turn off LED 1
    triggerPin(LED1, false);
    LED1State = LOW;
  }
  else if (commandline.equals(CMD_2_OFF)) { // turn off LED 2
    triggerPin(LED2, false);
    LED2State = LOW;
  }
  else if (commandline.equals(CMD_3_OFF)) { // turn off LED 3
    triggerPin(LED3, false);
    LED3State = LOW;
  }
  else if (commandline.equals(CMD_4_OFF)) { // turn off LED 4
    triggerPin(LED4, false);
    LED4State = LOW;
  }
  else if (commandline.equals(CMD_GET_STATUS)) { // get all LEDs state
    sendAllLEDState();
  }
  /*
  int value = getCommandValue(commandline);
  Serial.println("command value: "+value);
  
  switch (value) {
    case 11: // turn on LED 1
      triggerPin(LED1, true);
      break;
    case 21: // turn on LED 2
      triggerPin(LED2, true);
      break;
    case 31: // turn on LED 3
      triggerPin(LED3, true);
      break;
    case 41: // turn on LED 4
      triggerPin(LED4, true);
      break;
    case 12: // turn off LED 1
      triggerPin(LED1, false);
      break;
    case 22: // turn off LED 2
      triggerPin(LED2, false);
      break;
    case 32: // turn off LED 3
      triggerPin(LED3, false);
      break;
    case 42: // turn off LED 4
      triggerPin(LED4, false);
      break;    
    case 99: // send back LEDs state
      sendAllLEDState();
      break;
  }
  */
}
/*
int getCommandValue(String commandline) {
  String cmd = "cmd=";
  
  if (commandline.indexOf(cmd) == 0) { //if the command begins with "cmd="
    String strValue = commandline.substring(cmd.length()); //remove the "cmd=" from the command string
    int value = convertToInt(strValue);
    return value;
  }
  
  return 0;
}
*/
void triggerPin(int pin, boolean isOn) {
  boolean triggerSuccess = false;
  
  if (isOn) {
    digitalWrite(pin, HIGH);
    
    if (digitalRead(pin) == HIGH) {
      triggerSuccess = true;
    }
  }
  else {
    digitalWrite(pin, LOW);
    
    if (digitalRead(pin) == LOW) {
      triggerSuccess = true;
    }
  }
  
  delay(100);
  if (triggerSuccess) {
    sendLEDState(pin);
  }
  else {
    String commandline = "Failed to turn on/off the light !";
    server.println(commandline);
  } 
}

void sendAllLEDState() {
  sendLEDState(LED1);
  delay(1000);
  sendLEDState(LED2);
  delay(1000);
  sendLEDState(LED3);
  delay(1000);
  sendLEDState(LED4);
}

void sendLEDState(int LED) {
  String commandline = "cmd=";
  int val = digitalRead(LED);
  
  switch (LED) {
    case LED1:
      commandline += "1";
      break;
    case LED2:
      commandline += "2";
      break;
    case LED3:
      commandline += "3";
      break;
    case LED4:
      commandline += "4";
      break;
  }
  
  switch (val) {
    case HIGH:
      commandline += "1";
      break;
    case LOW:
      commandline += "2";
      break;
  }
  
  delay(100);
  server.println(commandline);
}
/*
int convertToInt(String value) {
  char buf[value.length()];
  value.toCharArray(buf, value.length());
  return atoi(buf);
}
*/
