package GUI;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class TestFrame extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private TestPanel panel;
	private static int score;
	
	public static void main(String[] args) {
		score = 0;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestFrame frame = new TestFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public TestFrame() {
		setTitle("Test GUI");
		setResizable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(200, 200, 9*64 + 30, 8*64 + 66);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.setDoubleBuffered(true);
		
		panel = new TestPanel(this);
		panel.setDoubleBuffered(true);
		panel.setBackground(Color.BLACK);
		contentPane.add(panel, BorderLayout.CENTER);
		panel.getInputMap().put(KeyStroke.getKeyStroke("ESCAPE"), "quit");
		panel.getActionMap().put("quit", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});
		
		JLabel lblMoney = new JLabel("Money: ");
		lblMoney.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblMoney, BorderLayout.SOUTH);
	}
	
	public void close() {
		WindowEvent winClosingEvent = new WindowEvent(this, WindowEvent.WINDOW_CLOSING );
		Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent( winClosingEvent );
	}
	
	public void setBounds(int x, int y) {
		setBounds(100, 100, x+30, y+66);
	}

}
