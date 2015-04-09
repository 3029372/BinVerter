package binverter.src;

import java.util.HashMap;

public class BinVerter {
	//List of commands
	enum Commands {
		CONVERT("_", ""), NULL("_", ""),
		CLEAR("clear", "Clears the command line"),
		EXIT("exit", "Quits the command line"),
		HELP("help", "Displays this help line"), 
		EGG1("LOL", ""),
		;
		private String command;
		private String help;
		Commands(String cmd, String help) {
			this.command = cmd;
			this.help = help;
		}
		public String getCommand() { return command; }
		public String getHelp() { return help; }
	}
	
	//List of prefixes
	enum Prefixes {
		EGG1("BLT", ""),
		BINDEC("BD", "Binary to Decimal"), 
		BINHEX("BH", "Binary to HexDecimal"),
		BINTEXT("BT", "Binary to Text"), 
		DECBIN("DB", "Decimal to Binary"),
		DECHEX("DH", "Decimal to HexDecimal"), 
		HEXBIN("HB", "HexDecimal to Binary"),
		HEXDEC("HD", "HexDecimal to Decimal"),
		TEXTBIN("TB", "Text to Binary"),
		;
		private String prefix;
		private String help;
		Prefixes(String prefix, String help) {
			this.prefix = prefix;
			this.help = help;
		}
		public String getPrefix() { return prefix; }
		public String getHelp() { return help; }
	}
	
	//The input generated from the window class
	private String input;
	
	//Controls weather the application should close or not
	private boolean shouldClose;
	//Map containing the prefix and data to be converted. Cleared after every call
	private HashMap<Prefixes, String> mapped;
	//The window object(for user input)
	private Window window;
	private boolean mayDisplayUnknown = false;
	
	public BinVerter() {
		window = new Window(this);
		input = "";
		window.clearStrings("BinVerter line. Type 'help' to grab all prefixes!\n\n");
		shouldClose = false;
		mapped = new HashMap<Prefixes, String>();
		while(!shouldClose) {
			//This boolean stops 'Unknown prefix/command' from being printed when the user
			//isn't giving an input.
			mayDisplayUnknown = true;
			Commands id;
			if(input.isEmpty()) {
				id = Commands.NULL;
				mayDisplayUnknown = false;
			}else{
				
				id = parseAction(grabInput());
			}
			switch(id) {
			case CONVERT:
				convertAction();
				break;
			case CLEAR:
				window.clearStrings("BinVerter line. Type 'help' to grab all prefixes!\n\n");
				break;
			case HELP:
				showHelp();
				break;
			case EXIT:
				shouldClose = true;
				break;
			case EGG1:
				window.addString("Hahaha, you so funny!\n");
				break;
			default:
				if(mayDisplayUnknown)
					window.addString("Unknown prefix/command\n");
				break;
			}
		}
		System.exit(0);
	}
	
	private void showHelp() {
		window.addString("BinVerter is an open source converter for\n"+
				"binaries, decimals, texts, etc.\n\n");
		window.addString("Commands:\n");
		for(Commands t : Commands.values()) {
			if(!t.getHelp().isEmpty()) {
				window.addString(t.getCommand() + " - " + t.getHelp()+"\n");
			}
		}
		window.addString("\nPrefixes:\n");
		for(Prefixes p : Prefixes.values()) {
			if(!p.getHelp().isEmpty()) {
				window.addString(p.getPrefix()+"_ - "+p.getHelp()+"\n");
			}
		}
		window.addString("\n");
	}
	
	private Commands parseAction(String[] tokens) {
		mapped.clear();
		Prefixes pref = null;
		
		if(!(tokens.length == 1)) {
			for(Prefixes p : Prefixes.values()) {
				if(tokens[0].equalsIgnoreCase(p.getPrefix())) {
					pref = p;
					break;
				}
			}
		}
		
		if(pref == null) {
			for(Commands t : Commands.values()) {
				if(tokens[0].equalsIgnoreCase(t.getCommand())) {
					return t;
				}
			}
			return Commands.NULL;
		}
		
		mapped.put(pref, tokens[1]);
		return Commands.CONVERT;
	}
	
