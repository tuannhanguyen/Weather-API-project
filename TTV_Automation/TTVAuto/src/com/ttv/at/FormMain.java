package com.ttv.at;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.table.TableCellEditor;
import javax.swing.text.AttributeSet;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyledDocument;
import javax.swing.text.html.HTML;
import javax.swing.text.Element;

public class FormMain implements ActionListener {
	static String txtAboutText = "Version 1.000";

	static int expected_test_count_of_suites = 0;
	static boolean stop = false;
	static String current_tester_emails;
	static com.ttv.at.test.testsuite current_testsuite;
	public static void main(String[] args) {

		FormMain mainFrm = new FormMain();
		mainFrm.CreateGUIComponent();
		FormMain.set_app_state_startup();

		// Init log setting
		com.ttv.at.log.log.get_instance().addlog_event_listener(new com.ttv.at.log.log_event_listener() {


			@Override
			public void newActionLogOccur(com.ttv.at.log.action evt) {
				// TODO Auto-generated method stub
				guiutil.txtOutput_append_10black(evt.get_full_message_start(), txtOutput, txtOutput_style, txtOutput_doc);
			}
			@Override
			public void updateActionLogOccur(com.ttv.at.log.action evt) { 
				// TODO Auto-generated method stub
				if (evt.get_print_after_action_image()) {

					String after_absolute_link = evt.get_after_action_image();
					if (after_absolute_link != null && after_absolute_link.length() > 0)
						after_absolute_link = com.ttv.at.test.testsetting.get_default_log_images_folder() + com.ttv.at.util.os.os_file_separator + after_absolute_link;
					
					if (evt.get_passed())
						guiutil.txtOutput_append_10blue(evt.get_full_message_result(), after_absolute_link, txtOutput, txtOutput_style, txtOutput_doc);
					else
						guiutil.txtOutput_append_10magenta(evt.get_full_message_result(), after_absolute_link, txtOutput, txtOutput_style, txtOutput_doc);
				}
				else {
					if (evt.get_passed())
						guiutil.txtOutput_append_10blue(evt.get_full_message_result(), txtOutput, txtOutput_style, txtOutput_doc);
					else
						guiutil.txtOutput_append_10magenta(evt.get_full_message_result(), txtOutput, txtOutput_style, txtOutput_doc);
				}
			}
			
			@Override
			public void newTestElementLogOccur(com.ttv.at.log.testelement evt) {
				// TODO Auto-generated method stub
				guiutil.txtOutput_append_10black_bold(evt.get_full_message_start(), txtOutput, txtOutput_style, txtOutput_doc);
			}
			@Override
			public void updateTestElementLogOccur(com.ttv.at.log.testelement evt) {
				// TODO Auto-generated method stub
				if (evt.get_passed() == 1)
					guiutil.txtOutput_append_10blue_bold(evt.get_full_message_result(), txtOutput, txtOutput_style, txtOutput_doc);
				else if (evt.get_passed() == 2)
					guiutil.txtOutput_append_10magenta_bold(evt.get_full_message_result(), txtOutput, txtOutput_style, txtOutput_doc);
				else if (evt.get_passed() == 0)
					guiutil.txtOutput_append_10red_bold(evt.get_full_message_result(), txtOutput, txtOutput_style, txtOutput_doc);

			}

			@Override
			public void newTestLogOccur(com.ttv.at.log.test evt) {
				// TODO Auto-generated method stub
				guiutil.txtOutput_append_12black_3lines(evt.get_full_start_message(), txtOutput, txtOutput_style, txtOutput_doc);
				com.ttv.at.tablemodel.testarea.update_instance_detail();
			}

			@Override
			public void updateTestLogOccur(com.ttv.at.log.test evt) {
				// TODO Auto-generated method stub
				if (evt.get_passed() == 1)
					guiutil.txtOutput_append_12blue(evt.get_full_end_message(), txtOutput, txtOutput_style, txtOutput_doc);
				else {
					String before_absolute_link = evt.get_before_failed_image();
					if (before_absolute_link != null && before_absolute_link.length() > 0)
						before_absolute_link = com.ttv.at.test.testsetting.get_default_log_images_folder() + com.ttv.at.util.os.os_file_separator + before_absolute_link;
					String after_absolute_link = evt.get_after_failed_image();
					if (after_absolute_link != null && after_absolute_link.length() > 0)
						after_absolute_link = com.ttv.at.test.testsetting.get_default_log_images_folder() + com.ttv.at.util.os.os_file_separator + after_absolute_link;
					guiutil.txtOutput_append_12red(evt.get_full_end_message(), before_absolute_link, after_absolute_link, txtOutput, txtOutput_style, txtOutput_doc);
				}
				com.ttv.at.tablemodel.testarea.update_instance_detail();
				com.ttv.at.tablemodel.testsuite.update_instance_detail();
				
				// send email to notify test complete
				if (current_testsuite != null)
					if (evt.get_passed() == 1) 
						com.ttv.at.util.test.report.send_email (com.ttv.at.test.testsetting.get_host_name() + " - PASSED - " + current_testsuite.get_name() + " : " + evt.get_end_message(), evt.get_full_end_message(), current_tester_emails);
					else
						com.ttv.at.util.test.report.send_email (com.ttv.at.test.testsetting.get_host_name() + " - FAILED - " + current_testsuite.get_name() + " : " + evt.get_end_message(), evt.get_full_end_message(), current_tester_emails);
				mainProgress.setValue(mainProgress.getValue() + 1);
			}

			@Override
			public void newTestSuiteLogOccur(com.ttv.at.log.testsuite evt) {
				current_testsuite = null;
				if (evt.get_testsuite_instance() != null) {
					current_testsuite = evt.get_testsuite_instance();
					expected_test_count_of_suites = expected_test_count_of_suites + evt.get_testsuite_instance().get_max_number_of_run_test();
					current_tester_emails = evt.get_testsuite_instance().get_dataset().get_string_value("tester.email");
					

					// Send Email
					String content = com.ttv.at.util.test.report.email_start(evt.get_testsuite_instance());
					
					FormMain.tableDataSetModel.update_detail();
				}
			}

			@Override
			public void endTestSuiteLogOccur(com.ttv.at.log.testsuite evt) {
				mainProgress.setValue(expected_test_count_of_suites);
				com.ttv.at.test.testsuite cur_suite = evt.get_testsuite_instance();
				
				if (cur_suite != null && cur_suite.get_total_run_tests() > 0) {
					// TODO Auto-generated method stub
					System.out.println("------ End Suite print report -----");
	
					// Counter
					//Get all test suites
					ArrayList<com.ttv.at.test.testarea> testareas = cur_suite.get_testareas();
					int total_passed = 0;
					int total_failed = 0;
					for (com.ttv.at.test.testarea scanArea:testareas)
						for (com.ttv.at.test.test scanTest:scanArea.get_tests()) {
							String Status = scanTest.get_run_status().name();
							if (Status == "PASSED")
								total_passed ++;
							else if (Status.endsWith("FAILED"))
								total_failed ++;
						}

					String excel_export_result = com.ttv.at.util.test.report.export_excel_summary(cur_suite);
					String excel_export_result2 = cur_suite.get_summary_report_path();
					if (excel_export_result2 != null && excel_export_result2.length() > 0)
						excel_export_result2 = com.ttv.at.util.test.report.update_excel_summary2(cur_suite);
					else
						excel_export_result2 = com.ttv.at.util.test.report.export_excel_summary2(cur_suite);
					String html_summary_log = null;
					if ((!stop) && excel_export_result2 != null && excel_export_result2.length() > 0) {
						html_summary_log = com.ttv.at.util.test.report.export_excel_summary2_to_html(cur_suite, excel_export_result2);
					}
					if (excel_export_result != null) {
						if (excel_export_result.startsWith("-- Error"))
							guiutil.txtOutput_append_12red(excel_export_result, txtOutput, txtOutput_style, txtOutput_doc);
						else
							guiutil.txtOutput_append_12blue("******** Excel file result is " + excel_export_result, txtOutput, txtOutput_style, txtOutput_doc);
					}
					
					// ****** PRINT OUT TOTAL PASSED / FAILED
					int total_suite_run_passed = evt.get_run_passed();
					if (total_suite_run_passed > 0)
						guiutil.txtOutput_append_12blue("-- PASSED: " + evt.get_run_passed(), txtOutput, txtOutput_style, txtOutput_doc);
					int total_suite_run_fail = evt.get_run_failed();
					if (total_suite_run_fail > 0)
						guiutil.txtOutput_append_12red("-- FAILED: " + evt.get_run_failed(), txtOutput, txtOutput_style, txtOutput_doc);
					
					guiutil.txtOutput_append_12magenta_bold_for_log("Click here to open Log Folder", com.ttv.at.test.testsetting.get_default_log_folder(), txtOutput, txtOutput_style, txtOutput_doc);

					// Send Email
					String content = null;
					if (html_summary_log != null && html_summary_log.length() > 0)
						guiutil.txtOutput_append_12magenta_bold_for_log("Click here to open Result Summary", html_summary_log, txtOutput, txtOutput_style, txtOutput_doc);
					com.ttv.at.util.test.report.email_summary(cur_suite, total_passed, total_failed);
					
					// ******** Export DB
					if (com.ttv.at.util.test.report.export_db(cur_suite, content))
						guiutil.txtOutput_append_12blue(" ******** Export DB successful for testsuite " + cur_suite.get_name() + " ********", txtOutput, txtOutput_style, txtOutput_doc);
				}

				FormMain.tableDataSetModel.update_detail();
				
				
				
			}

			@Override
			public void newTestSuiteSetLogOccur(com.ttv.at.log.testsuiteset evt) {
				// TODO Auto-generated method stub
				FormMain.expected_test_count_of_suites = 0;
			}

			@Override
			public void endTestSuiteSetLogOccur(com.ttv.at.log.testsuiteset evt) {
				// TODO Auto-generated method stub
				

				FormMain.tableDataSetModel.update_detail();
			}
		});
	
	
		// ****** Process with Argument ******
		// Search the run option
		boolean run_test_suite = false;
		boolean silent_mode = false;
		boolean display_help = false;
		boolean auto_exit = false;
		boolean wait_email_command = false;
		ArrayList <String> testsuitesets = new ArrayList<String>();
		if (args != null && args.length > 0)
			for (String ARG : args)
				if (ARG != null && ARG.length() > 0)
					if (ARG.toLowerCase().equals("/run") || ARG.toLowerCase().equals("-run"))
						run_test_suite = true;
					else if (ARG.toLowerCase().equals("/silent") || ARG.toLowerCase().equals("-run"))
						silent_mode = true;
					else if (ARG.toLowerCase().equals("/help") || ARG.toLowerCase().equals("-help") ||
							ARG.toLowerCase().equals("/?") || ARG.toLowerCase().equals("-?"))
						display_help = true;
					else if (ARG.toLowerCase().equals("/autoexit") || ARG.toLowerCase().equals("-autoexit"))
						auto_exit = true;
					else if (ARG.toLowerCase().equals("/waitforemailcommand") || ARG.toLowerCase().equals("-waitforemailcommand"))
						wait_email_command = true;
					else 
						testsuitesets.add(ARG);
		
		if (display_help)
			System.out.println ("This will guide to put parameter as follow \n\n" + 
					"java -jar KMS_AutomationFramework.jar [testsuite prefix 1] [testsuite prefix 2] [testsuite prefix..n] /run /silent\n\n" + 
					"	[testsuite prefix 1] [testsuite prefix 2] [testsuite prefix..n]\n" + 
					"		Specify the testsuite list will be loaded by testsuite prefix (testsuite prefix is get from \"Name Prefix\" combo of Framework Java application)\n" +
					"		There is not limit number of test suite to load and run\n\n" +
					"	/run\n" + 
					"		specify that run all the testsuite\n\n" + 
					"	/silent\n" +
					"		specify that running in silent mode, GUI will not display, User may look at Console to view which thing in running\n" +
					"		In silent mode, \"Test.Email\" should be configured in testsuite so that email will send to tester to notify which test case is running and when test suite complete\n\n" +
					"	/autoexit\n" + 
					"		Auto exit after complete running (availeble on if using with \"/run\" option)\n\n" + 
					"	Example (the following will run Delias test suite than continue running dwr test suite)\n" +
					"		java -jar KMS_AutomationFramework.jar \"delias\\5.9.3.\" \"dwr\\5.9.4.\" /run /silent\n\n");
		
		if (silent_mode)
			FormMain.mainFrame.setVisible(false);
		
		if (testsuitesets.size() > 0) {
			if (run_test_suite) {
				loadNrun startRun = new loadNrun(FormMain.mainProgress, testsuitesets, auto_exit);
				startRun.start();
			}
			else {
				DefaultComboBoxModel suiteName_model = (DefaultComboBoxModel) cboSuiteName.getModel();
				String selected_suite = testsuitesets.get(0);
				for (int i = 0 ; i < suiteName_model.getSize() ; i ++)
					if (suiteName_model.getElementAt(i).toString().toLowerCase().equals(selected_suite.toLowerCase())) {
						cboSuiteName.setSelectedIndex(i);
						break;
					}
			}
		}
		
		if (wait_email_command) {
			// disable all button
			set_app_state_disable ();
			
			// go to the wait State
			WaitEmailRun startWait = new WaitEmailRun();
			startWait.start();
		}
		
		
	}

