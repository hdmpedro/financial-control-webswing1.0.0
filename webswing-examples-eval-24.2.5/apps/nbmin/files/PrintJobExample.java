package org.webswing.demo.printing;

/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
import java.awt.Graphics;
import java.awt.PageAttributes;
import java.awt.PageAttributes.MediaType;
import java.awt.PageAttributes.OrientationRequestedType;
import java.awt.PrintJob;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.webswing.demo.util.LookAndFeelManager;

public class PrintJobExample implements ActionListener {

	private PrintingDemo frameToPrint;
	private PrintJob printjob;
	
	public PrintJobExample(PrintingDemo f) {
		frameToPrint = f;
	}

	public void actionPerformed(ActionEvent e) {
		if (LookAndFeelManager.isFlatLaFActive()) {
			JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(frameToPrint), "This example does not work with Flat LaF, please change LaF.", "Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		PrintJob localPrintJob = this.printjob;
		if (localPrintJob == null) {
			JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(frameToPrint);
			boolean portrait = "Portrait".equals(frameToPrint.orientation.getSelectedItem());
			localPrintJob = Toolkit.getDefaultToolkit().getPrintJob(topFrame, "Test", null, getPageAttrs(portrait));
		}
		if (localPrintJob == null) {
			return;
		}
		Graphics localGraphics = localPrintJob.getGraphics();
		frameToPrint.print(localGraphics);
		localGraphics.dispose();
		localPrintJob.end();
	}

	private PageAttributes getPageAttrs(boolean portrait) {
		PageAttributes attrs = new PageAttributes();
		attrs.setOrientationRequested(portrait ? OrientationRequestedType.PORTRAIT: OrientationRequestedType.LANDSCAPE);
		attrs.setMedia(MediaType.A4);
		return attrs;
	}

}
