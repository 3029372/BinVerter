package binverter.src;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Window {
	private JFrame frame;
	private JTextField input;
	private JTextArea console;
	
	public Window(final BinVerter main) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		frame = new JFrame("BinVerter");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(480, 280));
		frame.setLocationRelativeTo(null);
		//frame.setLayout(new BorderLayout());
		console = new JTextArea();
		console.setEditable(false);
		input = new JTextField();
		input.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				main.input(event.getActionCommand());
				input.setText("");
			}
		});
		frame.add(input, BorderLayout.SOUTH);
		JScrollPane panel = new JScrollPane(console);
		panel.setSize(480, 280-16);
		
		frame.add(panel, BorderLayout.CENTER);
		frame.setResizable(false);
		frame.setVisible(true);
		input.requestFocus();
	}
	
	/**
	 * Prints line
	 * @param consoleText
	 */
	public void addString(String consoleText) {
		console.append(consoleText);
		console.setCaretPosition(console.getDocument().getLength());
	}
	
	public void clearStrings(String startString) {
		console.setText(startString);
		console.setCaretPosition(console.getDocument().getLength());
	}
}