	static public SimpleDateFormat simple_date_time_format = new SimpleDateFormat("yyyy/MM/dd - HH:mm:ss");

	// Declare for test suite
	static private com.ttv.at.test.testsuiteset loaded_testsuiteset;
	static public com.ttv.at.test.testsuiteset get_loaded_testsuiteset () {return loaded_testsuiteset;}
	static public void set_loaded_testsuiteset (com.ttv.at.test.testsuiteset loaded_testsuiteset) {
		FormMain.loaded_testsuiteset = loaded_testsuiteset;

		FormMain.tableDataSetModel.clear();
		FormMain.tableTestSuiteModel.clear();
		FormMain.tableTestAreaModel.clear();
		
		if (loaded_testsuiteset != null) {
			FormMain.tableDataSetModel.set_loaded_testsuiteset(loaded_testsuiteset);
			
			// Select test suite 1
			if (loaded_testsuiteset.get_testsuites().size() > 0) {
				tableDataSet.getSelectionModel().setSelectionInterval(0, 0);
				selected_testsuite = loaded_testsuiteset.get_testsuites().get(0);
				
				// Load the test area
				if (tableTestSuite != null) {
					tableTestSuiteModel.set_loaded_testsuite(selected_testsuite);
					lblTestSuiteListTitle.setText("Test suite : " + selected_testsuite.get_name());
					tableTestSuite.getSelectionModel().setSelectionInterval(0, 0);
					tableTestAreaModel.set_loaded_testarea(selected_testsuite.get_testareas().get(0));
				}
			}
		}
	}
	static public void append_loaded_testsuiteset (com.ttv.at.test.testsuiteset input_loaded_testsuiteset) {
		if (FormMain.loaded_testsuiteset == null)
			set_loaded_testsuiteset (input_loaded_testsuiteset);
		else {
			// reload cbo
			if (input_loaded_testsuiteset != null) {
				
				String duplicated_suite_names = "";
				for (com.ttv.at.test.testsuite new_suite : input_loaded_testsuiteset.get_testsuites())
					for (com.ttv.at.test.testsuite old_suite : FormMain.loaded_testsuiteset.get_testsuites()) {
						if (old_suite.get_name().equals(new_suite.get_name()))
							duplicated_suite_names = "\n" + old_suite.get_name();
					}
				
				if (duplicated_suite_names.length() > 0) {
					JOptionPane.showMessageDialog(FormMain.mainFrame,
							"The Following suites are loaded : " + duplicated_suite_names + "\nThey will not be loaded again, Please select other testsuite to load",
						"Script Loading Eror",
						JOptionPane.ERROR_MESSAGE);
				}
				else {
					FormMain.loaded_testsuiteset.append_testsuiteset (input_loaded_testsuiteset);
				}
			}
			int old_Selection = tableDataSet.getSelectedRow();
			FormMain.tableDataSetModel.update_detail();
			if (old_Selection >= 0)
				FormMain.tableDataSet.getSelectionModel().setSelectionInterval(old_Selection, old_Selection);
		}
	}

	static private com.ttv.at.test.testsuite selected_testsuite = null;
	static private int selected_testsuite_index = -1;
	static public com.ttv.at.test.testsuite get_selected_testsuite () {return selected_testsuite;}
	static public void set_selected_testsuite (com.ttv.at.test.testsuite selected_testsuite, int selected_testsuite_index) {
		FormMain.selected_testsuite = selected_testsuite;
		FormMain.selected_testsuite_index = selected_testsuite_index;
		FormMain.tableTestSuiteModel.clear();
		FormMain.tableTestAreaModel.clear();
		if (selected_testsuite != null) {
			FormMain.tableTestSuiteModel.set_loaded_testsuite(selected_testsuite);
			lblTestSuiteListTitle.setText("Test suite : " + selected_testsuite.get_name());
			tableTestSuite.getSelectionModel().setSelectionInterval(0, 0);
			tableTestAreaModel.set_loaded_testarea(selected_testsuite.get_testareas().get(0));
		}
	}

	static private com.ttv.at.test.testsuite running_testsuite = null;
	static private int running_testsuite_index = -1;
	static public com.ttv.at.test.testsuite get_running_testsuite () {return running_testsuite;}
	static public void set_running_testsuite (com.ttv.at.test.testsuite running_testsuite, int running_testsuite_index) {
		// reset text for the old
		FormMain.running_testsuite = running_testsuite;
		FormMain.running_testsuite_index = running_testsuite_index;
	}


	static String run_state = "ready";

	// Declare GUI objects
	static public JFrame mainFrame;
	static public Container mainContainer;
	public void CreateGUIComponent () {
		// Create main display frame
		mainFrame = new JFrame("Transcosmos Automation");
		mainContainer = mainFrame.getContentPane();


		// *** Create main Menu *** //
		CreateGUIComponent_init_menu ();
		mainContainer.add(mainMenu, BorderLayout.NORTH);

		// *** Create main Panel *** //
		CreateGUIComponent_main_panel ();
		mainContainer.add(mainPanel, BorderLayout.CENTER);

		// *** Display main form *** ///
		Toolkit kit = Toolkit.getDefaultToolkit();
		Image img = kit.createImage("images\\TTV.png");
		mainFrame.setIconImage(img);
		Dimension screenSize = kit.getScreenSize();
		Dimension frameSize = new Dimension(1110, 680);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setPreferredSize(frameSize);
		mainFrame.setResizable(true);
		mainFrame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
		mainFrame.pack();
		mainFrame.setVisible(true);

		CreateGUIComponent_load_script_suite (com.ttv.at.test.testsetting.get_default_script_folder());
	}

	static ArrayList<String> ts_parent_folders = new ArrayList<String>();
	static ArrayList<String> ts_extensions = new ArrayList<String>();
	static ArrayList<String> script_suite_ts = new ArrayList<String>();
	static public void CreateGUIComponent_load_script_suite (String script_folder_full_path){
		// Add all test suite file
		CreateGUIComponent_load_script_suite_add_all_ts (script_folder_full_path, "");
		
		// Scan in each
		for (int i = 0 ; i < cboTestSuiteFile.getItemCount() ; i ++) {
			String ts_file_name = cboTestSuiteFile.getItemAt(i).toString();
			String ts_file_ext = ts_extensions.get(i);
			String ts_parent_folder = ts_parent_folders.get(i);
			
			// Get prefix
			String prefix = CreateGUIComponent_load_script_suite_get_ts_file_name_prefix(ts_file_name);
			
			// Search subFile
			String suite_name = "";
			String tc_path = "test_cases." + ts_file_ext;
			String tl_path = "test_libraries." + ts_file_ext;
			String go_path = "gui_objects." + ts_file_ext;
			if (prefix != null && prefix.length() > 0) {
				suite_name = prefix + suite_name;
				tc_path = prefix + tc_path;
				tl_path = prefix + tl_path;
				go_path = prefix + go_path;
			}
			if (ts_parent_folder != null && ts_parent_folder.length() > 0) {
				suite_name = ts_parent_folder + com.ttv.at.util.os.os_file_separator + suite_name;
				tc_path = ts_parent_folder + com.ttv.at.util.os.os_file_separator + tc_path;
				tl_path = ts_parent_folder + com.ttv.at.util.os.os_file_separator + tl_path;
				go_path = ts_parent_folder + com.ttv.at.util.os.os_file_separator + go_path;
			}
			String tc_full_path = script_folder_full_path + com.ttv.at.util.os.os_file_separator + tc_path;
			String tl_full_path = script_folder_full_path + com.ttv.at.util.os.os_file_separator + tl_path;
			String go_full_path = script_folder_full_path + com.ttv.at.util.os.os_file_separator + go_path;
			
			// Check to add to the comboBox
			if (	(new File(tc_full_path)).exists() &&
					(new File(tl_full_path)).exists() &&
					(new File(go_full_path)).exists()) {
				try {
					script_suite_ts.add(ts_file_name);
					cboGUIObjectFile.addItem(go_path);
					cboTestLibFile.addItem(tl_path);
					cboTestCaseFile.addItem(tc_path);
					cboSuiteName.addItem(suite_name);
				}
				catch (Exception e_add) {
					e_add.printStackTrace();
				}
			}
			
		}
	}
	static void CreateGUIComponent_load_script_suite_add_all_ts(String script_folder_full_path, String prefix) {
		String scan_path = script_folder_full_path;
		if (prefix != null && prefix.length() > 0)
			scan_path = script_folder_full_path + com.ttv.at.util.os.os_file_separator + prefix;

		File scanFolder = new File(scan_path);
		File[] listOfFiles = scanFolder.listFiles();
		if (listOfFiles != null && listOfFiles.length > 0) {
			// Add file for current folder
			for (File scanFile : listOfFiles) {
				if (scanFile.isFile()) {
					String scanFileName = scanFile.getName();
					int lastDot = scanFileName.lastIndexOf('.');
					String fExt = "";
					if (lastDot > 0)
						fExt = scanFileName.substring(lastDot + 1, scanFileName.length());
					if (scanFileName.toLowerCase().indexOf("test_suite") >= 0 && fExt.length() > 0 && fExt.startsWith("xls")){// Add to comboBox
						String addItem = scanFileName;
						if (prefix != null && prefix.length() > 0)
							addItem = prefix + com.ttv.at.util.os.os_file_separator + scanFileName;
						cboTestSuiteFile.addItem(addItem);
						ts_parent_folders.add(prefix);
						ts_extensions.add(fExt);
					}
				}
			}
			
			// Add file for sub folder
			for (File scanFile : listOfFiles) {
				if (scanFile.isDirectory())
					CreateGUIComponent_load_script_suite_add_all_ts (scan_path, scanFile.getName());
			}
		}
	}
	static String CreateGUIComponent_load_script_suite_get_ts_file_name_prefix (String ts_file_name) {
		int lastDefIndex = ts_file_name.lastIndexOf("test_suite.xls");
		int lastFileSep = ts_file_name.lastIndexOf(com.ttv.at.util.os.os_file_separator);
		if (lastDefIndex >= 0 && lastFileSep > 0)
			return ts_file_name.substring(lastFileSep+1, lastDefIndex);
		else if (lastDefIndex >= 0)
			return ts_file_name.substring(0, lastDefIndex);
		return null;
	}


