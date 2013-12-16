package it.eng.spagobi.meta.test.ui;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.locator.eclipse.WorkbenchLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;
import com.windowtester.runtime.swt.locator.MenuItemLocator;
import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.TableItemLocator;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.locator.eclipse.ContributedToolItemLocator;
import com.windowtester.runtime.swt.locator.ButtonLocator;

public class Test1 extends UITestCaseSWT {

	/* @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		IUIContext ui = getUI();
		ui.ensureThat(new WorkbenchLocator().hasFocus());
		ui.ensureThat(ViewLocator.forName("Welcome").isClosed());
	}

	/**
	 * Main test method.
	 */
	public void testTest1() throws Exception {
		IUIContext ui = getUI();
		ui.click(new MenuItemLocator("Window/Open Perspective/Other..."));
		ui.wait(new ShellShowingCondition("Open Perspective"));
		ui.click(2, new TableItemLocator("SpagoBI"));
		ui.wait(new ShellDisposedCondition("Open Perspective"));
	
		
		ContributedToolItemLocator contributor = new ContributedToolItemLocator(
		"org.eclipse.datatools.connectivity.toolbar.addprofile");
		
		ui.click(new ContributedToolItemLocator(
				"org.eclipse.datatools.connectivity.toolbar.addprofile"));
	
		ui.wait(new ShellShowingCondition("New Connection Profile"));
		ui.click(2, new TableItemLocator("MySQL"));
		ui.click(new ButtonLocator("Cancel"));
		ui.wait(new ShellDisposedCondition("New Connection Profile"));
	}

}