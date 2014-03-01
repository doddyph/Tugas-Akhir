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
byte ip[]   = {192.168.0.103};
//byte gateway[] = { 192, 168, 0, 1 }; //Manual setup only
//byte subnet[]  = { 255, 255, 255, 0 }; //Manual setup only
Server server(8888);

int LED1 = 2;
int LED2 = 3;
int LED3 = 4;
int LED4 = 5;

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
  //Ethernet.begin(mac, ip, gateway, subnet); //for manual setup
  server.begin();
  
  Serial.println("Server is at "+Ethernet.localIP());
  delay(100);
}

void loop() {
  // listen for incoming clients, and process request.
  Client client = server.available();
  
  if (client) {
    String commandline = ""; // where incoming commands are stored
    
    while (client.connected()) { // if a client is connected 
      if (client.available()) { // if client is sending
      
        char c = client.read(); // reading the inputs from the client
        commandline += c; // add to the command string
        
        if (c == '\n') { // if a newline character is sent from the client (commandline is fully received)
           processCommand(commandline);
           commandline = ""; //reset the commandline string
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
  boolean isOn = false;
           
  if (command.indexOf("cmd=") == 0) { //if the command begins with "cmd="
    command.replace("cmd=", ""); //remove the "cmd=" from the command string
             
    if (command.indexOf("&on") != -1) { //if the command contain "&on"
      command.replace("&on", "");//remove the "&on" from the command string
      isOn = true;
    }
    else if (command.indexOf("&off") != -1) { //if the command contain "&off"
      command.replace("&off", "");//remove the "&off" from the command string
      isOn = false;
    }
             
    int value = convertToInt(command);
    switch (value) {
      case 1:
        triggerPin(LED1, isOn);
        break;
      case 2:
        triggerPin(LED2, isOn);
        break;
      case 3:
        triggerPin(LED3, isOn);
        break;
      case 4:
        triggerPin(LED4, isOn);
        break;
    }
    
    commandline.replace("\n", "");
    server.println(commandline);
}

void triggerPin(int pin, boolean isOn) {
  if (isOn) {
    digitalWrite(pin, HIGH);
  }
  else {
    digitalWrite(pin, LOW);
  }
  delay(10);
}

int convertToInt(String value) {
  char buf[value.length()];
  value.toCharArray(buf, value.length());
  return atoi(buf);
}