	static public JMenuBar mainMenu;
	static public JMenu mnuFile, mnuEdit, mnuRun, mnuHelp;
	static public JMenuItem	mniOpenScriptFolder, mniExit, // mnuFile
							mniCheckSel, mniUnCheckSel, mniCheckAll, mniUnCheckAll, mniTestConfig, // mnuEdit
							mniClearLoadScript, mniLoadMoreScript, mniScheduleScript, mniStartSelected, mniStop, mniPause, mniResume,// mniExportResut,
							mniAbout, mniExecuteScript; // mnuHelp
	
	public void CreateGUIComponent_init_menu () {
		mainMenu = new JMenuBar();

		// *** init menu File *** //
		mnuFile = new JMenu("File");
		mnuFile.setMnemonic('F');

		mniOpenScriptFolder = new JMenuItem("Open script folder ...");
		mniOpenScriptFolder.setMnemonic('O');
		mniOpenScriptFolder.addActionListener(this);

		mniExit = new JMenuItem("Exit");
		mniExit.setMnemonic('X');
		mniExit.addActionListener(this);

		mnuFile.add(mniOpenScriptFolder);
		mnuFile.add(new JSeparator());
		mnuFile.add(mniExit);

		mainMenu.add(mnuFile);
		// *** *** *** *** *** *** //

		// *** Create MenuEdit components *** //
		mnuEdit = new JMenu("Edit");
		mnuEdit.setMnemonic('E');

		mniCheckSel = new JMenuItem("Check Selected");
		mniCheckSel.setMnemonic('S');
		mniCheckSel.addActionListener(this);

		mniUnCheckSel = new JMenuItem("UnCheck Selected");
		mniUnCheckSel.setMnemonic('N');
		mniUnCheckSel.addActionListener(this);

		mniCheckAll = new JMenuItem("Check All");
		mniCheckAll.setMnemonic('C');
		mniCheckAll.addActionListener(this);

		mniUnCheckAll = new JMenuItem("UnCheck All");
		mniUnCheckAll.setMnemonic('U');
		mniUnCheckAll.addActionListener(this);

		mniTestConfig = new JMenuItem("Configuration...");
		mniTestConfig.setMnemonic('F');
		mniTestConfig.addActionListener(this);
		mniTestConfig.setEnabled(false);

		mnuEdit.add(mniCheckSel);
		mnuEdit.add(mniTestConfig);
		mnuEdit.add(mniCheckAll);
		mnuEdit.add(mniUnCheckAll);
		mnuEdit.add(new JSeparator());
		mnuEdit.add(mniTestConfig);

		mainMenu.add(mnuEdit);
		// *** *** *** *** *** *** //

		// *** Create MenuRun components *** //
		mnuRun = new JMenu("Run");
		mnuRun.setMnemonic('R');

		mniClearLoadScript = new JMenuItem("Clear Load Test Suites");
		mniClearLoadScript.setMnemonic('L');
		mniClearLoadScript.addActionListener(this);

		mniLoadMoreScript = new JMenuItem("Load More Test Suites");
		mniLoadMoreScript.setMnemonic('A');
		mniLoadMoreScript.addActionListener(this);
		
		mniScheduleScript = new JMenuItem("Schedule Test Suites");
		mniScheduleScript.addActionListener(this);

		mniStartSelected = new JMenuItem("Start Current Test Suite");
		mniStartSelected.setEnabled(false);
		mniStartSelected.addActionListener(this);
		
//		mniExecuteScript = new JMenuItem("Execute Script E2S2");
//		mniExecuteScript.setMnemonic('A');
//		mniExecuteScript.addActionListener(this);

		mniStop = new JMenuItem("Stop");
		mniStop.setMnemonic('O');
		mniStop.addActionListener(this);

		mniPause = new JMenuItem("Pause");
		mniPause.setMnemonic('P');
		mniPause.addActionListener(this);

		mniResume = new JMenuItem("Resume");
		mniResume.setMnemonic('R');
		mniResume.addActionListener(this);
/*
		mniExportResut = new JMenuItem("Export Result to DB");
		mniExportResut.setMnemonic('E');
		mniExportResut.addActionListener(this);
*/
		mnuRun.add(mniClearLoadScript);
		mnuRun.add(mniLoadMoreScript);
		mnuRun.add(mniScheduleScript);
		// mnuRun.add(mniExecuteScript);
		mnuRun.add(new JSeparator());
		mnuRun.add(mniStartSelected);
		mnuRun.add(mniStop);
		mnuRun.add(mniPause);
		mnuRun.add(mniResume);
		mnuRun.add(new JSeparator());
		// mnuRun.add(mniExportResut);

		mainMenu.add(mnuRun);
		// *** *** *** *** *** *** //

		// *** Create MenuHelp components *** //
		mnuHelp = new JMenu("Help");
		mnuHelp.setMnemonic('H');

		mniAbout = new JMenuItem(txtAboutText);
		mniAbout.setEnabled(false);
		mniAbout.addActionListener(this);

		mnuHelp.add(mniAbout);

		mainMenu.add(mnuHelp);
		// *** *** *** *** *** *** //
	}

	static public JPanel mainPanel;
	public void CreateGUIComponent_main_panel () {
		mainPanel = new JPanel(new BorderLayout());

		// Create tool bar
		CreateGUIComponent_main_panel_toolbar ();
		mainPanel.add(mainToolBarPanel, BorderLayout.NORTH);

		// Create table panel
		CreateGUIComponent_main_panel_table_panel ();
		mainPanel.add(mainTablePanel, BorderLayout.CENTER);

		// Create output panel

	}

	static public JPanel mainToolBarPanel;
	static public JToolBar mainToolBar;
	static public JButton btnStartSelected, btnStop, btnPause, btnResume, btnClearLoadScript, btnExecuteScript, btnLoadMoreScript, btnScheduleScript; //, btnExportResult;
	static public JProgressBar mainProgress;
	public void CreateGUIComponent_main_panel_toolbar () {
		mainToolBar = new JToolBar();
		mainToolBar.setFloatable(false);
		mainToolBar.add(new javax.swing.JToolBar.Separator());
		
		btnStartSelected = new JButton();
		btnStartSelected.setToolTipText("Start Current Test Suite");
		// btnStart.setEnabled(false);
		btnStartSelected.addActionListener(this);
		btnStartSelected.setIcon(new javax.swing.ImageIcon("images\\Play_48x48.png"));
		btnStartSelected.setBorder(null);//javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

		mainToolBar.add(btnStartSelected);

		mainToolBar.add(new javax.swing.JToolBar.Separator());

		mainToolBar.add(new javax.swing.JToolBar.Separator());

		btnStop = new JButton();
		btnStop.setToolTipText("Stop");
		// btnStop.setEnabled(false);
		btnStop.addActionListener(this);
		btnStop.setIcon(new javax.swing.ImageIcon("images\\Stop_48x48.png")); // NOI18N
		btnStop.setBorder(null);//javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

		mainToolBar.add(btnStop);

		mainToolBar.add(new javax.swing.JToolBar.Separator());

		btnPause = new JButton();
		btnPause.setToolTipText("Pause");
		// btnPause.setEnabled(false);
		btnPause.addActionListener(this);
		btnPause.setIcon(new javax.swing.ImageIcon("images\\Pause_48x48.png"));
		btnPause.setBorder(null);//javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

		mainToolBar.add(btnPause);

		btnResume = new JButton();
		btnResume.setToolTipText("Resume");
		// btnPause.setEnabled(false);
		btnResume.addActionListener(this);
		btnResume.setIcon(new javax.swing.ImageIcon("images\\Resume_48x48.png"));
		btnResume.setBorder(null);//javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

		mainToolBar.add(btnResume);


		mainToolBar.add(new javax.swing.JToolBar.Separator());

		btnClearLoadScript = new JButton();
		btnClearLoadScript.setToolTipText("Clear Load Test Suites");
		// btnPause.setEnabled(false);
		btnClearLoadScript.addActionListener(this);
		btnClearLoadScript.setIcon(new javax.swing.ImageIcon("images\\Load_48x48.png"));
		btnClearLoadScript.setBorder(null);//javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

		mainToolBar.add(btnClearLoadScript);

		mainToolBar.add(new javax.swing.JToolBar.Separator());

		btnLoadMoreScript = new JButton();
		btnLoadMoreScript.setToolTipText("Load More Test Suites");
		// btnPause.setEnabled(false);
		btnLoadMoreScript.addActionListener(this);
		btnLoadMoreScript.setIcon(new javax.swing.ImageIcon("images\\LoadMore_48x48.png"));
		btnLoadMoreScript.setBorder(null);//javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

		mainToolBar.add(btnLoadMoreScript);

		mainToolBar.add(new javax.swing.JToolBar.Separator());
		
		
		
		btnScheduleScript = new JButton();
		btnScheduleScript.setToolTipText("Schedule Tests");
		// btnPause.setEnabled(false);
		btnScheduleScript.addActionListener(this);
		btnScheduleScript.setIcon(new javax.swing.ImageIcon("images\\Schedule_48x48.png"));
		btnScheduleScript.setBorder(null);

		mainToolBar.add(btnScheduleScript);

		mainToolBar.add(new javax.swing.JToolBar.Separator());
		
//		btnExecuteScript = new JButton();
//		btnExecuteScript.setToolTipText("Execute Script");
//		// btnPause.setEnabled(false);
//		btnExecuteScript.addActionListener(this);
//		btnExecuteScript.setIcon(new javax.swing.ImageIcon("images\\Load_48x48.png"));
//		btnExecuteScript.setBorder(null);//javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

//		mainToolBar.add(btnExecuteScript);
		
		
/*
		btnExportResult = new JButton();
		btnExportResult.setToolTipText("Export Result to Database");
		// btnPause.setEnabled(false);
		btnExportResult.addActionListener(this);
		btnExportResult.setIcon(new javax.swing.ImageIcon("images\\ExportDB_48x48.png"));
		btnExportResult.setBorder(null);//javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

		mainToolBar.add(btnExportResult); */


		// Init Proress Bar
		mainProgress = new JProgressBar();
		mainProgress.setPreferredSize(new Dimension(600, 20));

		mainToolBarPanel = new JPanel( new BorderLayout());
		mainToolBarPanel.add(mainToolBar, BorderLayout.NORTH);
		mainToolBarPanel.add(mainProgress, BorderLayout.SOUTH);
	}

	static public JPanel mainTablePanel;
	static public JSplitPane mainSplitPane, mainSplitPane_left, mainSplitPane_left_down, mainSplitPane_right;
	static public JTable tableTestResult;

