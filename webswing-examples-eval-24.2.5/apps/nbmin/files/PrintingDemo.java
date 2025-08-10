package org.webswing.demo.printing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.Polygon;
import java.awt.RadialGradientPaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Point2D;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import org.webswing.demo.util.WebswingDemoProperties;
import org.webswing.toolkit.api.file.WebswingFileNameExtensionFilter;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.swing.JRViewer;
import net.sf.jasperreports.swing.JRViewerToolbar;
import net.sf.jasperreports.view.JRSaveContributor;

@WebswingDemoProperties(value = "Printing", category = "Webswing", description = "Demonstrates printing abilities", sourceFiles = {"org/webswing/demo/printing/PaginationExample.java", "org/webswing/demo/printing/PrintableExample.java", "org/webswing/demo/printing/PrintJobExample.java"})
public class PrintingDemo extends JPanel {
	private static final long serialVersionUID = 8550928872207603286L;
	
	protected JComboBox<String> orientation = new JComboBox<>(new String[] { "Portrait", "Landscape" });
	private JScrollPane pane;
	
	public PrintingDemo() {
		super(new BorderLayout());
		setBorder(new EmptyBorder(10, 10, 10, 10));
		JTextArea text = new JTextArea(50, 20) {
			Random r = new Random();
			
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				if (!(g instanceof Graphics2D)) {
					return;
				}
				
	            Graphics2D g2 = (Graphics2D) g;
	
	    		Polygon shape = new Polygon();
	    		
	    		Point2D center = new Point2D.Float(200, 200);
	            float radius = 100;
	            float[] dist = {0.05f, .95f};
	            Color[] colors = {new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 0.4f), new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 0.4f)};
	            g2.setPaint(new RadialGradientPaint(center, radius, dist, colors, CycleMethod.REFLECT));
	    		
	            g2.fillOval(200, 200, 400, 400);
	            
	    		int w = pane.getWidth();
	    		int h = pane.getHeight();
	    		
	    		shape.addPoint(w / 2, 0);
	    		shape.addPoint((w / 3) * 2, h);
	    		shape.addPoint(0, (h / 3));
	    		shape.addPoint(w, (h / 3));
	    		shape.addPoint((w / 3), h);
	    		shape.addPoint(w / 2, 0);
	    		
	    		g2.setColor(new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 0.4f));
	    		g2.fill(shape);
	    		
	    		g2.setColor(Color.black);
			}
		};
		
		for (int i = 1; i <= 50; i++) {
			text.append("Line " + i + "\n");
		}
		pane = new JScrollPane(text);
		add("Center", pane);
		
		JButton printButton = new JButton("Printable");
		printButton.setToolTipText("using Printable interface");
		printButton.addActionListener(new PrintableExample(this));
		
		JButton print2Button = new JButton("Toolkit.getPrintJob()");
		print2Button.setToolTipText("using the Toolkit.getPrintJob() call");
		print2Button.addActionListener(new PrintJobExample(this));
		
		JButton print3Button = new JButton("Pageable, cancellable");
		print3Button.addActionListener(new PaginationExample(this));
		
		JPanel panel = new JPanel(new GridLayout(2, 5, 10, 10));
		panel.setBorder(new EmptyBorder(10, 0, 0, 0));
		panel.add(orientation);
		panel.add(printButton);
		panel.add(print2Button);
		panel.add(print3Button);
		
		JButton jasperButton = new JButton("Jasper Reports");
		jasperButton.setToolTipText("open jasper report viewer window");
		jasperButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					JRViewer viewer = new JRViewer(PrintingDemo.class.getResourceAsStream("resources/FirstJasper.jrprint"), false) {
						@Override
						protected JRViewerToolbar createToolbar() {
							JRViewerToolbar toolbar = super.createToolbar();
							List<JRSaveContributor> wrappers = new ArrayList<>();
							for (JRSaveContributor saveContributor: toolbar.getSaveContributors()) {
								// REMOVE PDF and XLS exporters, because excluded dependencies in demo
								if (!saveContributor.accept(new File("any.pdf")) || !saveContributor.accept(new File("any.xls"))) {
									wrappers.add(new JRSaveContributorWrapper(saveContributor));
								}
							}
							toolbar.setSaveContributors(wrappers.toArray(new JRSaveContributor[wrappers.size()]));
							return toolbar;
						}
					};
					JDialog jd = new JDialog();
					jd.setTitle("JasperViewer");
					jd.getContentPane().add(viewer);
					jd.pack();
					java.awt.Dimension dimension = new java.awt.Dimension(750, 550);
					jd.setSize(dimension);
					jd.setLocationRelativeTo(null);
					jd.setVisible(true);
					
					viewer.setFitWidthZoomRatio();
				} catch (JRException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		panel.add(jasperButton);
		add("South", panel);
	}

	public static void main(String[] args) {
		final JFrame f = new JFrame("Print UI Example");
		f.getContentPane().add(new PrintingDemo());
		f.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		f.pack();
		f.setVisible(true);
	}

	private static class JRSaveContributorWrapper extends JRSaveContributor implements WebswingFileNameExtensionFilter {
		private final String[] extensions = new String[]{"docx","csv","jrprint","jrpxml","odt","html", "rtf"};
		private final JRSaveContributor delegate;

		public JRSaveContributorWrapper(JRSaveContributor delegate){
			this.delegate = delegate;
		}

		@Override
		public void save(JasperPrint jasperPrint, File file) throws JRException {
			delegate.save(jasperPrint, file);
//UNCOMMENT THIS IF autoDownload is disabled
//			try {
//				if(file.exists()){
//					for(String ext: extensions){
//						File withExt=  new File(file.getAbsolutePath() +"."+ ext);
//						if(withExt.exists()){
//							Desktop.getDesktop().open(withExt);
//							break;
//						}
//					}
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
		}

		@Override
		public boolean accept(File f) {
			return delegate.accept(f);
		}

		@Override
		public String getDescription() {
			return delegate.getDescription();
		}

		@Override
		public String[] getExtensions() {
			for(String extension: extensions){
				if(delegate.accept(new File("any."+extension))){
					return new String[]{extension};
				}
			}
			return null;
		}
	}
	
	public static PrintRequestAttributeSet getDefaultPrintAttributes(boolean portrait, PrinterJob job) {
		PrintRequestAttributeSet attrs = new HashPrintRequestAttributeSet();
		attrs.add(portrait ? OrientationRequested.PORTRAIT: OrientationRequested.LANDSCAPE);
		attrs.add(MediaSizeName.ISO_A4);

		// margins
		PageFormat pf = job.getPageFormat(attrs);
		float inch = 1.0f;
		double pw = portrait ? pf.getWidth() : pf.getHeight();
		double ph = portrait ? pf.getHeight() : pf.getWidth();
		attrs.add(new MediaPrintableArea(inch, inch, (float) (pw / 72f) - (2 * inch), (float) (ph / 72f) - (2 * inch), MediaPrintableArea.INCH));
		
		return attrs;
	}
	
}
