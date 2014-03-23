package com.tugasakhir.droidduino.connection;

public class CmdMessage {
	
	public static final String CMD_GET_STATUS 	= "cmd=99"; // get all LEDs state

	public static final String CMD_1_ON 		= "cmd=11"; // turn on LED 1
	public static final String CMD_2_ON 		= "cmd=21"; // turn on LED 2
	public static final String CMD_3_ON 		= "cmd=31"; // turn on LED 3
	public static final String CMD_4_ON 		= "cmd=41"; // turn on LED 4
	
	public static final String CMD_1_OFF 		= "cmd=12"; // turn off LED 1
	public static final String CMD_2_OFF 		= "cmd=22"; // turn off LED 2
	public static final String CMD_3_OFF 		= "cmd=32"; // turn off LED 3
	public static final String CMD_4_OFF 		= "cmd=42"; // turn off LED 4
}