	static String selected_ts_full_path;
	static String selected_tc_full_path;
	static String selected_tl_full_path;
	static String selected_go_full_path;
	static public String get_selected_ts_full_path() {return selected_ts_full_path;};
	static public String get_selected_tc_full_path() {return selected_tc_full_path;};
	static public String get_selected_tl_full_path() {return selected_tl_full_path;};
	static public String get_selected_go_full_path() {return selected_go_full_path;};
	static public JComboBox cboSuiteName, cboTestSuiteFile, cboTestCaseFile, cboTestLibFile, cboGUIObjectFile; // , btnReLoadTestSuite;
	public void CreateGUIComponent_main_panel_table_panel() {

		UIManager.put( "ComboBox.disabledForeground", Color.DARK_GRAY );
		
		mainTablePanel = new JPanel(new BorderLayout());

		mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		mainSplitPane.setDividerLocation(500);
		mainSplitPane_right = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		mainSplitPane_right.setDividerLocation(200);
		mainSplitPane_left = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		mainSplitPane_left_down = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

		// init Test Suite table
		CreateGUIComponent_main_panel_table_panel_test_suite_table ();
		JScrollPane tableTestAreas_pane = new JScrollPane(tableTestSuite, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		// JScrollPane tableTestAreas_pane = new JScrollPane(tableTestSuite, );
		tableTestAreas_pane.setSize(new Dimension(400, 400));
		tableTestAreas_pane.setMinimumSize(new Dimension(50, 50));

		JPanel tableTestAreas_mainPanel_suitelist = new JPanel();
		tableTestAreas_mainPanel_suitelist.add(lblTestSuiteListTitle, BorderLayout.EAST);
		// tableTestAreas_mainPanel_suitelist.add(btnRefreshTestSuite, BorderLayout.WEST);
		
		JPanel tableTestAreas_mainPanel = new JPanel(new BorderLayout());
		tableTestAreas_mainPanel.add(tableTestAreas_mainPanel_suitelist, BorderLayout.NORTH);
		tableTestAreas_mainPanel.add(tableTestAreas_pane, BorderLayout.CENTER);
		
		
		
		mainSplitPane_right.setTopComponent(tableTestAreas_mainPanel);

		// init Test Area Detail table
		CreateGUIComponent_main_panel_table_panel_test_area_table ();
		JScrollPane tableTestAreaDetail_pane = new JScrollPane(tableTestArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		tableTestAreaDetail_pane.setMinimumSize(new Dimension(50, 50));
		mainSplitPane_right.setBottomComponent(tableTestAreaDetail_pane);

		// Create test result table
		tableTestResult = new JTable();
		JScrollPane tableTestResult_pane = new JScrollPane(tableTestResult);
		tableTestResult_pane.setPreferredSize(new Dimension(600, 300));
		tableTestResult_pane.setMinimumSize(new Dimension(50, 50));

		// add tables to up split pane
		mainSplitPane.setLeftComponent(mainSplitPane_left);

		// init combobox
		JPanel palSuiteName = new JPanel(new BorderLayout());
		JLabel lblSuiteName = new		JLabel("Prefix Name ");
		lblSuiteName.setPreferredSize(new Dimension(80, 20));
		cboSuiteName = 	new JComboBox();
		cboSuiteName.setPreferredSize(new Dimension(300, 20));
		palSuiteName.add(lblSuiteName, BorderLayout.WEST);
		palSuiteName.add(cboSuiteName, BorderLayout.CENTER);
		cboSuiteName.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				// Update for test suite file
				int cboSuiteName_selectedIndex = cboSuiteName.getSelectedIndex();
				if (cboSuiteName.getItemCount() > 0 && 
						cboSuiteName_selectedIndex >= 0) {
					String selected_script_suite_ts = script_suite_ts.get(cboSuiteName_selectedIndex);
					for (int i = 0 ; i < cboTestSuiteFile.getItemCount() ; i ++) {
						String cur_item = cboTestSuiteFile.getItemAt(i).toString();
						if (cur_item.equals(selected_script_suite_ts)) {
							cboTestSuiteFile.setSelectedIndex(i);
							break;
						}
					}
					if (cboTestCaseFile.getItemCount() > cboSuiteName.getSelectedIndex())
						cboTestCaseFile.setSelectedIndex(cboSuiteName.getSelectedIndex());
					if (cboTestLibFile.getItemCount() > cboSuiteName.getSelectedIndex())
						cboTestLibFile.setSelectedIndex(cboSuiteName.getSelectedIndex());
					if (cboGUIObjectFile.getItemCount() > cboSuiteName.getSelectedIndex())
						cboGUIObjectFile.setSelectedIndex(cboSuiteName.getSelectedIndex());
					
					reload_app_state();
				}
			}
		});