	private void convertAction() {
		Prefixes pref = null;
		String text = null;
		for(Prefixes pre : Prefixes.values()) {
			if(mapped.get(pre) != null) {
				text = mapped.get(pre);
				pref = pre;
				break;
			}
		}
		if(pref == null) {
			window.addString("An error occured retrieving your info!\n");
			return;
		}
		switch(pref) {
		case HEXBIN:
			String tempR;
			try{
				tempR = Integer.toBinaryString(Integer.parseInt(text, 16));
			}catch(NumberFormatException e) {
				window.addString("Invalid hexdecimal value!");
				return;
			}
			StringBuilder strR = new StringBuilder(tempR);
			tempR = null;
			while(strR.length() < 8) {
				strR.insert(0, "0");
			}
			
			window.addString(strR.toString()+"\n");
			break;
		case DECBIN:
			String tempI;
			try{
				tempI = Integer.toBinaryString(Integer.parseInt(text));
			}catch(NumberFormatException e) {
				window.addString("Invalid decimal value!");
				return;
			}
			StringBuilder strI = new StringBuilder(tempI);
			tempI = null;
			while(strI.length() < 8) {
				strI.insert(0, "0");
			}
			
			window.addString(strI.toString()+"\n");
			break;
		case BINDEC:
			try {
				window.addString(""+Integer.parseInt(text, 2)+"\n");
			}catch(NumberFormatException e) {
				window.addString("Invalid binary code!\n");
			}
			break;
		case BINHEX:
			try {
				window.addString(""+Integer.toHexString(Integer.parseInt(text, 2))+"\n");
			}catch(NumberFormatException e) {
				window.addString("Invalid binary code!\n");
			}
			break;
		case DECHEX:
			try {
				window.addString(Integer.toHexString(Integer.parseInt(text))+"\n");
			}catch(NumberFormatException e) {
				window.addString("Not a valid number!\n");
				return;
			}
			break;
		case HEXDEC:
			try{
				window.addString(""+Integer.parseInt(text, 16)+"\n");
			}catch(NumberFormatException e) {
				window.addString("Not a valid Hex Decimal format!\n");
				return;
			}
			break;
		case BINTEXT:
			String bytes[] = text.split(" ");
			byte newBytes[] = new byte[bytes.length];
			//Test for any other characters other than zero and one
			for(int i = 0; i < bytes.length; i++) {
				try {
					newBytes[i] = (byte)Integer.parseInt(bytes[i], 2);
				}catch(NumberFormatException e) {
					window.addString("Invalid binary code!\n");
					return;
				}
			}
			window.addString(new String(newBytes)+"\n");
			break;
		case TEXTBIN:
			byte textByte[] = text.getBytes();
			for(int i = 0; i < textByte.length; i++) {
				String temp = Integer.toBinaryString((int)textByte[i]);
				StringBuilder str = new StringBuilder(temp);
				temp = null;
				while(str.length() < 8) {
					str.insert(0, "0");
				}
				if(i == textByte.length-1)
					window.addString(str.toString());
				else
					window.addString(str.toString()+" ");
			}
			window.addString("\n");
			break;
		case EGG1:
			window.addString("Mmmmmmmmmmm.\nBacon Lettuce Tomato!\n");
			break;
		}
	}
	
	public void input(String in) {
		this.input = in;
	}
	
	private String[] grabInput() {
		if(input.isEmpty()) 
			return new String[] {""};
		
		String[] tokens = input.split("_");
		
		if(tokens.length < 2) {
			input = "";
			mayDisplayUnknown=false;
			return new String[] {tokens[0]};
		}
		
		input = "";
		return tokens;
	}
	
	public static void main(String[] args) {
		new BinVerter();
	}
}
