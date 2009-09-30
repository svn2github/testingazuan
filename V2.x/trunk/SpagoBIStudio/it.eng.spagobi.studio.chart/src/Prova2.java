import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;

/*
 * Floating point values in Spinner
 * 
 * For a list of all SWT example snippets see
 * http://dev.eclipse.org/viewcvs/index.cgi/%7Echeckout%7E/platform-swt-home/dev.html#snippets
 */

public class Prova2 {

  public static void main(String[] args) {
    Display display = new Display();
    Shell shell = new Shell(display);
    shell.setText("Spinner with float values");
    shell.setLayout(new GridLayout());
    final Spinner spinner = new Spinner(shell, SWT.NONE);
    // allow 3 decimal places
    spinner.setDigits(3);
    // set the minimum value to 0.001
    spinner.setMinimum(1);
    // set the maximum value to 20
    spinner.setMaximum(20000);
    // set the increment value to 0.010
    spinner.setIncrement(10);
    // set the seletion to 3.456
    spinner.setSelection(3456);
    spinner.setToolTipText("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\naaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
    spinner.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent e) {
        int selection = spinner.getSelection();
        int digits = spinner.getDigits();
        System.out.println("Selection is "
            + (selection / Math.pow(10, digits)));
        spinner.setSelection(3456);
      }
    });
    shell.setSize(200, 200);
    shell.open();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch())
        display.sleep();
    }
    display.dispose();
  }
}