		JPanel palTestSuiteFile = new JPanel(new BorderLayout());
		JLabel lblTestSuiteFile = new	JLabel("Test Suite  ");
		lblTestSuiteFile.setPreferredSize(new Dimension(80, 20));
		cboTestSuiteFile = 	new JComboBox();
		cboTestSuiteFile.setPreferredSize(new Dimension(300, 20));
		cboTestSuiteFile.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				selected_ts_full_path = com.ttv.at.test.testsetting.get_default_script_folder() + com.ttv.at.util.os.os_file_separator + cboTestSuiteFile.getSelectedItem().toString();
			}
		});
		
		JPanel panelLoadTestsuite = new JPanel(new BorderLayout());
		palTestSuiteFile.add(lblTestSuiteFile, BorderLayout.WEST);
		palTestSuiteFile.add(cboTestSuiteFile, BorderLayout.CENTER);
		palTestSuiteFile.add(panelLoadTestsuite, BorderLayout.EAST);

		JPanel palTestCaseFile = new JPanel(new BorderLayout());
		JLabel lblTestCaseFile = new	JLabel("Test Case   ");
		lblTestCaseFile.setPreferredSize(new Dimension(80, 20));
		cboTestCaseFile = 	new JComboBox();
		cboTestCaseFile.setEnabled(false);
		cboTestCaseFile.setPreferredSize(new Dimension(300, 20));
		cboTestCaseFile.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				selected_tc_full_path = com.ttv.at.test.testsetting.get_default_script_folder() + com.ttv.at.util.os.os_file_separator + cboTestCaseFile.getSelectedItem().toString();
			}
		});
		JPanel panelLoadTestCase = new JPanel(new BorderLayout());
		palTestCaseFile.add(lblTestCaseFile, BorderLayout.WEST);
		palTestCaseFile.add(cboTestCaseFile, BorderLayout.CENTER);
		palTestCaseFile.add(panelLoadTestCase, BorderLayout.EAST);

		JPanel palTestLibFile = new JPanel(new BorderLayout());
		JLabel lblTestLibFile = new		JLabel("Test Library");
		lblTestLibFile.setPreferredSize(new Dimension(80, 20));
		cboTestLibFile = 	new JComboBox();
		cboTestLibFile.setEnabled(false);
		cboTestLibFile.setPreferredSize(new Dimension(300, 20));
		cboTestLibFile.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				selected_tl_full_path = com.ttv.at.test.testsetting.get_default_script_folder() + com.ttv.at.util.os.os_file_separator + cboTestLibFile.getSelectedItem().toString();
			}
		});
		JPanel panelLoadTestLib = new JPanel(new BorderLayout());
		palTestLibFile.add(lblTestLibFile, BorderLayout.WEST);
		palTestLibFile.add(cboTestLibFile, BorderLayout.CENTER);
		palTestLibFile.add(panelLoadTestLib, BorderLayout.EAST);

		JPanel palGUIObjectFile = new JPanel(new BorderLayout());
		JLabel lblGUIObjectFile = new	JLabel("GUI Object  ");
		lblGUIObjectFile.setPreferredSize(new Dimension(80, 20));
		cboGUIObjectFile = 	new JComboBox();
		cboGUIObjectFile.setEnabled(false);
		cboGUIObjectFile.setPreferredSize(new Dimension(300, 20));
		cboGUIObjectFile.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				selected_go_full_path = com.ttv.at.test.testsetting.get_default_script_folder() + com.ttv.at.util.os.os_file_separator + cboGUIObjectFile.getSelectedItem().toString();
			}
		});
		JPanel panelLoadTestObj = new JPanel(new BorderLayout());
		palGUIObjectFile.add(lblGUIObjectFile, BorderLayout.WEST);
		palGUIObjectFile.add(cboGUIObjectFile, BorderLayout.CENTER);
		palGUIObjectFile.add(panelLoadTestObj, BorderLayout.EAST);

		JPanel loadSuite = new JPanel(new GridLayout(5,1));
		loadSuite.add(palSuiteName);
		loadSuite.add(palTestSuiteFile);
		loadSuite.add(palTestCaseFile);
		loadSuite.add(palTestLibFile);
		loadSuite.add(palGUIObjectFile);

		mainSplitPane_left.setTopComponent(loadSuite);

		// init output
		CreateGUIComponent_main_panel_table_panel_output ();
		JPanel noWrapPanel = new JPanel( new BorderLayout() );
		noWrapPanel.add(txtOutput); // anti wrap text
		panelOutput = new JScrollPane(noWrapPanel);

		
		mainSplitPane_left.setBottomComponent(mainSplitPane_left_down);
		// mainSplitPane_left_down.setTopComponent(comp);
		mainSplitPane_left_down.setBottomComponent(panelOutput);
		panelOutput_VerticalScrollBar = panelOutput.getVerticalScrollBar();
		mainSplitPane.setRightComponent(mainSplitPane_right);

		mainTablePanel.add(mainSplitPane, BorderLayout.CENTER);
		
		
		// init table for dataset
		tableDataSetModel = com.ttv.at.tablemodel.testdataset.get_instance();
		tableDataSet = new JTable(tableDataSetModel);
		
		tableDataSet.getTableHeader().setPreferredSize(new Dimension(660, 22));
		tableDataSet.getTableHeader().setReorderingAllowed(false);
		tableDataSet.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));

		// Set Column size
		tableDataSet.getColumnModel().getColumn(0).setPreferredWidth(10);
		tableDataSet.getColumnModel().getColumn(1).setPreferredWidth(20);
		tableDataSet.getColumnModel().getColumn(2).setPreferredWidth(20);
		tableDataSet.getColumnModel().getColumn(3).setPreferredWidth(150);
		tableDataSet.getColumnModel().getColumn(4).setPreferredWidth(200);
		tableDataSet.getColumnModel().getColumn(5).setPreferredWidth(50);
		/**/
		tableDataSet.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				if (tableDataSetModel != null) {
					int row = tableDataSet.getSelectedRow();
					int col = tableDataSet.getSelectedColumn();
					if(col < 0 || row < 0)
						return;

					selected_testsuite = loaded_testsuiteset.get_testsuites().get(row);
					
					if(col == com.ttv.at.tablemodel.testdataset.COLUMN_RUN)
						tableDataSetModel.toggle_test_selection(row);
					else if (col == com.ttv.at.tablemodel.testdataset.COLUMN_REFRESH) {
						// refresh
						ReloadTestSuite startReload = new ReloadTestSuite(FormMain.get_selected_testsuite());
						startReload.start();
					}
					
					// Load the test area
					if (tableTestSuite != null) {
						tableTestSuiteModel.set_loaded_testsuite(selected_testsuite);
						lblTestSuiteListTitle.setText("Test suite : " + selected_testsuite.get_name());
						// select 0 for test area
						tableTestSuite.getSelectionModel().setSelectionInterval(0, 0);
						tableTestAreaModel.set_loaded_testarea(selected_testsuite.get_testareas().get(0));
					}
				}
			}
		});
		
		JScrollPane tableDataSet_pane = new JScrollPane(tableDataSet, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		tableDataSet_pane.setSize(new Dimension(400, 400));
		tableDataSet_pane.setMinimumSize(new Dimension(50, 100));
		mainSplitPane_left_down.setTopComponent(tableDataSet_pane);
		
	}

	static public JLabel lblTestSuiteListTitle;
	static public String lblTestSuiteListTitle_deafult = "Please Select Test Suite";
	static public JButton btnRefreshTestSuite;
	static public JTable tableDataSet;
	static public com.ttv.at.tablemodel.testdataset tableDataSetModel;
	static public JTable tableTestSuite;
	static public com.ttv.at.tablemodel.testsuite tableTestSuiteModel;
	public void CreateGUIComponent_main_panel_table_panel_test_suite_table () {
		btnRefreshTestSuite = new JButton();
		btnRefreshTestSuite.setIcon(new javax.swing.ImageIcon("images\\refresh_16x16.png"));
		btnRefreshTestSuite.setPreferredSize(new Dimension(20,20));
		btnRefreshTestSuite.addActionListener(this);
		lblTestSuiteListTitle = new JLabel(lblTestSuiteListTitle_deafult);
		
		tableTestSuiteModel = com.ttv.at.tablemodel.testsuite.get_instance();
		tableTestSuite = new JTable(tableTestSuiteModel);
		tableTestSuite.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		// tableTestSuite.setPreferredSize(new Dimension(600, 240));
		tableTestSuite.getTableHeader().setPreferredSize(new Dimension(660, 22));
		tableTestSuite.getTableHeader().setReorderingAllowed(false);
		tableTestSuite.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));

		// Set Column size
		tableTestSuite.getColumnModel().getColumn(0).setPreferredWidth(20);
		tableTestSuite.getColumnModel().getColumn(1).setPreferredWidth(40);
		tableTestSuite.getColumnModel().getColumn(2).setPreferredWidth(300);
		tableTestSuite.getColumnModel().getColumn(3).setPreferredWidth(50);
		tableTestSuite.getColumnModel().getColumn(4).setPreferredWidth(50);
		tableTestSuite.getColumnModel().getColumn(5).setPreferredWidth(50);
		tableTestSuite.getColumnModel().getColumn(6).setPreferredWidth(50);

		// Add action listenner
		tableTestSuite.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				if (tableTestSuiteModel != null) {
					int row = tableTestSuite.getSelectedRow();
					int col = tableTestSuite.getSelectedColumn();
					if(col < 0 || row < 0)
						return;

					if(col == 1) { // Change the selection of the item
						String state = tableTestSuite.getValueAt(row, 1).toString();
						if (state.equals("true"))
							tableTestSuiteModel.checkedAtRow(row);
						else
							tableTestSuiteModel.uncheckedAtRow(row);
					}

					// Load the test area
					if (tableTestAreaModel != null) {
						tableTestAreaModel.set_loaded_testarea(selected_testsuite.get_testareas().get(row));
					}
				}
			}
		});
	}

	static public JTable tableTestArea;
	static public com.ttv.at.tablemodel.testarea tableTestAreaModel;
	public void CreateGUIComponent_main_panel_table_panel_test_area_table () {
		tableTestAreaModel = com.ttv.at.tablemodel.testarea.get_instance();
		String[] severities = {"Critical", "High", "Medium", "Low"};
		JComboBox severityCombo = new JComboBox (severities);
		// severityCombo.setEditable(true);
		final DefaultCellEditor severity_editor = new DefaultCellEditor(severityCombo);
		
		tableTestArea = new JTable(tableTestAreaModel) {
			public TableCellEditor getCellEditor(int row, int column) {
				if (column == com.ttv.at.tablemodel.testarea.COLUMN_SEVERITY){
					return severity_editor;
				}
				return super.getCellEditor(row, column);
			}
		};
		
		tableTestArea.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		// tableTestArea.setPreferredSize(new Dimension(1000, 240));
		tableTestArea.getTableHeader().setPreferredSize(new Dimension(1110, 22));
		tableTestArea.getTableHeader().setReorderingAllowed(false);
		tableTestArea.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));

		// Set Column size
		tableTestArea.getColumnModel().getColumn(0).setPreferredWidth(20);
		tableTestArea.getColumnModel().getColumn(1).setPreferredWidth(30);
		tableTestArea.getColumnModel().getColumn(2).setPreferredWidth(20);
		tableTestArea.getColumnModel().getColumn(3).setPreferredWidth(300);
		tableTestArea.getColumnModel().getColumn(4).setPreferredWidth(80);
		tableTestArea.getColumnModel().getColumn(5).setPreferredWidth(80);
		tableTestArea.getColumnModel().getColumn(6).setPreferredWidth(80);
		tableTestArea.getColumnModel().getColumn(7).setPreferredWidth(500);

		tableTestArea.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				if (tableTestAreaModel != null && tableTestArea.isEnabled()) {
					int row = tableTestArea.getSelectedRow();
					int col = tableTestArea.getSelectedColumn();
					if(col < 0 || row < 0)
						return;

					if(col == com.ttv.at.tablemodel.testarea.COLUMN_RUN) { // Change the selection of the item
						tableTestAreaModel.toggle_test_selection(row);
						com.ttv.at.tablemodel.testsuite.update_instance_detail();
					}
					else if (col == com.ttv.at.tablemodel.testarea.COLUMN_FOUNDBYMANUAL) { // found by manual
						tableTestAreaModel.toggle_found_by_manual_at_row(row);
						com.ttv.at.tablemodel.testsuite.update_instance_detail();
					}
					else if (col == com.ttv.at.tablemodel.testarea.COLUMN_DEBUG) { // found by manual
						com.ttv.at.test.test cur_test = tableTestAreaModel.get_test_instance_at_row (row);
						if (cur_test != null) {
							TestDebugger.get_instance().set_test_instance(cur_test);
							TestDebugger.get_instance().show();
						}
					}
				}
			}
		});
	}

	static public JScrollPane panelOutput;
	static public JScrollBar panelOutput_VerticalScrollBar;
	static public JTextPane txtOutput;
	static public StyledDocument txtOutput_doc;
	static public Style txtOutput_style;
	public void CreateGUIComponent_main_panel_table_panel_output () {
		// text for output
		txtOutput = new JTextPane();
		txtOutput.setEditable(false);
		txtOutput.setEnabled(true);
		txtOutput.setBackground(Color.white);
		txtOutput.setForeground(Color.black);
		txtOutput_doc = txtOutput.getStyledDocument();
		txtOutput_style = txtOutput.addStyle("KMS Style", null);

		txtOutput.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent ev) {
				// TODO Auto-generated method stub
				if (! txtOutput.isEditable()) {

					Point pt = new Point(ev.getX(), ev.getY());
					int pos = txtOutput.viewToModel(pt);
					if (pos >= 0) {
						Document doc = txtOutput.getDocument();
						if (doc instanceof DefaultStyledDocument) {
							DefaultStyledDocument hdoc = (DefaultStyledDocument) doc;
							Element e = hdoc.getCharacterElement(pos);
							AttributeSet a = e.getAttributes();
							String href = (String) a.getAttribute(HTML.Attribute.HREF);
							if (href != null && href.length() > 0) {
								try {
									File image_file = new File(href);
									if (image_file.exists()) {
										if (com.ttv.at.util.os.isWindows()) {
											String command_line = "explorer \"" + image_file + "\"";
											Runtime.getRuntime().exec(command_line);
										}
									}
									else {
										String folder = href;
										JOptionPane.showMessageDialog(FormMain.mainFrame,
												"Failed image: \"" + href + "\" is not found",
											"image warning",
											JOptionPane.WARNING_MESSAGE);
									}
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
						}
					}
				}
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		stop = false;
		// TODO Auto-generated method stub
		if (e.getSource() == btnClearLoadScript || e.getSource() == mniClearLoadScript) {
			actionPerformed_clearloadscript();
		}
		else if (e.getSource() == btnLoadMoreScript || e.getSource() == mniLoadMoreScript) {
			actionPerformed_loadmorescript();
		}
//		else if (e.getSource() == btnExecuteScript || e.getSource() == mniExecuteScript) {
//			actionPerformed_executeScript();
//		}
		else if (e.getSource() == btnScheduleScript || e.getSource() == mniScheduleScript) {
			actionPerformed_scheduleTests();
		}
		else if (e.getSource() == btnStartSelected || e.getSource() == mniStartSelected) {
			run_state = "start testsuite";
			FormMain.running_testsuite = FormMain.selected_testsuite;
			FormMain.running_testsuite_index = FormMain.selected_testsuite_index;
			executeTestSuiteSet startRun = new executeTestSuiteSet(FormMain.mainProgress);
			startRun.start();
		}
		else if (e.getSource() == btnStop || e.getSource() == mniStop) {
			stop = true;
			disable_app_state();
			FormMain.get_loaded_testsuiteset().stop();
			run_state = "stop testsuiteset";
		}
		else if (e.getSource() == btnPause || e.getSource() == mniPause) {
			disable_app_state();
			run_state = "pause testsuiteset";
			FormMain.get_loaded_testsuiteset().pause();
		}
		else if (e.getSource() == btnResume || e.getSource() == mniResume) {
			run_state = "resume testsuiteset";
			resumeTestSuiteSet resumeRun = new resumeTestSuiteSet(FormMain.mainProgress);
			resumeRun.start();
		}
		else if (e.getSource() == mniCheckAll) {
			check_all_tests();
		}
		else if (e.getSource() == mniUnCheckAll) {
			uncheck_all_tests();
		}
		else if (e.getSource() == mniExit)
			System.exit(0);
	}
	private void actionPerformed_executeScript() {
		
		
	}
	
	public void testAPI() {
		/*
		 * try {
		 * 
		 * URL url = new URL("http://localhost:3000/authorize/v1/cvservice");
		 * HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		 * conn.setDoOutput(true); conn.setRequestMethod("POST");
		 * 
		 * conn.setRequestProperty("X-apiName", "authorizev1cvservice");
		 * conn.setRequestProperty("X-sceName", "authorizev1cvservice_post_001");
		 * conn.setRequestProperty("Authorization", "dGVzdDp0ZXN0MTIz");
		 * conn.setRequestProperty("X-TransactionId", "AUTOTEST-20191113173347937");
		 * conn.setRequestProperty("Content-Type", "application/json");
		 * conn.setRequestProperty("Accept", "application/json");
		 * 
		 * String input = "{\"internalVin\":12345,\"internalUserId\":12345}";
		 * 
		 * OutputStream os = conn.getOutputStream(); os.write(input.getBytes());
		 * os.flush();
		 * 
		 * BufferedReader br = new BufferedReader(new InputStreamReader(
		 * (conn.getInputStream())));
		 * 
		 * String output; StringBuffer content = new StringBuffer();
		 * System.out.println("Output from Server .... \n"); while ((output =
		 * br.readLine()) != null) { content.append(output); }
		 * 
		 * System.out.print(content.toString()); conn.disconnect();
		 * 
		 * } catch (MalformedURLException e) {
		 * 
		 * e.printStackTrace();
		 * 
		 * } catch (IOException e) {
		 * 
		 * e.printStackTrace();
		 * 
		 * }
		 */
	}
	private void actionPerformed_clearloadscript() {
		com.ttv.at.test.testsetting.clear_default_log();
		FormMain.tableDataSetModel.clear();
		
		if (	selected_ts_full_path != null && selected_ts_full_path.length() > 0 &&
				selected_tc_full_path != null && selected_tc_full_path.length() > 0 &&
						selected_tl_full_path != null && selected_tl_full_path.length() > 0 &&
								selected_go_full_path != null && selected_go_full_path.length() > 0) {
			ClearLoadTestScript startRun = new ClearLoadTestScript(cboSuiteName.getSelectedItem().toString(), selected_ts_full_path, selected_tc_full_path, selected_tl_full_path, selected_go_full_path);
			startRun.start();
		}
	}
	private void actionPerformed_loadmorescript() {
		if (	selected_ts_full_path != null && selected_ts_full_path.length() > 0 &&
				selected_tc_full_path != null && selected_tc_full_path.length() > 0 &&
						selected_tl_full_path != null && selected_tl_full_path.length() > 0 &&
								selected_go_full_path != null && selected_go_full_path.length() > 0) {
			LoadMoreTestScript startRun = new LoadMoreTestScript(cboSuiteName.getSelectedItem().toString(), selected_ts_full_path, selected_tc_full_path, selected_tl_full_path, selected_go_full_path);
			startRun.start();
		}
	}
	private void actionPerformed_scheduleTests () {
		JFileChooser fileOpen = new JFileChooser(com.ttv.at.test.testsetting.get_default_schedule_folder());
		int returnVal = fileOpen.showOpenDialog(mainPanel);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fileOpen.getSelectedFile();
			//This is where a real application would open the file.
			System.out.println("Opening Schedule: " + file.getAbsolutePath() + ".");
			scheduleTestsuite scheduleT = new scheduleTestsuite(file.getAbsolutePath());
			scheduleT.start();
		}
	}
	// 0: disable
	// 1: start-up
	// 2: ready
	// 3: running
	// 4: pause
	// 5: end_test
	static private int app_state = 0;
	static public int get_app_state () { return app_state; }
	static public void set_app_state (int app_state) {
		FormMain.app_state = app_state;
		reload_app_state ();
	}
	static public void set_app_state_disable () {
		FormMain.app_state = 0;
		reload_app_state ();
	}
	static public void set_app_state_startup () {
		FormMain.app_state = 1;
		reload_app_state ();
	}
	static public void set_app_state_ready () {
		FormMain.app_state = 2;
		reload_app_state ();
	}
	static public void set_app_state_running () {
		FormMain.app_state = 3;
		reload_app_state ();
	}
	static public void set_app_state_pause () {
		FormMain.app_state = 4;
		reload_app_state ();
	}
	static public void set_app_state_endtest () {
		FormMain.app_state = 5;
		reload_app_state ();
	}
	
	static public void reload_app_state () {
		if (app_state == 0) { // DISABLE
			// button
			btnStartSelected.setEnabled(false);
			btnStop.setEnabled(false);
			btnPause.setEnabled(false);
			btnResume.setEnabled(false);
			// btnExecuteScript.setEnabled(false);
			btnClearLoadScript.setEnabled(false);
			btnLoadMoreScript.setEnabled(false);
			btnScheduleScript.setEnabled(false);
			// btnExportResult.setEnabled(false);

			// menu
			mniStartSelected.setEnabled(false);
			mniStop.setEnabled(false);
			mniPause.setEnabled(false);
			mniResume.setEnabled(false);
			mniClearLoadScript.setEnabled(false);
			// mniExecuteScript.setEnabled(false);
			mniLoadMoreScript.setEnabled(false);
			mniScheduleScript.setEnabled(false);
			mniOpenScriptFolder.setEnabled(false);
			mniCheckSel.setEnabled(false);
			mniUnCheckSel.setEnabled(false);
			mniCheckAll.setEnabled(false);
			mniUnCheckAll.setEnabled(false);
			mniTestConfig.setEnabled(false);
			// mniExportResut.setEnabled(false);
			
			disable_btnLoadScript();
		}
		else if (app_state == 1) { // START-UP
			// button
			btnStartSelected.setEnabled(false);
			btnStop.setEnabled(false);
			btnPause.setEnabled(false);
			btnResume.setEnabled(false);
			btnClearLoadScript.setEnabled(true);
			btnLoadMoreScript.setEnabled(true);
			btnScheduleScript.setEnabled(true);
			// btnExecuteScript.setEnabled(true);
			// btnExportResult.setEnabled(false);

			// menu
			mniStartSelected.setEnabled(false);
			mniStop.setEnabled(false);
			mniPause.setEnabled(false);
			mniResume.setEnabled(false);
			mniClearLoadScript.setEnabled(true);
			mniLoadMoreScript.setEnabled(true);
			mniScheduleScript.setEnabled(true);
			// mniExecuteScript.setEnabled(true);
			mniOpenScriptFolder.setEnabled(false);
			mniCheckSel.setEnabled(false);
			mniUnCheckSel.setEnabled(false);
			mniCheckAll.setEnabled(false);
			mniUnCheckAll.setEnabled(false);
			mniTestConfig.setEnabled(false);
			// mniExportResut.setEnabled(false);
			
			enable_btnLoadScript();
			
		}
		else if (app_state == 2) { // READY
			btnStartSelected.setEnabled(true);
			btnStop.setEnabled(false);
			btnPause.setEnabled(false);
			btnResume.setEnabled(false);
			btnClearLoadScript.setEnabled(false);
			btnLoadMoreScript.setEnabled(false);
			btnScheduleScript.setEnabled(false);
			// btnExecuteScript.setEnabled(false);
			// btnExportResult.setEnabled(false);

			// menu
			mniStartSelected.setEnabled(true);
			mniStop.setEnabled(false);
			mniPause.setEnabled(false);
			mniResume.setEnabled(false);
			mniClearLoadScript.setEnabled(false);
			mniLoadMoreScript.setEnabled(false);
			mniScheduleScript.setEnabled(false);
			mniOpenScriptFolder.setEnabled(false);
			// mniExecuteScript.setEnabled(false);
			mniCheckSel.setEnabled(false);
			mniUnCheckSel.setEnabled(false);
			mniCheckAll.setEnabled(true);
			mniUnCheckAll.setEnabled(true);
			mniTestConfig.setEnabled(false);
			// mniExportResut.setEnabled(false);
			
			enable_btnLoadScript();
		}
		else if (app_state == 3) { // RUNNING
			btnStartSelected.setEnabled(false);
			btnStop.setEnabled(true);
			btnPause.setEnabled(true);
			btnResume.setEnabled(false);
			btnClearLoadScript.setEnabled(false);
			btnLoadMoreScript.setEnabled(false);
			btnScheduleScript.setEnabled(false);
			// btnExecuteScript.setEnabled(false);

			// menu
			mniStartSelected.setEnabled(false);
			mniStop.setEnabled(true);
			mniPause.setEnabled(true);
			mniResume.setEnabled(false);
			mniClearLoadScript.setEnabled(false);
			mniLoadMoreScript.setEnabled(false);
			mniScheduleScript.setEnabled(false);
			// mniExecuteScript.setEnabled(false);
			mniOpenScriptFolder.setEnabled(false);
			mniCheckSel.setEnabled(false);
			mniUnCheckSel.setEnabled(false);
			mniCheckAll.setEnabled(false);
			mniUnCheckAll.setEnabled(false);
			mniTestConfig.setEnabled(false);
			
			disable_btnLoadScript();
		}
		else if (app_state == 4) { // PAUSE
			btnStartSelected.setEnabled(false);
			btnStop.setEnabled(false);
			btnPause.setEnabled(false);
			btnResume.setEnabled(true);
			btnClearLoadScript.setEnabled(false);
			btnLoadMoreScript.setEnabled(false);
			btnScheduleScript.setEnabled(false);
			// btnExecuteScript.setEnabled(false);

			// menu
			mniStartSelected.setEnabled(false);
			mniStop.setEnabled(false);
			mniPause.setEnabled(false);
			mniResume.setEnabled(true);
			mniClearLoadScript.setEnabled(false);
			mniLoadMoreScript.setEnabled(false);
			mniScheduleScript.setEnabled(false);
			// mniExecuteScript.setEnabled(false);
			mniOpenScriptFolder.setEnabled(false);
			mniCheckSel.setEnabled(false);
			mniUnCheckSel.setEnabled(false);
			mniCheckAll.setEnabled(false);
			mniUnCheckAll.setEnabled(false);
			mniTestConfig.setEnabled(false);
			
			disable_btnLoadScript();
		}
		else if (app_state == 5) { // END TEST
			btnStartSelected.setEnabled(true);
			btnStop.setEnabled(false);
			btnPause.setEnabled(false);
			btnResume.setEnabled(false);
			btnClearLoadScript.setEnabled(false);
			btnLoadMoreScript.setEnabled(false);
			btnScheduleScript.setEnabled(false);
			// btnExecuteScript.setEnabled(false);
			// btnExportResult.setEnabled(true);

			// menu
			mniStartSelected.setEnabled(true);
			mniStop.setEnabled(false);
			mniPause.setEnabled(false);
			mniResume.setEnabled(false);
			mniClearLoadScript.setEnabled(false);
			mniLoadMoreScript.setEnabled(false);
			mniScheduleScript.setEnabled(false);
			// mniExecuteScript.setEnabled(false);
			mniOpenScriptFolder.setEnabled(false);
			mniCheckSel.setEnabled(false);
			mniUnCheckSel.setEnabled(false);
			mniCheckAll.setEnabled(true);
			mniUnCheckAll.setEnabled(true);
			mniTestConfig.setEnabled(false);
			// mniExportResut.setEnabled(true);
			
			enable_btnLoadScript();
		}
	}
	
	static public void disable_app_state () {

		// button
		btnStartSelected.setEnabled(false);
		btnStop.setEnabled(false);
		btnPause.setEnabled(false);
		btnResume.setEnabled(false);
		btnClearLoadScript.setEnabled(false);
		btnLoadMoreScript.setEnabled(false);
		btnScheduleScript.setEnabled(false);
		// btnExecuteScript.setEnabled(false);
		// btnExportResult.setEnabled(false);

		// menu
		mniStartSelected.setEnabled(false);
		mniStop.setEnabled(false);
		mniPause.setEnabled(false);
		mniResume.setEnabled(false);
		mniClearLoadScript.setEnabled(false);
		mniLoadMoreScript.setEnabled(false);
		mniScheduleScript.setEnabled(false);
		// mniExecuteScript.setEnabled(false);
		mniOpenScriptFolder.setEnabled(false);
		mniCheckSel.setEnabled(false);
		mniUnCheckSel.setEnabled(false);
		mniCheckAll.setEnabled(false);
		mniUnCheckAll.setEnabled(false);
		mniTestConfig.setEnabled(false);
		// mniExportResut.setEnabled(false);
		
		disable_btnLoadScript();
	}
	
	static void disable_btnLoadScript () {
		btnClearLoadScript.setEnabled(false);
		btnLoadMoreScript.setEnabled(false);
		mniClearLoadScript.setEnabled(false);
		mniLoadMoreScript.setEnabled(false);
	}
	
	static void enable_btnLoadScript () {
		btnClearLoadScript.setEnabled(true);
		btnLoadMoreScript.setEnabled(true);
		mniClearLoadScript.setEnabled(true);
		mniLoadMoreScript.setEnabled(true);
	}

	static private void check_all_tests() {
		for (int i = 0 ; i < tableTestSuiteModel.getRowCount(); i ++) {
			tableTestSuiteModel.setValueAt(true, i,1);
			tableTestSuiteModel.checkedAtRow(i);
		}
		tableTestAreaModel.update_detail();
	}
	static private void uncheck_all_tests() {
		for (int i = 0 ; i < tableTestSuiteModel.getRowCount(); i ++) {
			tableTestSuiteModel.setValueAt(false, i,1);
			tableTestSuiteModel.uncheckedAtRow(i);
		}
		tableTestAreaModel.update_detail();
	}
}

