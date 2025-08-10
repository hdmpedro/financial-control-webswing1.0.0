package org.webswing.demo.printing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterAbortException;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.Random;
import java.util.function.Consumer;

import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import org.apache.commons.lang3.exception.ExceptionUtils;

public class PaginationExample implements ActionListener {

	private PrintingDemo frameToPrint;

	public PaginationExample(PrintingDemo pd) {
		this.frameToPrint = pd;
	}

	public void actionPerformed(ActionEvent e) {
		JDialog printingDialog = new PrintingDialog(SwingUtilities.getWindowAncestor(frameToPrint));
		printingDialog.setModalityType(ModalityType.APPLICATION_MODAL);
		printingDialog.pack();
		printingDialog.setLocationRelativeTo(null);
		printingDialog.setVisible(true);
	}
	
	private class PrintingDialog extends JDialog {

		private static final long serialVersionUID = 9020387651948423533L;
		
		private JSpinner pagesSpinner = new JSpinner(new SpinnerNumberModel(25, 1, 10000, 1));
		private JProgressBar progressBar = new JProgressBar();
		private JButton printButton;
		
		private boolean isPrinting;
		private PrinterJob job;
		
		public PrintingDialog(Window parent) {
			super(parent);
		
			setTitle("Print pages");
			setSize(400, 200);
			
			JPanel topPanel = new JPanel();
			topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			topPanel.add(new JLabel("Pages:"));
			topPanel.add(pagesSpinner);
			pagesSpinner.setMaximumSize(new Dimension(100, 30));
			pagesSpinner.setPreferredSize(new Dimension(100, 30));
			
			getContentPane().add(topPanel, BorderLayout.NORTH);
			
			JPanel centerPanel = new JPanel();
			progressBar.setVisible(false);
			progressBar.setSize(new Dimension(300, 20));
			progressBar.setMaximumSize(new Dimension(300, 20));
			progressBar.setPreferredSize(new Dimension(300, 20));
			centerPanel.add(progressBar);
			
			getContentPane().add(centerPanel, BorderLayout.CENTER);
			
			JPanel buttonPanel = new JPanel();
			printButton = new JButton("Print");
			printButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					startPrint();
				}
			});
			JButton cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					cancelPrint();
				}
			});
			buttonPanel.add(printButton);
			buttonPanel.add(cancelButton);
			
			getContentPane().add(buttonPanel, BorderLayout.SOUTH);
			
			addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					cancelPrint();
				}
			});
		}
		
		private synchronized void startPrint() {
			job = PrinterJob.getPrinterJob();
			boolean portrait = "Portrait".equals(frameToPrint.orientation.getSelectedItem());
			int pages = (Integer) pagesSpinner.getValue();
			
			PrintRequestAttributeSet attrs = PrintingDemo.getDefaultPrintAttributes(portrait, job);
			
			Book book = new Book();
			Page page = new Page(pagePrinted -> {
				SwingUtilities.invokeLater(() -> {
					progressBar.setValue(pagePrinted);
				});
			});
			book.append(page, job.getPageFormat(attrs), pages);
			job.setPageable(book);
			
			boolean ok = job.printDialog();
			if (ok) {
				isPrinting = true;
				pagesSpinner.setEnabled(false);
				printButton.setEnabled(false);
				progressBar.setVisible(true);
				progressBar.setMaximum(pages);
				progressBar.setValue(0);
				pack();
				setLocationRelativeTo(null);
				
				Thread t = new Thread(() -> {
					try {
						job.print();
					} catch (PrinterAbortException ex) {
						// ignore
					} catch (PrinterException ex) {
						SwingUtilities.invokeLater(() -> {
							JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(frameToPrint), "Exception during print!\n\n" + ExceptionUtils.getRootCauseStackTrace(ex), "Error", JOptionPane.ERROR_MESSAGE);
						});
					} finally {
						isPrinting = false;
						cancelPrint();
					}
				}, "Print thread " + System.currentTimeMillis());
				t.start();
			}
		}
		
		private synchronized void cancelPrint() {
			setVisible(false);
			
			if (isPrinting) {
				if (job != null) {
					job.cancel();
				}
				JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(frameToPrint), "Print job cancelled by user!", "Warning", JOptionPane.WARNING_MESSAGE);
			}
		}
		
	}

    private class Page implements Printable {

    	private Consumer<Integer> observer;
    	private Random r = new Random();
    	
        public Page(Consumer<Integer> observer) {
			super();
			this.observer = observer;
		}
        
		@Override
        public int print(Graphics graphics, PageFormat pf, int pageIndex) throws PrinterException {
			if (!(graphics instanceof Graphics2D)) {
				return PAGE_EXISTS;
			}
			
            Graphics2D g2 = (Graphics2D) graphics;
            g2.translate(pf.getImageableX(), pf.getImageableY());
            g2.translate(0f, 0f);
            
            FontMetrics fm = g2.getFontMetrics();
            
    		int lineHeight = fm.getHeight();
    		int linesPerPage = (int) (pf.getImageableHeight() / lineHeight);

    		Polygon shape = new Polygon();
    		int w = (int) pf.getImageableWidth();
    		int h = (int) pf.getImageableHeight();
    		shape.addPoint(w / 2, 0);
    		shape.addPoint((w / 3) * 2, h);
    		shape.addPoint(0, (h / 3));
    		shape.addPoint(w, (h / 3));
    		shape.addPoint((w / 3), h);
    		shape.addPoint(w / 2, 0);
    		g2.setColor(new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 0.4f));
    		g2.fill(shape);
    		
    		g2.setColor(Color.black);
    		
    		int y = 0;
    		for (int i = 0; i < linesPerPage; i++) {
    			y += lineHeight;
    			g2.drawString("This is line number " + ((pageIndex * linesPerPage) + i + 1), 0, y);
    		}
    		
    		onPagePrinted(pageIndex);

    		return PAGE_EXISTS;
        }
        
        public void onPagePrinted(int pageIndex) {
        	if (observer == null) {
        		return;
        	}
        	observer.accept(pageIndex);
        }

    }

}