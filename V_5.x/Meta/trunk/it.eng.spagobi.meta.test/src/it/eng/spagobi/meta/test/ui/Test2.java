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
import com.windowtester.runtime.swt.locator.LabeledTextLocator;
import com.windowtester.runtime.locator.XYLocator;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.SWTWidgetLocator;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.ToolBar;
import com.windowtester.runtime.swt.locator.LabeledLocator;
import org.eclipse.swt.widgets.Composite;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import org.eclipse.swt.widgets.Tree;
import com.windowtester.runtime.swt.locator.TabItemLocator;
import com.windowtester.runtime.swt.locator.ListItemLocator;

public class Test2 extends UITestCaseSWT {

	/* @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		IUIContext ui = getUI();
		ui.ensureThat(new WorkbenchLocator().hasFocus());
		ui.ensureThat(ViewLocator.forName("Welcome").isClosed());
		ui.ensureThat(new WorkbenchLocator().isMaximized());
	}

	/**
	 * Main test method.
	 */
	public void testTest2() throws Exception {
		IUIContext ui = getUI();
		ui.click(new MenuItemLocator("Window/Open Perspective/Other..."));
		ui.wait(new ShellShowingCondition("Open Perspective"));
		ui.click(2, new TableItemLocator("SpagoBI"));
		ui.wait(new ShellDisposedCondition("Open Perspective"));
		ui.click(new ContributedToolItemLocator(
				"org.eclipse.datatools.connectivity.toolbar.addprofile"));
		ui.wait(new ShellShowingCondition("New Connection Profile"));
		ui.click(new TableItemLocator("MySQL"));
		ui.click(new XYLocator(new LabeledTextLocator("Na&me:"), -34, -1));
		ui.enterText("foodmart");
		ui.click(new ButtonLocator("&Next >"));
		System.err.println("connection wizard opened");
		ui.click(new SWTWidgetLocator(ToolItem.class, "", new SWTWidgetLocator(
				ToolBar.class, 0, new LabeledLocator(Composite.class,
						"&Drivers:"))));
		ui.wait(new ShellShowingCondition("New Driver Definition"));
		ui.click(new TreeItemLocator("Database/MySQL JDBC Driver",
				new LabeledLocator(Tree.class, "&Available driver templates:")));
		ui.click(new TabItemLocator("JAR List"));
		ui.click(new ListItemLocator("mysql-connector-java-5.0.0-bin.jar"));
		ui.click(new ButtonLocator("&Clear All"));
		ui.click(new ButtonLocator("&Add JAR/Zip..."));
		ui.click(new ButtonLocator("OK"));
		ui.wait(new ShellDisposedCondition("New Driver Definition"));
		System.err.println("driver created");
		ui.click(new XYLocator(new LabeledTextLocator("&URL:"), 276, 9));
		ui.enterText("foodmart_key");
		ui.click(new XYLocator(new LabeledTextLocator("D&atabase:"), -42, -4));
		ui.enterText("foodmart");
		ui.click(new LabeledTextLocator("Pass&word:"));
		ui.enterText("mysql");
		ui.click(new ButtonLocator("Sa&ve password"));
		ui.click(new ButtonLocator("&Test Connection"));
		ui.wait(new ShellShowingCondition("Success"));
		ui.wait(new ShellDisposedCondition("Ping server job"));
		ui.click(new ButtonLocator("OK"));
		ui.wait(new ShellDisposedCondition("Success"));
		ui.click(new ButtonLocator("&Finish"));
		ui.wait(new ShellDisposedCondition("New Connection Profile"));
		ui.wait(new ShellDisposedCondition("Creating connections to foodmart."));
		ui.wait(new ShellDisposedCondition(
				"Creating SQL Model Connection to foodmart."));
		ui.wait(new ShellDisposedCondition(
				"Creating JDBC Connection to foodmart."));
		ui.wait(new ShellDisposedCondition("Refreshing View"));
	}

}