class ClearLoadTestScript extends Thread {
	private String prefix_name = null;
	private String testsuite_file = null;
	private String testcase_file = null;
	private String testlib_file = null;
	private String guiobject_file = null;
	public ClearLoadTestScript(String prefix_name, String testsuite_file, String testcase_file, String testlib_file, String guiobject_file) {
		this.prefix_name = prefix_name;
		this.testsuite_file = testsuite_file;
		this.testcase_file = testcase_file;
		this.testlib_file = testlib_file;
		this.guiobject_file = guiobject_file;
	}

	public void run() {
		// Clean up before loading
		FormMain.tableTestSuiteModel.clear();
		FormMain.tableTestAreaModel.clear();


		FormMain.disable_app_state();
		guiutil.txtOutput_append_12black (FormMain.simple_date_time_format.format(new Date()) + " -- Start loading test script", FormMain.txtOutput, FormMain.txtOutput_style, FormMain.txtOutput_doc);
		com.ttv.at.test.testcaseset.clear_instance();
		com.ttv.at.test.testsuiteset loaded_testsuiteset = com.ttv.at.util.test.loader.load_testsuites(testsuite_file, prefix_name, FormMain.mainProgress); 
		
		String tsError = com.ttv.at.util.test.loader.get_load_testsuites_warning_message();
		
		if (tsError != null && tsError.length() > 0) {
			guiutil.txtOutput_append_12red(FormMain.simple_date_time_format.format(new Date()) + " -- " + tsError, FormMain.txtOutput, FormMain.txtOutput_style, FormMain.txtOutput_doc);
			JOptionPane.showMessageDialog(FormMain.mainFrame,
					"Error in loading test suite",
				"Script Eror",
				JOptionPane.WARNING_MESSAGE);
		}
		else {
			FormMain.set_loaded_testsuiteset(loaded_testsuiteset);
			guiutil.txtOutput_append_12green(FormMain.simple_date_time_format.format(new Date()) + " -- Test suite is loaded successful", FormMain.txtOutput, FormMain.txtOutput_style, FormMain.txtOutput_doc);
		}

		if (FormMain.tableTestSuite.getRowCount() > 0) {
			ListSelectionModel selectionModel = FormMain.tableTestSuite.getSelectionModel();
			selectionModel.setSelectionInterval(0, 0);

			// Load the test area
			if (FormMain.tableTestAreaModel != null) {
				FormMain.tableTestAreaModel.set_loaded_testarea(FormMain.get_selected_testsuite().get_testareas().get(0));
			}
		}
		FormMain.set_app_state_ready();
	}
}

