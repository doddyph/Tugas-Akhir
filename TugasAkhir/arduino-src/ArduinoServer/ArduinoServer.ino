/*
References:
1. Arduino code example: WebServer
2. http://www.dfrobot.com/wiki/index.php?title=X-Board_V2_(SKU:DFR0162)
3. http://bildr.org/2011/06/arduino-ethernet-pin-control/
4. http://www.lauridmeyer.com/2012/04/bidirectional-tcp-communication-between-android-smartphone-and-arduino-using-the-ethernetshield/
*/

#include <SPI.h>
#include <Ethernet.h>

// Enter a MAC address and IP address for your controller below.
// The IP address will be dependent on your local network:
byte mac[]  = {0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED};
IPAddress ip(192,168,0,103);

// Initialize the Ethernet server library
// with the IP address and port you want to use
EthernetServer server(8888);

int LED1 = 2;
int LED2 = 3;
int LED3 = 4;
int LED4 = 5;

String CMD_1_ON   = "cmd=11"; // turn on LED 1
String CMD_2_ON   = "cmd=21"; // turn on LED 2
String CMD_3_ON   = "cmd=31"; // turn on LED 3
String CMD_4_ON   = "cmd=41"; // turn on LED 4
String CMD_1_OFF  = "cmd=12"; // turn off LED 1
String CMD_2_OFF  = "cmd=22"; // turn off LED 2
String CMD_3_OFF  = "cmd=32"; // turn off LED 3
String CMD_4_OFF  = "cmd=42"; // turn off LED 4

void setup() { 
  Serial.begin(57600); // XBee module baud rate
  while (!Serial) {
    ; // wait for serial port to connect. Needed for Leonardo only
  }
  
  pinMode(LED1, OUTPUT); // LED 1
  pinMode(LED2, OUTPUT); // LED 2
  pinMode(LED3, OUTPUT); // LED 3
  pinMode(LED4, OUTPUT); // LED 4
  
  // start the Ethernet connection and the server:
  Ethernet.begin(mac, ip);
  server.begin();
  
  Serial.println("Server is at "+Ethernet.localIP());
  delay(100);
}

void loop() {
  // listen for incoming clients, and process request.
  EthernetClient client = server.available();
  
  if (client) {
    String commandline = ""; // where incoming commands are stored
    
    while (client.connected()) { // if a client is connected 
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
    client.stop(); // close the connection:
    Serial.println("client disonnected");
  }
}

void processCommand(String commandline) {
  Serial.println("Incoming request command... "+commandline);
  String command = commandline;
  //boolean isOn = false;
           
  if (command.indexOf("cmd=") == 0) { //if the command begins with "cmd="
    command.replace("cmd=", ""); //remove the "cmd=" from the command string
    int value = convertToInt(command);
    
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
        sendLEDState();
        break;
    }
  }  
}

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
    
  if (!triggerSuccess) { // failed to trigger pin
    commandLine = "Failed to turn on/off the light !";
  }
    
  delay(100);
  server.println(commandline);
}

void int sendLEDState() {
  sendLEDState(LED1);
  delay(100);
  sendLEDState(LED2);
  delay(100);
  sendLEDState(LED3);
  delay(100);
  sendLEDState(LED4);
}

void void sendLEDState(int LED) {
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

int convertToInt(String value) {
  char buf[value.length()];
  value.toCharArray(buf, value.length());
  return atoi(buf);
}