class LoadMoreTestScript extends Thread {
	private String prefix_name = null;
	private String testsuite_file = null;
	private String testcase_file = null;
	private String testlib_file = null;
	private String guiobject_file = null;
	public LoadMoreTestScript(String prefix_name, String testsuite_file, String testcase_file, String testlib_file, String guiobject_file) {
		this.prefix_name = prefix_name;
		this.testsuite_file = testsuite_file;
		this.testcase_file = testcase_file;
		this.testlib_file = testlib_file;
		this.guiobject_file = guiobject_file;
	}

	public void run() {
		// Clean up before loading
		// FormMain.tableTestSuiteModel.clear();
		// FormMain.tableTestAreaModel.clear();


		FormMain.disable_app_state();
		guiutil.txtOutput_append_12black (FormMain.simple_date_time_format.format(new Date()) + " -- Start loading test script", FormMain.txtOutput, FormMain.txtOutput_style, FormMain.txtOutput_doc);

		com.ttv.at.test.testsuiteset loaded_testsuiteset = com.ttv.at.util.test.loader.load_testsuites(testsuite_file, prefix_name, FormMain.mainProgress); 
		
		String tsError = com.ttv.at.util.test.loader.get_load_testsuites_warning_message();
		
		if (tsError != null && tsError.length() > 0) {
			guiutil.txtOutput_append_12red(FormMain.simple_date_time_format.format(new Date()) + " -- " + tsError, FormMain.txtOutput, FormMain.txtOutput_style, FormMain.txtOutput_doc);
			JOptionPane.showMessageDialog(FormMain.mainFrame,
					"Error in loading test suite",
				"Script Eror",
				JOptionPane.WARNING_MESSAGE);
		}
		else {
			FormMain.append_loaded_testsuiteset(loaded_testsuiteset);
			guiutil.txtOutput_append_12green(FormMain.simple_date_time_format.format(new Date()) + " -- Test suite is loaded successful", FormMain.txtOutput, FormMain.txtOutput_style, FormMain.txtOutput_doc);
		}

		if (FormMain.tableTestSuite.getRowCount() > 0) {
			ListSelectionModel selectionModel = FormMain.tableTestSuite.getSelectionModel();
			selectionModel.setSelectionInterval(0, 0);

			// Load the test area
			if (FormMain.tableTestAreaModel != null) {
				FormMain.tableTestAreaModel.set_loaded_testarea(FormMain.get_selected_testsuite().get_testareas().get(0));
			}
		}
		FormMain.set_app_state_ready();
	}
}

class ReloadTestSuite extends Thread {
	com.ttv.at.test.testsuite reload_testsuite = null;
	public ReloadTestSuite (com.ttv.at.test.testsuite reload_testsuite) {
		this.reload_testsuite = reload_testsuite;
	}
	public void run() {
		FormMain.disable_app_state();
		
		// Get list of test suite need to update based on the reloaded test suite (testcaseset)
		String reload_tc_set_name = reload_testsuite.get_tc_set().get_name();
		com.ttv.at.test.testsuiteset loaded_set = FormMain.get_loaded_testsuiteset();
		ArrayList<com.ttv.at.test.testsuite> reload_testsuite_list = new ArrayList<com.ttv.at.test.testsuite>();
		for (com.ttv.at.test.testsuite scan_suite : loaded_set.get_testsuites())
			if (scan_suite.get_tc_set().get_name().equals(reload_tc_set_name))
				reload_testsuite_list.add(scan_suite);
		
		// Reload test case
		if (!com.ttv.at.util.test.loader.reload_testcaseset (reload_tc_set_name)) {
			guiutil.txtOutput_append_12red(FormMain.simple_date_time_format.format(new Date()) + " -- " + com.ttv.at.util.test.loader.get_reload_testcaseset_warning_message(), FormMain.txtOutput, FormMain.txtOutput_style, FormMain.txtOutput_doc);
			JOptionPane.showMessageDialog(FormMain.mainFrame,
					"Error in reloading test suite",
				"Script Eror",
				JOptionPane.WARNING_MESSAGE);
		}
		else {
			// Reload test suites based on the new reloaded test case
			com.ttv.at.util.test.loader.reload_testsuite(reload_testsuite, FormMain.mainProgress, reload_tc_set_name);
			String tsError = com.ttv.at.util.test.loader.get_reload_testsuite_warning_message();
			if (tsError != null && tsError.length() > 0) {
				guiutil.txtOutput_append_12red(FormMain.simple_date_time_format.format(new Date()) + " -- " + tsError, FormMain.txtOutput, FormMain.txtOutput_style, FormMain.txtOutput_doc);
				JOptionPane.showMessageDialog(FormMain.mainFrame,
						"Error in reloading test suite",
					"Script Eror",
					JOptionPane.WARNING_MESSAGE);
			}
			else {
				guiutil.txtOutput_append_12green(FormMain.simple_date_time_format.format(new Date()) + " -- Test suite is reloaded successful", FormMain.txtOutput, FormMain.txtOutput_style, FormMain.txtOutput_doc);
			}
		}
		
		FormMain.set_app_state_ready();
	}
}

class executeTestSuite extends Thread {
	JProgressBar runProgressBar;
	public executeTestSuite() {
		runProgressBar = null;
	}
	public executeTestSuite(JProgressBar runProgressBar) {
		this.runProgressBar = runProgressBar;
	}
	public void run() {
		FormMain.set_app_state_running();
		if (FormMain.get_running_testsuite() != null) {

			int max_running_tests = FormMain.get_running_testsuite().get_max_number_of_run_test();
			// Prepare progressBar for running
			if (runProgressBar != null) {
				runProgressBar.setMaximum(max_running_tests);
				runProgressBar.setMinimum(0);

				runProgressBar.setValue(0);
			}
			
			// FormMain.loaded_testsuite.clean_result();
			FormMain.tableTestSuiteModel.update_detail();
			
			FormMain.get_running_testsuite().get_dataset().apply_env();
			FormMain.get_running_testsuite().execute();
			runProgressBar.setValue(max_running_tests);
		}
		// Determine state after running
		if (FormMain.run_state.startsWith("pause"))
			FormMain.set_app_state_pause();
		else {
			FormMain.set_app_state_endtest();
		}
	}
}

class resumeTestSuite extends Thread {
	JProgressBar runProgressBar;
	static public SimpleDateFormat simple_date_time_format = new SimpleDateFormat("yyyyMMdd_HHmmss");
	public resumeTestSuite() {
		runProgressBar = null;
	}
	public resumeTestSuite(JProgressBar runProgressBar) {
		this.runProgressBar = runProgressBar;
	}
	public void run() {
		if (FormMain.get_loaded_testsuiteset() != null) {
			FormMain.set_app_state_running();
			// Prepare progressBar for running
			int max_running_tests = FormMain.get_running_testsuite().get_max_number_of_run_test();
			if (runProgressBar != null) {
				runProgressBar.setMinimum(0);
				runProgressBar.setMaximum(max_running_tests);
	
				runProgressBar.setValue(0);
				if (FormMain.get_loaded_testsuiteset().get_resume_index () > 0)
					runProgressBar.setValue(FormMain.get_loaded_testsuiteset().get_resume_index ());
			}

			if (FormMain.get_running_testsuite() != null)
				FormMain.get_running_testsuite().resume();
			if (FormMain.run_state.startsWith("pause"))
				FormMain.set_app_state_pause();
			else {
				FormMain.set_app_state_endtest();
			}
		}
	}
}

class executeTestSuiteSet extends Thread {
	JProgressBar runProgressBar;
	static public SimpleDateFormat simple_date_time_format = new SimpleDateFormat("yyyyMMdd_HHmmss");
	public executeTestSuiteSet() {
		runProgressBar = null;
	}
	public executeTestSuiteSet(JProgressBar runProgressBar) {
		this.runProgressBar = runProgressBar;
	}
	public void run() {
		FormMain.set_app_state_running();
		// Prepare progressBar for running
		int max_running_tests = FormMain.get_loaded_testsuiteset().get_max_number_of_run_test();
		if (runProgressBar != null) {
			runProgressBar.setMinimum(0);
			runProgressBar.setMaximum(max_running_tests);

			runProgressBar.setValue(0);
		}
		
		// FormMain.loaded_testsuite.clean_result();
		// FormMain.tableTestSuiteModel.update_detail();

		if (FormMain.get_loaded_testsuiteset() != null) {
			FormMain.get_loaded_testsuiteset().execute();
		}
		// Determine state after running
		if (FormMain.run_state.startsWith("pause"))
			FormMain.set_app_state_pause();
		else {
			FormMain.set_app_state_endtest();
			
		}
	}
}

class resumeTestSuiteSet extends Thread {
	JProgressBar runProgressBar;
	static public SimpleDateFormat simple_date_time_format = new SimpleDateFormat("yyyyMMdd_HHmmss");
	public resumeTestSuiteSet() {
		runProgressBar = null;
	}
	public resumeTestSuiteSet(JProgressBar runProgressBar) {
		this.runProgressBar = runProgressBar;
	}
	public void run() {
		if (FormMain.get_loaded_testsuiteset() != null) {
			FormMain.set_app_state_running();
			// Prepare progressBar for running
			if (runProgressBar != null) {
				runProgressBar.setMinimum(0);
				//runProgressBar.setMaximum(FormMain.loaded_testsuiteset.get_total_number_of_run_test());
	
				runProgressBar.setValue(0);
				if (FormMain.get_loaded_testsuiteset().get_resume_index () > 0)
					runProgressBar.setValue(FormMain.get_loaded_testsuiteset().get_resume_index ());
			}
			
			if (FormMain.get_loaded_testsuiteset() != null)
				FormMain.get_loaded_testsuiteset().resume();
			if (FormMain.run_state.startsWith("pause"))
				FormMain.set_app_state_pause();
			else {
				FormMain.set_app_state_endtest();
			}
		}
	}
}

class loadNrun extends Thread {

	JProgressBar runProgressBar;
	ArrayList<String> testsuite_prefixes = null;
	boolean auto_exit = false;
	
	static public SimpleDateFormat simple_date_time_format = new SimpleDateFormat("yyyyMMdd_HHmmss");
	
	public loadNrun(JProgressBar runProgressBar, ArrayList<String> testsuite_prefixes, boolean auto_exit) {
		this.runProgressBar = runProgressBar;
		this.testsuite_prefixes = testsuite_prefixes;
		this.auto_exit = auto_exit;
	}
	public void run() {
		System.out.println("******** START LOAD AND RUN COMMAND ********");
		if (testsuite_prefixes != null && testsuite_prefixes.size() > 0 && FormMain.cboSuiteName != null) {
			DefaultComboBoxModel suiteName_model = (DefaultComboBoxModel) FormMain.cboSuiteName.getModel();
			for (String testsuite_prefix:testsuite_prefixes) {
				System.out.println("Loading testsuite set of " + testsuite_prefix);
				boolean bFoundPrefix = false;
				// Set the selection for prefix
				for (int i = 0 ; i < suiteName_model.getSize() ; i ++)
					if (suiteName_model.getElementAt(i).toString().toLowerCase().equals(testsuite_prefix.toLowerCase())) {
						FormMain.cboSuiteName.setSelectedIndex(i);
						bFoundPrefix = true;
						break;
					}

				// ****** Load Script ******
				boolean load_Script_OK = false;
				if (bFoundPrefix) {
					// Clean up before loading
					FormMain.tableTestSuiteModel.clear();
					FormMain.tableTestAreaModel.clear();


					FormMain.disable_app_state();
					guiutil.txtOutput_append_12black (FormMain.simple_date_time_format.format(new Date()) + " -- Start loading test script", FormMain.txtOutput, FormMain.txtOutput_style, FormMain.txtOutput_doc);

					com.ttv.at.test.testsuiteset loaded_testsuiteset = com.ttv.at.util.test.loader.load_testsuites(FormMain.get_selected_ts_full_path(),testsuite_prefix, FormMain.mainProgress); 
					
					String tsError = com.ttv.at.util.test.loader.get_load_testsuites_warning_message();
					
					if (tsError != null && tsError.length() > 0) {
						guiutil.txtOutput_append_12red(FormMain.simple_date_time_format.format(new Date()) + " -- " + tsError, FormMain.txtOutput, FormMain.txtOutput_style, FormMain.txtOutput_doc);

						System.out.println("Loaded testsuite failed, error " + tsError);
						load_Script_OK = false;
					}
					else {
						FormMain.set_loaded_testsuiteset(loaded_testsuiteset);
						guiutil.txtOutput_append_12green(FormMain.simple_date_time_format.format(new Date()) + " -- Test suite is loaded successful", FormMain.txtOutput, FormMain.txtOutput_style, FormMain.txtOutput_doc);
						load_Script_OK = true;
					}

					if (FormMain.tableTestSuite.getRowCount() > 0) {
						ListSelectionModel selectionModel = FormMain.tableTestSuite.getSelectionModel();
						selectionModel.setSelectionInterval(0, 0);

						// Load the test area
						if (FormMain.tableTestAreaModel != null) {
							FormMain.tableTestAreaModel.set_loaded_testarea(FormMain.get_selected_testsuite().get_testareas().get(0));
						}
					}
					FormMain.set_app_state_ready();
				}
				
				// ****** Run the testsuiteSet ******
				if (load_Script_OK) {
					System.out.println("Styart running testsuite set of " + testsuite_prefix);
					FormMain.set_app_state_running();
					FormMain.run_state = "start testsuiteset";
					// Prepare progressBar for running
					int max_running_tests = FormMain.get_loaded_testsuiteset().get_max_number_of_run_test();
					if (runProgressBar != null) {
						runProgressBar.setMinimum(0);
						runProgressBar.setMaximum(max_running_tests);

						runProgressBar.setValue(0);
					}
					
					// Create log folder
					String log_title = System.getProperty("file.separator") + "SuiteSet_" +  simple_date_time_format.format(new Date());
					
					// Check to create log folder
					com.ttv.at.test.testsetting.init_default_log_folder(log_title);
					
					// FormMain.loaded_testsuite.clean_result();
					// FormMain.tableTestSuiteModel.update_detail();

					if (FormMain.get_loaded_testsuiteset() != null) {
						FormMain.get_loaded_testsuiteset().execute();
					}
					// Determine state after running
					if (FormMain.run_state.startsWith("pause"))
						FormMain.set_app_state_pause();
					else {
						FormMain.set_app_state_endtest();
					}
				}
			}
		}
		System.out.println("******** COMPLETE COMMAND ********");
		System.out.println("**********************************");
		System.out.println("Please press CTRL+C to exit the application");
		
		if (auto_exit)
			wait_to_exit ();
	}
	
	static void wait_to_exit () {
		while (true) {
			//Scan all running thread to quit the application
			ThreadGroup currentGroup = Thread.currentThread().getThreadGroup();
			int noThreads = currentGroup.activeCount();
			Thread[] lstThreads = new Thread[noThreads];
			currentGroup.enumerate(lstThreads);
			int alive_thread = 0;
			for (int i = 0; i < noThreads; i++) {
				String thread_name_lowwer = lstThreads[i].getName().toLowerCase();
				// System.out.println("Thread No:" + i + " = " + lstThreads[i].getName() + " - isInterrupted : " + lstThreads[i].isInterrupted() + " - isAlive : " + lstThreads[i].isAlive());
				if (	!(thread_name_lowwer.equals("awt-shutdown") ||
						thread_name_lowwer.equals("awt-windows") ||
						thread_name_lowwer.startsWith("awt-eventqueue-") ||
						thread_name_lowwer.equals("destroyjavavm")))
					alive_thread ++ ;
			}
			
			if (alive_thread <= 1) // that mean only this thread alive -> quit
				Runtime.getRuntime().exit(NORM_PRIORITY);
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

class WaitEmailRun extends Thread {
	public void run () {
		// wait until all other thread complete
		wait_all_complete ();
		
		// Start pool with request list as queue
		// abc
	}
	
	static void wait_all_complete () {
		
		int complete_count = 0;
		while (true) {
			//Scan all running thread to quit the application
			ThreadGroup currentGroup = Thread.currentThread().getThreadGroup();
			int noThreads = currentGroup.activeCount();
			Thread[] lstThreads = new Thread[noThreads];
			currentGroup.enumerate(lstThreads);
			int alive_thread = 0;
			for (int i = 0; i < noThreads; i++) {
				String thread_name_lowwer = lstThreads[i].getName().toLowerCase();
				// System.out.println("Thread No:" + i + " = " + lstThreads[i].getName() + " - isInterrupted : " + lstThreads[i].isInterrupted() + " - isAlive : " + lstThreads[i].isAlive());
				if (	!(thread_name_lowwer.equals("awt-shutdown") ||
						thread_name_lowwer.equals("awt-windows") ||
						thread_name_lowwer.startsWith("awt-eventqueue-") ||
						thread_name_lowwer.equals("destroyjavavm")))
					alive_thread ++ ;
			}
			
			if (alive_thread <= 1) // that mean only this thread alive -> quit
				complete_count ++;
			else
				complete_count = 0;
			
			if (complete_count > 3)
				break;
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

class scheduleTestsuite extends Thread {
	String schedule_file_path;
	public scheduleTestsuite (String schedule_file_path) {
		this.schedule_file_path = schedule_file_path;
	}
	public void run() {
		// Clean up before loading
		FormMain.tableTestSuiteModel.clear();
		FormMain.tableTestAreaModel.clear();


		FormMain.disable_app_state();
		guiutil.txtOutput_append_12black (FormMain.simple_date_time_format.format(new Date()) + " -- Start loading test script", FormMain.txtOutput, FormMain.txtOutput_style, FormMain.txtOutput_doc);
		com.ttv.at.test.testcaseset.clear_instance();
		com.ttv.at.test.testsuiteset loaded_testsuiteset = com.ttv.at.util.test.loader.load_schedule(schedule_file_path, FormMain.mainProgress); 
		
		String tsError = com.ttv.at.util.test.loader.get_load_schedule_message();
		
		if (tsError != null && tsError.length() > 0) {
			guiutil.txtOutput_append_12red(FormMain.simple_date_time_format.format(new Date()) + " -- " + tsError, FormMain.txtOutput, FormMain.txtOutput_style, FormMain.txtOutput_doc);
			JOptionPane.showMessageDialog(FormMain.mainFrame,
					"Error in loading test suite",
				"Script Eror",
				JOptionPane.WARNING_MESSAGE);
			FormMain.set_app_state_ready();
		}
		else {

			if (FormMain.tableTestSuite.getRowCount() > 0) {
				ListSelectionModel selectionModel = FormMain.tableTestSuite.getSelectionModel();
				selectionModel.setSelectionInterval(0, 0);

				// Load the test area
				if (FormMain.tableTestAreaModel != null) {
					FormMain.tableTestAreaModel.set_loaded_testarea(FormMain.get_selected_testsuite().get_testareas().get(0));
				}
			}
			
			FormMain.set_loaded_testsuiteset(loaded_testsuiteset);

			if (FormMain.tableTestSuite.getRowCount() > 0) {
				ListSelectionModel selectionModel = FormMain.tableTestSuite.getSelectionModel();
				selectionModel.setSelectionInterval(0, 0);

				// Load the test area
				if (FormMain.tableTestAreaModel != null) {
					FormMain.tableTestAreaModel.set_loaded_testarea(FormMain.get_selected_testsuite().get_testareas().get(0));
				}
			}
			
			guiutil.txtOutput_append_12green(FormMain.simple_date_time_format.format(new Date()) + " -- Test suite is loaded successful", FormMain.txtOutput, FormMain.txtOutput_style, FormMain.txtOutput_doc);

			FormMain.set_app_state_running();
			loaded_testsuiteset.execute_schedule ();
			FormMain.set_app_state_ready();
		}
	}
}